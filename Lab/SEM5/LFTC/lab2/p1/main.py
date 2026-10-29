
from finite_automaton import FiniteAutomaton

automaton = FiniteAutomaton.read_from_file(
    "automata/cpp_int_literal.txt"
)

while True:
    print("\nFinite Automaton Menu")
    print("1. Display states")
    print("2. Display alphabet")
    print("3. Display transitions")
    print("4. Display final states")
    print("5. Check sequence acceptance")
    print("6. Find longest accepted prefix")
    print("0. Exit")

    option = input("Choose an option: ")

    if option == "1":
        automaton.display_states()
    elif option == "2":
        automaton.display_alphabet()
    elif option == "3":
        automaton.display_transitions()
    elif option == "4":
        automaton.display_final_states()
    elif option == "5":
        sequence = input("Enter a sequence: ")
        try:
            print("Accepted:", automaton.accepts(sequence))
        except ValueError as error:
            print(error)
    elif option == "6":
        sequence = input("Enter a sequence: ")
        try:
            prefix = automaton.longest_accepted_prefix(sequence)
            print("Longest accepted prefix:", repr(prefix))
        except ValueError as error:
            print(error)
    elif option == "0":
        break
    else:
        print("Invalid option.")
