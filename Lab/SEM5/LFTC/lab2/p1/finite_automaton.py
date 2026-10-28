class FiniteAutomaton:
    def __init__(self, states, alphabet, initial_state, final_states, transitions):
        self.states = set(states)
        self.alphabet = set(alphabet)
        self.initial_state = initial_state
        self.final_states = set(final_states)
        self.transitions = transitions
        self._validate()

    def display_states(self):
        print("States:", self.states)

    def display_alphabet(self):
        print("Alphabet:", self.alphabet)

    def display_transitions(self):
        print("Transitions:")
        for (state, symbol), destinations in self.transitions.items():
            for destination in destinations:
                print(f"{state} --{symbol}--> {destination}")

    def display_final_states(self):
        print("Final states:", self.final_states)

    def is_deterministic(self):
        return all(
            len(destinations) <= 1
            for destinations in self.transitions.values()
        )

    def _validate(self):
        if not self.states:
            raise ValueError("The set of states cannot be empty.")

        if not self.alphabet:
            raise ValueError("The alphabet cannot be empty.")

        if self.initial_state not in self.states:
            raise ValueError("The initial state must belong to the set of states.")

        if not self.final_states.issubset(self.states):
            raise ValueError("All final states must belong to the set of states.")

        for (source, symbol), destinations in self.transitions.items():
            if source not in self.states:
                raise ValueError(f"Unknown source state: {source}")

            if symbol not in self.alphabet:
                raise ValueError(f"Unknown symbol: {symbol}")

            if not destinations:
                raise ValueError("A transition must have at least one destination.")

            if not destinations.issubset(self.states):
                raise ValueError(f"Unknown destination state in transition from {source}.")

    @classmethod
    def read_from_file(cls, filename):
        states = set()
        alphabet = set()
        initial_state = None
        final_states = set()
        transitions = {}

        with open(filename, "r", encoding="utf-8") as file:
            lines = [line.strip() for line in file if line.strip()]

        section = None

        for line in lines:
            if line.startswith("states:"):
                states = set(line.removeprefix("states:").split())
            elif line.startswith("alphabet:"):
                alphabet = set(line.removeprefix("alphabet:").split())
            elif line.startswith("initial:"):
                initial_state = line.removeprefix("initial:").strip()
            elif line.startswith("final:"):
                final_states = set(line.removeprefix("final:").split())
            elif line == "transitions:":
                section = "transitions"
            elif section == "transitions":
                source, symbol, destination = line.split()
                transitions.setdefault((source, symbol), set()).add(destination)

        return cls(states, alphabet, initial_state, final_states, transitions)

    @classmethod
    def read_from_keyboard(cls):
        states = set(input("States (space-separated): ").split())
        alphabet = set(input("Alphabet (space-separated): ").split())
        initial_state = input("Initial state: ").strip()
        final_states = set(input("Final states (space-separated): ").split())

        transitions = {}
        print("Enter transitions as: source symbol destination")
        print("Enter an empty line to finish.")

        while True:
            line = input("> ").strip()
            if not line:
                break

            source, symbol, destination = line.split()
            transitions.setdefault((source, symbol), set()).add(destination)

        return cls(states, alphabet, initial_state, final_states, transitions)

    
    def accepts(self, sequence):
        if not self.is_deterministic():
            raise ValueError("Sequence acceptance requires a deterministic automaton.")

        current_state = self.initial_state

        for symbol in sequence:
            if symbol not in self.alphabet:
                return False

            destinations = self.transitions.get((current_state, symbol), set())

            if not destinations:
                return False

            current_state = next(iter(destinations))

        return current_state in self.final_states


    def longest_accepted_prefix(self, sequence):
        if not self.is_deterministic():
            raise ValueError("This operation requires a deterministic automaton.")

        current_state = self.initial_state
        longest_prefix = "" if current_state in self.final_states else None

        for i, symbol in enumerate(sequence):
            if symbol not in self.alphabet:
                break

            destinations = self.transitions.get((current_state, symbol), set())
            if not destinations:
                break

            current_state = next(iter(destinations))

            if current_state in self.final_states:
                longest_prefix = sequence[:i + 1]

        return longest_prefix
