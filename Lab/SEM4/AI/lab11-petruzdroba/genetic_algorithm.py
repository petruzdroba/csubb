from chromosome import Chromosome
from fitness import modularity, coverage, conductance
import random

class GeneticAlgorithm:
    def __init__(self, network, pop_size=100, generations=200, probability=0.05):
        self.network = network
        self.pop_size = pop_size
        self.generations = generations
        self.probability = probability
        n = network['noNodes']
        self.maxCommunities = n if n <= 50 else int(n ** 0.5)
        self.population = []

    def initialisation(self): # create the starting pop
        for _ in range(self.pop_size):
            self.population.append(Chromosome(self.network['noNodes'], self.maxCommunities))

    def evaluation(self): # score based on the fitness function
        for c in self.population:
            c.fitness = modularity(c.genes, self.network)
 
    def bestChromosome(self):
        return max(self.population, key=lambda c: c.fitness)

    def selection(self): # pick 2 random chroms and return the better one
        p1 = random.randint(0, self.pop_size - 1)
        p2 = random.randint(0, self.pop_size - 1)
        return p1 if self.population[p1].fitness > self.population[p2].fitness else p2

    def oneGenerationElitism(self):# run a generation, kee the best chrom unchanged
        newPop = [self.bestChromosome()] 
        for _ in range(self.pop_size - 1):
            p1 = self.population[self.selection()]
            p2 = self.population[self.selection()]
            off = p1.crossover(p2)
            off.mutate(self.probability, self.network['mat'])
            newPop.append(off)
        self.population = newPop
        self.evaluation()
 
    def run(self):
        self.initialisation()
        self.evaluation()
        for g in range(self.generations):
            self.oneGenerationElitism()
            if g % 25 == 0:
                print(f"  Gen {g:>4} | Q = {self.bestChromosome().fitness:.4f}")
        return self.bestChromosome().genes
