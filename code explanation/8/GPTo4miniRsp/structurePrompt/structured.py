# GPT4omini output using basic prompt

# ------------------------------------------------------------------------------------------------------------------------------------------------------------------


# Here’s a line‑by‑line walkthrough of the code with inline comments, followed by a high‑level description of each class/method’s purpose, inputs and outputs.

# ```python
# class Empty(Exception):
#     """Custom exception raised when attempting to access elements in an empty queue."""
#     pass

# class PriorityQueueBase:
#     """Abstract base class defining an Item and an is_empty test."""
#     class Item:
#         __slots__ = '_key', '_value'
#         def __init__(self, k, v):
#             # store the priority and associated value
#             self.key   = k
#             self.value = v
#         def __lt__(self, other):
#             # compare items by key for heap ordering
#             return self.key < other.key

#     def is_empty(self):
#         # True if no elements in the queue
#         return len(self) == 0


# class HeapPriorityQueue(PriorityQueueBase):
#     """Min-heap implementation of a priority queue."""
#     def __init__(self):
#         # underlying list of Item instances
#         self.data = []

#     # index arithmetic for parent/child in a binary heap
#     def parent(self, j):       return (j - 1) // 2
#     def left(self, j):         return 2*j + 1
#     def right(self, j):        return 2*j + 2
#     def has_left(self, j):     return self.left(j) < len(self.data)
#     def has_right(self, j):    return self.right(j) < len(self.data)

#     def swap(self, i, j):
#         # exchange two items in the heap
#         self.data[i], self.data[j] = self.data[j], self.data[i]

#     def upheap(self, j):
#         # move item at index j up to restore heap property
#         if j > 0:
#             p = self.parent(j)
#             if self.data[j] < self.data[p]:
#                 self.swap(j, p)
#                 self.upheap(p)

#     def downheap(self, j):
#         # move item at index j down to restore heap property
#         if self.has_left(j):
#             left = self.left(j)
#             small = left
#             if self.has_right(j):
#                 right = self.right(j)
#                 if self.data[right] < self.data[left]:
#                     small = right
#             if self.data[small] < self.data[j]:
#                 self.swap(j, small)
#                 self.downheap(small)

#     def __len__(self):
#         # number of items in the queue
#         return len(self.data)

#     def add(self, key, value):
#         """Insert a new key-value pair."""
#         self.data.append(self.Item(key, value))
#         self.upheap(len(self.data) - 1)

#     def min(self):
#         """Return—but do not remove—the (key, value) pair with minimum key."""
#         if self.is_empty():
#             raise Empty('Priority queue is empty')
#         item = self.data[0]
#         return (item.key, item.value)

#     def remove_min(self):
#         """Remove and return the (key, value) pair with minimum key."""
#         if self.is_empty():
#             raise Empty('Priority queue is empty')
#         # swap min with last, pop it, then restore heap
#         self.swap(0, len(self.data) - 1)
#         item = self.data.pop()
#         self.downheap(0)
#         return (item.key, item.value)


# class AdaptableHeapPriorityQueue(HeapPriorityQueue):
#     """Heap-based priority queue supporting update and remove by locator."""
#     class Locator(HeapPriorityQueue.Item):
#         __slots__ = '_index'
#         def __init__(self, k, v, j):
#             super().__init__(k, v)
#             # track its own current index in the heap
#             self.index = j

#     def swap(self, i, j):
#         # override to update each locator’s index after swapping
#         super().swap(i, j)
#         self.data[i].index = i
#         self.data[j].index = j

#     def bubble(self, j):
#         # choose upheap or downheap depending on new key
#         if j > 0 and self.data[j] < self.data[self.parent(j)]:
#             self.upheap(j)
#         else:
#             self.downheap(j)

#     def add(self, key, value):
#         """
#         Insert a new key-value pair and return a locator token
#         that the caller can use later to update or remove this entry.
#         """
#         token = self.Locator(key, value, len(self.data))
#         self.data.append(token)
#         self.upheap(len(self.data) - 1)
#         return token

#     def update(self, loc, newkey, newval):
#         """
#         Given a locator returned by add(), change its key and value.
#         Then restore heap order by bubbling the locator up or down.
#         """
#         j = loc.index
#         # validate locator
#         if not (0 <= j < len(self.data) and self.data[j] is loc):
#             raise ValueError('Invalid locator')
#         loc.key = newkey
#         loc.value = newval
#         self.bubble(j)

