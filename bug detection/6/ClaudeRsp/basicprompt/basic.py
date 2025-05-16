# Claude output using basic prompt


# ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
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
    
    c1 = [[" ", 0], 0]
    c2 = [[" ", 0], 0]
    
    if not lst1:  # Handle empty list case
        return n
    
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
    
    # Handle the last element of lst1
    if a >= c1[0][1]:
        c1[1] = c1[0][1]
        c1[0][0] = last
        c1[0][1] = a
    elif a > c1[1]:
        c1[1] = a
    
    if not lst2:  # Handle empty list case
        return n
    
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
    
    # Handle the last element of lst2
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

# Test the code
n = int(input())
lst = input().split()
print(main(n, lst))


# The main fixes I made:

# Fixed indentation throughout the entire code
# Added empty list checks for lst1 and lst2 to prevent index errors
# Fixed the logic for tracking the last element in each frequency calculation
# Added proper spacing between code blocks for readability

# The core logic of the algorithm remains unchanged. The code splits the input list into two alternating sublists, finds the most frequent elements in each sublist, and calculates a result based on those frequencies.
# Would you like me to explain how this algorithm works in more detail?
# ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


# Research Team:
# the output
# ================== SUMMARY ==================
# Program: buggy_9.py
# Tests passed: 20/20
# Pass rate: 100.00%
# =============================================