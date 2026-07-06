# What are we doing - community detection ?
*Imagine a social network. Million of users (nodes) connected by friendships (edges). Naturally, people cluster into groups (college friends, family members etc. ).*

**Community detection** is the task of auto finding these clusters just from the graph struct. 

# My Graph datasets

## Prison gangs
[[data/prison/]]
*200 inmantes, 5 gangs*

Hard modular separation (*inmates cannot be part of 2 gangs*), high-control inter-community links. Communities behave like near-isolated cliques with controlled leakage.
Low cross-community density.
**Good for testing**: modularity maximisation vs bridge detection failure nodes.

## Bus shared network
[[data/bus/]]
*24 busses, 6 stations*

Linear + transfer-hub structure. Communities are **route corridors**. Structure is dominated by hub stations connecting communities. 
**Good for testing**: sensitivity to bridge edges and articulation points.

## Bee pollination
[[data/bee/]]
*30 bees, 10 flowers*

Role-projection structure (*bipartite-induced communities*). True structure is bee <-> flower. Communities are dense local cliques induced by shared visitation. Weak overlap via cross-pollinating.
**Good for testing**: bipartite projection dist. in community detection.

## Dog territory marking
[[data/dog/]]
*7 dogs, 10 spots*

Spatial hotspot-driven network. Structure is shared env. anchors. Communities are indirect and weak - because of repeated co-use of locations. Network - set of overlapping mictro-clusters.
**Good for testing**: location-based clustering vs actor-based

## Corporate Hierarchy
[[data/corporate/]]

Based on what employee interacts with what employee, what role do they have in the structure.

## Disease 
[[data/disease/]]
*32 diseases, 4 groups*

Based on how frequently they appear togheter. Nodes are diseases, endges represent known transmission relationships.

# Genetic Algorithm

Its an optimization algo. inspired by evolution.

**How it works:**
- Chromosome - one solution: one list where each number is the community that node belongs to.
- Population : start with 100 random chromosomes (100 random community assignments)
- Fitness : score each chromosome using `modularity`, the higher the better
- Selection : keep the best ones, throw out the worst
- Crossover : take two parent chromosomes, mix them to make a child. *Like cutting both lists in half and combining them*
- Mutation : randomly change one nodes community label in a chromosome. Keeping the diversity, avoid getting stuck
- Repeat: do this for `N` generations. Each generation the population gets slightly better
- Return the best chromosome found
# My Utils

## Ground Truth

*Kind of like a loss function.* The actual assignment of every node to a community.
## Modularity-Q

*basically we use the communities that each chromosome gets randomly, and that is how we know if 2 nodes are in the same community, and using modularity we just see if they are or not by the Q score*

*Are there more edges inside communities than you would expect by chance ?*
We calculate the degree of the graph, `M = 2 * noEdges`. For every pair of nodes in the same community, we subtract the expected edges from the actual -> *in an actual community the edges are more than the chance predicts*.

## Normalized Mutual Information

*How similar are two partitions ?* We use it to compare the detected communities against the ground truth. Using:
- Contingency table: matrix that shows how many nodes are in community C detected and in community G in ground truth
- H_c: entropy of detected communities. *How spread out / uncertain are the assignments ?*
- H_g: entropy of ground truth
- I: mutual information. Knowing one partition tells us about the other.
- Formula: `2 * I / (H_c + H_g)` - normalizes to 0–1
- Result: 0 = completely different partitions, 1 = identical partitions.

# My Chromosome

Instead of randomly mutating to a community, I changed it to do neighbour mutation, it picks out a random community that a neigh. has. It has bumped up from normal (500 popSize, 1000 generations -> Q=0.1) to a super (100 popsize, 200 gen -> Q=0.9) on the prison dataset.

# Fitness functions

[[fitness.py]]

**Modularity**: [Modularity Q](#modularity-q)

**Coverage**: For every pair of nodes in the same community, count the actual edges between them and divide by the degree. Ratio of edges. Higher means more edges are captured in communities. Dosent penalize expected edges.

**Conductance**: Counts edges inside community A and edges between communities B. Returns `B / (A + B)`. The lower the better -> so we divide it from 1 so it works with our GA.

## Diffs with our Genetic Algorithm

| Dataset | Modularity | Coverage | Conductance |
| ------- | ---------- | -------- | ----------- |
| Bee     | 0.5127     | 0.454    | 0.4661      |
