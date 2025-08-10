from collections import Counter
import sys

def main(n: int, lst: list[str]) -> int:
    # split into the two alternating sublists
    lst1 = lst[::2]
    lst2 = lst[1::2]

    # count frequencies
    c1 = Counter(lst1)
    c2 = Counter(lst2)

    # get top two (element, count) pairs, filling missing slots with count=0
    top1 = c1.most_common(2) + [(None, 0)]
    top2 = c2.most_common(2) + [(None, 0)]
    (val1, cnt1), (_, sec1) = top1[0], top1[1]
    (val2, cnt2), (_, sec2) = top2[0], top2[1]

    # if the most common elements differ, we can keep both of them
    if val1 != val2:
        return n - (cnt1 + cnt2)
    # otherwise we have to pick the better of:
    #   (keep lst1's mode + lst2's second)  vs.  (keep lst2's mode + lst1's second)
    keep_if_switch = max(cnt1 + sec2, cnt2 + sec1)
    return n - keep_if_switch

if __name__ == "__main__":
    n = int(sys.stdin.readline())
    lst = sys.stdin.readline().split()
    print(main(n, lst))
