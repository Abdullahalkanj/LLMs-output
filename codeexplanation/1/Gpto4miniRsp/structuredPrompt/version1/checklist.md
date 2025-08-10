| Coverage | Item                                   | Notes                                                                                                                          |
| :------: | -------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------ |
|    ✔️    | Base Case (Termination Condition)      | Explains recursion stops when subarrays are of length 1 (`left >= right`), which are trivially sorted.                         |
|    ✔️    | Divide Step (Computing `center`)       | Comments `int center = (left + right) / 2` and how that splits the subarray.                                                   |
|    ✔️    | Recursive Calls                        | Lists and explains the two recursive calls on left and right halves.                                                           |
|    ✔️    | Merge Function Signature & Parameters  | Includes `merge(...)` signature and defines `leftPos`, `rightPos`, `leftEnd`, `rightEnd`, `tmpPos`, `numElements`.             |
|    ✔️    | Core Merging Loop                      | Covers the `while (leftPos ≤ leftEnd && rightPos ≤ rightEnd)` compare-and-copy logic using `compareTo()`.                      |
|    ✔️    | Draining Remaining Elements            | Shows both `while` loops that copy any remaining elements from the left or right run.                                          |
|    ✔️    | Copying Back to Original Array         | Explains the backward `for`-loop copying `numElements` merged entries back into `a[]`.                                         |
|    ✔️    | Role of `tmpArray` (Auxiliary Space)   | Notes that `tmpArray` is a scratch array used to hold merged results before writing back.                                      |
|    ✔️    | Use of `Comparable` & `compareTo`      | Highlights use of `compareTo()` for ordering in the merge loop.                                                                |
|    ✔️    | Handling Edge Cases                    | Covers base case for subarrays of size ≤1 (trivially sorted).                                                                  |
|    ✔️    | Parameter Roles & Pre-/Post-conditions | Clearly defines inputs (`a`, `tmpArray`, `left`, `right`) and that output is the in-place sorted array.                        |
|    ✔️    | High-Level Algorithm Summary           | Provides a concise “divide → sort halves → merge” overview in the summary.                                                     |

