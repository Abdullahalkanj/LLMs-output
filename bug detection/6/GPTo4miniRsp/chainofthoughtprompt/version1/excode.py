from collections import Counter

def main(n, lst):
    # 1) Split into even/odd positions
    evens = lst[::2]
    odds  = lst[1::2]

    # 2) Count frequencies and get the two most common
    even_counts = Counter(evens).most_common(2)
    odd_counts  = Counter(odds).most_common(2)

    # Ensure each list has two entries (pad with (None,0) if needed)
    even_counts += [(None, 0)] * (2 - len(even_counts))
    odd_counts  += [(None, 0)] * (2 - len(odd_counts))

    (e1, f1), (e2, f2) = even_counts
    (o1, g1), (o2, g2) = odd_counts

    # 3) If top choices differ, keep both tops
    if e1 != o1:
        keep = f1 + g1
    else:
        # 4) Otherwise pick the better between (top_even + 2nd_odd)
        #    vs. (2nd_even + top_odd)
        keep = max(f1 + g2, f2 + g1)

    # Minimum changes = total length minus how many we can keep
    return n - keep

if __name__ == "__main__":
    n   = int(input())
    lst = input().split()
    print(main(n, lst))
