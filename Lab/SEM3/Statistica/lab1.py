from random import sample
from math import factorial, perm, comb
from itertools import permutations, combinations, combinations_with_replacement

sample("word", k=len("word"))

factorial(len("word"))

list(permutations("word"))
len(list(permutations("word")))

k = 3

list(permutations("word", k))

perm(len("word"), 3)

list(combinations("word", 3))

len(list(combinations("word", 3)))

list(combinations_with_replacement("ABCDE", 4))

pozitii = list(combinations(range(8), 5))
perm_pers = list(permutations("12345"))

pers = "12345"
contor = 0
toate_asezarile = []

for pozitie in pozitii:
    scaune = list("_" * 8)
    for pers in perm_pers:
        for i in range(5):
            scaune[pozitie[i]] = pers[i]
        toate_asezarile.append(scaune)
        contor += 1

print(len(toate_asezarile))
print(toate_asezarile[3000])

scaune = toate_asezarile[3000]
scaune_noi = scaune

for i in range(len(scaune)):
    if scaune[i] != '_':
        scaune_noi.insert(i + 1, '_')

print(scaune_noi)