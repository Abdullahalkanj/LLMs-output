// GPT4o output using structur prompt

// ------------------------------------------------------------------------------------------------------------------------------------------------------------------


// This code is a **Java implementation of the Heapsort algorithm**, which is a comparison-based sorting technique based on a binary heap data structure. It consists of helper functions for managing the heap and a main function to sort an array.

// ---

// ### 🔹 **Functionality Overview (in words):**

// 1. **`leftChild(int i)`**:
//    - Calculates the left child index of a node in a binary heap.
//    - **Input:** `i` – index of a node in the heap.
//    - **Output:** Index of its left child (`2 * i + 1`).

// 2. **`percDown(AnyType[] a, int i, int n)`**:
//    - Performs the *"percolate down"* operation on a binary heap.
//    - Ensures that the subtree rooted at index `i` maintains the **max-heap** property.
//    - **Input:**
//      - `a` – the heap array.
//      - `i` – index of the node to percolate down.
//      - `n` – number of elements in the heap to consider.
//    - **Output:** The heap is modified in place.

// 3. **`heapsort(AnyType[] a)`**:
//    - Sorts the input array using the Heapsort algorithm.
//    - First, it builds a max-heap using `percDown`.
//    - Then it repeatedly removes the largest element (root of heap), puts it at the end, and re-heapifies the remaining heap.
//    - **Input:** `a` – the array to be sorted.
//    - **Output:** The array `a` is sorted in **ascending order** (in-place).

// ---

// ### 🔹 **Detailed Comments in Code:**

// ```java
// // Returns the index of the left child of a given node in a binary heap.
// private static int leftChild(int i) {
//     return 2 * i + 1;
// }

// // Percolates the element at index i down the heap to restore heap order.
// // a: the array representing the heap
// // i: index to percolate down from
// // n: size of the heap (can be less than a.length during sorting phase)
// private static <AnyType extends Comparable<? super AnyType>>
// void percDown(AnyType[] a, int i, int n) {
//     int child;
//     AnyType tmp = a[i];  // Save the element to be moved

//     // While there is at least one child
//     for (; leftChild(i) < n; i = child) {
//         child = leftChild(i);

//         // Select the larger of the two children
//         if (child != n - 1 && a[child].compareTo(a[child + 1]) < 0)
//             child++;

//         // If the child is larger than tmp, move the child up
//         if (tmp.compareTo(a[child]) < 0)
//             a[i] = a[child];
//         else
//             break;
//     }
//     a[i] = tmp;  // Place the saved element in its correct position
// }

// // Sorts the array using the Heapsort algorithm.
// // a: the array to be sorted
// public static <AnyType extends Comparable<? super AnyType>>
// void heapsort(AnyType[] a) {
//     // Build heap (rearrange array)
//     for (int i = a.length / 2 - 1; i >= 0; i--)
//         percDown(a, i, a.length);

//     // Extract elements from heap one by one
//     for (int i = a.length - 1; i > 0; i--) {
//         swapReferences(a, 0, i);     // Move current root to end
//         percDown(a, 0, i);           // Call max heapify on the reduced heap
//     }
// }
// ```

// > **Note:** The method `swapReferences(a, 0, i)` is not defined in the code you provided. It is expected to swap elements at index `0` and `i` in array `a`. It should look something like:
// ```java
// private static <AnyType> void swapReferences(AnyType[] a, int i, int j) {
//     AnyType temp = a[i];
//     a[i] = a[j];
//     a[j] = temp;
// }
// ```

// Would you like me to include that missing `swapReferences` method as well in full?

// --------------------------------------------------------------------------------------------------------

// Research Team
// Pass good in all way, clear, detailed 