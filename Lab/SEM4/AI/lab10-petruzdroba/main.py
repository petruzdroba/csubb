from env import CliffWalkingEnv
from q_learning import QLearning
from sarsa import Sarsa
import numpy as np
from plots import plot_rewards, plot_policies

env = CliffWalkingEnv()

print("Q-Learning")
ql = QLearning(env)
ql_rewards = ql.train()

print(f"Episode 1   total reward: {ql_rewards[0]}")
print(f"Episode 100 total reward: {ql_rewards[99]}")
print(f"Episode 500 total reward: {ql_rewards[499]}")
print(f"\nGreedy policy after training:")

policy = [int(np.argmax(ql.Q[s])) for s in range(env.n_states)]
env.render_moves(policy)

print("\nSARSA")
# sarsa = Sarsa(env, decay=0.980)
sarsa = Sarsa(env)
sarsa_rewards = sarsa.train()

print(f"Episode 1   total reward: {sarsa_rewards[0]}")
print(f"Episode 100 total reward: {sarsa_rewards[99]}")
print(f"Episode 500 total reward: {sarsa_rewards[499]}")

policy = [int(np.argmax(sarsa.Q[s])) for s in range(env.n_states)]
env.render_moves(policy)

plot_rewards(ql_rewards, sarsa_rewards)
plot_policies(env, ql.Q, sarsa.Q)