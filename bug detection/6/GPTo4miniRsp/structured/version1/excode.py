from collections import Counter
from typing import List, Tuple, Optional

def main(n: int, lst: List[str]) -> int:
    # 1. Split lst into two lists, alternating
    lst1: List[str] = []
    lst2: List[str] = []
    b = True
    for e in lst:
        if b:
            lst1.append(e)
        else:
            lst2.append(e)
        b = not b  # toggle every iteration

    # 2. Count frequencies in each sublist
    cnt1 = Counter(lst1)
    cnt2 = Counter(lst2)

    # Helper to get (element, count) for top and second
    def top_two(counter: Counter) -> Tuple[Tuple[Optional[str], int], Tuple[Optional[str], int]]:
        # Return ((top_elem, top_count), (second_elem, second_count))
        if not counter:
            return (None, 0), (None, 0)
        # most_common gives list sorted by count descending
        common = counter.most_common()
        top = common[0]
        second = common[1] if len(common) > 1 else (None, 0)
        return top, second

    (e1, c1), (e1_s, c1_s) = top_two(cnt1)
    (e2, c2), (e2_s, c2_s) = top_two(cnt2)

    # 3. If the same element is top in both, we have to choose which list
    #    to break ties against by using its second-best count.
    if e1 is not None and e1 == e2:
        # Option A: keep e1 in list1, so we’d need to remove all of list2’s top
        # except its runner-up
        # That costs: remove c1 from list1 + (c2_s) from list2
        cost1 = (n - c1 - c2_s)
        # Option B: keep e2 in list2, remove list1’s top except runner-up
        cost2 = (n - c2 - c1_s)
        return min(cost1, cost2)

    # 4. Otherwise they differ, so just remove all occurrences of both tops
    return n - c1 - c2


if __name__ == "__main__":
    n = int(input())
    lst = input().split()
    print(main(n, lst))
