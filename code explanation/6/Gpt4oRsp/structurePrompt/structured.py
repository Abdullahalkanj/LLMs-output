# GPT4o output using structure prompt

# ------------------------------------------------------------------------------------------------------------------------------------------------------------------

# Here’s a detailed explanation of the functionality, input, and output of this code, both in words and with inline comments for clarity.

# ---

# ## 🔍 **Purpose:**
# This code implements a **double-ended queue (deque)** using a **doubly linked list**. It supports constant-time insertions and deletions from both ends of the queue.

# ---

# ## 🧾 **Code Breakdown and Comments:**

# ```python
# # Custom exception for handling empty deque access
# class Empty(Exception):
#     pass

# # Base class implementing a doubly linked list
# class DoublyLinkedBase:
#     # Node structure for doubly linked list
#     class Node:
#         def __init__(self, element, prev, next):
#             self.element = element  # data
#             self.prev = prev        # pointer to previous node
#             self.next = next        # pointer to next node

#     def __init__(self):
#         # Create dummy header and trailer nodes
#         self.header = self.Node(None, None, None)
#         self.trailer = self.Node(None, None, None)
#         # Link header to trailer and trailer to header
#         self.header.next = self.trailer
#         self.trailer.prev = self.header
#         self.size = 0  # number of elements

#     def __len__(self):
#         return self.size

#     def is_empty(self):
#         return self.size == 0

#     # Insert new node between two given nodes
#     def insert_between(self, e, predecessor, successor):
#         newest = self.Node(e, predecessor, successor)
#         predecessor.next = newest
#         successor.prev = newest
#         self.size += 1
#         return newest

#     # Remove given node from the list and return its element
#     def delete_node(self, node):
#         predecessor = node.prev
#         successor = node.next
#         predecessor.next = successor
#         successor.prev = predecessor
#         self.size -= 1
#         element = node.element
#         # Help garbage collection
#         node.prev = node.next = node.element = None
#         return element
# ```

# ---

# ## 📦 **LinkedDeque:**
# Extends `DoublyLinkedBase` to implement a **deque interface**.

# ```python
# class LinkedDeque(DoublyLinkedBase):
#     def first(self):
#         if self.is_empty():
#             raise Empty("Deque is empty")
#         return self.header.next.element  # Element at the front

#     def last(self):
#         if self.is_empty():
#             raise Empty("Deque is empty")
#         return self.trailer.prev.element  # Element at the end

#     def insert_first(self, e):
#         self.insert_between(e, self.header, self.header.next)

#     def insert_last(self, e):
#         self.insert_between(e, self.trailer.prev, self.trailer)

#     def delete_first(self):
#         if self.is_empty():
#             raise Empty("Deque is empty")
#         return self.delete_node(self.header.next)

#     def delete_last(self):
#         if self.is_empty():
#             raise Empty("Deque is empty")
#         return self.delete_node(self.trailer.prev)
# ```

# ---

# ## 🧪 **Main Block (Test Case):**
# ```python
# if __name__ == "__main__":
#     D = LinkedDeque()
#     for i in [1, 2, 3]:
#         D.insert_last(i)  # Insert elements at the back: [1, 2, 3]

#     print("first ->", D.first())         # Output: 1
#     print("last ->", D.last())           # Output: 3
#     print("delete_first ->", D.delete_first())  # Remove 1
#     print("delete_last ->", D.delete_last())    # Remove 3
#     print("size ->", len(D))             # Output: 1 (only element 2 remains)
# ```

# ---

# ## 🔁 **Input and Output Summary:**

# ### ✅ **Input:**
# - A series of insertions: `[1, 2, 3]` (at the end)
# - Operations: get first/last, delete first/last

# ### ✅ **Output:**
# ```
# first -> 1
# last -> 3
# delete_first -> 1
# delete_last -> 3
# size -> 1
# ```

# ---

# Would you like me to visualize how the doubly linked deque changes after each operation?
# --------------------------------------------------------------------------------------------------------

# Research Team 
# Fail. Explain almost nothing in the class LinkedDeque(DoublyLinkedBase)
