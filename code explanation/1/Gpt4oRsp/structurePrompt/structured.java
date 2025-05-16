// GPT4o output using structure prompt

// ------------------------------------------------------------------------------------------------------------------------------------------------------------------

// This Java code implements the **Merge Sort** algorithm using a generic type `AnyType` that extends `Comparable`. The logic is split into two methods: `mergeSort` (which recursively splits the array) and `merge` (which merges sorted subarrays).

// ---

// ### 💡 **Functionality (in words)**

// This is a classic **Divide and Conquer** sorting algorithm:
// 1. `mergeSort()` splits the array into halves recursively until the subarrays have one element each.
// 2. `merge()` then merges two sorted subarrays into one sorted array.
// 3. The algorithm guarantees a time complexity of **O(n log n)** and is **stable** (maintains order of equal elements).

// ---

// ### 📥 **Input**
// Both methods work with:
// - `a`: The original array of generic type `AnyType` to be sorted.
// - `tmpArray`: A temporary array used for merging.
// - `left`, `right`: The bounds (inclusive) of the subarray in `a` to sort.

// For `merge`, specifically:
// - `leftPos`: Start index of the left sorted subarray.
// - `rightPos`: Start index of the right sorted subarray.
// - `rightEnd`: End index of the right subarray.

// ---

// ### 📤 **Output**
// - **No return value** (both methods are `void`).
// - The input array `a` is **sorted in place**.

// ---

// ### 📝 **In-Code Comments**

// ```java
// // Recursively sorts the array `a` using the merge sort algorithm
// private static <AnyType extends Comparable<? super AnyType>>
// void mergeSort( AnyType [ ] a, AnyType [ ] tmpArray, int left, int right )
// {
//     if( left < right )
//     {
//         int center = ( left + right ) / 2;  // Find midpoint
//         mergeSort( a, tmpArray, left, center );        // Sort left half
//         mergeSort( a, tmpArray, center + 1, right );   // Sort right half
//         merge( a, tmpArray, left, center + 1, right ); // Merge sorted halves
//     }
// }

// // Merges two sorted subarrays into one sorted segment
// private static <AnyType extends Comparable<? super AnyType>>
// void merge( AnyType [ ] a, AnyType [ ] tmpArray,
// int leftPos, int rightPos, int rightEnd )
// {
//     int leftEnd = rightPos - 1;                  // End of left subarray
//     int tmpPos = leftPos;                        // Start position in tmpArray
//     int numElements = rightEnd - leftPos + 1;    // Total number of elements to merge

//     // Merge elements from both subarrays in sorted order
//     while( leftPos <= leftEnd && rightPos <= rightEnd )
//         if( a[ leftPos ].compareTo( a[ rightPos ] ) <= 0 )
//             tmpArray[ tmpPos++ ] = a[ leftPos++ ];     // Left element is smaller
//         else
//             tmpArray[ tmpPos++ ] = a[ rightPos++ ];    // Right element is smaller

//     // Copy remaining elements from left subarray (if any)
//     while( leftPos <= leftEnd )
//         tmpArray[ tmpPos++ ] = a[ leftPos++ ];

//     // Copy remaining elements from right subarray (if any)
//     while( rightPos <= rightEnd ) 
//         tmpArray[ tmpPos++ ] = a[ rightPos++ ];

//     // Copy merged elements back into original array
//     for( int i = 0; i < numElements; i++, rightEnd-- )
//         a[ rightEnd ] = tmpArray[ rightEnd ];
// }
// ```

// ---

// Would you like me to show you a concrete example with array values to demonstrate how this works step by step?

// ------------------------------------------------------------------------------------------------------------------------------------------------------------------

// Research Team
// good in all ways 