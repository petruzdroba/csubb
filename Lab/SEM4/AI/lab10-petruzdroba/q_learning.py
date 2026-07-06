import numpy as np


class QLearning:

    def __init__(
        self,
        env,
        alpha: float = 0.1,
        gamma: float = 1.0,
        epsilon: float = 0.1,
        episodes: int = 500,
        seed: int = 42,
    ):
        self.env = env
        self.alpha = alpha # learning rate
        self.gamma = gamma # discount factor
        self.epsilon = epsilon # random action probability
        self.episodes = episodes # epochs

        self.Q = np.zeros((env.n_states, env.n_actions))
        self.rng = np.random.default_rng(seed)

    def _epsilon_greedy(self, state: int) -> int:
        if self.rng.random() < self.epsilon:
            return self.rng.integers(self.env.n_actions)

        return np.argmax(self.Q[state])

    def train(self) -> list[float]:
        rewards = []

        for episode in range(self.episodes):
            state = self.env.reset()
            ep_reward = 0.0
            done = False

            while not done:
                action = self._epsilon_greedy(state)
                next_state, reward, done = self.env.step(action)

                best_next_q = np.max(self.Q[next_state])
                target = reward + self.gamma * best_next_q
                self.Q[state, action] += self.alpha * (target - self.Q[state, action])

                state = next_state
                ep_reward += reward
            
            rewards.append(ep_reward)

        return rewards

