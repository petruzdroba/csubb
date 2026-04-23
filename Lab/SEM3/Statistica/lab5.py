from scipy.stats import uniform, expon
from numpy import mean
from matplotlib.pyplot import bar, show, hist, grid, xticks, plot

y = uniform.rvs(size=100000)

a = uniform.rvs(loc=0, scale=0.5)
b = a + 0.5

print(mean([(a < elem < b) for elem in y]))

def f(y, p, x):
    c = 0
    for i in range(len(p)):
        if c <= y <= c + p[i]:
            return x[i]
        c += p[i]
    return x[len(p) - 1]

print([f(uniform.rvs(), [0.2, 0.5, 0.3], [-1, 1, 3]) for _ in range(1000)])

x = [-1, 1, 3]
p = [0.2, 0.5, 0.3]

data = [f(uniform.rvs(), p, x) for _ in range(1000)]

bin_edges = [k - 0.5 for k in range(min(x), max(x) + 2)]

hist(data, bin_edges, density=True, rwidth=0.9,
     color='Orchid', edgecolor='RebeccaPurple')

bar(x, p, width=0.85, color='RebeccaPurple',
    edgecolor='orchid', alpha=0.6)

grid()
show()

alpha = 1 / 10

def f2(alpha):
    return expon.rvs(scale=1 / alpha)

data = [f2(alpha) for _ in range(1000)]

hist(data, bins=10, density=True, range=(0, 30),
     color="rebeccapurple")

x_vals = range(30)
plot(x_vals, expon.pdf(x_vals, loc=0, scale=1 / alpha), 'r-')

xticks(range(0, 30, 3))
grid()
show()