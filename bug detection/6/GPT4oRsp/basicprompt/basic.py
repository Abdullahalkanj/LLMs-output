# ChatGPT4o output using basic prompt


# ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
# Alright, the code you posted has several problems:

# Indentation issues: Python requires strict indentation.

# Logical bugs: like incorrect updating of last and wrong placement of checks.

# Misplaced or duplicated code: many sections are unnecessarily repeated.

# Wrong initialization of some variables.


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
        b = not b

    lst1.sort()
    lst2.sort()

    c1 = [["", 0], 0]
    c2 = [["", 0], 0]

    # Process lst1
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
    if a >= c1[0][1]:
        c1[1] = c1[0][1]
        c1[0][0] = last
        c1[0][1] = a
    elif a > c1[1]:
        c1[1] = a

    # Process lst2
    if lst2:
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
# Main execution
n = int(input())
lst = input().split()
print(main(n, lst))

# Summary of Main Fixes
# Proper indentation everywhere.

# Correct initial value of a = 1 when starting to count (you had a = 0 before, incorrect).

# Process the first element separately (don't forget to finalize after the loop).

# Correctly manage case when lst2 could be empty.

# Handle ties properly when checking c1[0][0] == c2[0][0].

# What does the code actually do?
# It:

# Divides the list into two groups: one with even-indexed items, another with odd-indexed items.

# Finds the most frequent element in each group.

# Calculates a minimal number of deletions needed to make all even indices have one value and all odd indices have another.
# ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


# Research Team:
# the output 
# ================== SUMMARY ==================
# Program: buggy_9.py
# Tests passed: 20/20
# Pass rate: 100.00%
# =============================================