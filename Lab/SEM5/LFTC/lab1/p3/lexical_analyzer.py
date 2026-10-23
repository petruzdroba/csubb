import re
import json
from BST import BST

with open('tokens.json', 'r') as f:
    token_table = json.load(f)

with open('program.txt', 'r') as f:
    code = f.read()

errors = []


def position(code, offset):
    line = code.count('\n', 0, offset) + 1
    col = offset - (code.rfind('\n', 0, offset) + 1) + 1
    return line, col


def tokenize(code):
    tokens = []
    pattern = (r'==|!=|<=|>=|<<|>>|&&|\|\||'
           r'\d+(?:\.\d+){2,}[A-Za-z_]\w*|\d+(?:\.\d+){2,}|'
           r'\d+(?:\.\d+)?[A-Za-z_]\w*|'
           r'\d+\.\d+|\d+|\w+|'
           r'"[^"\n]*"|"[^"\n]*|'
           r'[(){};,.=+\-*/%<>?]|\S')

    for m in re.finditer(pattern, code):
        match = m.group()
        line, col = position(code, m.start())

        if match in token_table:
            tokens.append((token_table[match], match))

        elif match.startswith('"'):
            if len(match) >= 2 and match.endswith('"'):
                tokens.append((token_table["CONST"], match))
            else:
                errors.append((line, col, f'unterminated string {match}'))

        elif match[0].isdigit():
            if re.fullmatch(r'\d+(\.\d+)?', match):
                tokens.append((token_table["CONST"], match))
            else:
                errors.append((line, col, f"invalid number '{match}'"))

        elif re.fullmatch(r'\w+', match):
            tokens.append((token_table["VAR"], match))

        else:
            errors.append((line, col, f'unknown character {match!r}'))

    return tokens


tokens = tokenize(code)

tokens = tokenize(code)

filtered_tokens = []
inside_struct = False
brace_depth = 0

for i, (atom_code, text) in enumerate(tokens):
    if text == "struct":
        inside_struct = True
        brace_depth = 0

    if inside_struct and text == "{":
        brace_depth += 1
        filtered_tokens.append((atom_code, text))
        continue

    if inside_struct and text == "}":
        brace_depth -= 1
        filtered_tokens.append((atom_code, text))
        if brace_depth == 0:
            inside_struct = False
        continue

    if atom_code == token_table["VAR"]:
        if inside_struct and i > 0 and tokens[i - 1][0] == token_table["double"]:
            filtered_tokens.append((token_table["FIELD"], text))
            continue

        if i > 0 and tokens[i - 1][0] == token_table["."]:
            filtered_tokens.append((token_table["FIELD"], text))
            continue

    filtered_tokens.append((atom_code, text))

tokens = filtered_tokens

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

with open("TS_id.txt", "w") as f:
    f.write("\n".join(names_id) + "\n")

with open("TS_const.txt", "w") as f:
    f.write("\n".join(names_c) + "\n")

with open("FIP.txt", "w") as f:
    for atom_code, which, id_ in fip:
        pos = lines[which][id_] if which else -1
        f.write(f"{atom_code} {pos}\n")

if errors:
    print("Lexical errors:")
    for line, col, msg in errors:
        print(f"  line {line}, column {col}: {msg}")
else:
    print("No lexical errors.")

print(ids.lookup(0))
print(consts.lookup(1))
