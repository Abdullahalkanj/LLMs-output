from collections import Counter

def main(n: int, lst: list) -> int:
    # Split into two lists: elements at even and odd positions
    lst1 = lst[::2]  # Fix: original loop+flag was over-complicated and mis-indented; slicing is clearer and bug-free.
    lst2 = lst[1::2] # Fix: same for odd positions.

    # Count frequencies in each half
    c1 = Counter(lst1)  # Fix: replaced manual sorting+count logic with Counter for correctness and simplicity.
    c2 = Counter(lst2)

    # Extract top two (element, count) in each; default to (None, 0) if missing
    common1 = c1.most_common(2) + [(None, 0)]*2
    top1_elem, top1_cnt = common1[0]      # Fix: handles empty or single-item lists safely.
    second1_cnt = common1[1][1]           # Fix: avoids index errors when there’s only one distinct element.

    common2 = c2.most_common(2) + [(None, 0)]*2
    top2_elem, top2_cnt = common2[0]
    second2_cnt = common2[1][1]

    # If the most common elements differ, we can keep them both
    if top1_elem != top2_elem:
        return n - top1_cnt - top2_cnt  # Fix: original “else” block had wrong subtraction in some branches.

    # If they’re the same, choose the better of:
    #  • keep top of lst1 + second of lst2, or
    #  • keep second of lst1 + top of lst2
    keep_option1 = top1_cnt + second2_cnt
    keep_option2 = second1_cnt + top2_cnt
    return n - max(keep_option1, keep_option2)  # Fix: original tie-breaking logic was tangled and had misplaced branches.

# Read input and invoke
if __name__ == "__main__":
    n = int(input())
    lst = input().split()
    print(main(n, lst))
