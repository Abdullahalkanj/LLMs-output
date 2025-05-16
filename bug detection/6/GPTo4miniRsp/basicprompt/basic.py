# ChatGPT4omini output using Basic prompt


# ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
# The core bug here turned out not to be in your arithmetic at the very end, but in the way you were:

# Mis-indenting

# You sorted your lst1 / lst2 inside the for e in lst: loop instead of after it.

# You also accidentally left your b = not b at the wrong indentation level, so you weren’t alternating properly.

# Never “closing out” your very last run of identical elements when you walked the sorted lists to find the top-1 and top-2 frequencies.

# Rather than try to untangle all of the manual counting, the simplest fix is to

# split the two parity-classes properly,

# use collections.Counter to grab the top two frequencies in each,

# and then just implement the two-cases logic at the end.

# Here’s a drop-in replacement for your entire main() that fixes the bug, preserves exactly the same input/output interface, and is both (a) correct and (b) far more readable:

from collections import Counter
import sys

def main(n: int, lst: list) -> int:
    # 1) split into the two parity-classes
    lst1, lst2 = [], []
    for i, e in enumerate(lst):
        if i % 2 == 0:
            lst1.append(e)
        else:
            lst2.append(e)

    # 2) count frequencies in each
    cnt1 = Counter(lst1)
    cnt2 = Counter(lst2)

    # 3) pull off the top two (value, freq) pairs in each
    #    if there aren't two distinct values, pad with (None, 0)
    common1 = cnt1.most_common(2) + [(None, 0)]
    common2 = cnt2.most_common(2) + [(None, 0)]

    (v11, f11), (v12, f12) = common1[0], common1[1]
    (v21, f21), (v22, f22) = common2[0], common2[1]

    # 4) if the two highest-frequency values differ, we can keep both
    if v11 != v21:
        return n - (f11 + f21)
    # 5) otherwise we must “break the tie” by giving one side its #2 choice
    #    choose whichever yields fewer recolors
    else:
        # option A: keep lst1’s top, lst2’s second
        cA = n - (f11 + f22)
        # option B: keep lst2’s top, lst1’s second
        cB = n - (f21 + f12)
        return min(cA, cB)

if __name__ == "__main__":
    n = int(sys.stdin.readline())
    lst = sys.stdin.readline().split()
    print(main(n, lst))

# What was going wrong in your original version
# Sorting too early and flipping b at the wrong level meant your two lists weren’t the true “even-index” and “odd-index” sublists you thought you had.

# Your manual “run-length” logic that tried to track the top-1 and top-2 frequencies never performed its final comparison on the very last group of identical elements, so whenever the most frequent element happened to sit at the end of the sorted list it simply never got counted.

# By moving the split, the sort, and the frequency-finding into these clear, small steps (and leaning on Counter for the counting), both of those bugs disappear.
# ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


# Research Team:
# the output
# ================== SUMMARY ==================
# Program: buggy_9.py
# Tests passed: 20/20
# Pass rate: 100.00%
# =============================================