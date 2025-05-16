# GPT4omini output using basic prompt

# ------------------------------------------------------------------------------------------------------------------------------------------------------------------

# This snippet implements a **queue** (first‑in, first‑out) data structure using a singly linked list. Here’s a breakdown:

# ---

# ## 1. Inner `Node` class  
# ```python
# class Node:
#     def __init__(self, element, next):
#         self.element = element    # the stored value
#         self.next = next          # reference to the next node
# ```
# - Each `Node` holds an `element` (the data) and a pointer `next` to the following node in the list.

# ---

# ## 2. `LinkedQueue` initialization  
# ```python
# def __init__(self):
#     self.head = None   # front of the queue
#     self.tail = None   # back of the queue
#     self.size = 0      # number of elements
# ```
# - An empty queue starts with no nodes (`head` and `tail` are `None`) and size zero.

# ---

# ## 3. Size and emptiness checks  
# ```python
# def __len__(self):
#     return self.size

# def is_empty(self):
#     return self.size == 0
# ```
# - `len(q)` returns how many items are in the queue.
# - `is_empty()` tells you whether the queue has any elements.

# ---

# ## 4. Enqueue (add to back)  
# ```python
# def enqueue(self, e):
#     newest = self.Node(e, None)
#     if self.is_empty():
#         self.head = newest
#     else:
#         self.tail.next = newest
#     self.tail = newest
#     self.size += 1
# ```
# - Create a new node containing `e`.
# - If the queue is empty, both `head` and `tail` become this new node.
# - Otherwise, link the old `tail`’s `next` to the new node and update `tail`.
# - Increment `size`.

# ---

# ## 5. Dequeue (remove from front)  
# ```python
# def dequeue(self):
#     if self.is_empty():
#         raise Exception('Queue is empty')
#     answer = self.head.element
#     self.head = self.head.next
#     self.size -= 1
#     if self.is_empty():
#         self.tail = None
#     return answer
# ```
# - If empty, raises an error.
# - Otherwise, grab the element at `head`, advance `head` to the next node, decrement `size`.
# - If that removal made the queue empty, also set `tail` to `None`.
# - Return the removed element.

# ---

# ## 6. Demo in `__main__`  
# ```python
# if __name__ == "__main__":
#     Q = LinkedQueue()
#     for i in [10, 20, 30]:
#         print(f"enqueue({i})")
#         Q.enqueue(i)
#     print("dequeue() ->", Q.dequeue())
#     print("first remaining ->", Q.head.element if not Q.is_empty() else None)
#     print("current size:", Q.size)
# ```
# - Creates an empty queue `Q`.
# - Enqueues 10, 20, 30 in turn, printing each action.
# - Dequeues once (removing and printing `10`).
# - Prints the current front element (`20`) and the queue’s size (`2`).

# ---

# ### Summary  
# - **Data structure**: Queue implemented via linked nodes.  
# - **Operations**:
#   - **Enqueue**: O(1) to add at the tail.
#   - **Dequeue**: O(1) to remove from the head.  
# - **Use case**: When you need FIFO behavior without the overhead of shifting elements in a Python list.

# --------------------------------------------------------------------------------------------------------

# Research Team
# Pass good in all way, clear, detailed 