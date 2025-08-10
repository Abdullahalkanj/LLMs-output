| Coverage | Item                                   | Notes                                                                                                                               |
| :------: | -------------------------------------- | ----------------------------------------------------------------------------------------------------------------------------------- |
|    ✔️    | Base Case (Termination Condition)      | Clearly describes that when `left == right` the subarray is size 1 (and sorted).                                                    |
|    ✔️    | Divide Step (Computing `center`)       | Describes computing `center = (left + right) / 2` to split the range.                                                               |
|    ✔️    | Recursive Calls                        | Shows the two recursive calls to sort left and right halves.                                                                        |
|     ❌    | Merge Function Signature & Parameters  | Includes the signature and parameters, but omits explicit discussion of `leftEnd` and `numElements`.                                |
|    ✔️    | Core Merging Loop                      | Covers the `while (leftPos ≤ leftEnd && rightPos ≤ rightEnd)` compare-and-copy logic.                                               |
|    ✔️    | Draining Remaining Elements            | Describes the loops that copy leftover elements from either half.                                                                   |
|    ✔️    | Copying Back to Original Array         | Explains the backward `for`-loop copying `numElements` merged entries back into `a[]`.                                              |
|    ✔️    | Role of `tmpArray` (Auxiliary Space)   | Notes use of an auxiliary array `tmpArray` of the same length for merging.                                                          |
|    ✔️    | Use of `Comparable` & `compareTo`      | Demonstrates and explains use of `compareTo()` for element ordering.                                                                |
|    ✔️    | Handling Edge Cases                    | Discusses empty arrays, `null` elements, and invalid index ranges.                                                                  |
|    ✔️    | Parameter Roles & Pre-/Post-conditions | Defines roles of `a`, `tmpArray`, `left`, `right`, and merge pointers (`leftPos`, `rightPos`, `tmpPos`).                            |
|    ✔️    | High-Level Algorithm Summary           | Provides a concise “divide → recursively sort → merge” overview at the start.                                                       |
