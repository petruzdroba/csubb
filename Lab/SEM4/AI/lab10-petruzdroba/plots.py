import numpy as np
import matplotlib.pyplot as plt

def moving_average(data: list[float], window: int = 10) -> np.ndarray:
    return np.convolve(data, np.ones(window) / window, mode='valid')

def plot_rewards(ql_rewards: list[float], sarsa_rewards: list[float], window: int = 10):
    ql_avg = moving_average(ql_rewards, window)
    sarsa_avg = moving_average(sarsa_rewards, window)
    episodes = range(window, len(ql_rewards) + 1)

    plt.figure(figsize=(12, 5))
    plt.plot(episodes, ql_avg, label='Q-Learning', color='blue')
    plt.plot(episodes, sarsa_avg, label='SARSA', color='red')
    plt.xlabel('Episode')
    plt.ylabel('Total Reward (moving avg 10)')
    plt.title('Q-Learning vs SARSA - Rewards per Episode')
    plt.legend()
    plt.grid(True)
    plt.tight_layout()
    plt.show()

def plot_policies(env, ql_Q: np.ndarray, sarsa_Q: np.ndarray):
    action_arrows = {0: '↑', 1: '↓', 2: '←', 3: '→'}

    fig, axes = plt.subplots(1, 2, figsize=(14, 4))
    titles = ['Q-Learning Policy', 'SARSA Policy']
    Q_tables = [ql_Q, sarsa_Q]

    for ax, Q, title in zip(axes, Q_tables, titles):
        ax.set_title(title, fontsize=13)
        ax.set_xlim(0, env.cols)
        ax.set_ylim(0, env.rows)
        ax.set_xticks(range(env.cols))
        ax.set_yticks(range(env.rows))
        ax.grid(True)
        ax.invert_yaxis()

        for row in range(env.rows):
            for col in range(env.cols):
                if (row, col) in env.cliff:
                    ax.add_patch(plt.Rectangle((col, row), 1, 1, color='black'))
                    ax.text(col + 0.5, row + 0.5, 'cliff',
                            ha='center', va='center', color='white', fontsize=7)
                elif (row, col) == env.goal:
                    ax.add_patch(plt.Rectangle((col, row), 1, 1, color='green'))
                    ax.text(col + 0.5, row + 0.5, 'G',
                            ha='center', va='center', color='white', fontsize=11, fontweight='bold')
                elif (row, col) == env.start:
                    ax.add_patch(plt.Rectangle((col, row), 1, 1, color='orange'))
                    ax.text(col + 0.5, row + 0.5, 'S',
                            ha='center', va='center', color='white', fontsize=11, fontweight='bold')
                else:
                    state = env._encode((row, col))
                    best_action = int(np.argmax(Q[state]))
                    arrow = action_arrows[best_action]
                    ax.text(col + 0.5, row + 0.5, arrow,
                            ha='center', va='center', fontsize=14)

    plt.tight_layout()
    plt.show()