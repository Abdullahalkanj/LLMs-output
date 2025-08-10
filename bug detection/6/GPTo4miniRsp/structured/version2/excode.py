from collections import Counter
from typing import List

def main(n: int, lst: List[str]) -> int:
    # 1) Split into two lists, alternating elements
    lst1: List[str] = []
    lst2: List[str] = []
    toggle = True
    for e in lst:
        if toggle:
            lst1.append(e)
        else:
            lst2.append(e)
        toggle = not toggle  # fix: flip every iteration

    # 2) Count frequencies in each sublist
    cnt1 = Counter(lst1)
    cnt2 = Counter(lst2)

    # Helper: get top two (value, count), defaulting to (None, 0)
    def top_two(counter: Counter):
        common = counter.most_common(2)
        if not common:
            return (None, 0), (None, 0)
        if len(common) == 1:
            return common[0], (None, 0)
        return common[0], common[1]

    (val1, freq1), (_, freq1_2) = top_two(cnt1)
    (val2, freq2), (_, freq2_2) = top_two(cnt2)

    # 3) If the two most frequent values differ, we're done
    if val1 != val2:
        return n - (freq1 + freq2)

    # 4) Otherwise, try skipping the runner-up in either group, pick the best
    keep_if_skip1 = freq1_2 + freq2   # use second-best from group1
    keep_if_skip2 = freq1   + freq2_2 # use second-best from group2
    best_keep = max(keep_if_skip1, keep_if_skip2)

    return n - best_keep


if __name__ == "__main__":
    n = int(input())
    lst = input().split()
    print(main(n, lst))
