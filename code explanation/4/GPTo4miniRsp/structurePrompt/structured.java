// GPT4omini output using structure prompt

// ------------------------------------------------------------------------------------------------------------------------------------------------------------------

// Thought for a couple of seconds


// Here’s a detailed, line‑by‑line walkthrough of the `countingRadixSort` method, followed by a high‑level description of its inputs, outputs, and behavior.

// ```java
// public static void countingRadixSort(String[] arr, int stringLen) {
//     // Number of possible character values (0–255 for extended ASCII)
//     final int BUCKETS = 256;

//     // N = total number of strings to sort
//     int N = arr.length;

//     // A secondary array used for “ping‑pong” buffering between passes
//     String[] buffer = new String[N];

//     // in/out references: we alternate which array we read from and write to
//     String[] in = arr;
//     String[] out = buffer;

//     // Perform one stable counting sort pass for each character position,
//     // starting from the least significant position (rightmost) to the
//     // most significant (leftmost)
//     for (int pos = stringLen - 1; pos >= 0; pos--) {
//         // count[k] will hold the number of input strings whose character
//         // at `pos` equals k–1 (we shift by +1 to simplify prefix sums)
//         int[] count = new int[BUCKETS + 1];

//         // 1) Count frequencies of each character at position `pos`
//         for (int i = 0; i < N; i++) {
//             int c = in[i].charAt(pos);
//             count[c + 1]++;
//         }

//         // 2) Transform counts to starting indices via prefix sums.
//         //    After this loop, count[k] is the index in `out` where
//         //    the next string with character code k should go.
//         for (int b = 1; b <= BUCKETS; b++) {
//             count[b] += count[b - 1];
//         }

//         // 3) Distribute (stable) into `out`, using count[] for positions.
//         for (int i = 0; i < N; i++) {
//             int c = in[i].charAt(pos);
//             out[count[c]++] = in[i];
//         }

//         // 4) Swap roles of in/out for next pass (avoids copying back each time)
//         String[] tmp = in;
//         in = out;
//         out = tmp;
//     }

//     // After stringLen passes, if we ended in the buffer array (i.e. stringLen
//     // was odd), copy sorted data back into the original array reference `arr`
//     if (stringLen % 2 == 1) {
//         for (int i = 0; i < arr.length; i++) {
//             out[i] = in[i];
//         }
//     }
// }
// ```

// ---

// ### High‑Level Description

// * **What it is:**
//   A fixed‑length, LSD (least‑significant‑digit) Radix Sort for an array of ASCII strings, implemented via repeated stable Counting Sort passes on each character position.

// * **Inputs:**

//   1. `String[] arr` – an array of **N** strings, each of exactly `stringLen` characters (padded if necessary).
//   2. `int stringLen` – the length of each string in `arr`. All strings must be this length.

// * **Outputs (side‑effects):**

//   * The original array `arr` is rearranged *in place* into lexicographically sorted order.

// * **How it works:**

//   1. **Bucket Size**: Uses 256 buckets, one per possible extended‐ASCII codepoint (0–255).
//   2. **Passes**: Performs `stringLen` passes. In pass **i**, it sorts the entire array by the character at position `(stringLen−1−i)`.
//   3. **Counting Sort**: Each pass:

//      * Counts frequencies of each byte value at that position.
//      * Builds prefix sums to determine the starting index for each bucket.
//      * Places strings into an auxiliary array in stable order.
//   4. **Buffer Swapping**: Rather than copying back after each pass, it swaps the roles of the “input” and “output” arrays—this is a common ping‑pong buffering trick.
//   5. **Final Copy**: If there have been an odd number of passes (`stringLen` is odd), the result ends up in the `buffer`, so it’s copied back into `arr`. Otherwise (even number of passes) it’s already in `arr`.

// * **Time Complexity:**

//   * **O((N + B) × L)** where

//     * **N** = number of strings,
//     * **B** = number of buckets (256, constant),
//     * **L** = `stringLen`.
//   * In practice, **O(N × L)** since **B** is a constant.

// * **Space Complexity:**

//   * **O(N + B)** auxiliary space for the buffer array and the count array.

// ---

// ### When to Use

// * **Best suited for:**

//   * Very large collections of fixed‑length strings over a small, dense alphabet (e.g. ASCII).
//   * Situations where stable, linear‑time sorting by character from least to most significant is desirable.

// * **Not ideal for:**

//   * Variable‑length strings (unless manually padded).
//   * Extremely large alphabets (would blow up the bucket array).
//   * Memory‑constrained environments where the auxiliary buffer is too large.

// This implementation is a classic, efficient way to sort fixed‑length keys in linear time with respect to the number of strings and the string length.

// --------------------------------------------------------------------------------------------------------

// Research Team
// Pass good in all way, clear, detailed 