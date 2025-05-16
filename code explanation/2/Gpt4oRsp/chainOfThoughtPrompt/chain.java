// GPT4o output using chain of thought prompt

// ------------------------------------------------------------------------------------------------------------------------------------------------------------------


// ### 📌 **Purpose of the Code**
// This code implements the **heapsort algorithm** using a binary heap data structure. The key operations are:

// 1. **`leftChild(int i)`**: Computes the left child index of a node in a binary heap.
// 2. **`percDown(...)`**: Maintains the heap property by "percolating down" a node (used for heapifying).
// 3. **`heapsort(...)`**: Sorts an array in-place using the heap sort algorithm.

// ---

// ## ✅ **Line-by-Line Explanation**

// ---

// ### **1. `leftChild(int i)`**
// ```java
// private static int leftChild( int i )
// {
//     return 2 * i + 1;
// }
// ```

// - **Purpose**: Returns the index of the left child of a node at index `i` in a binary heap (represented as an array).
// - **`2 * i + 1`** is the standard formula for the left child index in a zero-indexed binary heap.

// ---

// ### **2. `percDown(...)`**
// ```java
// private static <AnyType extends Comparable<? super AnyType>>
// void percDown( AnyType [ ] a, int i, int n )
// ```
// - **Generic method** to allow sorting of any comparable type (`AnyType`).
// - **Parameters**:
//   - `a`: the array representing the heap.
//   - `i`: the index of the node to percolate down.
//   - `n`: size of the heap.

// ---

// #### Inside `percDown(...)`

// ```java
// int child;
// AnyType tmp;
// ```
// - Declares the index `child` and a temporary variable `tmp` to hold the current value.

// ```java
// for( tmp = a[ i ]; leftChild( i ) < n; i = child )
// ```
// - Start by saving the element at index `i` into `tmp`.
// - While the left child index is within the bounds of the heap (`n`):
//   - The loop continues as long as the current node has a child.
//   - `i = child` moves down the heap.

// ```java
//     child = leftChild( i );
//     if( child != n - 1 && a[ child ].compareTo( a[ child + 1]) < 0)
//         child++;
// ```
// - Calculate the left child of `i`.
// - If there's a right child (i.e., `child + 1 < n`) and it's **larger** than the left child, move `child` to point to the right child.

// ```java
//     if( tmp.compareTo( a[ child ]) < 0 )
//         a[ i ] = a[ child ];
//     else
//         break;
// ```
// - If the current node `tmp` is **less** than the larger child, move the child up (heapify down).
// - Else, the heap property is satisfied — break out of the loop.

// ```java
// a[ i ] = tmp;
// ```
// - Finally, place `tmp` in its correct location.

// ---

// ### **3. `heapsort(...)`**
// ```java
// public static <AnyType extends Comparable<? super AnyType>>
// void heapsort( AnyType []a)
// ```
// - Public generic method to sort an array using heap sort.

// ---

// #### First `for` loop: **Build the max heap**
// ```java
// for( int i = a.length / 2 - 1; i >= 0; i-- ) 
//     percDown( a, i, a.length );
// ```
// - Starts from the last non-leaf node (`a.length / 2 - 1`) and moves up to the root.
// - Calls `percDown` to build a **max-heap**.

// ---

// #### Second `for` loop: **Extract elements and rebuild heap**
// ```java
// for( int i = a.length - 1; i > 0; i-- )
// {
//     swapReferences( a, 0, i ); 
//     percDown( a, 0, i );
// }
// ```
// - Repeatedly swaps the root (max element) with the last element of the heap.
// - Reduces the heap size (`i`) and re-heapifies the root to maintain max-heap property.

// ---

// ### ⚠️ **Missing Piece**
// ```java
// swapReferences(a, 0, i)
// ```
// This method is not defined in the code snippet, but it likely does:
// ```java
// private static <AnyType> void swapReferences(AnyType[] a, int i, int j) {
//     AnyType temp = a[i];
//     a[i] = a[j];
//     a[j] = temp;
// }
// ```
// Used to swap two elements in the array.

// ---

// Would you like me to visualize how this heap sort works with a sample array?

// --------------------------------------------------------------------------------------------------------

// Research Team
// Pass good in all way, clear, detailed 