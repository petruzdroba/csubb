import os

from utils import readNet, print_results, draw
from genetic_algorithm import GeneticAlgorithm

def run_ga(network, popSize=100):
    ga = GeneticAlgorithm(network, pop_size=popSize, generations=200)
    communities = ga.run()
    return communities
 
 
crtDir = os.getcwd()
filePath = os.path.join(crtDir, 'data','disease', 'disease.gml')

network = readNet(filePath)
communities = run_ga(network, popSize=100)
print_results(communities, network, "Genetic Algorithm")
draw(network, communities, "Genetic Algorithm")