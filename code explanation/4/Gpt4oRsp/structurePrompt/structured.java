// GPT4omini output using structure prompt

// ------------------------------------------------------------------------------------------------------------------------------------------------------------------


// This Java method implements a **Counting Radix Sort** for fixed-length strings using **Least Significant Digit (LSD) first sorting**. Below is an explanation of the functionality, input/output, and detailed comments for better clarity.

// ---

// ### 🔧 **Function Name**

// ```java
// public static void countingRadixSort(String[] arr, int stringLen)
// ```

// ### 📥 **Input Parameters**

// * `arr`: An array of `String` values, where all strings must have the same length.
// * `stringLen`: The fixed length of each string in the array.

// ### 📤 **Output**

// * The input array `arr` is sorted **in-place** in **lexicographic order**.

// ---

// ### 💡 **Functionality Overview**

// The function performs a **radix sort** using **counting sort** as the stable sort for each character position. It processes characters from the **last position** (`stringLen - 1`) to the **first position** (`0`).

// The alphabet size (or bucket count) is set to 256, assuming the characters are standard extended ASCII.

// ---

// ### 🧠 **How It Works (Step-by-Step)**

// 1. **Initialize Constants and Buffers:**

//    * `BUCKETS = 256`: Supports all ASCII characters.
//    * `buffer`: Temporary array for output during sorting.
//    * `in`/`out`: References for alternating between the input and output arrays.

// 2. **Loop Over Each Character Position (Right to Left):**

//    * For each character position:

//      * Count occurrences of each character (offset by 1 to avoid conflict at 0).
//      * Compute prefix sums to determine positions in the output.
//      * Distribute the strings into the `out` array based on current character.
//      * Swap `in` and `out` for next iteration.

// 3. **Final Copy (if stringLen is odd):**

//    * If the number of passes is odd, the sorted array is in the `in` buffer, and it needs to be copied back into `arr`.

// ---

// ### 💬 **In-Code Comments Version**

// ```java
// public static void countingRadixSort(String[] arr, int stringLen) {
//     final int BUCKETS = 256; // Number of possible ASCII characters

//     int N = arr.length;
//     String[] buffer = new String[N]; // Temporary buffer for sorting
//     String[] in = arr;
//     String[] out = buffer;

//     // Loop through each character position from right to left
//     for (int pos = stringLen - 1; pos >= 0; pos--) {
//         int[] count = new int[BUCKETS + 1]; // Frequency count array

//         // Count frequency of each character at current position
//         for (int i = 0; i < N; i++)
//             count[in[i].charAt(pos) + 1]++;

//         // Convert counts to indices via prefix sums
//         for (int b = 1; b <= BUCKETS; b++)
//             count[b] += count[b - 1];

//         // Distribute strings into output array based on current character
//         for (int i = 0; i < N; i++)
//             out[count[in[i].charAt(pos)]++] = in[i];

//         // Swap input and output arrays for the next pass
//         String[] tmp = in;
//         in = out;
//         out = tmp;
//     }

//     // If number of passes is odd, copy sorted array back into original
//     if (stringLen % 2 == 1)
//         for (int i = 0; i < arr.length; i++)
//             out[i] = in[i];
// }
// ```

// ---

// Would you like a visual illustration of how one iteration of the sort works?

// --------------------------------------------------------------------------------------------------------

// Research Team
// Pass good in all way, clear, detailed 