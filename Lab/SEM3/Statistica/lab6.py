from scipy.stats import norm, uniform, expon
from numpy import mean, std, linspace, exp
from matplotlib.pyplot import show, hist, grid, xticks, plot
from scipy.integrate import quad

m = 165
s = 10

data = norm.rvs(loc=m, scale=s, size=1000)

hist(data, bins=16, density=True, range=(130, 210),
     rwidth=0.9, color='Orchid', edgecolor='RebeccaPurple')

xticks(range(130, 210, 5), fontsize=8)
grid()

x = linspace(130, 210, 1000)
plot(x, norm.pdf(x, loc=m, scale=s), color='RebeccaPurple')

show()

print(mean(data))
print(std(data))

print(mean([160 < x < 170 for x in data]))
print(norm.cdf(170, loc=m, scale=s))
print(norm.cdf(170, loc=m, scale=s) - norm.cdf(160, loc=m, scale=s))

def timp_printare():
    y = uniform.rvs()

    if y <= 0.4:
        return expon.rvs(scale=5)
    else:
        return uniform.rvs(loc=4, scale=2)

data = [timp_printare() for _ in range(1000)]

print(mean(data))
print(std(data))

u = uniform.rvs(loc=-1, scale=4, size=100000)
g = lambda x: exp(-x**2)

print(4 * mean(g(u)))
print(quad(g, -1, 3))

print(mean([x <= 5 for x in data]))
print(0.4 * expon.cdf(5, scale=5) + 0.6 * uniform.cdf(5, loc=4, scale=2))

print(norm.cdf(170, loc=m, scale=s) - norm.cdf(160, loc=m, scale=s))