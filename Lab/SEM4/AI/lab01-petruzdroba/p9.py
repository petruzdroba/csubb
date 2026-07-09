# Considerându-se o matrice cu n x m elemente întregi și o listă cu perechi formate din
# coordonatele a 2 căsuțe din matrice ((p,q) și (r,s)),
# să se calculeze suma elementelor din sub-matricile identificate de fieare pereche.

    # De ex, pt matricea
    # [[0, 2, 5, 4, 1],
    # [4, 8, 2, 3, 7],
    # [6, 3, 4, 6, 2],
    # [7, 3, 1, 8, 3],
    # [1, 5, 7, 9, 4]]
    # și lista de perechi ((1, 1) și (3, 3)), ((2, 2) și (4, 4)), 
    # suma elementelor din prima sub-matrice este 38, iar suma elementelor din a 2-a sub-matrice este 44.

#O(nm)
def subsum(x, y, matrix):
    suma = 0
    
    for idx1 in range(x[0], y[0]+1):
        for idx2 in range(x[1], y[1]+1):
            suma += matrix[idx1][idx2]
    
    return suma

#O(n*m)
#corect syntax
#passes tests -> after 5 prompts => dosent understand task and example
def claude(p, r, matrix):
    n, m = len(matrix), len(matrix[0])
    pre = [[0] * (m + 1) for _ in range(n + 1)]
    for i in range(1, n + 1):
        for j in range(1, m + 1):
            pre[i][j] = matrix[i-1][j-1] + pre[i-1][j] + pre[i][j-1] - pre[i-1][j-1]
    p1,q1 = p
    r1,s1 = r
    return pre[r1+1][s1+1] - pre[p1][s1+1] - pre[r1+1][q1] + pre[p1][q1]

def tester(func):
    assert(func((1,1), (3,3),
    [[0, 2, 5, 4, 1],
    [4, 8, 2, 3, 7],
    [6, 3, 4, 6, 2],
    [7, 3, 1, 8, 3],
    [1, 5, 7, 9, 4]]) == 38) 
    assert(func((2,2), (4,4),
    [[0, 2, 5, 4, 1],
    [4, 8, 2, 3, 7],
    [6, 3, 4, 6, 2],
    [7, 3, 1, 8, 3],
    [1, 5, 7, 9, 4]]) == 44) 
    assert(func((0,0), (0,0),
    [[0, 2, 5, 4, 1],
    [4, 8, 2, 3, 7],
    [6, 3, 4, 6, 2],
    [7, 3, 1, 8, 3],
    [1, 5, 7, 9, 4]]) == 0) 
    assert(func((0,0), (4,1),
    [[0, 2, 5, 4, 1],
    [4, 8, 2, 3, 7],
    [6, 3, 4, 6, 2],
    [7, 3, 1, 8, 3],
    [1, 5, 7, 9, 4]]) == 39) 
    print('ok')

tester(subsum)
tester(claude)