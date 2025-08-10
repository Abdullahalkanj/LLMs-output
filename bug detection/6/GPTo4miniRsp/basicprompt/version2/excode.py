from collections import Counter
from typing import List

def main(n: int, lst: List[str]) -> int:
    # 1) Split into two lists:
    lst1 = lst[0::2]  # elements at even indices (0,2,4,…)
    lst2 = lst[1::2]  # elements at odd indices  (1,3,5,…)

    # 2) Count frequencies:
    cnt1 = Counter(lst1)
    cnt2 = Counter(lst2)

    # 3) Find the two most common for each list:
    #    .most_common() returns [(elem, freq), …], sorted by freq desc.
    top1 = cnt1.most_common(2) + [(None, 0)]  # pad so that index 1 always exists
    top2 = cnt2.most_common(2) + [(None, 0)]

    # Unpack
    (v1a, f1a), (v1b, f1b) = top1[0], top1[1]
    (v2a, f2a), (v2b, f2b) = top2[0], top2[1]

    # 4) If their most-common elements differ, we can keep both:
    if v1a != v2a:
        return n - (f1a + f2a)

    # 5) Otherwise, choose the best alternative:
    #    Option A: keep lst1’s 1st and lst2’s 2nd
    #    Option B: keep lst1’s 2nd and lst2’s 1st
    keep_a = f1a + f2b
    keep_b = f1b + f2a
    keep_best = max(keep_a, keep_b)

    return n - keep_best


if __name__ == "__main__":
    n = int(input())
    lst = input().split()
    print(main(n, lst))
