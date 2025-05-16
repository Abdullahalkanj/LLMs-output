// Claude output using basic prompt


// -----------------------------------------------------------------------------------------------------------------------------------------------------------------

// This code implements the heapsort algorithm in Java. Heapsort is an efficient comparison-based sorting algorithm with O(n log n) time complexity. Let me break down what each part does:

// 1. `leftChild(int i)` - Calculates the index of the left child of a node in a binary heap stored as an array. In a binary heap, for a node at index i, its left child is at index 2i+1.

// 2. `percDown()` - This is the core "percolate down" operation used in heap operations:
//    - It starts with an element at position i and moves it down the heap until heap properties are restored
//    - It compares the current node with its larger child and swaps if necessary
//    - The parameter n represents the heap size

// 3. `heapsort()` - The main sorting function that:
//    - First builds a max-heap from the array (the first for loop)
//    - Then repeatedly extracts the maximum element and rebuilds the heap (the second for loop)

// The algorithm works in two phases:
// - Build phase: Transform the input array into a max-heap structure
// - Sort phase: Repeatedly extract the maximum element (at the root) and rebuild the heap

// The `swapReferences()` method isn't shown but it presumably swaps two elements in the array.

// This implementation uses generics (`<AnyType extends Comparable<? super AnyType>>`) which allows it to sort any type that implements the Comparable interface, making it very flexible.

// Would you like me to explain any particular part of this code in more detail?

// --------------------------------------------------------------------------------------------------------

// Research Team
// Good 