import os
import community as community_louvain

from utils import readNet, print_results, draw


def run_louvain(network):
    G = network['G']
    partition = community_louvain.best_partition(G)

    communities = [partition[n] for n in network['nodes']]
    return communities


crtDir = os.getcwd()
filePath = os.path.join(crtDir, 'data','bus', 'bus.gml')

network = readNet(filePath)
communities = run_louvain(network)
print_results(communities, network, "Louvain (library)")

draw(network, communities, "Louvain (library)")