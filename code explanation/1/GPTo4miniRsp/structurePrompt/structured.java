// GPT4omini output using structure prompt

// ------------------------------------------------------------------------------------------------------------------------------------------------------------------


// Here’s a fully annotated version of your code, followed by a concise description of what it does, its inputs, and its outputs.

// ```java
// // Recursively sorts the subarray a[left..right] using merge sort.
// // a        – the array to sort.
// // tmpArray – a workspace array of the same length as a.
// // left     – the index of the first element in the subarray.
// // right    – the index of the last element in the subarray.
// private static <AnyType extends Comparable<? super AnyType>>
// void mergeSort( AnyType[] a, AnyType[] tmpArray, int left, int right ) {
//     // Only proceed if there are at least two elements
//     if( left < right ) {
//         // Find midpoint
//         int center = ( left + right ) / 2;
//         // Sort left half a[left..center]
//         mergeSort( a, tmpArray, left, center );
//         // Sort right half a[center+1..right]
//         mergeSort( a, tmpArray, center + 1, right );
//         // Merge the two sorted halves
//         merge( a, tmpArray, left, center + 1, right );
//     }
// }

// // Merges two consecutive sorted runs in a[] into one.
// // a        – the array containing two sorted runs.
// // tmpArray – workspace array.
// // leftPos  – index of first element of the left run.
// // rightPos – index of first element of the right run.
// // rightEnd – index of last element of the right run.
// private static <AnyType extends Comparable<? super AnyType>>
// void merge( AnyType[] a, AnyType[] tmpArray,
//             int leftPos, int rightPos, int rightEnd ) {
//     int leftEnd   = rightPos - 1;         // last index of left run
//     int tmpPos    = leftPos;              // where to put next element in tmpArray
//     int numElements = rightEnd - leftPos + 1; // total elements to merge

//     // Merge elements from each run in sorted order
//     while( leftPos <= leftEnd && rightPos <= rightEnd ) {
//         if( a[leftPos].compareTo( a[rightPos] ) <= 0 ) {
//             tmpArray[tmpPos++] = a[leftPos++];
//         } else {
//             tmpArray[tmpPos++] = a[rightPos++];
//         }
//     }

//     // Copy any remaining elements from left run
//     while( leftPos <= leftEnd ) {
//         tmpArray[tmpPos++] = a[leftPos++];
//     }

//     // Copy any remaining elements from right run
//     while( rightPos <= rightEnd ) {
//         tmpArray[tmpPos++] = a[rightPos++];
//     }

//     // Copy merged elements back into original array
//     // Walk backwards to avoid overwriting elements we still need
//     for( int i = 0; i < numElements; i++, rightEnd-- ) {
//         a[rightEnd] = tmpArray[rightEnd];
//     }
// }
// ```

// ---

// ### What it does (by words)
// This is a standard **merge sort** implementation in Java, made generic so it can sort any array of objects `AnyType` that implement `Comparable`.  
// 1. **Divide**: `mergeSort` splits the array into two halves recursively until each subarray has at most one element.  
// 2. **Conquer**: Single‐element (or empty) subarrays are inherently sorted.  
// 3. **Combine**: The `merge` method takes two adjacent sorted subarrays in `a[]` and merges them into a single sorted run, using `tmpArray[]` as temporary storage, then copies the merged result back into `a[]`.

// This sorting is **stable** (equal elements retain their original relative order) and runs in **O(n log n)** time, using **O(n)** extra space.

// ---

// ### Input
// - **`a`**: the array to be sorted (contains the elements to order).
// - **`tmpArray`**: a temporary array of the same length as `a`, used for merging.
// - **`left`**, **`right`**: the bounds (inclusive) of the subarray of `a` to sort.

// Typically called initially as:
// ```java
// mergeSort(myArray, new MyType[myArray.length], 0, myArray.length - 1);
// ```

// ### Output
// - **None returned**; the array `a` is sorted _in place_ into ascending order according to its `compareTo` method.

// --------------------------------------------------------------------------------------------------------

// Research Team
// Pass good in all way, clear, detailed 