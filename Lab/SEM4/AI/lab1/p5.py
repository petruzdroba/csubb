# Pentru un șir cu n elemente care conține valori din mulțimea {1, 2, ..., n - 1} astfel încât o singură valoare se repetă de două ori,
# să se identifice acea valoare care se repetă.
# De ex. în șirul [1,2,3,4,2] valoarea 2 apare de două ori.

#O(n)
def double_finder(numbers):
    counter = dict()
    
    for number in numbers:
        
        if number in counter.keys():
            return number
        
        if number not in counter.keys():
            counter[number] = 1
            
        
    return None

#O(n)
#correct syntax
#fails basic tests -> thinks the subset starts from one and ends at any n, dosent understand task
def claude(arr):
    n = len(arr)
    return sum(arr) - n * (n - 1) // 2

#O(n)
#correct syntax
#passes edge cases
def claude2(arr):
    seen = set()
    for x in arr:
        if x in seen:
            return x
        seen.add(x)
        
def tester(func):
    assert(func([1,2,3,4,5,6,1]) == 1)
    assert(func([2,2,3,4,5,6,7]) == 2)    
    assert(func([1,2,3,4,5,6,7,7]) == 7)  
    assert(func([1,2,3,4,5,6,7]) == None)    
    print("ok")
      
tester(double_finder)
try:
    tester(claude)
except AssertionError:
    print("Assertion eror for claude1")
tester(claude2)
