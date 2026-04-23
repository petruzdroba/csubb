from random import randint, random
from math import dist
from matplotlib.pyplot import plot, grid, title, show, xticks, axis

def simulare(n, nr_sim=1000):
    c = 0
    for _ in range(nr_sim):
        zile_nastere = [randint(1, 356) for _ in range(n)]
        if len(set(zile_nastere)) != n:
            c += 1
    return c / nr_sim

print(simulare(30))

title('Probabilitate zile de nastere comune')

for n in range(2, 50):
    y = simulare(n, 100)
    plot(n, y, 'r*')

xticks(range(0, 50, 5))
grid()
show()

axis('square')
axis((0, 1, 0, 1))

for _ in range(5000):
    F = [random(), random()]
    A = dist(F, [0, 0])
    B = dist(F, [1, 0])
    C = dist(F, [1, 1])
    D = dist(F, [0, 1])

    c = 0
    if A * A + B * B > 1:
        c += 1
    if A * A + D * D > 1:
        c += 1
    if B * B + C * C > 1:
        c += 1
    if C * C + D * D > 1:
        c += 1

    if c == 2:
        plot(F[0], F[1], 'ro')
    else:
        plot(F[0], F[1], 'bo')

show()

axis('square')
axis((0, 1, 0, 1))

in_c = 0
out_c = 0

for _ in range(1000):
    F = [random(), random()]
    if dist(F, [0.5, 0.5]) > 0.5:
        plot(F[0], F[1], 'ro')
        out_c += 1
    else:
        plot(F[0], F[1], 'bo')
        in_c += 1

print(in_c / (in_c + out_c) * 4)
show()