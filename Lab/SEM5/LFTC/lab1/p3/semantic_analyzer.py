
def check_semantics(tokens, token_table):
    errors = []
    types = {"int", "double", "string"}
    structs = {}
    variables = {}
    declared_types = set(types)

    def error(index, message):
        errors.append((index, message))

    def find_end(index):
        depth = 0
        for i in range(index, len(tokens)):
            if tokens[i][1] == "{":
                depth += 1
            elif tokens[i][1] == "}":
                depth -= 1
                if depth == 0:
                    return i
        return len(tokens)

    def read_declarations(start, end, target):
        i = start
        while i < end:
            if tokens[i][1] in declared_types:
                type_name = tokens[i][1]
                i += 1

                while i < end and tokens[i][1] != ";":
                    name = tokens[i][1]
                    if tokens[i][0] == token_table["VAR"]:
                        if name in target:
                            error(i, f"Duplicate declaration '{name}'")
                        else:
                            target[name] = type_name
                    i += 1
                i += 1
            else:
                i += 1

    i = 0
    while i < len(tokens):
        if tokens[i][1] == "struct" and i + 2 < len(tokens):
            name = tokens[i + 1][1]
            opening = i + 2

            if tokens[opening][1] == "{":
                closing = find_end(opening)

                if name in declared_types:
                    error(i + 1, f"Duplicate type '{name}'")
                else:
                    declared_types.add(name)
                    fields = {}
                    read_declarations(opening + 1, closing, fields)
                    structs[name] = fields

                i = closing + 1
                continue
        i += 1

    main = next(
        (i for i, (_, text) in enumerate(tokens) if text == "main"),
        None
    )

    if main is None:
        return errors

    opening = next(
        (i for i in range(main, len(tokens)) if tokens[i][1] == "{"),
        len(tokens)
    )
    closing = find_end(opening)
    read_declarations(opening + 1, closing, variables)

    keywords = {
        "main", "cin", "cout", "return", "if", "else", "while",
        "struct", "int", "double", "string"
    }

    for i, (atom_code, name) in enumerate(tokens):
        if atom_code != token_table["VAR"] or name in keywords:
            continue

        previous = tokens[i - 1][1] if i > 0 else ""
        following = tokens[i + 1][1] if i + 1 < len(tokens) else ""

        if previous in declared_types:
            continue

        if previous == ".":
            continue

        if following == ".":
            if name not in variables:
                error(i, f"Undeclared variable '{name}'")
            continue

        if name not in variables and name not in declared_types:
            error(i, f"Undeclared variable '{name}'")

    return errors
