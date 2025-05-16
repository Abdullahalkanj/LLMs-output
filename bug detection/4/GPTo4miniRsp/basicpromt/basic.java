// ChatGPT4omini output using basic prompt


// ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
// The bug is in this line:

// ```java
// steps.add(new Pair<Integer,Integer>(start, helper));
// ```

// That’s moving the “largest” disk from **start** to the **helper** peg, but it should be moved from **start** to the **end** peg. Change it to:

// ```java
// steps.add(new Pair<Integer,Integer>(start, end));
// ```

// ---

// Here’s the corrected `hanoi` method (I’ve also replaced the PriorityQueue hack with the simpler `6 – start – end` trick to compute the helper peg):

// ```java
// public static List<Pair<Integer,Integer>> hanoi(int height, int start, int end) {
//     List<Pair<Integer,Integer>> steps = new ArrayList<>();

//     if (height > 0) {
//         int helper = 6 - start - end;              // the one peg that’s not start or end
//         steps.addAll(hanoi(height - 1, start, helper));
//         steps.add(new Pair<>(start, end));        // <-- fixed: move disk from start to end
//         steps.addAll(hanoi(height - 1, helper, end));
//     }

//     return steps;
// }
// ```

// With that change, your recursive solution will generate the correct sequence of moves!
// ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
