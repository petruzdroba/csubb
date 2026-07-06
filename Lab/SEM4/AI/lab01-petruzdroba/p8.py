# Să se genereze toate numerele (în reprezentare binară) cuprinse între 1 și n.
# De ex. dacă n = 4, numerele sunt: 1,10, 11, 100

#O(nlogn)
def binary(number):
    return [int(bin(current)[2:]) for current in range(1,number+1)]

#O(nlogn)
#syntax correct
#fails each test -> didnt read the task properly
def claude1(n):
    return [bin(i)[2:] for i in range(1, n+1)]

#O(nlogn)
#syntax correct
#passes each test
def claude2(n):
    bits = n.bit_length()
    return [int(bin(i)[2:].zfill(bits)) for i in range(1, n+1)]

def tester(func):
    assert(func(4) == [1,10,11,100])
    assert(func(1) == [1])
    assert(func(-1) == [])
    print('ok')
    
tester(binary)
try:
    tester(claude1)
except AssertionError:
    print("Assertion error claude1")
tester(claude2)
