import numpy as np

class CliffWalkingEnv:

    def __init__(self, rows:int=4, cols:int=12, start:tuple=(3, 0), goal:tuple=(3, 11), seed:int=42):
        self.rows = rows
        self.cols = cols
        self.n_states = rows * cols
        self.n_actions = 4

        self._moves = {
            0: (-1, 0),  # Up
            1: (1, 0),   # Down
            2: (0, -1),  # Left
            3: (0, 1)     # Right
        }
        self._action_symbols = {0: 'U', 1: 'D', 2: 'L', 3: 'R'}

        self.start = start
        self.goal = goal
        self.state = self._encode(self.start)
        self.cliff = {(self.rows - 1, c) for c in range(1, self.cols - 1)}

        self.rng = np.random.default_rng(seed)

    def _encode(self, state:tuple) -> int:
        # gets a state and converts it to a unique interger
        return state[0] * self.cols + state[1]

    def _decode(self, state_id:int) -> tuple:
        # gets the integer and converts it back to the original state
        return (state_id // self.cols, state_id % self.cols)

    def reset(self) -> int:
        self.state = self._encode(self.start)
        return self.state

    # def _get_reward(self, next_pos: tuple[int, int]) -> float:
    #     if next_pos in self.cliff:
    #         return -38.0
    #     if next_pos == self.goal:
    #         return 0.0
    #     if next_pos[0] == self.rows - 2:
    #         return -2.0
    #     return -1.0

    def _get_reward(self, next_pos: tuple[int, int]) -> float:
        if next_pos in self.cliff:
            return -100.0
        if next_pos == self.goal:
            return 0.0
        return -1.0

    def step(self, action: int) -> tuple[int, float, bool]:
        row, col = self._decode(self.state)
        move = self._moves[action]

        next_row = np.clip(row + move[0], 0, self.rows - 1)
        next_col = np.clip(col + move[1], 0, self.cols - 1)

        next_pos = (next_row, next_col)

        reward = self._get_reward(next_pos)

        if next_pos in self.cliff:
            self.reset()
            return self.state, reward, False

        if next_pos == self.goal:
            self.state = self._encode(next_pos)
            return self.state, reward, True

        self.state = self._encode(next_pos)
        return self.state, reward, False

    def render(self):
        pos = self._decode(self.state)

        print("----" * self.cols + "-")

        for row in range(self.rows):
            row_str = "|"

            for col in range(self.cols):
                if (row, col) == pos:
                    row_str += " P |"
                elif (row, col) == self.goal:
                    row_str += " $ |"
                elif (row, col) in self.cliff:
                    row_str += " ^ |"
                else:
                    row_str += "   |"

            print(row_str)
            print("----" * self.cols + "-")

    
    def render_moves(self, moves: list[int]):
        pos = self._decode(self.state)

        print("----" * self.cols + "-")

        for row in range(self.rows):
            row_str = "|"

            for col in range(self.cols):

                state = self._encode((row, col))

                if (row, col) == pos:
                    row_str += " P |"

                elif (row, col) == self.goal:
                    row_str += " $ |"

                elif (row, col) in self.cliff:
                    row_str += " ^ |"

                else:
                    action_symbol = self._action_symbols.get(moves[state], ' ')
                    row_str += f" {action_symbol} |"

            print(row_str)
            print("----" * self.cols + "-")