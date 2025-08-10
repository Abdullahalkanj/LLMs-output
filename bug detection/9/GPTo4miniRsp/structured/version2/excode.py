import sys
import heapq
import re
from itertools import permutations
from bisect import bisect_left, bisect_right
from collections import Counter, deque
from math import factorial, sqrt, ceil, gcd
from functools import lru_cache, reduce
from decimal import Decimal

INF = 1 << 60
MOD = 10**9 + 7
sys.setrecursionlimit(10**7)


class UnionFind:
    def __init__(self, n):
        self.n = n
        # Negative size for roots, parent pointers otherwise
        self.parents = [-1] * n

    def find(self, x):
        # Path compression
        if self.parents[x] < 0:
            return x
        self.parents[x] = self.find(self.parents[x])
        return self.parents[x]

    def union(self, x, y):
        x = self.find(x)
        y = self.find(y)
        if x == y:
            return
        # Union by size (more negative = larger)
        if self.parents[x] > self.parents[y]:
            x, y = y, x
        self.parents[x] += self.parents[y]
        self.parents[y] = x

    def size(self, x):
        return -self.parents[self.find(x)]

    def same(self, x, y):
        return self.find(x) == self.find(y)

    def members(self, x):
        root = self.find(x)
        return [i for i in range(self.n) if self.find(i) == root]

    def roots(self):
        return [i for i, p in enumerate(self.parents) if p < 0]

    def group_count(self):
        return len(self.roots())

    def all_group_members(self):
        return {r: self.members(r) for r in self.roots()}

    def __str__(self):
        return "\n".join(f"{r}: {self.members(r)}" for r in self.roots())


def dijkstra_heap(s, edge, n):
    """Compute shortest distances from s in a weighted digraph using a min-heap."""
    INF_DIST = 10**20
    dist = [INF_DIST] * n
    visited = [False] * n  # False = not yet finalized
    dist[s] = 0
    # push (distance, vertex)
    heap = []
    for w, v in edge[s]:
        heapq.heappush(heap, (w, v))

    while heap:
        d_u, u = heapq.heappop(heap)
        if visited[u]:
            continue
        visited[u] = True
        dist[u] = d_u
        for w, v in edge[u]:
            if not visited[v]:
                heapq.heappush(heap, (d_u + w, v))
    return dist


def factorization(n):
    """Return prime factorization of n as list of [prime, exponent]."""
    arr = []
    temp = n
    # ceil(sqrt(n))
    limit = int(sqrt(n)) + 1
    for i in range(2, limit):
        if temp % i == 0:
            cnt = 0
            while temp % i == 0:
                cnt += 1
                temp //= i
            arr.append([i, cnt])
    if temp != 1:
        arr.append([temp, 1])
    if not arr:  # n was 1 or prime
        arr.append([n, 1])
    return arr


def lcm(x, y):
    return x // gcd(x, y) * y


def lcm_list(numbers):
    return reduce(lcm, numbers, 1)


def gcd_list(numbers):
    return reduce(gcd, numbers)


def is_prime(n):
    if n <= 1:
        return False
    p = 2
    while p * p <= n:
        if n % p == 0:
            return False
        p += 1
    return True


def eratosthenes(limit):
    """Return list of primes up to `limit`."""
    A = list(range(2, limit + 1))
    P = []
    while A and A[0] * A[0] <= limit:
        p = A.pop(0)
        P.append(p)
        A = [x for x in A if x % p != 0]
    P.extend(A)
    return P


def permutation_with_duplicates(L):
    """Generate all distinct permutations of list L, even if it has duplicates."""
    if not L:
        return [[]]
    result = []
    seen = set()
    for i, x in enumerate(L):
        if x in seen:
            continue
        seen.add(x)
        rest = L[:i] + L[i+1:]
        for perm in permutation_with_duplicates(rest):
            result.append([x] + perm)
    return result


def main():
    n = int(input())
    ans1 = 0
    ans2 = 0
    d1 = 1
    # Count how many factors of 2 in n! beyond 2^1
    while 2 ** (d1 + 1) <= n:
        ans1 += n // (2 ** (d1 + 1))
        d1 += 1

    d2 = 1
    # Count how many factors of 5 in n! beyond 5^1
    while 5 ** (d2 + 1) <= n:
        ans2 += n // (5 ** (d2 + 1))
        d2 += 1

    ans = min(ans1, ans2)
    print(ans)


if __name__ == "__main__":
    main()
