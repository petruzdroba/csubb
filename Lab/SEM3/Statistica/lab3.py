from random import randrange, sample, randint
from matplotlib.pyplot import bar, hist, grid, show, legend
from scipy.stats import binom

data = [randrange(1, 7) for _ in range(500)]

bin_edges = [k + 0.5 for k in range(7)]
hist(data, bin_edges, density=True, rwidth=0.9, color='green', edgecolor='black',
     alpha=0.5, label='frecvente relative')

distribution = dict([(i, 1/6) for i in range(1, 7)])
bar(distribution.keys(), distribution.values(), width=0.85, color='red',
    edgecolor='black', alpha=0.6, label='probabilitati')

legend(loc='lower left')
grid()
show()

data = binom.rvs(n=5, p=0.6, size=1000)

bin_edges = [k + 0.5 for k in range(-1, 6)]
hist(data, bin_edges, density=True, rwidth=0.9, color='green',
     edgecolor='black', alpha=0.5)

distribution = dict([(k, binom.pmf(k, n=5, p=0.6)) for k in range(6)])
bar(distribution.keys(), distribution.values(), width=0.85, color='red',
    edgecolor='black', alpha=0.6)

grid()
show()

prob5 = binom.cdf(5, n=5, p=0.6)
prob2 = binom.cdf(2, n=5, p=0.6)

print(prob5 - prob2)

c1, c2, m = sample([0, 1, 2], k=3)

asez = list('🐐' * 3)
asez[m] = '🏎️'
print("Asezare: ", asez)

usi = list('🚪' * 3)

aleg1 = randint(0, 2)
usi[aleg1] = '👋'
print("Alegere1:", usi)

if aleg1 == m:
    deschid = sample([c1, c2], k=1)[0]
elif aleg1 == c1:
    deschid = c2
else:
    deschid = c1

usi[deschid] = '👁️'
print("Capra    ", usi)

aleg2 = 3 - deschid - aleg1
usi[aleg1], usi[aleg2] = usi[aleg2], usi[aleg1]

print("Alegere2:", usi)

if aleg2 == m:
    print("Ai castigat vere")
else:
    print("Nu ai castigat vere")