// Claude output using structure prompt

// ------------------------------------------------------------------------------------------------------------------------------------------------------------------


// I'll provide a detailed explanation of this code, which implements the heap sort algorithm in Java.

// ## Overview

// This code implements a heap sort algorithm using a binary heap data structure. Heap sort is an efficient comparison-based sorting algorithm with O(n log n) time complexity. The implementation uses a max-heap to sort elements in ascending order.

// ## Function Breakdown

// ### 1. `leftChild` Function

// ```java
// private static int leftChild(int i) {
//     return 2 * i + 1;
// }
// ```

// **Functionality**: This helper function calculates the index of the left child of a node in a binary heap represented as an array.

// **Input**: 
// - `i`: The index of the parent node in the array

// **Output**: 
// - The index of the left child of node `i`

// **How It Works**: In a zero-indexed array representation of a binary heap, the left child of a node at index `i` is located at index `2*i + 1`.

// ### 2. `percDown` Function

// ```java
// private static <AnyType extends Comparable<? super AnyType>>
// void percDown(AnyType[] a, int i, int n) {
//     int child;
//     AnyType tmp;

//     for(tmp = a[i]; leftChild(i) < n; i = child) {
//         child = leftChild(i);
//         if(child != n - 1 && a[child].compareTo(a[child + 1]) < 0)
//             child++;
//         if(tmp.compareTo(a[child]) < 0)
//             a[i] = a[child];
//         else
//             break;
//     }
//     a[i] = tmp;
// }
// ```

// **Functionality**: This is the "percolate down" operation for maintaining the heap property. It's used when an element might be violating the heap property by being smaller than its children.

// **Input**:
// - `a`: An array of comparable elements
// - `i`: The index of the element that might violate the heap property
// - `n`: The size of the heap (not necessarily the full array size)

// **Output**: 
// - No return value; modifies the array `a` in-place

// **How It Works**:
// 1. Saves the value at position `i` in `tmp`
// 2. While the node has a left child (within bounds):
//    - Finds the left child's index
//    - If the right child exists and is larger than the left child, selects the right child instead (this is the comparison `a[child].compareTo(a[child + 1]) < 0`)
//    - If the selected child is larger than the current value `tmp`, moves the child up
//    - Otherwise, the heap property is satisfied, so break
// 3. Places the original value `tmp` at its final position

// ### 3. `heapsort` Function

// ```java
// public static <AnyType extends Comparable<? super AnyType>>
// void heapsort(AnyType[] a) {
//     for(int i = a.length / 2 - 1; i >= 0; i--) 
//         percDown(a, i, a.length);
//     for(int i = a.length - 1; i > 0; i--) {
//         swapReferences(a, 0, i); 
//         percDown(a, 0, i);
//     }
// }
// ```

// **Functionality**: This is the main heap sort algorithm function that sorts an array in-place.

// **Input**:
// - `a`: An array of comparable elements to be sorted

// **Output**:
// - No return value; sorts the array `a` in-place in ascending order

// **How It Works**:
// 1. **Build-Heap Phase**: Transforms the array into a max-heap
//    - Starts from the middle of the array and works backward
//    - For each element, calls `percDown` to ensure the heap property is maintained
//    - After this phase, the largest element is at the root (index 0)

// 2. **Sort Phase**: Extracts elements from the heap one by one
//    - Swaps the root (largest element) with the last element of the current heap
//    - Reduces the heap size by 1
//    - Restores the heap property for the new root
//    - Repeats until the heap is empty

// ## Generic Type Constraint

// The `<AnyType extends Comparable<? super AnyType>>` type parameter ensures that:
// - The elements can be compared with each other
// - The algorithm can work with any type that implements the Comparable interface

// ## Missing Functions

// Note that `swapReferences` is used but not provided in the code snippet. It should be a function that swaps two elements in the array.

// ## Summary

// This implementation of heap sort works by:
// 1. Building a max-heap from the unordered array
// 2. Repeatedly extracting the maximum element from the heap and placing it at the end of the array
// 3. Maintaining the heap property during the extraction process

// The time complexity is O(n log n) for all cases, and the space complexity is O(1) as it sorts in-place.

// --------------------------------------------------------------------------------------------------------

// Research Team
// Pass good in all way, clear, detailed 