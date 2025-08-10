# M. T. Goodrich, R. Tamassia, and M. H. Goldwasser, Data Structures and Algorithms in Python. Hoboken, NJ, USA: John Wiley & Sons, Inc., 2013. [Book]
class LinkedQueue:
    class Node:
        def __init__(self, element, next):
            self.element = element
            self.next = next

    def __init__(self):
        self.head = None
        self.tail = None
        self.size = 0

    def __len__(self):
        return self.size

    def is_empty(self):
        return self.size == 0

    def enqueue(self, e):
        newest = self.Node(e, None)
        if self.is_empty():
            self.head = newest
        else:
            self.tail.next = newest
        self.tail = newest
        self.size += 1

    def dequeue(self):
        if self.is_empty():
            raise Exception('Queue is empty')
        answer = self.head.element
        self.head = self.head.next
        self.size -= 1
        if self.is_empty():
            self.tail = None
        return answer

if __name__ == "__main__":
    Q = LinkedQueue()
    for i in [10, 20, 30]:
        print(f"enqueue({i})")
        Q.enqueue(i)
    print("dequeue() ->", Q.dequeue())
    print("first remaining ->", Q.head.element if not Q.is_empty() else None)
    print("current size:", Q.size)
