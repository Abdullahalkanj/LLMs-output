// GPT4o output using chain of thought prompt

// ------------------------------------------------------------------------------------------------------------------------------------------------------------------


// This code is an implementation of the **Merge Sort** algorithm in Java, designed to sort an array of any type that implements the `Comparable` interface. It uses a **divide-and-conquer** strategy, recursively dividing the array into halves, sorting them, and then merging them back together.


// ## 🔍 **Purpose**
// To sort an array `a` of any type (`AnyType`) using the **Merge Sort** algorithm. It does so by:
// 1. Recursively splitting the array into smaller subarrays (`mergeSort`).
// 2. Merging sorted subarrays back into one (`merge`).

// ---

// ## 📌 **Function: `mergeSort`**
// ```java
// private static <AnyType extends Comparable<? super AnyType>>
// void mergeSort( AnyType [ ] a, AnyType [ ] tmpArray, int left, int right )
// ```

// ### Line-by-line Explanation:
// 1. `private static <AnyType extends Comparable<? super AnyType>>`
//    - Declares a generic method restricted to types that are comparable to themselves or a superclass. Ensures elements can be compared for sorting.

// 2. `void mergeSort( AnyType [ ] a, AnyType [ ] tmpArray, int left, int right )`
//    - Defines a recursive merge sort function with:
//      - `a`: the input array to sort,
//      - `tmpArray`: a temporary array used during merging,
//      - `left` and `right`: the current segment of the array being processed.

// 3. `if( left < right )`
//    - Base case check. If the segment has more than one element, proceed to sort.

// 4. `int center = ( left + right ) / 2;`
//    - Find the midpoint of the current segment.

// 5. `mergeSort( a, tmpArray, left, center );`
//    - Recursively sort the left half.

// 6. `mergeSort( a, tmpArray, center + 1, right );`
//    - Recursively sort the right half.

// 7. `merge( a, tmpArray, left, center + 1, right );`
//    - Merge the two sorted halves into one.

// ---

// ## 📌 **Function: `merge`**
// ```java
// private static <AnyType extends Comparable<? super AnyType>>
// void merge( AnyType [ ] a, AnyType [ ] tmpArray,
// int leftPos, int rightPos, int rightEnd )
// ```

// ### Line-by-line Explanation:
// 1. `private static <AnyType extends Comparable<? super AnyType>>`
//    - Same generic restriction as above to allow comparisons.

// 2. `void merge(...)`
//    - Merges two sorted parts of the array:
//      - `leftPos`: start index of the left half,
//      - `rightPos`: start index of the right half,
//      - `rightEnd`: end index of the right half.

// 3. `int leftEnd = rightPos - 1;`
//    - Calculates the end of the left half.

// 4. `int tmpPos = leftPos;`
//    - Start position in `tmpArray` for merged elements.

// 5. `int numElements = rightEnd - leftPos + 1;`
//    - Total number of elements to merge.

// ---

// ### 🔄 **Main merging loop**:
// ```java
// while( leftPos <= leftEnd && rightPos <= rightEnd )
// ```
// - While there are elements in both halves:
// ```java
// if( a[ leftPos ].compareTo( a[ rightPos ] ) <= 0 )
//     tmpArray[ tmpPos++ ] = a[ leftPos++ ];
// else
//     tmpArray[ tmpPos++ ] = a[ rightPos++ ];
// ```
// - Copy the smaller element into `tmpArray`, advancing the corresponding index.

// ---

// ### 🚚 **Copy leftovers**:
// ```java
// while( leftPos <= leftEnd )
//     tmpArray[ tmpPos++ ] = a[ leftPos++ ];
// ```
// - If elements remain in the left half, copy them.

// ```java
// while( rightPos <= rightEnd )
//     tmpArray[ tmpPos++ ] = a[ rightPos++ ];
// ```
// - If elements remain in the right half, copy them.

// ---

// ### 🧩 **Copy back to original array**:
// ```java
// for( int i = 0; i < numElements; i++, rightEnd-- )
//     a[ rightEnd ] = tmpArray[ rightEnd ];
// ```
// - Copy the sorted segment from `tmpArray` back into `a`, starting from the end.

// ---

// Would you like a visual example (with numbers) showing how the merge sort works step by step?

// --------------------------------------------------------------------------------------------------------

// Research Team 
//  Pass