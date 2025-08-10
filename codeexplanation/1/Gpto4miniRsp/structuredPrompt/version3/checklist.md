| Coverage | Item                                   | Notes                                                                                            |
| :------: | -------------------------------------- | ------------------------------------------------------------------------------------------------ |
|    ✔️    | Base Case (Termination Condition)      | Explicitly checks `if (left < right)` vs. `left ≥ right` for subarrays of size ≤ 1.              |
|    ✔️    | Divide Step (Computing `center`)       | Computes `center = (left + right) / 2` to split the array.                                       |
|    ✔️    | Recursive Calls                        | Shows `mergeSort(a, tmpArray, left, center)` and `mergeSort(..., center+1, right)`.              |
|     ❌    | Merge Function Signature & Parameters  | Signature is shown, but `leftPos`, `rightPos`, and `rightEnd` roles aren’t separately described. |
|    ✔️    | Core Merging Loop                      | Covers the `while (leftPos ≤ leftEnd && rightPos ≤ rightEnd)` with `compareTo` logic.            |
|    ✔️    | Draining Remaining Elements            | Includes loops for copying leftover elements from left or right run.                             |
|    ✔️    | Copying Back to Original Array         | Explains the backward `for`-loop copying `numElements` back into `a[]`.                          |
|    ✔️    | Role of `tmpArray` (Auxiliary Space)   | Notes that `tmpArray` is an auxiliary array of the same length used during merging.              |
|    ✔️    | Use of `Comparable` & `compareTo`      | Describes reliance on `compareTo()` for element comparisons.                                     |
|    ✔️    | Handling Edge Cases                    | Discusses empty/single‐element subarrays, odd lengths, and null‐entry implications.              |
|    ✔️    | Parameter Roles & Pre-/Post-conditions | Defines `a`, `tmpArray`, `left`, `right` (inclusive bounds) for `mergeSort`.                     |
|    ✔️    | High-Level Algorithm Summary           | Gives a divide-and-conquer overview: split until size 1, then merge.                             |
