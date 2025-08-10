#  M. T. Goodrich, R. Tamassia, and M. H. Goldwasser, Data Structures and Algorithms in Python. Hoboken, NJ, USA: John Wiley & Sons, Inc., 2013.
# Pages:
# PriorityQueueBase (Code Fragment 9.1, pp. 365–366)
# HeapPriorityQueue (Code Fragments 9.4–9.5, pp. 377–379)
# Section 9.5 “Adaptable Priority Queues”: pp 390–394


class Empty(Exception):
    pass

class PriorityQueueBase:
    class Item:
        __slots__ = '_key', '_value'
        def __init__(self, k, v):
            self.key   = k
            self.value = v
        def __lt__(self, other):
            return self.key < other.key
    def is_empty(self):
        return len(self) == 0

class HeapPriorityQueue(PriorityQueueBase):
    def __init__(self):
        self.data = []
    def parent(self, j):       return (j - 1) // 2
    def left(self, j):         return 2*j + 1
    def right(self, j):        return 2*j + 2
    def has_left(self, j):     return self.left(j) < len(self.data)
    def has_right(self, j):    return self.right(j) < len(self.data)
    def swap(self, i, j):
        self.data[i], self.data[j] = self.data[j], self.data[i]
    def upheap(self, j):
        if j > 0:
            p = self.parent(j)
            if self.data[j] < self.data[p]:
                self.swap(j, p)
                self.upheap(p)
    def downheap(self, j):
        if self.has_left(j):
            left = self.left(j)
            small = left
            if self.has_right(j):
                right = self.right(j)
                if self.data[right] < self.data[left]:
                    small = right
            if self.data[small] < self.data[j]:
                self.swap(j, small)
                self.downheap(small)
    def __len__(self):
        return len(self.data)
    def add(self, key, value):
        self.data.append(self.Item(key, value))
        self.upheap(len(self.data) - 1)
    def min(self):
        if self.is_empty():
            raise Empty('Priority queue is empty')
        item = self.data[0]
        return (item.key, item.value)
    def remove_min(self):
        if self.is_empty():
            raise Empty('Priority queue is empty')
        self.swap(0, len(self.data) - 1)
        item = self.data.pop()
        self.downheap(0)
        return (item.key, item.value)

class AdaptableHeapPriorityQueue(HeapPriorityQueue):
    class Locator(HeapPriorityQueue.Item):
        __slots__ = '_index'
        def __init__(self, k, v, j):
            super().__init__(k, v)
            self.index = j
    def swap(self, i, j):
        super().swap(i, j)
        self.data[i].index = i
        self.data[j].index = j
    def bubble(self, j):
        if j > 0 and self.data[j] < self.data[self.parent(j)]:
            self.upheap(j)
        else:
            self.downheap(j)
    def add(self, key, value):
        token = self.Locator(key, value, len(self.data))
        self.data.append(token)
        self.upheap(len(self.data) - 1)
        return token
    def update(self, loc, newkey, newval):
        j = loc.index
        if not (0 <= j < len(self.data) and self.data[j] is loc):
            raise ValueError('Invalid locator')
        loc.key = newkey
        loc.value = newval
        self.bubble(j)
    def remove(self, loc):
        j = loc.index
        if not (0 <= j < len(self.data) and self.data[j] is loc):
            raise ValueError('Invalid locator')
        if j == len(self.data) - 1:
            self.data.pop()
        else:
            self.swap(j, len(self.data) - 1)
            self.data.pop()
            self.bubble(j)
        return (loc.key, loc.value)

if __name__ == "__main__":
    pq = AdaptableHeapPriorityQueue()
    locs = [pq.add(k, f"val{k}") for k in [5, 9, 3, 7]]
    print("min:", pq.min())
    pq.update(locs[1], 2, "val2")
    print("min after update:", pq.min())
    print("removed:", pq.remove(locs[2]))
    while not pq.is_empty():
        print("remove_min:", pq.remove_min())
