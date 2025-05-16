# M. T. Goodrich, R. Tamassia, and M. H. Goldwasser, Data Structures and Algorithms in Python. Hoboken, NJ, USA: John Wiley & Sons, Inc., 2013. 
# DoublyLinkedBase: Code Fragments 7.11–7.12, pp. 273–274
# LinkedDeque: Code Fragment 7.13, p. 276
# LinkedQueue implementation on pp. 265–266 and in the LinkedDeque on p. 276)
class Empty(Exception):
    pass

class DoublyLinkedBase:
    class Node:
        def __init__(self, element, prev, next):
            self.element = element
            self.prev = prev
            self.next = next

    def __init__(self):
        self.header = self.Node(None, None, None)
        self.trailer = self.Node(None, None, None)
        self.header.next = self.trailer
        self.trailer.prev = self.header
        self.size = 0

    def __len__(self):
        return self.size

    def is_empty(self):
        return self.size == 0

    def insert_between(self, e, predecessor, successor):
        newest = self.Node(e, predecessor, successor)
        predecessor.next = newest
        successor.prev = newest
        self.size += 1
        return newest

    def delete_node(self, node):
        predecessor = node.prev
        successor = node.next
        predecessor.next = successor
        successor.prev = predecessor
        self.size -= 1
        element = node.element
        node.prev = node.next = node.element = None
        return element

class LinkedDeque(DoublyLinkedBase):
    def first(self):
        if self.is_empty():
            raise Empty("Deque is empty")
        return self.header.next.element

    def last(self):
        if self.is_empty():
            raise Empty("Deque is empty")
        return self.trailer.prev.element

    def insert_first(self, e):
        self.insert_between(e, self.header, self.header.next)

    def insert_last(self, e):
        self.insert_between(e, self.trailer.prev, self.trailer)

    def delete_first(self):
        if self.is_empty():
            raise Empty("Deque is empty")
        return self.delete_node(self.header.next)

    def delete_last(self):
        if self.is_empty():
            raise Empty("Deque is empty")
        return self.delete_node(self.trailer.prev)

if __name__ == "__main__":
    D = LinkedDeque()
    for i in [1, 2, 3]:
        D.insert_last(i)
    print("first ->", D.first())
    print("last ->", D.last())
    print("delete_first ->", D.delete_first())
    print("delete_last ->", D.delete_last())
    print("size ->", len(D))
