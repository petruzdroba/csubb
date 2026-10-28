
from finite_automaton import FiniteAutomaton

# automaton = FiniteAutomaton.read_from_keyboard()
automaton = FiniteAutomaton.read_from_file(
    "automata/cpp_int_literal.txt"
)

sequence = input("Sequence: ")
prefix = automaton.longest_accepted_prefix(sequence)

if prefix is None:
    print("No accepted prefix.")
else:
    print("Longest accepted prefix:", repr(prefix))

print("Accepted:", automaton.accepts(sequence))
