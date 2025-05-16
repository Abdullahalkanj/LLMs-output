// Claude output using chain of thought prompt

// ------------------------------------------------------------------------------------------------------------------------------------------------------------------


// # Purpose of the Code

// This code implements the heap sort algorithm, which is a comparison-based sorting algorithm with O(n log n) time complexity. The heap sort works by:

// 1. Building a max heap from the input array
// 2. Repeatedly extracting the maximum element from the heap and rebuilding the heap until the array is sorted

// ## Line-by-Line Explanation

// ### `leftChild` Method
// ```java
// private static int leftChild(int i)
// {
//     return 2 * i + 1;
// }
// ```
// - This function calculates the index of the left child of a node in a binary heap
// - In a binary heap represented as an array, if a node is at index `i`, its left child is at index `2*i + 1`
// - This is a fundamental operation for navigating the heap structure

// ### `percDown` Method
// ```java
// private static <AnyType extends Comparable<? super AnyType>>
// void percDown(AnyType[] a, int i, int n)
// {
//     int child;
//     AnyType tmp;

//     for(tmp = a[i]; leftChild(i) < n; i = child)
//     {
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
// - This method "percolates down" an element in a heap to maintain the heap property
// - It uses generics (`<AnyType extends Comparable<? super AnyType>>`) to work with any comparable type
// - Parameters:
//   - `a`: The array representing the heap
//   - `i`: The index of the element to be percolated down
//   - `n`: The size of the heap (which may be smaller than the array length)
// - Implementation:
//   1. Saves the value at index `i` in `tmp`
//   2. Enters a loop that continues as long as the current node has a left child within the heap bounds
//   3. `child = leftChild(i)` gets the index of the left child
//   4. `if(child != n - 1 && a[child].compareTo(a[child + 1]) < 0)` checks if:
//      - The left child is not the last element in the heap AND
//      - The left child is less than the right child
//      - If both conditions are true, it increments `child` to point to the right child
//   5. `if(tmp.compareTo(a[child]) < 0)` checks if the current value is less than the largest child
//      - If true, it moves the child up to the parent position
//      - If false, the heap property is restored and the loop breaks
//   6. After the loop, it places the original value (`tmp`) at its final position

// ### `heapsort` Method
// ```java
// public static <AnyType extends Comparable<? super AnyType>>
// void heapsort(AnyType[] a)
// {
//     for(int i = a.length / 2 - 1; i >= 0; i--) 
//         percDown(a, i, a.length);
//     for(int i = a.length - 1; i > 0; i--)
//     {
//         swapReferences(a, 0, i); 
//         percDown(a, 0, i);
//     }
// }
// ```
// - This is the main heap sort method that sorts the array in-place
// - It also uses generics to work with any comparable type
// - The algorithm works in two phases:
  
//   1. **Build the heap** (first loop):
//      - Starts from the last non-leaf node (`a.length / 2 - 1`) and works backwards
//      - Calls `percDown` on each node to establish the max heap property
//      - After this loop, the array represents a valid max heap with the largest element at index 0
  
//   2. **Sort the array** (second loop):
//      - Iterates from the end of the array towards the beginning
//      - In each iteration:
//        - Swaps the maximum element (at index 0) with the element at the current position `i`
//        - Calls `percDown` to restore the heap property for the remaining unsorted portion
//      - After this loop, the array will be sorted in ascending order

// Note: The `swapReferences` method is not shown in the provided code but is used to swap elements in the array.

// --------------------------------------------------------------------------------------------------------

// Research Team
// Pass good in all way, clear, detailed 