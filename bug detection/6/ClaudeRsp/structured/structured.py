# Claude output using Structured prompt


# ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
# Bug Analysis
# The primary issue in this code is indentation problems, which cause syntax errors and logic errors. Python uses indentation to determine code blocks, and the indentation in this function is inconsistent.
# Other issues I've identified:

# The indentation is completely incorrect throughout the function
# There may be logic errors in the tracking of element frequencies
# Some variable references are potentially problematic (like using e after the loop)

# Code Explanation
# The function appears to be designed to:

# Split the input list into two alternating sublists
# Sort each sublist
# Find the most frequent elements in each sublist
# Calculate a result based on the frequencies of these elements

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
    
    c1 = [[None, 0], 0]  # Changed empty string to None
    c2 = [[None, 0], 0]  # Changed empty string to None
    
    # Handle empty lists edge case
    if not lst1:
        return n
    if not lst2:
        return n
    
    # Process first list
    last = lst1[0]
    a = 0
    for e in lst1:
        if last == e:
            a += 1
        else:
            if a >= c1[0][1]:
                c1[1] = c1[0][1]
                c1[0][0] = last  # Changed e to last
                c1[0][1] = a
            elif a > c1[1]:
                c1[1] = a
            last = e
            a = 1
    
    # Handle the last element of lst1
    if a >= c1[0][1]:
        c1[1] = c1[0][1]
        c1[0][0] = last
        c1[0][1] = a
    elif a > c1[1]:
        c1[1] = a
    
    # Process second list
    last = lst2[0]
    a = 0
    for e in lst2:
        if last == e:
            a += 1
        else:
            if a >= c2[0][1]:
                c2[1] = c2[0][1]
                c2[0][0] = last  # Changed e to last
                c2[0][1] = a
            elif a > c2[1]:
                c2[1] = a
            last = e
            a = 1
    
    # Handle the last element of lst2
    if a >= c2[0][1]:
        c2[1] = c2[0][1]
        c2[0][0] = last
        c2[0][1] = a
    elif a > c2[1]:
        c2[1] = a
    
    # Calculate the result
    if c1[0][0] == c2[0][0]:
        if c1[0][1] > c2[0][1] or (c1[0][1] == c2[0][1] and c2[1] > c1[1]):
            return n - c1[0][1] - c2[1]
        else:
            return n - c2[0][1] - c1[1]
    else:
        return n - c1[0][1] - c2[0][1]

# Input handling
if __name__ == "__main__":
    n = int(input())
    lst = input().split()
    print(main(n, lst))


# Key Bug Fixes:

# Indentation: Fixed all indentation issues throughout the code.
# Logic Errors:

# Changed c1[0][0] = e to c1[0][0] = last in the frequency tracking logic.
# Same correction for c2[0][0] = e to c2[0][0] = last.
# These changes ensure we're storing the correct element that has the frequency we're tracking.


# Initialization:

# Changed empty string "" to None for more clarity in the frequency tracking lists.
# Added edge case handling for empty lists.


# Variable Clarity:

# Ensured proper assignment of the last variable throughout the code.



# Algorithm Explanation:
# This function analyzes frequencies of elements in two alternating sublists. It:

# Divides the input list into two sublists by alternating elements
# For each sublist, finds the most frequent element and the second most frequent count
# Calculates a result based on these frequencies, with special handling for when both sublists have the same most frequent element

# The core logic involves finding the optimal way to replace elements to minimize the final count, which is what the function returns.
# ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


# Research Team:
# the output
# ================== SUMMARY ==================
# Program: buggy_9.py
# Tests passed: 20/20
# Pass rate: 100.00%
# =============================================