| Coverage | Item                                   | Notes                                                                                                                      |
| :------: | -------------------------------------- | -------------------------------------------------------------------------------------------------------------------------- |
|    ✔️    | Base Case (Termination Condition)      | Explicitly notes that when `left >= right`, the subarray has ≤ 1 element and is already sorted.                            |
|    ✔️    | Divide Step (Computing `center`)       | Shows `int center = (left + right) / 2` and how it defines the two halves.                                                 |
|    ✔️    | Recursive Calls                        | Clearly lists the two recursive `mergeSort(...)` calls on left and right subarrays.                                        |
|    ✔️    | Merge Function Signature & Parameters  | Includes the `merge(...)` signature and defines `leftPos`, `rightPos`, `leftEnd`, `rightEnd`, `tmpPos`, and `numElements`. |
|    ✔️    | Core Merging Loop                      | Describes the `while (leftPos ≤ leftEnd && rightPos ≤ rightEnd)` compare-and-copy logic.                                   |
|    ✔️    | Draining Remaining Elements            | Covers both loops that copy any remaining elements from either half.                                                       |
|    ✔️    | Copying Back to Original Array         | Explains the backward `for`-loop copying `numElements` merged entries back into `a[]`.                                     |
|    ✔️    | Role of `tmpArray` (Auxiliary Space)   | Notes use of an auxiliary `tmpArray` of the same length to hold merged results.                                            |
|    ✔️    | Use of `Comparable` & `compareTo`      | Details how `compareTo()` is used to decide ordering in the merge step.                                                    |
|    ✔️    | Handling Edge Cases                    | Covers the base case for subarrays of size ≤ 1, implying correct behavior on empty or single‐element arrays.               |
|    ✔️    | Parameter Roles & Pre-/Post-conditions | Defines roles for `a`, `tmpArray`, `left`, `right`, and the merge pointers.                                                |
|    ✔️    | High-Level Algorithm Summary           | Provides a succinct “split → sort halves → merge” overview up front.                                                       |
