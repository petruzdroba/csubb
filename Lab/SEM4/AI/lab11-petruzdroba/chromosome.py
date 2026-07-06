import random


class Chromosome:
    def __init__(self, noNodes, maxCommunities):
        self.noNodes = noNodes
        self.maxCommunities = maxCommunities
        self.__genes = [random.randint(1, maxCommunities) for _ in range(noNodes)]
        self.__fitness = 0.0

    @property
    def genes(self):
        return self.__genes

    @genes.setter
    def genes(self, value):
        self.__genes = value

    @property
    def fitness(self):
        return self.__fitness

    @fitness.setter
    def fitness(self, value):
        self.__fitness = value

    def crossover(self, other: Chromosome):
        child = Chromosome(self.noNodes, self.maxCommunities)
        point = random.randint(0, self.noNodes - 1)

        child.genes = self.__genes[:point] + other.genes[point:]
        return child

    def mutate(self, probability, mat=None):
        for i in range(self.noNodes):
            if random.random() < probability:
                if mat is not None:
                    neighbors = [j for j in range(self.noNodes) if mat[i][j] == 1]
                    if neighbors:
                        self.__genes[i] = self.__genes[random.choice(neighbors)]
                        continue
                self.__genes[i] = random.randint(1, self.maxCommunities)
