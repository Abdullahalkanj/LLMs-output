# This code implements a **deque (double-ended queue)** using a **doubly linked list**. Here’s a breakdown of what each part of the code does:

# ---

# ### 1. **`class Empty(Exception):`**

# Defines a custom exception used to signal errors when attempting to access or remove elements from an empty deque.

# ---

# ### 2. **`class DoublyLinkedBase:`**

# Provides the **base functionality** for a doubly linked list:

# * **`Node` class**: Represents an element in the list with `element`, `prev` (pointer to previous node), and `next` (pointer to next node).
# * **`__init__`**: Creates an empty list with two sentinel nodes: `header` and `trailer`. These are dummy nodes that simplify insertions and deletions at the ends.
# * **`insert_between`**: Adds a new node between two existing nodes.
# * **`delete_node`**: Removes a node from the list and clears its links.

# ---

# ### 3. **`class LinkedDeque(DoublyLinkedBase):`**

# Extends the base to implement **deque operations**:

# * **`first()` / `last()`**: Returns the first or last element, raising `Empty` if the deque is empty.
# * **`insert_first(e)` / `insert_last(e)`**: Inserts `e` at the front or rear of the deque.
# * **`delete_first()` / `delete_last()`**: Removes and returns the first or last element, raising `Empty` if the deque is empty.

# ---

# ### 4. **Main block (`if __name__ == "__main__":`)**

# Demonstrates usage:

# * Creates a new `LinkedDeque`.
# * Inserts `1`, `2`, and `3` at the back.
# * Prints:

#   * First element (`1`)
#   * Last element (`3`)
#   * Result of deleting the first (`1`)
#   * Result of deleting the last (`3`)
#   * Final size of deque (`1`, since only `2` remains)

# ---

# ### Summary:

# This is a complete implementation of a **deque** using a **doubly linked list**, supporting constant-time insertions and deletions at both ends.

# Would you like a visual diagram of how the linked nodes are connected?

# Research Team Opinions 

# Fail. didnt goes with details, for example:
# in this part of the code:

#     def insert_between(self, e, predecessor, successor):
#         newest = self.Node(e, predecessor, successor)
#         predecessor.next = newest
#         successor.prev = newest
#         self.size += 1
#         return newest

#     def delete_node(self, node):
#         predecessor = node.prev
#         successor = node.next
#         predecessor.next = successor
#         successor.prev = predecessor
#         self.size -= 1
#         element = node.element
#         node.prev = node.next = node.element = None
#         return element

# the explanation was just:
# * **`insert_between`**: Adds a new node between two existing nodes.
# * **`delete_node`**: Removes a node from the list and clears its links.