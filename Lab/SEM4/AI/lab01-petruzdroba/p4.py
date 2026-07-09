# Să se determine cuvintele unui text care apar exact o singură dată în acel text.
# De ex. cuvintele care apar o singură dată în ”ana are ana are mere rosii ana" sunt: 'mere' și 'rosii'.

#O(n)
def singles(sentence):
    result = []
    removed = []
    
    for word in sentence.split():
        if word in removed:
            continue
        
        if word not in result:
            result.append(word)
        else:
            result.remove(word)
            removed.append(word)
            
    return result

#O(n)
#correct syntax
#passes edge cases
def claude(text):
    from collections import Counter
    return [w for w, c in Counter(text.split()).items() if c == 1]

def tester(func):
    assert(func("ana are ana are mere rosii ana") == ["mere", "rosii"])
    assert(func("ana ana ana ana ana ") == [])
    assert(func("") == [])
    assert(func("ana are ana are ana") == [])
    assert(func("ana   are    mere") == ["ana", "are", "mere"])
    assert(func("măr mar măr") == ["mar"])
    print("ok")
    
tester(singles)
tester(claude)