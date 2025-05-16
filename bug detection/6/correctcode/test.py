# Problem ID: p03244_s908501993
# Fixed version
from collections import Counter

def solve(V):
    n = len(V)
    ac = Counter(V[::2]).most_common(2)
    bc = Counter(V[1::2]).most_common(2)
    if ac[0][0] != bc[0][0]:
        return n - ac[0][1] - bc[0][1]
    else:
        if len(bc) == 1 or len(ac) == 1:
            return n // 2
        else:
            return n - max(ac[0][1] + bc[1][1], ac[1][1] + bc[0][1])

if __name__ == "__main__":
    n = int(input())
    l = list(map(int, input().split()))
    print(solve(l))
