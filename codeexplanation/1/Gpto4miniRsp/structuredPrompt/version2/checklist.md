| Coverage | Item                                   | Notes                                                                                                     |
| :------: | -------------------------------------- | --------------------------------------------------------------------------------------------------------- |
|    ✔️    | Base Case (Termination Condition)      | Explicitly states when `left >= right` the segment has zero or one element and is already sorted.         |
|    ✔️    | Divide Step (Computing `center`)       | Shows `center = (left + right) / 2` and how it defines the two halves.                                    |
|    ✔️    | Recursive Calls                        | Clearly lists the recursive calls to sort the left and right halves.                                      |
|     ❌    | Merge Function Signature & Parameters  | Includes the signature but provides only partial detail on parameters (`leftPos`/`rightPos` roles).       |
|    ✔️    | Core Merging Loop                      | Covers the `while(... compareTo ...)` logic for merging the two runs.                                     |
|    ✔️    | Draining Remaining Elements            | Describes the loops copying leftover elements from each half.                                             |
|    ✔️    | Copying Back to Original Array         | Explains the backward `for`-loop copying merged entries back into `a`.                                    |
|    ✔️    | Role of `tmpArray` (Auxiliary Space)   | Mentions use of a temporary array to hold merged results before writing back.                             |
|    ✔️    | Use of `Comparable` & `compareTo`      | Highlights use of `compareTo()` for element ordering in the merge step.                                   |
|    ✔️    | Handling Edge Cases                    | Covers behavior for segments of size 0 or 1 via the base-case check.                                      |
|     ❌    | Parameter Roles & Pre-/Post-conditions | Implicitly shows how parameters control behavior but lacks a standalone summary of each parameter’s role. |
|    ✔️    | High-Level Algorithm Summary           | Provides a concise “split → sort halves → merge” overview at the start.                                   |

