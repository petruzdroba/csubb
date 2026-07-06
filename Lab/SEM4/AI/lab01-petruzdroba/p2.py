# Să se determine distanța Euclideană între două locații identificate prin perechi de numere.
# De ex. distanța între (1,5) și (4,1) este 5.0

from math import sqrt

#O(1)
def euclidian(x,y):
    dx = x[0] - y[0]
    dy = x[1] - y[1]
    return sqrt(dx*dx + dy*dy)

#O(1) -> best case
#correct
#passes tests
def claude(p1, p2):
    return ((p1[0]-p2[0])**2 + (p1[1]-p2[1])**2) ** 0.5

def tester(func):
    assert(func((1,5), (4,1)) == 5.0)
    assert(func((0,0), (0,0)) == 0.0)
    assert(func((-1,-5), (-4,-1)) == 5.0)
    assert(func((-1,-5), (4,1)) == 7.810249675906654)
    print("ok")
    
tester(euclidian)
tester(claude)