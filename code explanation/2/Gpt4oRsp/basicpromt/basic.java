// GPT4o output using basic prompt

// ------------------------------------------------------------------------------------------------------------------------------------------------------------------

// This code implements **heap sort**, a comparison-based sorting algorithm that uses a **binary heap** data structure to sort an array in-place. Here's a breakdown of each part:

// ---

// ### `leftChild(int i)`

// ```java
// private static int leftChild(int i) {
//     return 2 * i + 1;
// }
// ```

// - **Purpose**: Given a node index `i` in a binary heap (represented as an array), this function returns the index of its **left child**.
// - **In a binary heap**, for a node at index `i`:
//   - Left child = `2 * i + 1`
//   - Right child = `2 * i + 2`

// ---

// ### `percDown(...)`

// ```java
// private static <AnyType extends Comparable<? super AnyType>>
// void percDown(AnyType[] a, int i, int n)
// ```

// - **Purpose**: Percolates a node at index `i` down the heap to maintain the **max-heap property** (i.e., every parent is greater than or equal to its children).
// - `a` is the array (heap), `i` is the current index, and `n` is the size of the heap.
// - It swaps the node with its larger child (if needed) until it’s in the correct position.

// ---

// ### `heapsort(...)`

// ```java
// public static <AnyType extends Comparable<? super AnyType>>
// void heapsort(AnyType[] a)
// ```

// - **Purpose**: Sorts the array `a` using the **heap sort algorithm** in two main steps:

// #### Step 1: Build a max-heap
// ```java
// for (int i = a.length / 2 - 1; i >= 0; i--)
//     percDown(a, i, a.length);
// ```
// - Converts the array into a **max-heap** (heapify). Starts from the middle of the array and moves backward to the root.

// #### Step 2: Sort the array
// ```java
// for (int i = a.length - 1; i > 0; i--) {
//     swapReferences(a, 0, i);
//     percDown(a, 0, i);
// }
// ```
// - Repeatedly removes the max element (root), places it at the end, and rebuilds the heap for the remaining elements.

// ---

// ### Missing Function: `swapReferences(...)`

// The function `swapReferences(a, 0, i)` is assumed to swap two elements in the array. It's likely implemented like this:

// ```java
// private static <AnyType> void swapReferences(AnyType[] a, int index1, int index2) {
//     AnyType tmp = a[index1];
//     a[index1] = a[index2];
//     a[index2] = tmp;
// }
// ```

// ---

// ### Summary:

// - **Time complexity**: O(n log n)
// - **Space complexity**: O(1) (in-place)
// - **Stability**: Not stable
// - **Use case**: Efficient sorting when constant space is preferred

// Would you like a visual diagram of how the heap is built or sorted?
// --------------------------------------------------------------------------------------------------------

// Research Team
// Good