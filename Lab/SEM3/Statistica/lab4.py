from scipy.stats import bernoulli, binom, hypergeom, geom
from matplotlib.pyplot import bar, show, hist, grid, legend, xticks
from random import random

p = 0.5

bernoulli.rvs(p)

def random_walk(n, p):
    poz = 0
    poz_viz = []
    for _ in range(n):
        if bernoulli.rvs(p):
            poz += 1
        else:
            poz -= 1
        poz_viz.append(poz)
    return poz_viz

print(random_walk(7, 0.7))

def thousand_walk(n, p):
    data = [random_walk(n, p)[-1] for _ in range(1000)]
    bin_edges = [k + 0.5 for k in range(-n - 1, n + 1)]

    hist(data, bin_edges, density=True, rwidth=0.9,
         color='magenta', edgecolor='black')

    distribution = dict([(2 * k - n, binom.pmf(k, n, p)) for k in range(n + 1)])
    bar(distribution.keys(), distribution.values(),
        width=0.85, color='orange', edgecolor='black', alpha=0.6)

    xticks(range(-n, n + 1))
    grid()
    show()

thousand_walk(10, 0.5)

def thousand_circle_walk(n, p, m):
    data = [random_walk(n, p)[-1] % m for _ in range(1000)]
    bin_edges = [k + 0.5 for k in range(-1, m)]

    hist(data, bin_edges, density=True, rwidth=0.9,
         color='magenta', edgecolor='black')

    distribution = dict([(k, 0) for k in range(m)])

    for k in range(n + 1):
        distribution[(2 * k - n) % m] += binom.pmf(k, n, p)

    bar(distribution.keys(), distribution.values(),
        width=0.85, color='orange', edgecolor='black', alpha=0.6)

    xticks(range(0, m))
    grid()
    show()

thousand_circle_walk(10, 0.5, 5)

p = sum([hypergeom.pmf(k, 49, 6, 6) for k in [3, 4, 5, 6]])

data = geom.rvs(p, size=1000)

probabilitate_estimata = sum([x >= 10 for x in data]) / 1000
probabilitate_teoretica = 1 - geom.cdf(9, p)

print(probabilitate_estimata, probabilitate_teoretica)