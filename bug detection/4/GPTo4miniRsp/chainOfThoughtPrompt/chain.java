// ChatGPT4omini output using Chain-of-thought prompt


// ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
// Let’s walk through what the recursive algorithm *should* be doing, compare that to what your code is actually doing, pinpoint the discrepancy, and then suggest the minimal change to get it right.

// ---

// ### 1. What the Tower-of-Hanoi algorithm does

// To move a stack of *n* disks from peg **A** to peg **C**, using **B** as auxiliary:

// 1. **Base case**  
//    If *n* = 0, do nothing.

// 2. **Recursive case**  
//    1. Move *n–1* disks from **A** to **B** (using **C** as auxiliary).  
//    2. Move the largest (bottom) disk from **A** to **C**.  
//    3. Move the *n–1* disks from **B** to **C** (using **A** as auxiliary).

// Concretely, with parameters `(height, start, end)`, you compute  
// ```java
// int helper = the one peg that’s neither start nor end;
// ```
// then you do
// ```java
// hanoi(height-1, start, helper);   // step 1
// // └─> move disk from start → end  ⟵ this is step 2
// hanoi(height-1, helper, end);     // step 3
// ```

// ---

// ### 2. What your code does

// Your code is almost modeling exactly that—except in step 2 you have:

// ```java
// // you computed helper correctly:
// int helper = …;

// // correct: move height–1 from start to helper
// steps.addAll(hanoi(height-1, start, helper));

// // **but then** you do
// steps.add(new Pair<>(start, helper));  
// // ← this is moving the big disk start → helper (the *aux*!), not start → end

// // finally: move the n–1 disks from helper to end
// steps.addAll(hanoi(height-1, helper, end));
// ```

// So instead of moving the “bottom” disk to its intended destination (`end`), the code is moving it back onto the *auxiliary* peg (`helper`).  That’s why, for example, when you try `hanoi(1, 1, 2)` you end up with a single move `(1 → 3)` instead of the correct `(1 → 2)`.

// ---

// ### 3. The minimal fix

// Change the “move” line so that the disk goes **from** `start` **to** `end`, not to `helper`.  In your code, replace:

// ```java
// steps.add(new Pair<Integer,Integer>(start, helper));
// ```

// with

// ```java
// steps.add(new Pair<Integer,Integer>(start, end));
// ```

// Here’s that snippet in context:

// ```java
// public static List<Pair<Integer,Integer>> hanoi(int height, int start, int end) {
//     List<Pair<Integer,Integer>> steps = new ArrayList<>();

//     if (height > 0) {
//         // find the spare peg
//         int helper = 6 - start - end;   // equivalent to your PriorityQueue trick

//         // 1) move n–1 from start to helper
//         steps.addAll(hanoi(height-1, start, helper));

//         // 2) move the largest disk from start → end  ← **fixed here**
//         steps.add(new Pair<>(start, end));

//         // 3) move n–1 from helper to end
//         steps.addAll(hanoi(height-1, helper, end));
//     }

//     return steps;
// }
// ```

// ---

// ### 4. Optional clean-ups

// 1. **Compute `helper` arithmetically**  
//    Instead of building & trimming a small `PriorityQueue`, you can do:
//    ```java
//    int helper = 6 - start - end;
//    ```
//    since 1+2+3 = 6.

// 2. **Overload for defaults**  
//    If you want callers to only ever say `hanoi(n)` and default to pegs 1→3, add
//    ```java
//    public static List<Pair<Integer,Integer>> hanoi(int height) {
//      return hanoi(height, 1, 3);
//    }
//    ```

// With that one-line fix your recursion will faithfully implement the classic three-step solution.
// ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
