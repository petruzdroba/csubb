import re
import json

with open('tokens.json', 'r') as f:
    token_table = json.load(f)

with open('program.txt', 'r') as f:
    code = f.read()

def tokenize(code):
    tokens = []
    
#    pattern = r'==|!=|<=|>=|<<|>>|&&|\|\||\w+|"[^"]*"|[(){};,."\'=+\-*/%<>]'
    pattern = r'==|!=|<=|>=|<<|>>|&&|\|\||\d+\.\d+|\d+|\w+|"[^"]*"|[(){};,."\'=+\-*/%<>?]'

    matches = re.findall(pattern, code)
    
    for match in matches:

        if match in token_table:
            tokens.append((token_table[match], match))

        elif match.startswith('"'):
            tokens.append((token_table["CONST"], match))

        elif match[0].isdigit():
            tokens.append((token_table["CONST"], match))

        else:
            tokens.append((token_table["VAR"], match))
    
    return tokens


tokens = tokenize(code)
for token in tokens:
    print(token)