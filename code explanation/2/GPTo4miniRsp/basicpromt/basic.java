// GPT4omini output using basic prompt

// ------------------------------------------------------------------------------------------------------------------------------------------------------------------


// This is a classic in-place, generic implementation of **heap-sort** in Java.  It works by first turning the array into a max-heap, then repeatedly removing the largest element and restoring the heap property. Here’s what each piece does:

// ---

// ### 1. `leftChild(int i)`

// ```java
// private static int leftChild(int i) {
//     return 2 * i + 1;
// }
// ```

// - If you store a binary heap in a zero-based array, the left child of node at index `i` lives at `2*i + 1`.  
// - Likewise (though not shown), the right child would be at `2*i + 2`.

// ---

// ### 2. `percDown(AnyType[] a, int i, int n)`

// ```java
// private static <AnyType extends Comparable<? super AnyType>>
// void percDown(AnyType[] a, int i, int n) {
//     int child;
//     AnyType tmp;

//     // save the value we’re percolating down
//     for (tmp = a[i]; leftChild(i) < n; i = child) {
//         // pick the larger of the two children
//         child = leftChild(i);
//         if (child != n - 1
//             && a[child].compareTo(a[child + 1]) < 0)
//         {
//             child++;
//         }

//         // if the child is larger than tmp, move it up
//         if (tmp.compareTo(a[child]) < 0) {
//             a[i] = a[child];
//         } else {
//             break;   // heap property is restored
//         }
//     }
//     // put tmp in its final spot
//     a[i] = tmp;
// }
// ```

// - **Purpose**: “Percolate down” the element at index `i` within the subarray `a[0 .. n−1]` so that the subtree rooted at `i` becomes a valid max-heap again.
// - **How**:
//   1. Save `tmp = a[i]`.
//   2. While `i` has at least a left child (`leftChild(i) < n`):
//      - Let `child = leftChild(i)`.
//      - If there is a right child (`child + 1 < n`) and it’s larger, use that one instead (`child++`).
//      - If that larger child is bigger than `tmp`, move the child up to `a[i]`, and continue percolating down from the child’s index.
//      - Otherwise, you’re done—`tmp` belongs at `i`.
//   3. Finally, write `tmp` into its proper spot.

// This runs in O(h) time for a subtree of height h, so up to O(log n) in a heap of size n.

// ---

// ### 3. `heapsort(AnyType[] a)`

// ```java
// public static <AnyType extends Comparable<? super AnyType>>
// void heapsort(AnyType[] a) {
//     // 1) Build (max-)heap in-place
//     for (int i = a.length / 2 - 1; i >= 0; i--)
//         percDown(a, i, a.length);

//     // 2) Repeatedly remove max and rebuild heap
//     for (int i = a.length - 1; i > 0; i--) {
//         swapReferences(a, 0, i);    // move current max to the end
//         percDown(a, 0, i);          // restore heap on a[0..i-1]
//     }
// }
// ```

// 1. **Heap construction**  
//    We start from the last non-leaf node at index `a.length/2 - 1` and percolate each one down.  By the time `i` reaches 0, the entire array satisfies the max-heap property (largest element at `a[0]`).

// 2. **Sort-down phase**  
//    Then we shrink the “active” heap one element at a time:
//    - Swap the root (`a[0]`, the maximum) with `a[i]` (the last element of the heap).
//    - Treat the heap as size `i` (ignoring the now-sorted tail) and call `percDown(a, 0, i)` to re-heapify.
//    - Repeat until `i == 1`.  At that point the array is sorted in **ascending order**.

// ---

// ### Key Characteristics

// - **Time complexity**: O(n log n) worst- and average-case.
// - **Space complexity**: O(1) extra space (in-place).
// - **Stability**: Not stable (equal elements may be reordered).
// - **Generics**: Works on any array of `Comparable` elements.

// In short: **this code implements an in-place, generic heap-sort** for arrays of any comparable type.

// --------------------------------------------------------------------------------------------------------

// Research Team
// Pass good in all way, clear, detailed 