
# ChatGPT4omini output using Structured prompt


# ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------# The core bug is an indentation error in your splitting loop:
for e in lst :
    if b :
        lst1.append(e)
    else :
        lst2.append(e)
        b = not b   # ← this line is indented under the `else`, so it only runs when b was already False
    lst1.sort()    # ← also accidentally inside the loop
# Because b = not b lives inside the else, it never executes on the very first iteration (when b is True), so b never toggles at all. That means every element goes into lst1, lst2 stays empty, and your later lst2[0] blows up with an IndexError.

# Fixed version
def main(n: int, lst: list) -> int:
    # 1) Split alternately into lst1 (odd positions) and lst2 (even positions)
    lst1, lst2 = [], []
    toggle = True
    for e in lst:
        if toggle:
            lst1.append(e)
        else:
            lst2.append(e)
        toggle = not toggle
    # 2) Sort once, after the loop
    lst1.sort()
    lst2.sort()

    # 3) Helper to find the top two frequencies in a sorted list
    def top_two(arr):
        top_val = None
        top_cnt = second_cnt = 0
        last = None
        cnt = 0
        for x in arr + [None]:             # sentinel to flush final run
            if x == last:
                cnt += 1
            else:
                if last is not None:
                    if cnt > top_cnt:
                        second_cnt, top_cnt = top_cnt, cnt
                        top_val = last
                    elif cnt > second_cnt:
                        second_cnt = cnt
                last = x
                cnt = 1
        return top_val, top_cnt, second_cnt

    v1, c1, s1 = top_two(lst1)
    v2, c2, s2 = top_two(lst2)

    # 4) If the two most-frequent values differ, keep both; otherwise pick the better second-best pairing
    if v1 != v2:
        return n - (c1 + c2)
    else:
        return n - max(c1 + s2, c2 + s1)


if __name__ == "__main__":
    n = int(input())
    lst = input().split()
    print(main(n, lst))

# What changed
# toggle = not toggle is now outside the if/else, so it flips on every iteration.

# Moved the two .sort() calls to after the loop (you only need to sort once).

# Pulled your mode/second-mode logic into a small helper that’s easier to follow and correctly scoped.

# Simplified the final “if the top values are the same” branch to a single max(...) expression.

# With those fixes in place, you’ll actually split into two non-empty lists and compute the right “minimal changes” answer without any IndexError (and with the correct toggle logic).
# --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


# Research Team:
# the output
# ================== SUMMARY ==================
# Program: buggy_9.py
# Tests passed: 20/20
# Pass rate: 100.00%
# =============================================