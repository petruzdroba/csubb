import networkx as nx
import numpy as np
import os
from fitness import modularity


def readNet(file_path):
    directory = os.path.dirname(file_path)
    label_path = None
    for fname in os.listdir(directory):
        if fname.startswith('classLabel') and fname.endswith('.txt'):
            label_path = os.path.join(directory, fname)
            break
    return load_network(file_path, label_path)


def _read_gml_tolerant(path):
    G = nx.Graph()
    with open(path, 'r') as f:
        content = f.read()

    import re
    node_ids = re.findall(r'node\s*\[\s*id\s+(\d+)', content)
    for nid in node_ids:
        G.add_node(int(nid))

    edges = re.findall(r'edge\s*\[\s*source\s+(\d+)\s+target\s+(\d+)', content)
    for src, tgt in edges:
        s, t = int(src), int(tgt)
        if G.has_node(s) and G.has_node(t):
            G.add_edge(s, t)

    return G


def load_network(gml_path, label_path=None):
    G = _read_gml_tolerant(gml_path)
    G = nx.Graph(G)

    nodes = sorted(G.nodes())
    node_index = {n: i for i, n in enumerate(nodes)}
    noNodes = len(nodes)
    noEdges = G.number_of_edges()

    mat = np.zeros((noNodes, noNodes), dtype=float)
    for u, v in G.edges():
        i, j = node_index[u], node_index[v]
        mat[i][j] = 1
        mat[j][i] = 1

    degrees = np.sum(mat, axis=1).tolist()

    param = {
        'G': G,
        'nodes': nodes,
        'node_index': node_index,
        'noNodes': noNodes,
        'noEdges': noEdges,
        'mat': mat,
        'degrees': degrees,
    }

    if label_path:
        param['ground_truth'] = load_ground_truth(label_path, nodes)

    return param


def load_ground_truth(label_path, nodes):
    label_map = {}
    with open(label_path, 'r') as f:
        for line in f:
            line = line.strip()
            if not line:
                continue
            parts = line.split()
            label_map[int(parts[0])] = int(parts[1])

    if nodes[0] == 0 and 0 not in label_map and 1 in label_map:
        return [label_map[n + 1] for n in nodes]
    return [label_map[n] for n in nodes]

def communities_to_dict(communities, param):
    result = {}
    for i, c in enumerate(communities):
        node = param['nodes'][i]
        result.setdefault(c, []).append(node)
    return result


def nmi(communities, ground_truth):
    from math import log
    n = len(communities)
    comm_labels = list(set(communities))
    gt_labels = list(set(ground_truth))

    contingency = {(c, g): 0 for c in comm_labels for g in gt_labels}
    for i in range(n):
        contingency[(communities[i], ground_truth[i])] += 1

    H_c = -sum((sum(contingency[(c, g)] for g in gt_labels) / n) *
               log(sum(contingency[(c, g)] for g in gt_labels) / n, 2)
               for c in comm_labels
               if sum(contingency[(c, g)] for g in gt_labels) > 0)

    H_g = -sum((sum(contingency[(c, g)] for c in comm_labels) / n) *
               log(sum(contingency[(c, g)] for c in comm_labels) / n, 2)
               for g in gt_labels
               if sum(contingency[(c, g)] for c in comm_labels) > 0)

    I = sum((contingency[(c, g)] / n) *
            log((contingency[(c, g)] * n) /
                (sum(contingency[(c, gg)] for gg in gt_labels) *
                 sum(contingency[(cc, g)] for cc in comm_labels)), 2)
            for c in comm_labels for g in gt_labels
            if contingency[(c, g)] > 0)

    return 2 * I / (H_c + H_g) if (H_c + H_g) > 0 else 1.0


def print_results(communities, param, method_name):
    comm_dict = communities_to_dict(communities, param)
    Q = modularity(communities, param)

    print(f"  Method : {method_name}")
    print(f"  Communities found : {len(comm_dict)}")
    print(f"  Modularity (Q)    : {Q:.4f}")
    if 'ground_truth' in param:
        print(f"  NMI vs ground truth: {nmi(communities, param['ground_truth']):.4f}")
        
    print(f"\n  Node assignments:")
    for i, c in enumerate(communities):
        print(f"    Node {param['nodes'][i]:>3} -> Community {c}")

    print(f"\n  Community breakdown:")
    for c, members in sorted(comm_dict.items()):
        print(f"    Community {c}: {members}")

def draw(network, communities, title=""):
    import matplotlib.pyplot as plt

    A = np.array(network["mat"])
    G = nx.from_numpy_array(A)

    pos = nx.spring_layout(G, seed=42, k=2)

    unique = sorted(set(communities))
    cmap = plt.cm.get_cmap('tab20', len(unique))
    colors = [cmap(unique.index(c)) for c in communities]

    plt.figure(figsize=(8, 6))
    plt.title(title)

    nx.draw_networkx_nodes(G, pos, node_size=200, node_color=colors)
    nx.draw_networkx_edges(G, pos, alpha=0.3)
    nx.draw_networkx_labels(G, pos, font_size=7, font_color='white')

    plt.axis('off')
    plt.tight_layout()
    plt.show()