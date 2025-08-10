from collections import Counter
from typing import List, Tuple, Optional

def main(n: int, lst: List[str]) -> int:
    # 1) Split into two lists by alternating indices
    lst1, lst2 = [], []
    flag = True
    for e in lst:
        if flag:
            lst1.append(e)
        else:
            lst2.append(e)
        flag = not flag

    # 2) Count frequencies
    c1 = Counter(lst1)
    c2 = Counter(lst2)

    # 3) Helper to get (element, count) of top two frequencies
    def top_two(counter: Counter) -> Tuple[Tuple[Optional[str], int], Tuple[Optional[str], int]]:
        items = counter.most_common()
        if not items:
            return (None, 0), (None, 0)
        mode = items[0]
        second = items[1] if len(items) > 1 else (None, 0)
        return mode, second

    (e1, cnt1), (e1_2, cnt1_2) = top_two(c1)
    (e2, cnt2), (e2_2, cnt2_2) = top_two(c2)

    # 4) If the top choices differ, we can keep all of both modes.
    if e1 != e2:
        return n - (cnt1 + cnt2)
    # 5) If they coincide, we must drop either
    #    (mode1 + second2) or (second1 + mode2), whichever is larger.
    else:
        keep = max(cnt1 + cnt2_2, cnt1_2 + cnt2)
        return n - keep

if __name__ == "__main__":
    n = int(input())
    lst = input().split()
    print(main(n, lst))
