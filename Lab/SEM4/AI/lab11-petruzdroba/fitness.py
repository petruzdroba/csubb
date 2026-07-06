def modularity(communities, param):
    noNodes = param['noNodes']
    mat = param['mat']
    degrees = param['degrees']
    noEdges = param['noEdges']
    M = 2 * noEdges
    Q = 0.0
    for i in range(noNodes):
        for j in range(noNodes):
            if communities[i] == communities[j]:
                Q += (mat[i][j] - degrees[i] * degrees[j] / M)
    return Q / M

def coverage(communities, param):
    noNodes = param['noNodes']
    mat = param['mat']
    degrees = param['degrees']
    noEdges = param['noEdges']
    M = 2 * noEdges
    C = 0.0
    for i in range(noNodes):
        for j in range(noNodes):
            if communities[i] == communities[j]:
                C += mat[i][j]
    return C / M

def conductance(communities, param):
    noNodes = param['noNodes']
    mat = param['mat']
    degrees = param['degrees']
    noEdges = param['noEdges']
    M = 2 * noEdges
    A = 0.0
    B = 0.0
    for i in range(noNodes):
        for j in range(noNodes):
            if communities[i] == communities[j]:
                A += mat[i][j]
            else:
                B += mat[i][j]
    if A + B == 0:
        return 1.0
    return 1 - B / (A + B)