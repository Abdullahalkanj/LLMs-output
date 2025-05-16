# GPT4o output using chain of thought prompt

# ------------------------------------------------------------------------------------------------------------------------------------------------------------------


# ### **Purpose of the Code**

# This Python code defines a **doubly linked list-based deque (double-ended queue)** structure. It allows insertion and deletion of elements from both ends efficiently. It is implemented using an object-oriented approach with inheritance. There are three main components:

# 1. **`Empty` Exception** – Raised when deque operations are attempted on an empty deque.
# 2. **`DoublyLinkedBase` Class** – Provides the fundamental doubly linked list structure and core methods for insertion and deletion of nodes.
# 3. **`LinkedDeque` Class** – Inherits from `DoublyLinkedBase` and offers deque-specific operations like insert/delete from front and rear.
# 4. **Main Block** – Demonstrates basic usage of the deque.

# ---

# ### **Line-by-Line Explanation**

# ```python
# class Empty(Exception):
#     pass
# ```
# - Defines a custom exception class `Empty`, used to raise errors when deque operations are invalid (like removing from an empty deque).

# ---

# ```python
# class DoublyLinkedBase:
# ```
# - Declares a base class for a doubly linked list.

# ```python
#     class Node:
#         def __init__(self, element, prev, next):
#             self.element = element
#             self.prev = prev
#             self.next = next
# ```
# - An inner class representing a node in the doubly linked list. Each node stores:
#   - `element`: The data value.
#   - `prev`: Reference to the previous node.
#   - `next`: Reference to the next node.

# ---

# ```python
#     def __init__(self):
#         self.header = self.Node(None, None, None)
#         self.trailer = self.Node(None, None, None)
#         self.header.next = self.trailer
#         self.trailer.prev = self.header
#         self.size = 0
# ```
# - Initializes an empty list with:
#   - A **header** node (sentinel at the front).
#   - A **trailer** node (sentinel at the back).
#   - Header's `next` points to trailer; trailer's `prev` points to header.
#   - `size` tracks the number of actual elements (not including sentinels).

# ---

# ```python
#     def __len__(self):
#         return self.size
# ```
# - Returns the current number of elements in the list.

# ```python
#     def is_empty(self):
#         return self.size == 0
# ```
# - Returns `True` if the list is empty, `False` otherwise.

# ---

# ```python
#     def insert_between(self, e, predecessor, successor):
#         newest = self.Node(e, predecessor, successor)
#         predecessor.next = newest
#         successor.prev = newest
#         self.size += 1
#         return newest
# ```
# - Inserts a new node with element `e` between two given nodes (`predecessor` and `successor`).
# - Adjusts pointers and increments the size.
# - Returns the newly created node.

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
# - Deletes a specified node from the list:
#   - Updates surrounding node pointers to bypass the target node.
#   - Clears the node's data and links.
#   - Decreases size and returns the removed element.

# ---

# ```python
# class LinkedDeque(DoublyLinkedBase):
# ```
# - Subclass of `DoublyLinkedBase`, representing a double-ended queue.

# ```python
#     def first(self):
#         if self.is_empty():
#             raise Empty("Deque is empty")
#         return self.header.next.element
# ```
# - Returns the element at the front of the deque. Raises `Empty` if deque is empty.

# ```python
#     def last(self):
#         if self.is_empty():
#             raise Empty("Deque is empty")
#         return self.trailer.prev.element
# ```
# - Returns the element at the back of the deque. Raises `Empty` if deque is empty.

# ```python
#     def insert_first(self, e):
#         self.insert_between(e, self.header, self.header.next)
# ```
# - Inserts an element `e` at the front of the deque.

# ```python
#     def insert_last(self, e):
#         self.insert_between(e, self.trailer.prev, self.trailer)
# ```
# - Inserts an element `e` at the back of the deque.

# ```python
#     def delete_first(self):
#         if self.is_empty():
#             raise Empty("Deque is empty")
#         return self.delete_node(self.header.next)
# ```
# - Removes and returns the front element. Raises `Empty` if deque is empty.

# ```python
#     def delete_last(self):
#         if self.is_empty():
#             raise Empty("Deque is empty")
#         return self.delete_node(self.trailer.prev)
# ```
# - Removes and returns the back element. Raises `Empty` if deque is empty.

# ---

# ```python
# if __name__ == "__main__":
#     D = LinkedDeque()
# ```
# - Starts the test block. Creates an instance of `LinkedDeque`.

# ```python
#     for i in [1, 2, 3]:
#         D.insert_last(i)
# ```
# - Inserts `1`, `2`, and `3` at the end of the deque.

# ```python
#     print("first ->", D.first())
# ```
# - Prints the first element (should be `1`).

# ```python
#     print("last ->", D.last())
# ```
# - Prints the last element (should be `3`).

# ```python
#     print("delete_first ->", D.delete_first())
# ```
# - Removes and prints the first element (should remove `1`).

# ```python
#     print("delete_last ->", D.delete_last())
# ```
# - Removes and prints the last element (should remove `3`).

# ```python
#     print("size ->", len(D))
# ```
# - Prints the size of the deque (should be `1`, as only `2` remains).

# ---

# Would you like a diagram to visualize how the nodes are connected in memory?

# --------------------------------------------------------------------------------------------------------

# Research Team
# Pass good in all way, clear, detailed 