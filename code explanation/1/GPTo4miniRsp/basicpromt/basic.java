// GPT4omini output using basic prompt

// ------------------------------------------------------------------------------------------------------------------------------------------------------------------

// This is a classic implementation of **merge sort** in Java, written to work on any array of objects that implement the `Comparable` interface. Here’s a breakdown of how it works:

// ---

// ## 1. Public API: `mergeSort(...)`

// ```java
// private static <AnyType extends Comparable<? super AnyType>>
// void mergeSort( AnyType[] a, AnyType[] tmpArray, int left, int right ) {
//     if( left < right ) {
//         int center = (left + right) / 2;
//         mergeSort( a, tmpArray, left, center );           // sort left half
//         mergeSort( a, tmpArray, center+1, right );        // sort right half
//         merge( a, tmpArray, left, center+1, right );      // merge them
//     }
// }
// ```

// - **Generics**  
//   `<AnyType extends Comparable<? super AnyType>>`  
//   Ensures that the elements in the array (`AnyType`) can be compared to one another via their `compareTo` method.

// - **Parameters**  
//   - `a` – the array to be sorted  
//   - `tmpArray` – a scratch array of the same length, used during merging  
//   - `left`, `right` – the bounds (inclusive) of the subarray to sort  

// - **Divide & Conquer**  
//   1. **Base case**: if `left >= right`, the subarray has one or zero elements, and is already sorted.  
//   2. **Divide**: compute `center = (left + right) / 2`.  
//   3. **Conquer**: recursively sort the left half (`left…center`) and the right half (`center+1…right`).  
//   4. **Combine**: merge the two sorted halves back together via the `merge(...)` method.

// ---

// ## 2. Merge Step: `merge(...)`

// ```java
// private static <AnyType extends Comparable<? super AnyType>>
// void merge( AnyType[] a, AnyType[] tmpArray,
//             int leftPos, int rightPos, int rightEnd ) {
//     int leftEnd   = rightPos - 1;        // end of left subarray
//     int tmpPos    = leftPos;             // current position in tmpArray
//     int numElements = rightEnd - leftPos + 1;

//     // 1) Merge elements from both halves in sorted order
//     while( leftPos <= leftEnd && rightPos <= rightEnd ) {
//         if( a[leftPos].compareTo(a[rightPos]) <= 0 )
//             tmpArray[tmpPos++] = a[leftPos++];
//         else
//             tmpArray[tmpPos++] = a[rightPos++];
//     }

//     // 2) Copy any remaining from left half
//     while( leftPos <= leftEnd )
//         tmpArray[tmpPos++] = a[leftPos++];

//     // 3) Copy any remaining from right half
//     while( rightPos <= rightEnd )
//         tmpArray[tmpPos++] = a[rightPos++];

//     // 4) Copy merged results back into original array
//     //    starting from rightEnd going backwards
//     for( int i = 0; i < numElements; i++, rightEnd-- )
//         a[rightEnd] = tmpArray[rightEnd];
// }
// ```

// 1. **Pointers setup**  
//    - `leftPos…leftEnd` points to the left sorted subarray.  
//    - `rightPos…rightEnd` points to the right sorted subarray.  
//    - `tmpPos` is where we write the next smallest element in `tmpArray`.

// 2. **Two-way merge**  
//    - Repeatedly compare the current elements of the two subarrays.  
//    - Whichever is smaller (or equal) gets copied into `tmpArray[tmpPos++]`.  
//    - Advance the pointer in whichever subarray you took from.

// 3. **Exhaust leftovers**  
//    - If one half runs out, copy the rest of the other half into `tmpArray`.

// 4. **Copy back to `a[]`**  
//    - Copy the newly merged block from `tmpArray` back into the corresponding positions in `a[]`.  
//    - Doing it from the end backwards ensures we don’t overwrite elements in `a[]` before they’ve been merged.

// ---

// ## 3. Complexity & Characteristics

// - **Time complexity**:  
//   - **Best/Average/Worst**: _O(n log n)_ comparisons  
// - **Space complexity**:  
//   - Requires an auxiliary array of size _n_ → _O(n)_ extra space  
// - **Stability**:  
//   - Yes, because equal elements are taken from the left subarray first (`<=` test).

// ---

// ### In a nutshell

// 1. **Split** the array in half repeatedly until you have subarrays of size 1.  
// 2. **Merge** those subarrays pairwise in sorted order, using a temporary array to hold merged results.  
// 3. **Copy** merged blocks back into the original array.  

// This divide-and-conquer approach guarantees _O(n log n)_ performance regardless of input order, making merge sort one of the more predictable and stable comparison-based sorting algorithms.

// --------------------------------------------------------------------------------------------------------

// Research Team
// Pass good in all way, clear, detailed 