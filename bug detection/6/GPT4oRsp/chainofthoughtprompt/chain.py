# ChatGPT4o output using Chain-of-thought prompt


# ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

# 1. First, general understanding:
# The function main is given n (an integer) and lst (a list).

# It splits the list into two lists lst1 and lst2, alternating elements between them.

# Then it sorts both lists.

# After sorting, it seems to be finding the most frequent element in lst1 and lst2 separately, keeping track of:

# The most frequent element

# The second most frequent count

# Finally, it calculates and returns something based on these frequencies.

# From the name of variables and structure, this looks like a classic minimize changes needed to satisfy some condition problem.

# 2. Observations about the bugs and issues:
# Indentation issues:
# The code is severely broken with indentation. Some if, else, and loop blocks are improperly indented.

# Python is sensitive to indentation!

# Logical issues:
# When looping through lst1 and lst2 to count frequencies, the code forgets to handle the last element properly sometimes.

# When updating c1 and c2, it mistakenly updates with the wrong e inside the else block (it should use last, not e when updating).

# Misuse of variables:
# c1 and c2 store two things:

# c1[0]: a pair [element, count] for the most frequent element

# c1[1]: second best frequency

# However, when updating, the code sometimes overwrites the wrong parts.

# Parsing input at the end:
# The code at the bottom seems mashed together:


n = int ( input ( ) )
lst = input ( ) . split ( )
print ( main ( n , lst ) )
# but it should be outside the main function, properly indented.

# 3. Strategy for the fix:
# Fix the indentation first to make code readable.

# Correct the frequency counting for lst1 and lst2.

# Ensure that after finishing each list, the last counted element is properly considered.

# Fix the final comparison between c1 and c2.

# 4. Now let's write the corrected version step by step:

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

    c1 = [["", 0], 0]  # [most frequent element and its count], second best count
    c2 = [["", 0], 0]

    # Process lst1
    last = lst1[0]
    a = 0
    for e in lst1:
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
    # Don't forget the last group
    if a >= c1[0][1]:
        c1[1] = c1[0][1]
        c1[0][0] = last
        c1[0][1] = a
    elif a > c1[1]:
        c1[1] = a

    # Process lst2
    last = lst2[0]
    a = 0
    for e in lst2:
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
    # Don't forget the last group
    if a >= c2[0][1]:
        c2[1] = c2[0][1]
        c2[0][0] = last
        c2[0][1] = a
    elif a > c2[1]:
        c2[1] = a

    # Now decide based on most frequent elements
    if c1[0][0] == c2[0][0]:
        if c1[0][1] > c2[0][1] or (c1[0][1] == c2[0][1] and c2[1] > c1[1]):
            return n - c1[0][1] - c2[1]
        else:
            return n - c2[0][1] - c1[1]
    else:
        return n - c1[0][1] - c2[0][1]


if __name__ == "__main__":
    n = int(input())
    lst = input().split()
    print(main(n, lst))

# 5. Summary of changes:
# Indentation fixed throughout.

# last used correctly during frequency calculation.

# Handled the final frequency group properly after the loop.

# Input parsing separated and cleaned up.

# Correct return calculation depending on frequency ties.
# ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


# Research Team:
# the output 
# ================== SUMMARY ==================
# Program: buggy_9.py
# Tests passed: 20/20
# Pass rate: 100.00%
# =============================================