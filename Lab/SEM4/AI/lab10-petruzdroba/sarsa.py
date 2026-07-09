import numpy as np

class Sarsa:

    def __init__(
        self,
        env,
        alpha: float = 0.1,
        gamma: float = 1.0,
        epsilon: float = 0.1,
        decay: float = 1.0,
        episodes: int = 500,
        seed: int = 42,
    ):
        self.env = env
        self.alpha = alpha # learning rate
        self.gamma = gamma # discount factor
        self.epsilon = epsilon # random action probability
        self.decay = decay # epsilon decay rate
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
            action = self._epsilon_greedy(state)
            ep_reward = 0.0
            done = False

            while not done:
                next_state, reward, done = self.env.step(action)
                next_action = self._epsilon_greedy(next_state) # we choose the next action

                target = reward + self.gamma * self.Q[next_state, next_action]
                self.Q[state, action] += self.alpha * (target - self.Q[state, action])

                state, action = next_state, next_action
                ep_reward += reward
            
            self.epsilon =  self.epsilon * self.decay
            rewards.append(ep_reward)

        return rewards