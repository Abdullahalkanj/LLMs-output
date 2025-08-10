import sys
import heapq
from math import gcd
from functools import reduce
from math import sqrt

sys.setrecursionlimit(10**7)

def trailing_zeros_even_product(n):
    """Return number of trailing zeros in the product 2·4·6·…·n."""
    if n % 2 == 1:
        return 0

    # count factors of 2
    v2 = 0
    power = 2
    while power <= n:
        v2 += n // power
        power *= 2

    # count factors of 5 in the even terms: floor(n/(2*5^i))
    v5 = 0
    power5 = 5
    while 2 * power5 <= n:
        v5 += n // (2 * power5)
        power5 *= 5

    return min(v2, v5)

if __name__ == "__main__":
    n = int(input())
    print(trailing_zeros_even_product(n))
