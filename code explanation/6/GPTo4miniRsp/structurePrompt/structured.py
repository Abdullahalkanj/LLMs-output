# Thought for a couple of seconds


# Below is the same code annotated with detailed comments, followed by a prose description of each class/method (with inputs, outputs, and exceptions), and finally a summary of the sample run (its input and printed output).

# ```python
# class Empty(Exception):
#     """Custom exception raised when attempting to access or remove
#     an element from an empty deque."""
#     pass

# class DoublyLinkedBase:
#     """A base class providing a doubly linked list structure with
#     header and trailer sentinels. Supports insertion and deletion
#     between any two existing nodes."""
    
#     class Node:
#         """Lightweight, nonpublic class for storing a doubly linked node."""
#         def __init__(self, element, prev, next):
#             self.element = element  # The data stored at this node
#             self.prev = prev        # Reference to previous node
#             self.next = next        # Reference to next node

#     def __init__(self):
#         """Create an empty list with header and trailer sentinels."""
#         self.header = self.Node(None, None, None)
#         self.trailer = self.Node(None, None, None)
#         # Initially, header is linked to trailer
#         self.header.next = self.trailer
#         self.trailer.prev = self.header
#         self.size = 0  # Number of real elements

#     def __len__(self):
#         """Return the number of elements in the list."""
#         return self.size

#     def is_empty(self):
#         """Return True if list is empty."""
#         return self.size == 0

#     def insert_between(self, e, predecessor, successor):
#         """Add element e between two existing nodes and return new node.
        
#         Inputs:
#           - e: the element to store
#           - predecessor: Node after which the new node goes
#           - successor: Node before which the new node goes
#         Effects:
#           - Links in a new Node between predecessor and successor
#           - Increments size
#         Returns:
#           - The newly-created Node
#         """
#         newest = self.Node(e, predecessor, successor)
#         predecessor.next = newest
#         successor.prev = newest
#         self.size += 1
#         return newest

#     def delete_node(self, node):
#         """Remove a non-sentinel node from the list and return its element.
        
#         Inputs:
#           - node: the Node to remove
#         Effects:
#           - Bypasses node in the linked structure
#           - Decrements size
#           - Cleans node’s fields to help garbage collection
#         Returns:
#           - The element formerly stored at node
#         """
#         predecessor = node.prev
#         successor = node.next
#         predecessor.next = successor
#         successor.prev = predecessor
#         self.size -= 1
        
#         element = node.element
#         # Help garbage collection
#         node.prev = node.next = node.element = None
#         return element

# class LinkedDeque(DoublyLinkedBase):
#     """Deque implementation based on a DoublyLinkedBase. Supports
#     FIFO and LIFO operations from both ends."""
    
#     def first(self):
#         """Return (but do not remove) the element at the front of the deque.
        
#         Output:
#           - The front element
#         Raises:
#           - Empty: if the deque is empty
#         """
#         if self.is_empty():
#             raise Empty("Deque is empty")
#         return self.header.next.element

#     def last(self):
#         """Return (but do not remove) the element at the back of the deque.
        
#         Output:
#           - The back element
#         Raises:
#           - Empty: if the deque is empty
#         """
#         if self.is_empty():
#             raise Empty("Deque is empty")
#         return self.trailer.prev.element

#     def insert_first(self, e):
#         """Add element e to the front of the deque.
        
#         Input:
#           - e: element to add
#         """
#         self.insert_between(e, self.header, self.header.next)

#     def insert_last(self, e):
#         """Add element e to the back of the deque.
        
#         Input:
#           - e: element to add
#         """
#         self.insert_between(e, self.trailer.prev, self.trailer)

#     def delete_first(self):
#         """Remove and return the element from the front of the deque.
        
#         Output:
#           - The removed front element
#         Raises:
#           - Empty: if the deque is empty
#         """
#         if self.is_empty():
#             raise Empty("Deque is empty")
#         return self.delete_node(self.header.next)

#     def delete_last(self):
#         """Remove and return the element from the back of the deque.
        
#         Output:
#           - The removed back element
#         Raises:
#           - Empty: if the deque is empty
#         """
#         if self.is_empty():
#             raise Empty("Deque is empty")
#         return self.delete_node(self.trailer.prev)

# if __name__ == "__main__":
#     # -------------------------
#     # Sample usage / demonstration
#     # -------------------------
#     D = LinkedDeque()
#     for i in [1, 2, 3]:
#         D.insert_last(i)            # enqueue 1, then 2, then 3
#     print("first ->", D.first())    # peek front
#     print("last ->", D.last())      # peek back
#     print("delete_first ->", D.delete_first())  # dequeue front
#     print("delete_last ->", D.delete_last())    # dequeue back
#     print("size ->", len(D))        # remaining size
# ```

# ---

# ## Method Summary

# | Method                    | Description                                                 | Input                                 | Output / Return Value             | Exceptions       |
# | ------------------------- | ----------------------------------------------------------- | ------------------------------------- | --------------------------------- | ---------------- |
# | `__len__`                 | Number of elements in the deque                             | —                                     | `int` size                        | —                |
# | `is_empty`                | Whether deque has zero elements                             | —                                     | `bool`                            | —                |
# | `first`                   | Peek at front element                                       | —                                     | Element at front                  | `Empty` if empty |
# | `last`                    | Peek at back element                                        | —                                     | Element at back                   | `Empty` if empty |
# | `insert_first(e)`         | Add `e` to the front                                        | `e` (any)                             | None                              | —                |
# | `insert_last(e)`          | Add `e` to the back                                         | `e` (any)                             | None                              | —                |
# | `delete_first`            | Remove and return front                                     | —                                     | Removed front element             | `Empty` if empty |
# | `delete_last`             | Remove and return back                                      | —                                     | Removed back element              | `Empty` if empty |
# | `insert_between(e, p, s)` | (protected) Insert `e` between nodes `p` and `s`            | `e`, predecessor node, successor node | Newly created `Node`              | —                |
# | `delete_node(node)`       | (protected) Remove given node and return its stored element | Node to delete                        | The element from the removed node | —                |

# ---

# ## Sample Run: Input & Output

# **Input** (in `__main__` block):

# ```python
# # Start with empty deque D
# D.insert_last(1)
# D.insert_last(2)
# D.insert_last(3)
# print("first ->", D.first())
# print("last ->", D.last())
# print("delete_first ->", D.delete_first())
# print("delete_last ->", D.delete_last())
# print("size ->", len(D))
# ```

# **Output** (printed to console):

# ```
# first -> 1
# last -> 3
# delete_first -> 1
# delete_last -> 3
# size -> 1
# ```

# * After inserting `[1, 2, 3]`, the front is `1` and the back is `3`.
# * Removing first returns `1`, removing last returns `3`, leaving only `[2]` so `len(D)` is `1`.

# Researsh Team Opinions
# Perfect