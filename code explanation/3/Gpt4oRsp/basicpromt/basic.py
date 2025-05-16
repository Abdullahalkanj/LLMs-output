# GPT4o output using basic prompt

# ------------------------------------------------------------------------------------------------------------------------------------------------------------------


# This Python code implements a **Binary Search Tree (BST)** with basic operations: insertion, search, in-order traversal, and deletion. Here's a breakdown of what each part does:

# ---

# ### **Classes Defined**

# #### `class Node`
# - Represents a single node in the BST.
# - Has a `key` (value), and pointers to `left` and `right` child nodes.

# #### `class BST`
# - Represents the Binary Search Tree structure.
# - **Attributes**:
#   - `root`: The root node of the BST.
# - **Methods**:
#   - `insert(key)`: Public method to insert a key into the BST.
#   - `_insert_recursive(node, key)`: Helper method that recursively finds the correct place for the new key.
#   - `search(key)`: Searches for a node with the given key.
#   - `_search_recursive(node, key)`: Helper for recursive search.
#   - `inorder()`: Returns a list of keys from in-order traversal (sorted order for BST).
#   - `_inorder_recursive(node)`: Recursive generator for in-order traversal.
#   - `delete(key)`: Removes a key from the BST if it exists.
#   - `_delete_recursive(node, key)`: Helper method that handles deletion with all three BST cases:
#     1. Node with no children (leaf)
#     2. Node with one child
#     3. Node with two children (uses in-order successor)
#   - `_min_value_node(node)`: Finds the smallest key in the given subtree (used during deletion for two-child nodes).

# ---

# ### **Main Program Logic**

# ```python
# if __name__ == "__main__":
# ```
# - A BST is created and initialized with the keys `[50, 30, 20, 40, 70, 60, 80]`.
# - Then it prints the in-order traversal (should be sorted).
# - Deletes the keys `20`, `30`, and `50` one by one, printing the in-order traversal after each deletion.

# ---

# ### **Example Output**

# Before deletions:
# ```
# Inorder before deletions: [20, 30, 40, 50, 60, 70, 80]
# ```

# After deleting `20` (a leaf):
# ```
# Inorder after deleting 20 : [30, 40, 50, 60, 70, 80]
# ```

# After deleting `30` (node with one child):
# ```
# Inorder after deleting 30 : [40, 50, 60, 70, 80]
# ```

# After deleting `50` (node with two children):
# ```
# Inorder after deleting 50 : [40, 60, 70, 80]
# ```

# Would you like a visual diagram of the tree before and after deletions?

# --------------------------------------------------------------------------------------------------------

# // Research Team
# // Failled. It mention all the method but didnt explain the functionality.