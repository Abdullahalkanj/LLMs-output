// Claude output using chian of thought prompt

// --------------------------------------------------------------------------------------------------------
// # Purpose of the Merge Sort Implementation

// This code implements the merge sort algorithm, which is a divide-and-conquer sorting technique with O(n log n) time complexity. Merge sort works by dividing the array into smaller subarrays, sorting them recursively, and then merging the sorted subarrays back together.

// ## Line-by-Line Explanation

// ### The `mergeSort` Method

// ```java
// private static <AnyType extends Comparable<? super AnyType>>
// void mergeSort( AnyType [ ] a, AnyType [ ] tmpArray, int left, int right )
// ```

// - This declares a generic method that works with any type that implements the `Comparable` interface
// - `AnyType extends Comparable<? super AnyType>` means the type must be comparable to itself or its superclass
// - Parameters:
//   - `a`: The array to be sorted
//   - `tmpArray`: Temporary working array for the merge operation
//   - `left`: Starting index of the subarray
//   - `right`: Ending index of the subarray

// ```java
// if( left < right )
// ```
// - The base case check: only proceed if there are at least two elements to sort

// ```java
// int center = ( left + right ) / 2;
// ```
// - Calculates the midpoint to divide the array into two subarrays

// ```java
// mergeSort( a, tmpArray, left, center );
// ```
// - Recursively sorts the left subarray (from `left` to `center`)

// ```java
// mergeSort( a, tmpArray, center + 1, right );
// ```
// - Recursively sorts the right subarray (from `center + 1` to `right`)

// ```java
// merge( a, tmpArray, left, center + 1, right );
// ```
// - After both subarrays are sorted, merges them back together

// ### The `merge` Method

// ```java
// private static <AnyType extends Comparable<? super AnyType>>
// void merge( AnyType [ ] a, AnyType [ ] tmpArray,
// int leftPos, int rightPos, int rightEnd )
// ```
// - Parameters:
//   - `a`: The original array containing elements to merge
//   - `tmpArray`: Temporary array to hold the merged result
//   - `leftPos`: Starting position of left subarray
//   - `rightPos`: Starting position of right subarray (also end of left subarray + 1)
//   - `rightEnd`: Ending position of right subarray

// ```java
// int leftEnd = rightPos - 1;
// ```
// - Calculates the ending position of the left subarray

// ```java
// int tmpPos = leftPos;
// ```
// - Initializes the position in the temporary array where merged elements will be placed

// ```java
// int numElements = rightEnd - leftPos + 1;
// ```
// - Calculates the total number of elements to be merged

// ```java
// while( leftPos <= leftEnd && rightPos <= rightEnd )
//     if( a[ leftPos ].compareTo( a[ rightPos ] ) <= 0 )
//         tmpArray[ tmpPos++ ] = a[ leftPos++ ];
//     else
//         tmpArray[ tmpPos++ ] = a[ rightPos++ ];
// ```
// - This is the main merging loop that compares elements from both subarrays
// - It takes the smaller element (determined by `compareTo`) and places it in the temporary array
// - The positions are incremented after each placement

// ```java
// while( leftPos <= leftEnd )
//     tmpArray[ tmpPos++ ] = a[ leftPos++ ];
// ```
// - Copies any remaining elements from the left subarray to the temporary array

// ```java
// while( rightPos <= rightEnd ) 
//     tmpArray[ tmpPos++ ] = a[ rightPos++ ];
// ```
// - Copies any remaining elements from the right subarray to the temporary array

// ```java
// for( int i = 0; i < numElements; i++, rightEnd-- )
//     a[ rightEnd ] = tmpArray[ rightEnd ];
// ```
// - Copies the merged elements from the temporary array back to the original array
// - This is done in reverse order from right to left

// This implementation of merge sort uses a temporary array to perform the merging operation, which is passed down through the recursive calls to avoid creating new arrays on each recursive call. The algorithm has O(n log n) time complexity and O(n) space complexity.

// --------------------------------------------------------------------------------------------------------

// Research Team 
// Pass (Good Explanation)