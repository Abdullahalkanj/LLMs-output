// GPT4omini output using structur prompt

// ------------------------------------------------------------------------------------------------------------------------------------------------------------------


// Here’s a breakdown of each method—including its inputs, outputs, and step-by-step functionality—plus the fully commented Java code:

// ```java
// /**
//  * Compute the index of the left child in a binary heap stored in array form.
//  *
//  * @param i  index of the current node
//  * @return   index of its left child (2*i + 1)
//  */
// private static int leftChild(int i) {
//     return 2 * i + 1;
// }

// /**
//  * “Percolate down” element a[i] in the array into the heap of size n.
//  * 
//  * Starting from index i, repeatedly swap the element with its larger child
//  * until the heap property is restored (parent ≥ both children). Used both
//  * to build the heap and to re‐heapify after removing the max.
//  *
//  * @param a  the array holding the heap
//  * @param i  the root index of the (sub)heap to percolate down
//  * @param n  number of elements in the heap (elements at indices ≥ n are ignored)
//  */
// private static <AnyType extends Comparable<? super AnyType>>
// void percDown(AnyType[] a, int i, int n) {
//     int child;
//     AnyType tmp = a[i];            // save the element to percolate
    
//     // as long as this node has at least a left child
//     while (leftChild(i) < n) {
//         child = leftChild(i);     
        
//         // pick the larger of the two children (if right child exists)
//         if (child + 1 < n 
//             && a[child].compareTo(a[child + 1]) < 0) {
//             child++;
//         }
        
//         // if the child is larger than tmp, move child up
//         if (tmp.compareTo(a[child]) < 0) {
//             a[i] = a[child];
//             i = child;              // continue percolating down
//         } else {
//             break;                  // heap property restored
//         }
//     }
    
//     // put original element in its final position
//     a[i] = tmp;
// }

// /**
//  * In‐place heapsort: first turn array into a max-heap, then repeatedly remove
//  * the max element and rebuild the heap on the remaining portion.
//  *
//  * @param a  the array to sort; on return it is sorted in ascending order
//  */
// public static <AnyType extends Comparable<? super AnyType>>
// void heapsort(AnyType[] a) {
//     int n = a.length;
    
//     // 1) Build max-heap (heapify)
//     //    Start from the last internal node and percolate down each.
//     for (int i = n / 2 - 1; i >= 0; i--) {
//         percDown(a, i, n);
//     }
    
//     // 2) Repeatedly remove max (a[0]) and rebuild heap on a[0..i-1]
//     for (int i = n - 1; i > 0; i--) {
//         swapReferences(a, 0, i);   // move current max to the end
//         percDown(a, 0, i);         // restore heap on the reduced array
//     }
// }
// ```

// ---

// ### By-Method Summary

// | Method        | Input                                     | Output / Side-Effects                                                      | Purpose                                                                                                        |
// | ------------- | ----------------------------------------- | --------------------------------------------------------------------------- | -------------------------------------------------------------------------------------------------------------- |
// | **leftChild** | `int i`                                   | returns `2*i + 1`                                                           | Computes the array index of the left child in a binary‐heap representation.                                     |
// | **percDown**  | `AnyType[] a`, `int i`, `int n`           | Reorders part of array in place so that subarray `a[i..n-1]` satisfies max-heap property. | “Sifts” element at index i down the heap to restore heap order, comparing and swapping with larger children.   |
// | **heapsort**  | `AnyType[] a`                             | Sorts array **in place** into ascending order; returns void.                | Implements heap-sort: heapifies the entire array, then repeatedly extracts the max into the end of the array. |

// ---

// ### Overall Input & Output

// - **Input:**  
//   - An array `a` of any objects implementing `Comparable` (i.e. they can be ordered).
// - **Output:**  
//   - The same array `a`, rearranged in ascending sorted order.  

// All operations are done in place, so no additional arrays are allocated beyond O(1) extra space. The time complexity is O(n log n) in all cases.

// --------------------------------------------------------------------------------------------------------

// Research Team
// Pass good in all way, clear, detailed 