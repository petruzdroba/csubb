# Să se determine produsul scalar a doi vectori rari care conțin numere reale.
# Un vector este rar atunci când conține multe elemente nule.
# Vectorii pot avea oricâte dimensiuni.
# De ex. produsul scalar a 2 vectori unisimensionali [1,0,2,0,3] și [1,2,0,3,1] este 4.

#O(n)
def scalar(vector1, vector2):
    result = 0
    
    smaller = len(vector1)
    if len(vector2) < smaller:
        smaller = len(vector2)
    
    for idx in range(smaller):
        if vector1[idx] and vector2[idx]:
            result += vector1[idx] * vector2[idx]
            
    return result

#O(n) -> best case
# correct
#passes tests
def claude(v1, v2):
    d1 = {i: v for i, v in enumerate(v1) if v}
    d2 = {i: v for i, v in enumerate(v2) if v}
    return sum(d1[i] * d2[i] for i in d1 if i in d2)

def tester(func):
    assert(func([1,0,2,0,3], [1,2,0,3,1]) == 4)
    assert(func([], []) == 0)
    assert(func([None], [None]) == 0)
    assert(func([2,3], [2]) == 4)
    assert(func([2,None,70], [2,70,None]) == 4)
    print("ok")
    
tester(scalar)
tester(claude)