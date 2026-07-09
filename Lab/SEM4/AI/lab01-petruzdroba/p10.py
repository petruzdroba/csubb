# Considerându-se o matrice cu n x m elemente binare (0 sau 1) sortate crescător pe linii,
# să se identifice indexul liniei care conține cele mai multe elemente de 1.

# De ex. în matricea
# [[0,0,0,1,1],
# [0,1,1,1,1],
# [0,0,1,1,1]]
# a doua linie conține cele mai multe elemente 1

#O(nm)
def finder(matrix):
    if matrix == []:
        return None
    
    n,m= len(matrix), len(matrix[0])
    result = (None, -1)
    
    for idx1 in range(n):
        idx2 = 0
        while idx2 < m and not matrix[idx1][idx2]:
            idx2 += 1
            
        if n-idx2 >= result[1]:
            result = (idx1+1, n-idx2)
            
    return result[0]

#O(nlogm)
#correct syntax
#fails edgecases
def claude(matrix):
    best, best_j = 0, len(matrix[0])
    for i, row in enumerate(matrix):
        lo, hi = 0, best_j
        while lo < hi:
            mid = (lo + hi) // 2
            if row[mid] == 0:
                lo = mid + 1
            else:
                hi = mid
        if lo < best_j:
            best, best_j = i, lo
    return best + 1

def tester(func):
    assert(func(
    [[0,0,0,1,1],
    [0,1,1,1,1],
    [0,0,1,1,1]]) == 2)
    assert(func(
    [[0,0,0,0,0],
    [0,0,0,0,0],
    [0,0,0,0,1]]) == 3)
    assert(func(
    [[0,0,0,0,0],
    [0,0,0,0,0],
    [0,0,0,0,0]]) == None)
    assert(func([]) == None)
    print('ok')
    
tester(finder)
tester(claude)