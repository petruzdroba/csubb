# Să se determine al k-lea cel mai mare element al unui șir de numere cu
# n elemente (k < n). De ex. al 2-lea cel mai mare
# element din șirul [7,4,6,3,9,1] este 7.

#O(nlogn)
def biggest(numbers, k):
    if numbers == []:
        return None
    
    numbers.sort(reverse=True)
    return numbers[k-1]

#O(nlogk)
#correct syntax
#fails edgecase
import heapq
def claude(arr, k):
    return heapq.nlargest(k, arr)[-1]

def tester(func):
    assert(func([7,4,6,3,9,1],2) == 7)
    assert(func([7,4,6,3,9,1,9],2) == 9)
    assert(func([],2) == None)
    print("ok")
    
tester(biggest)
try:
    tester(claude)
except AssertionError:
    print("Assertion error claude")