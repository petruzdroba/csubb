# Automaton File Format
[[LFTC_Finite_automata]]
![Diagrama](lftcp2.png)
## BNF Grammar

```EBNF
<automaton> ::= <states> <alphabet> <initial> <final> <transitions>

<states> ::= "states:" <identifier> <identifier-list> <newline>

<alphabet> ::= "alphabet:" <symbol> <symbol-list> <newline>

<initial> ::= "initial:" <identifier> <newline>

<final> ::= "final:" <identifier-list> <newline>

<transitions> ::= "transitions:" <newline> <transition-list>

<transition-list> ::= <transition> <newline> <transition-list>
                    | <transition> <newline>

<transition> ::= <identifier> <space> <symbol> <space> <identifier>

<identifier-list> ::= <identifier>
                    | <identifier> <space> <identifier-list>

<symbol-list> ::= <symbol>
                | <symbol> <space> <symbol-list>

<identifier> ::= <letter> <identifier-tail>

<identifier-tail> ::= <letter-or-digit> <identifier-tail>
                    | "_ " <identifier-tail>
                    | ε

<symbol> ::= <letter> | <digit>

<letter> ::= "a" | "b" | ... | "z"
           | "A" | "B" | ... | "Z" | "_"

<digit> ::= "0" | "1" | "2" | "3" | "4"
          | "5" | "6" | "7" | "8" | "9"

<letter-or-digit> ::= <letter> | <digit>

<space> ::= " "
<newline> ::= newline character
```

## Example Input

```
states: q_even q_odd
alphabet: 0 1
initial: q_even
final: q_even
transitions:
q_even 0 q_even
q_even 1 q_odd
q_odd 0 q_odd
q_odd 1 q_even
```

Last task see [[LFTC_Cpp_int_literal]]

Any number of B's
followed by even number of A's
followed by a B