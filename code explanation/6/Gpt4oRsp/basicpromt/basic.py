# GPT4o output using basic prompt

# ------------------------------------------------------------------------------------------------------------------------------------------------------------------


# This script implements a **doubly linked‐list**–based deque (double‑ended queue) from scratch, including:

# ---

# ## 1. Custom Exception  
# ```python
# class Empty(Exception):
#     pass
# ```
# Defines an `Empty` exception to signal operations on an empty deque.

# ---

# ## 2. Base Doubly‑Linked Structure  
# ```python
# class DoublyLinkedBase:
#     class Node:
#         def __init__(self, element, prev, next):
#             self.element = element
#             self.prev = prev
#             self.next = next
#     …
# ```
# - **Inner `Node` class** holds an element and pointers to its predecessor (`prev`) and successor (`next`).  
# - The constructor (`__init__`) sets up two **sentinel nodes**:  
#   - `header` at the front  
#   - `trailer` at the back  
#   and links them to each other.  
# - Maintains `size` to track the number of real nodes (excludes sentinels).

# Key methods:
# - `__len__` / `is_empty`: report size or emptiness.  
# - `insert_between(e, predecessor, successor)`: splices a new node containing `e` between two existing nodes, updates pointers, increments size, and returns the new node.  
# - `delete_node(node)`: unlinks a given node, reconnects its neighbors, decrements size, cleanly removes references, and returns the deleted element.

# ---

# ## 3. `LinkedDeque` Subclass  
# ```python
# class LinkedDeque(DoublyLinkedBase):
#     …
# ```
# Builds on `DoublyLinkedBase` to provide the standard **deque interface** in O(1) time:

# - **Accessors**  
#   - `first()`: peek at the front element (raises `Empty` if empty)  
#   - `last()`: peek at the back element  

# - **Mutators**  
#   - `insert_first(e)`: add `e` at the front  
#   - `insert_last(e)`: add `e` at the back  
#   - `delete_first()`: remove and return the front element (raises if empty)  
#   - `delete_last()`: remove and return the back element  

# All of these just call the underlying `insert_between` or `delete_node` with the appropriate sentinel neighbors.

# ---

# ## 4. Usage Example  
# When run as a script, it demonstrates:

# ```python
# D = LinkedDeque()
# for i in [1, 2, 3]:
#     D.insert_last(i)
# print("first ->", D.first())       # 1
# print("last ->", D.last())         # 3
# print("delete_first ->", D.delete_first())  # removes & prints 1
# print("delete_last ->", D.delete_last())    # removes & prints 3
# print("size ->", len(D))           # now 1 element left
# ```

# This shows creating a deque, inserting elements at the end, inspecting both ends, deleting from both ends, and querying its size.

# --------------------------------------------------------------------------------------------------------

# Research Team
# good 