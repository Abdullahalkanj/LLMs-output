# M. T. Goodrich, R. Tamassia, and M. H. Goldwasser, Data Structures and Algorithms in Python. Hoboken, NJ, USA: John Wiley & Sons, Inc., 2013. [Book]

class Node:
    def __init__(self, key):
        self.key = key
        self.left = None
        self.right = None

class BST:
    def __init__(self):
        self.root = None

    def insert(self, key):
        self.root = self._insert_recursive(self.root, key)

    def _insert_recursive(self, node, key):
        if node is None:
            return Node(key)
        if key < node.key:
            node.left = self._insert_recursive(node.left, key)
        elif key > node.key:
            node.right = self._insert_recursive(node.right, key)
        return node

    def search(self, key):
        return self._search_recursive(self.root, key)

    def _search_recursive(self, node, key):
        if node is None or node.key == key:
            return node
        if key < node.key:
            return self._search_recursive(node.left, key)
        else:
            return self._search_recursive(node.right, key)

    def inorder(self):
        return list(self._inorder_recursive(self.root))

    def _inorder_recursive(self, node):
        if node:
            yield from self._inorder_recursive(node.left)
            yield node.key
            yield from self._inorder_recursive(node.right)

    def delete(self, key):
        self.root = self._delete_recursive(self.root, key)

    def _delete_recursive(self, node, key):
        if node is None:
            return None
        if key < node.key:
            node.left = self._delete_recursive(node.left, key)
        elif key > node.key:
            node.right = self._delete_recursive(node.right, key)
        else:
            if node.left is None:
                return node.right
            elif node.right is None:
                return node.left
            succ = self._min_value_node(node.right)
            node.key = succ.key
            node.right = self._delete_recursive(node.right, succ.key)
        return node

    def _min_value_node(self, node):
        current = node
        while current.left is not None:
            current = current.left
        return current

if __name__ == "__main__":
    bst = BST()
    for key in [50, 30, 20, 40, 70, 60, 80]:
        bst.insert(key)
    print("Inorder before deletions:", bst.inorder())
    for key in (20, 30, 50):
        bst.delete(key)
        print("Inorder after deleting", key, ":", bst.inorder())
