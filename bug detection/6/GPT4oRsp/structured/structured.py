# ChatGPT4o output using Structured prompt


# ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

# Summary of the code’s idea:

# The function main:

# Alternates elements from lst into lst1 and lst2.

# Then, for both lists separately:

# Finds the most frequent element (and the second-most if needed).

# Finally, based on those findings, it calculates and returns a value based on the counts.

# Major issues in the code:

# Indentation is severely broken.

# Python requires consistent indentation (spaces or tabs, and correct levels).

# Here, the block structure is invalid. (Would raise IndentationError.)

# Bug: Wrong element saved when updating max frequency.

# When updating c1[0][0] = e, you should actually set it to last, because last is the element whose count a you've been counting up.

# Similarly for c2.

# Initialization bugs:

# a = 0 initially is wrong; it should be a = 1 because you're starting with last = lst1[0], and you already have one occurrence of last.

# Same for lst2.

# Final edge case bug:

# After the last loop, you must perform a final update to the counters, after the loop ends, because the last sequence won't trigger the else clause (no new element to compare against).

# Other minor problems:

# Use of list of list [[" ", 0], 0] is messy; using simple named structures would be much clearer.

# Unnecessary whitespace and messy bracket style.

# No input validation.

# Problem ID: p03244_s908501993
# Buggy version
def main(n: int, lst: list) -> int:
    b = True
    lst1 = []
    lst2 = []
    for e in lst:
        if b:
            lst1.append(e)
        else:
            lst2.append(e)
        b = not b  # <=== Important, must toggle each time!

    lst1.sort()
    lst2.sort()

    c1 = [["", 0], 0]  # [ [most_common_element, count], second_count ]
    c2 = [["", 0], 0]

    # Analyze lst1
    last = lst1[0]
    a = 1
    for e in lst1[1:]:
        if last == e:
            a += 1
        else:
            if a >= c1[0][1]:
                c1[1] = c1[0][1]
                c1[0][0] = last
                c1[0][1] = a
            elif a > c1[1]:
                c1[1] = a
            last = e
            a = 1
    # Final update after the loop
    if a >= c1[0][1]:
        c1[1] = c1[0][1]
        c1[0][0] = last
        c1[0][1] = a
    elif a > c1[1]:
        c1[1] = a

    # Analyze lst2
    last = lst2[0]
    a = 1
    for e in lst2[1:]:
        if last == e:
            a += 1
        else:
            if a >= c2[0][1]:
                c2[1] = c2[0][1]
                c2[0][0] = last
                c2[0][1] = a
            elif a > c2[1]:
                c2[1] = a
            last = e
            a = 1
    if a >= c2[0][1]:
        c2[1] = c2[0][1]
        c2[0][0] = last
        c2[0][1] = a
    elif a > c2[1]:
        c2[1] = a

    if c1[0][0] == c2[0][0]:
        if c1[0][1] > c2[0][1] or (c1[0][1] == c2[0][1] and c2[1] > c1[1]):
            return n - c1[0][1] - c2[1]
        else:
            return n - c2[0][1] - c1[1]
    else:
        return n - c1[0][1] - c2[0][1]

# Read input
n = int(input())
lst = input().split()
print(main(n, lst))

# Key changes explained:

# Toggle b correctly after each element.

# Track last element properly in the frequency counting.

# Correctly update counters at the end of the loop.

# Fix wrong assignment (last, not e) when updating the best element.
# ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


# Research Team:
# the output
# ================== SUMMARY ==================
# Program: buggy_9.py
# Tests passed: 20/20
# Pass rate: 100.00%
# =============================================
