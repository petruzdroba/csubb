# Pentru un șir cu n numere întregi care conține și duplicate, să se determine elementul
# majoritar (care apare de mai mult de n / 2 ori).
# De ex. 2 este elementul majoritar în șirul [2,8,7,2,2,5,2,3,1,2,2].

#O(n)
def majority(numbers):
    counter = dict()
    size = len(numbers)/2
    
    for number in numbers:
        if number not in counter.keys():
            counter[number] = 1
        else:
            counter[number] += 1
    
    for app in counter.keys():
        if counter[app] > size:
            return app
        
    return None

#O(n) ->  Boyer-Moore Voting Algorithm
#syntax cool
#edgecases failed
def claude(arr):
    candidate, count = None, 0
    for x in arr:
        if count == 0:
            candidate = x
        count += 1 if x == candidate else -1
    return candidate
        
def tester(func):
    assert(func([2,8,7,2,2,5,2,3,1,2,2]) == 2)
    assert(func([1,1,1,2,2]) == 1)
    assert(func([1,1,2,2]) == None)
    assert(func([]) == None)
    assert(func([1,2,3,4,5,6,7]) == None)
    print("ok")
    
tester(majority)
try:
    tester(claude)
except AssertionError:
    print("Assertion error claude")