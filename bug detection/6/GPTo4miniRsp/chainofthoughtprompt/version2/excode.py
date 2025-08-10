from collections import Counter
import sys

def main(n: int, lst: list) -> int:
    # 1. Split into even-index and odd-index elements:
    even_vals = lst[0::2]
    odd_vals  = lst[1::2]

    # 2. Count frequencies:
    cnt_even = Counter(even_vals)
    cnt_odd  = Counter(odd_vals)

    # 3. Get the two most common (value, count) pairs, with defaults if missing:
    #    .most_common() returns a list of (value, count) sorted descending.
    ev = cnt_even.most_common(2) + [(None, 0)]
    od = cnt_odd.most_common(2)  + [(None, 0)]
    (even_val1, even_count1), (even_val2, even_count2) = ev[0], ev[1]
    (odd_val1, odd_count1),   (odd_val2, odd_count2)   = od[0], od[1]

    # 4. If best even and best odd are different, use them both:
    if even_val1 != odd_val1:
        keep = even_count1 + odd_count1
    else:
        # 5. Otherwise pick the better of (best_even + 2nd_best_odd)
        #    vs. (2nd_best_even + best_odd)
        choice1 = even_count1 + odd_count2
        choice2 = even_count2 + odd_count1
        keep = max(choice1, choice2)

    # 6. Answer is total – kept
    return n - keep

if __name__ == "__main__":
    data = sys.stdin.read().split()
    n = int(data[0])
    lst = data[1:]
    print(main(n, lst))
