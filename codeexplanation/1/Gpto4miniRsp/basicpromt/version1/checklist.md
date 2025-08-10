| Coverage | Item                                   | Notes                                                                                              |
| :------: | -------------------------------------- | -------------------------------------------------------------------------------------------------- |
|    ✔️    | Base Case (Termination Condition)      | Clearly describes `if (left >= right)` stops recursion.                                            |
|    ✔️    | Divide Step (Computing `center`)       | Shows `center = (left + right) / 2` in logic.                                                      |
|    ✔️    | Recursive Calls                        | Covers both calls to sort the left and right halves.                                               |
|    ✔️    | Merge Function Signature & Parameters  | Lists and labels `leftPos`, `rightPos`, `leftEnd`, `rightEnd`, `tmpPos`.                           |
|    ✔️    | Core Merging Loop                      | Explains `while (…) compareTo …` copying into `tmpArray`.                                          |
|    ✔️    | Draining Remaining Elements            | Shows both `while` loops for leftover elements.                                                    |
|    ✔️    | Copying Back to Original Array         | Describes backward copy via `for` loop to avoid overwriting.                                       |
|    ✔️    | Role of `tmpArray` (Auxiliary Space)   | Notes `tmpArray` is a scratch array; mentions O(n) extra space.                                    |
|    ✔️    | Use of `Comparable` & `compareTo`      | Shows and discusses use of `compareTo` for ordering.                                               |
|    ✔️    | Handling Edge Cases                    | Covers empty/single-element via base case.                                                         |
|    ✔️    | Parameter Roles & Pre-/Post-conditions | Clearly defines each parameter’s role in both methods.                                             |
|    ✔️    | High-Level Algorithm Summary           | Gives “divide → sort halves → merge” overview.                                                     |

