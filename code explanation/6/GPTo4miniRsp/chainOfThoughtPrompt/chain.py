# Thought for a couple of seconds


# Here’s what this code does:

# ---

# ## High‑level purpose

# 1. **Define a lightweight exception** (`Empty`) to signal operations on an empty structure.
# 2. **Implement a base class** `DoublyLinkedBase` that manages a doubly linked list with *header* and *trailer* sentinels.
# 3. **Provide fundamental primitives** in that base class:

#    * `insert_between(e, predecessor, successor)` to splice in a new node.
#    * `delete_node(node)` to remove a node cleanly.
# 4. **Build a deque** (double‑ended queue) on top of that base class as `LinkedDeque`, exposing operations to insert/delete at both ends and to inspect the first/last elements.
# 5. **Demo usage** under `if __name__ == "__main__":` by enqueuing a few items, querying front/back, deleting front/back, and printing the resulting size.

# ---

# ## Line‑by‑line explanation

# ```python
# class Empty(Exception):
#     pass
# ```

# * Defines a custom exception type `Empty`, which subclasses Python’s built‑in `Exception`.
# * Used to signal errors when attempting operations on an empty deque.

# ---

# ```python
# class DoublyLinkedBase:
# ```

# * Begins definition of the base class for a doubly linked list.

# ```python
#     class Node:
#         def __init__(self, element, prev, next):
#             self.element = element
#             self.prev = prev
#             self.next = next
# ```

# * **Nested** `Node` class to represent each element.
# * **Attributes**:

#   * `element`: the stored value
#   * `prev`: reference to the previous node
#   * `next`: reference to the next node

# ---

# ```python
#     def __init__(self):
#         self.header = self.Node(None, None, None)
#         self.trailer = self.Node(None, None, None)
#         self.header.next = self.trailer
#         self.trailer.prev = self.header
#         self.size = 0
# ```

# * **Constructor** for the linked‑list base:

#   1. Creates two sentinel nodes: `header` and `trailer`, both initially “empty” (`element=None`).
#   2. Links them to each other (`header.next → trailer`, `trailer.prev → header`), so the list is empty but correctly bounded.
#   3. Initializes `size = 0`.

# ---

# ```python
#     def __len__(self):
#         return self.size
# ```

# * Enables `len(instance)` to return the current number of real elements in the list.

# ---

# ```python
#     def is_empty(self):
#         return self.size == 0
# ```

# * Convenience method to check if the list has no elements.

# ---

# ```python
#     def insert_between(self, e, predecessor, successor):
#         newest = self.Node(e, predecessor, successor)
#         predecessor.next = newest
#         successor.prev = newest
#         self.size += 1
#         return newest
# ```

# * **Primitive insertion**: splices a new node holding `e` between two existing nodes.
# * Steps:

#   1. Create `newest` node with `prev=predecessor` and `next=successor`.
#   2. Update `predecessor.next` and `successor.prev` to point to the new node.
#   3. Increment size.
#   4. Return the newly created node.

# ---

# ```python
#     def delete_node(self, node):
#         predecessor = node.prev
#         successor = node.next
#         predecessor.next = successor
#         successor.prev = predecessor
#         self.size -= 1
#         element = node.element
#         node.prev = node.next = node.element = None
#         return element
# ```

# * **Primitive deletion**: removes `node` from the list.
# * Steps:

#   1. Identify its neighbors (`predecessor`, `successor`).
#   2. Bypass `node` by linking `predecessor → successor`.
#   3. Decrement size.
#   4. Extract and store `element` to return.
#   5. Clean up the removed node’s pointers (to help garbage collection / avoid accidental use).
#   6. Return the stored element.

# ---

# ```python
# class LinkedDeque(DoublyLinkedBase):
# ```

# * Defines `LinkedDeque` as a subclass of `DoublyLinkedBase`. It inherits all the low‑level linked‑list machinery.

# ```python
#     def first(self):
#         if self.is_empty():
#             raise Empty("Deque is empty")
#         return self.header.next.element
# ```

# * **Inspect**: return the element at the front, i.e. right after the header sentinel.
# * Raises `Empty` if there are no elements.

# ```python
#     def last(self):
#         if self.is_empty():
#             raise Empty("Deque is empty")
#         return self.trailer.prev.element
# ```

# * **Inspect**: return the element at the back, i.e. right before the trailer sentinel.
# * Similarly raises `Empty` if empty.

# ```python
#     def insert_first(self, e):
#         self.insert_between(e, self.header, self.header.next)
# ```

# * **Insert at front**: calls the base’s `insert_between`, placing the new element just after the header.

# ```python
#     def insert_last(self, e):
#         self.insert_between(e, self.trailer.prev, self.trailer)
# ```

# * **Insert at back**: splices just before the trailer sentinel.

# ```python
#     def delete_first(self):
#         if self.is_empty():
#             raise Empty("Deque is empty")
#         return self.delete_node(self.header.next)
# ```

# * **Remove from front**: checks emptiness, then deletes the node right after the header, returning its element.

# ```python
#     def delete_last(self):
#         if self.is_empty():
#             raise Empty("Deque is empty")
#         return self.delete_node(self.trailer.prev)
# ```

# * **Remove from back**: deletes the node right before the trailer, returning its element.

# ---

# ```python
# if __name__ == "__main__":
#     D = LinkedDeque()
#     for i in [1, 2, 3]:
#         D.insert_last(i)
#     print("first ->", D.first())
#     print("last ->", D.last())
#     print("delete_first ->", D.delete_first())
#     print("delete_last ->", D.delete_last())
#     print("size ->", len(D))
# ```

# * **Demo section** that only runs when the script is executed directly (not when imported):

#   1. Creates an empty `LinkedDeque` named `D`.
#   2. Inserts 1, 2, 3 at the back, so the deque holds \[1, 2, 3].
#   3. Prints the front element (`1`).
#   4. Prints the back element (`3`).
#   5. Deletes and prints the front (`1`), leaving \[2, 3].
#   6. Deletes and prints the back (`3`), leaving \[2].
#   7. Prints the remaining size (`1`).

# ---

# ### Summary

# * **`Empty`**: custom exception for empty‑deque errors.
# * **`DoublyLinkedBase`**: core doubly linked list with sentinels, insert/delete primitives, and size tracking.
# * **`LinkedDeque`**: public API for a deque (double‑ended queue), allowing O(1) insertion/removal at both ends and O(1) access to front/back.
# * **Demo**: shows basic usage and validates that the operations work as intended.

# Research Team Opinons 