#     def remove(self, loc):
#         """
#         Given a locator, remove that entry from the heap
#         and return its (key, value) pair.
#         """
#         j = loc.index
#         if not (0 <= j < len(self.data) and self.data[j] is loc):
#             raise ValueError('Invalid locator')
#         # if it’s the last one, just pop; otherwise swap to end and pop
#         if j == len(self.data) - 1:
#             self.data.pop()
#         else:
#             self.swap(j, len(self.data) - 1)
#             self.data.pop()
#             self.bubble(j)
#         return (loc.key, loc.value)


# if __name__ == "__main__":
#     # Demo of AdaptableHeapPriorityQueue
#     pq = AdaptableHeapPriorityQueue()
#     # add four items (keys 5,9,3,7) and keep their locators
#     locs = [pq.add(k, f"val{k}") for k in [5, 9, 3, 7]]
#     print("min:", pq.min())                     
#     # → min: (3, 'val3')
#     pq.update(locs[1], 2, "val2")                
#     # change key 9 → 2; now this is the new minimum
#     print("min after update:", pq.min())        
#     # → min after update: (2, 'val2')
#     print("removed:", pq.remove(locs[2]))       
#     # remove the item that was originally key=3 (locator at index 2)
#     while not pq.is_empty():
#         print("remove_min:", pq.remove_min())
#     # sequentially pops remaining items in ascending key order
# ```

# ---

# ## High‑Level Overview

# ### `Empty`  
# - **What it is**: A custom exception class.  
# - **When it’s raised**: By `min()` or `remove_min()` when the queue has no items.

# ---

# ### `PriorityQueueBase`  
# - **Purpose**: Defines a simple `Item` container (with `key`, `value`) and a shared `is_empty()` check.  
# - **Key piece**: `Item.__lt__()` so that items compare by their `key`, enabling heap ordering.

# ---

# ### `HeapPriorityQueue`  
# - **Data structure**: A **binary min‑heap** stored in a Python list `self.data`.  
# - **Core methods**:  
#   - `add(key, value)`:  
#     - **Input**: any comparable `key`, plus an associated `value`.  
#     - **Side‑effect**: Appends a new `Item` and restores the heap via **upheap**.  
#     - **Return**: `None`.  
#   - `min()`:  
#     - **Input**: none.  
#     - **Output**: `(key, value)` of the smallest key in the queue.  
#     - **Exception**: raises `Empty` if queue empty.  
#   - `remove_min()`:  
#     - **Input**: none.  
#     - **Output**: removes and returns `(key, value)` of the smallest key.  
#     - **Exception**: raises `Empty` if queue empty.  
#   - Helper methods: `upheap`, `downheap`, index arithmetic, `swap()`, `__len__`.

# ---

# ### `AdaptableHeapPriorityQueue`  
# - **Extension** of `HeapPriorityQueue` allowing **locators** so you can later update or remove arbitrary entries, not just the minimum.  
# - **Locator tokens**: Instances of `Locator` that remember their index in the heap.  
# - **New methods**:  
#   - `add(key, value)`:  
#     - **Input**: same as base.  
#     - **Output**: returns a `locator` object.  
#   - `update(loc, newkey, newval)`:  
#     - **Input**: a valid locator (from a prior `add`), a new key, and new value.  
#     - **Side‑effect**: changes the entry in place and restores heap order via `bubble()`.  
#     - **Return**: `None`.  
#   - `remove(loc)`:  
#     - **Input**: a valid locator.  
#     - **Output**: removes that entry from the heap and returns its `(key, value)` pair.

# ---

# ### Demonstration (in `__main__`)

# 1. **Adding** four items with keys `[5,9,3,7]`.  
# 2. **Printing** the minimum → `(3, 'val3')`.  
# 3. **Updating** the item with original key `9` down to key `2`, making it the new min → `(2, 'val2')`.  
# 4. **Removing** the locator that pointed to the original key `3`.  
# 5. **Draining** the queue by successive `remove_min()` calls, printing each `(key, value)` in ascending order.

# ---

# This design gives you:  
# - **O(log n)** insertion and removal of the min,  
# - **O(1)** access to a locator,  
# - **O(log n)** update or removal of any entry, thanks to the locator/index bookkeeping.
# --------------------------------------------------------------------------------------------------------

# Research Team
# Fail, give a general thought without explaining the functionality, for example:

#     def downheap(self, j):
#         # move item at index j down to restore heap property
#         if self.has_left(j):
#             left = self.left(j)
#             small = left
#             if self.has_right(j):
#                 right = self.right(j)
#                 if self.data[right] < self.data[left]:
#                     small = right
#             if self.data[small] < self.data[j]:
#                 self.swap(j, small)
#                 self.downheap(small)