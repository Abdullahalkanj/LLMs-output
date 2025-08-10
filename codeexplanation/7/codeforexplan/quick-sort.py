#  M. T. Goodrich, R. Tamassia, and M. H. Goldwasser, Data Structures and Algorithms in Python. Hoboken, NJ, USA: John Wiley & Sons, Inc., 2013.
# Code Fragments 7.7 and 7.8 p 264-265
# Section 12.3 “Quick-Sort”: pp 550–559
# Code Fragment 12.5: p 556
# Code Fragment 12.6: p 559

class Empty(Exception):
    pass

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
            raise Empty('Queue is empty')
        answer = self.head.element
        self.head = self.head.next
        self.size -= 1
        if self.is_empty():
            self.tail = None
        return answer

    def first(self):
        if self.is_empty():
            raise Empty('Queue is empty')
        return self.head.element

def quick_sort_queue(S):
    n = len(S)
    if n < 2:
        return
    p = S.first()
    L, E, G = LinkedQueue(), LinkedQueue(), LinkedQueue()
    while not S.is_empty():
        if S.first() < p:
            L.enqueue(S.dequeue())
        elif p < S.first():
            G.enqueue(S.dequeue())
        else:
            E.enqueue(S.dequeue())
    quick_sort_queue(L)
    quick_sort_queue(G)
    for Q in (L, E, G):
        while not Q.is_empty():
            S.enqueue(Q.dequeue())

def inplace_quick_sort(S, a, b):
    if a >= b:
        return
    pivot = S[b]
    left, right = a, b - 1
    while left <= right:
        while left <= right and S[left] < pivot:
            left += 1
        while left <= right and pivot < S[right]:
            right -= 1
        if left <= right:
            S[left], S[right] = S[right], S[left]
            left, right = left + 1, right - 1
    S[left], S[b] = S[b], S[left]
    inplace_quick_sort(S, a, left - 1)
    inplace_quick_sort(S, left + 1, b)

if __name__ == "__main__":
    Q = LinkedQueue()
    for i in [3, 1, 4, 1, 5, 9, 2]:
        Q.enqueue(i)
    quick_sort_queue(Q)
    print("Queue-sorted:", [Q.dequeue() for _ in range(len(Q))])

    A = [3, 1, 4, 1, 5, 9, 2]
    inplace_quick_sort(A, 0, len(A) - 1)
    print("List-sorted:", A)
