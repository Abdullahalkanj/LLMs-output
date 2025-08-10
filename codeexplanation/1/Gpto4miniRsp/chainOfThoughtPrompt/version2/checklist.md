| Coverage | Item                                   | Notes                                                                                                           |
| :------: | -------------------------------------- | --------------------------------------------------------------------------------------------------------------- |
|     ❌    | Base Case (Termination Condition)      | Implies recursion stops when `left < right` is false, but doesn’t explicitly state “if `left ≥ right`, return.” |
|    ✔️    | Divide Step (Computing `center`)       | Shows `int center = (left + right) / 2` and defines the two halves.                                             |
|    ✔️    | Recursive Calls                        | Clearly lists the two recursive `mergeSort(...)` calls on left and right subarrays.                             |
|    ✔️    | Merge Function Signature & Parameters  | Includes the signature and defines `leftPos`, `rightPos`, `rightEnd`, `leftEnd`, `tmpPos`, and `numElements`.   |
|    ✔️    | Core Merging Loop                      | Describes the `while (leftPos ≤ leftEnd && rightPos ≤ rightEnd)` compare-and-copy logic.                        |
|    ✔️    | Draining Remaining Elements            | Covers both leftover-copy loops for left and right runs.                                                        |
|    ✔️    | Copying Back to Original Array         | Explains the backward `for`-loop copying `numElements` back into `a[]`.                                         |
|    ✔️    | Role of `tmpArray` (Auxiliary Space)   | Notes use of an auxiliary array `tmpArray` of the same length for merging.                                      |
|    ✔️    | Use of `Comparable` & `compareTo`      | Details use of `compareTo()` in the merge loop for ordering.                                                    |
|    ✔️    | Handling Edge Cases                    | Discusses behavior on small arrays (n ≤ 1), already sorted/reverse/all-equal inputs.                            |
|    ✔️    | Parameter Roles & Pre-/Post-conditions | Defines roles of `a`, `tmpArray`, `left`, `right`, and merge pointers.                                          |
|    ✔️    | High-Level Algorithm Summary           | Provides a succinct “split → sort halves → merge” overview at the top.                                          |

