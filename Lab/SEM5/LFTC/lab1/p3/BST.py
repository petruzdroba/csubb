class BST:
    class Node:
        __slots__ = ("name", "id", "left", "right")

        def __init__(self, name, id):
            self.name = name
            self.id = id
            self.left = self.right = None

    def __init__(self):
        self.root = None
        self.count = 0

    def add(self, name) -> int:
        if self.root is None:
            self.root = self.Node(name, self.count)
            self.count += 1

            return self.root.id

        current = self.root
        while True:
            # if already in tree, return
            if name == current.name:
                return current.id
            # find a spot in the subtree
            if name < current.name:
                side = "left"
            else:
                side = "right"

            child = getattr(current, side)
            # found empty spot
            if child is None:
                new = self.Node(name, self.count)
                self.count += 1
                setattr(current, side, new)
                return new.id

            current = child

    def inorder(self) -> tuple[list[str], dict[int, int]]:
        out, stack = [], []
        current = self.root

        while stack or current:
            while current:
                stack.append(current)
                current = current.left
            current = stack.pop()
            out.append((current.name, current.id))
            current = current.right

        names = [name for name, _ in out]
        line_of = {i: k for k, (_, i) in enumerate(out)}
        
        return names, line_of