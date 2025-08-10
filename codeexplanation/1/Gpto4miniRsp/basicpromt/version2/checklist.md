| Coverage | Item                                   | Notes                                                                                                                                        |
| :------: | -------------------------------------- | -------------------------------------------------------------------------------------------------------------------------------------------- |
|    ✔️    | Base Case (Termination Condition)      | Clearly states that if `left >= right`, the subarray has 0 or 1 element and is already sorted.                                               |
|    ✔️    | Divide Step (Computing `center`)       | Describes `center = (left + right) / 2` and the resulting subarray split.                                                                    |
|    ✔️    | Recursive Calls                        | Explicitly shows and explains the two recursive calls for left and right halves.                                                             |
|    ✔️    | Merge Function Signature & Parameters  | Provides the signature, lists parameters, and later clarifies `leftPos`, `rightPos`, `leftEnd`, `rightEnd`, `tmpPos`, and `numElements`.     |
|    ✔️    | Core Merging Loop                      | Walks through the `while (leftPos ≤ leftEnd && rightPos ≤ rightEnd)` loop with `compareTo` logic.                                            |
|    ✔️    | Draining Remaining Elements            | Covers both leftover-copy loops for the left and right runs.                                                                                 |
|    ✔️    | Copying Back to Original Array         | Explains the backward copy via the `for` loop decrementing `rightEnd` to avoid overwriting.                                                  |
|    ✔️    | Role of `tmpArray` (Auxiliary Space)   | Notes that `tmpArray` is used as an auxiliary array to hold merged results before copying back.                                              |
|    ✔️    | Use of `Comparable` & `compareTo`      | Demonstrates and explains how `compareTo` drives element ordering in the merge step.                                                         |
|    ✔️    | Handling Edge Cases                    | Implicitly handled via the base case for empty or single-element subarrays.                                                                  |
|    ✔️    | Parameter Roles & Pre-/Post-conditions | Clearly defines what each parameter represents (`a`, `tmpArray`, `left`, `right`), satisfying parameter roles.                               |
|    ✔️    | High-Level Algorithm Summary           | Provides a concise “divide → recursively sort → merge” overview at the top.                                                                  |   
