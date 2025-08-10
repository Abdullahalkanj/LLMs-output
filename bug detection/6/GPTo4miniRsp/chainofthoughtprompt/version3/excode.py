from collections import Counter

def main(n: int, lst: list) -> int:
    # 1. Split into two alternating subsequences
    subseq1 = lst[0::2]
    subseq2 = lst[1::2]

    # 2. Count frequencies
    cnt1 = Counter(subseq1)
    cnt2 = Counter(subseq2)

    # 3. Get the top two frequencies for each (mode and runner-up)
    #    most_common returns list of (element, count) pairs
    mc1 = cnt1.most_common(2)
    mc2 = cnt2.most_common(2)

    # Unpack, defaulting second-best count to 0 if not present
    mode1, freq1 = mc1[0] if mc1 else (None, 0)
    sec1 = mc1[1][1] if len(mc1) > 1 else 0

    mode2, freq2 = mc2[0] if mc2 else (None, 0)
    sec2 = mc2[1][1] if len(mc2) > 1 else 0

    # 4. If modes differ, we can keep both modes
    if mode1 != mode2:
        return n - (freq1 + freq2)

    # 5. If modes are the same, choose which subsequence "gives up" its mode
    #    Option A: keep mode1 in subseq1, switch subseq2 to its runner-up
    remove_if_drop2 = n - (freq1 + sec2)
    #    Option B: keep mode2 in subseq2, switch subseq1 to its runner-up
    remove_if_drop1 = n - (sec1 + freq2)

    # 6. Return the minimal removals of the two strategies
    return min(remove_if_drop2, remove_if_drop1)


if __name__ == "__main__":
    n = int(input())
    lst = input().split()
    print(main(n, lst))
