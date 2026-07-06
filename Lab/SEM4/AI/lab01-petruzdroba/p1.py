# Să se determine ultimul (din punct de vedere alfabetic) cuvânt care poate apărea într-un text care 
# conține mai multe cuvinte separate prin ” ” (spațiu). 
# De ex. ultimul (dpdv alfabetic) cuvânt din ”Ana are mere rosii si galbene” este cuvântul "si".

#O(nlogn)
def last_word_1 (sentence):
    if sentence == "":
        return ""
    arr = sentence.split()
    arr.sort()
    return arr[-1]

#O(n)
def last_word_2(sentence):
    biggest = ""
    
    for word in sentence.split():
        if word > biggest:
            biggest = word
            
    return biggest

#O(n^2)
def last_word_3(sentence):
    if sentence == "":
        return ""
    
    arr = sentence.split()
    
    for idx1 in range(0, len(arr)-1):
        for idx2 in range(idx1 + 1, len(arr)):
            if arr[idx1] > arr[idx2]:
                arr[idx1], arr[idx2] = arr[idx2], arr[idx1] 
                
    return arr[-1]

#O(n) -> best time for me 
#is correct in most cases
# fails extreme case ""
def claude(text):
    return max(text.split())

def tester(func):
    assert( func("Ana are mere rosii si galbene") == "si")
    assert( func("A A A A A A A A Ana") == "Ana")
    assert( func("") == "")
    assert( func("Andrei are acuarele albastre") == "are")
    assert( func("Ana zambeste") == "zambeste")
    assert( func("Ana Zambeste") == "Zambeste")
    print("ok")
    
tester(last_word_1)
tester(last_word_2)
tester(last_word_3)
tester(claude)