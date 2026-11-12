import json
import os
import sys

from BST import BST
from finite_automaton import FiniteAutomaton

BASE = os.path.dirname(os.path.abspath(__file__))

AUTOMATA_FILES = [
    ("string", "string_literal.txt"),
    ("real", "real_literal.txt"),
    ("integer", "int_literal.txt"),
    ("identifier", "identifier.txt"),
    ("operator", "operator.txt"),
]

WHITESPACE = " \t\r\f\v"
CLASSES = {"VAR", "CONST", "FIELD"}


def load_automata():
    return [
        (kind, FiniteAutomaton.read_from_file(os.path.join(BASE, "automata", name)))
        for kind, name in AUTOMATA_FILES
    ]


def is_word_char(c):
    return c == "_" or "a" <= c <= "z" or "A" <= c <= "Z" or "0" <= c <= "9"


def longest_match(text, automata):
    best_kind, best = None, ""

    for kind, automaton in automata:
        prefix = automaton.longest_accepted_prefix(text)

        if prefix and len(prefix) > len(best):
            best_kind, best = kind, prefix

    return best_kind, best


def tokenize(code, automata, token_table):
    """Returns (tokens, errors).
    tokens: list of (atom code, text); errors: list of (line, column, message)."""
    tokens = []
    errors = []

    for line_no, line in enumerate(code.split("\n"), start=1):
        pos = 0

        while pos < len(line):
            if line[pos] in WHITESPACE:
                pos += 1
                continue

            rest = line[pos:]
            col = pos + 1
            kind, lexeme = longest_match(rest, automata)

            # string error or unknown char
            if kind is None:
                if rest[0] == '"':
                    errors.append((line_no, col, f"unterminated string {rest}"))
                    pos = len(line)
                else:
                    errors.append((line_no, col, f"unknown character {rest[0]!r}"))
                    pos += 1
                continue

            if kind in ("integer", "real"):
                end = len(lexeme)

                if end < len(rest) and (is_word_char(rest[end]) or rest[end] == "."):
                    while end < len(rest) and (is_word_char(rest[end]) or rest[end] == "."):
                        end += 1

                    errors.append((line_no, col, f"invalid number '{rest[:end]}'"))
                    pos += end
                    continue

                tokens.append((token_table["CONST"], lexeme))

            elif kind == "string":
                tokens.append((token_table["CONST"], lexeme))

            elif kind == "identifier":
                if lexeme in token_table and lexeme not in CLASSES:
                    tokens.append((token_table[lexeme], lexeme))  # keyword
                else:
                    tokens.append((token_table["VAR"], lexeme))

            else:  # operator or separator
                if lexeme in token_table and lexeme not in CLASSES:
                    tokens.append((token_table[lexeme], lexeme))
                else:
                    errors.append((line_no, col, f"unknown character {lexeme!r}"))

            pos += len(lexeme)

    return tokens, errors


def mark_fields(tokens, token_table):
    result = []
    inside_struct = False
    brace_depth = 0

    for i, (atom_code, text) in enumerate(tokens):
        if text == "struct":
            inside_struct = True
            brace_depth = 0

        if inside_struct and text == "{":
            brace_depth += 1
            result.append((atom_code, text))
            continue

        if inside_struct and text == "}":
            brace_depth -= 1
            result.append((atom_code, text))
            if brace_depth == 0:
                inside_struct = False
            continue

        if atom_code == token_table["VAR"]:
            if inside_struct and i > 0 and tokens[i - 1][0] == token_table["double"]:
                result.append((token_table["FIELD"], text))
                continue

            if i > 0 and tokens[i - 1][0] == token_table["."]:
                result.append((token_table["FIELD"], text))
                continue

        result.append((atom_code, text))

    return result


def build_outputs(tokens, token_table):
    ids, consts = BST(), BST()
    fip = []

    for atom_code, text in tokens:
        if atom_code == token_table["VAR"]:
            fip.append((atom_code, "id", ids.add(text)))
        elif atom_code == token_table["CONST"]:
            fip.append((atom_code, "const", consts.add(text)))
        else:
            fip.append((atom_code, None, None))

    names_id, line_id = ids.inorder()
    names_c, line_c = consts.inorder()
    lines = {"id": line_id, "const": line_c}

    return names_id, names_c, [
        (atom_code, lines[which][id_] if which else -1)
        for atom_code, which, id_ in fip
    ]


def main():
    source = sys.argv[1] if len(sys.argv) > 1 else os.path.join(BASE, "program.txt")

    with open(os.path.join(BASE, "tokens.json"), "r") as f:
        token_table = json.load(f)

    with open(source, "r") as f:
        code = f.read()

    tokens, errors = tokenize(code, load_automata(), token_table)
    tokens = mark_fields(tokens, token_table)
    names_id, names_c, fip = build_outputs(tokens, token_table)

    with open(os.path.join(BASE, "TS_id.txt"), "w") as f:
        f.write("\n".join(names_id) + "\n")

    with open(os.path.join(BASE, "TS_const.txt"), "w") as f:
        f.write("\n".join(names_c) + "\n")

    with open(os.path.join(BASE, "FIP.txt"), "w") as f:
        for atom_code, pos in fip:
            f.write(f"{atom_code} {pos}\n")

    if errors:
        print("Lexical errors:")
        for line, col, msg in errors:
            print(f"  line {line}, column {col}: {msg}")
    else:
        print("No lexical errors.")


if __name__ == "__main__":
    main()
