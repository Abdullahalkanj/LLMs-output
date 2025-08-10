import sys
import heapq
from collections import deque
from math import sqrt, ceil, gcd
from functools import reduce

INF = 1 << 60
MOD = 10**9 + 7
sys.setrecursionlimit(10**7)


class UnionFind:
    def __init__(self, n):
        self.n = n
        # parents[x] < 0 means x is a root and |parents[x]| is the size
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
        # Always attach smaller tree to larger
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
        return "\n".join(
            f"{r}: {self.members(r)}" for r in self.roots()
        )


def dijkstra_heap(start, edges, n):
    """
    Standard Dijkstra with min-heap;
    edges: adjacency list of (neighbor, weight) pairs.
    """
    dist = [INF] * n
    dist[start] = 0
    heap = [(0, start)]
    while heap:
        d, u = heapq.heappop(heap)
        if d > dist[u]:
            continue
        for v, w in edges[u]:
            nd = d + w
            if nd < dist[v]:
                dist[v] = nd
                heapq.heappush(heap, (nd, v))
    return dist


def factorization(n):
    """
    Return list of [prime, exponent] pairs.
    """
    arr = []
    temp = n
    limit = int(sqrt(n)) + 1
    for i in range(2, limit):
        if temp % i == 0:
            cnt = 0
            while temp % i == 0:
                cnt += 1
                temp //= i
            arr.append([i, cnt])  # append once per prime
    if temp > 1:
        arr.append([temp, 1])
    if not arr:
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
    i = 2
    while i * i <= n:
        if n % i == 0:
            return False
        i += 1
    return True


def eratosthenes(limit):
    """
    Return all primes up to 'limit'.
    """
    sieve = [True] * (limit + 1)
    sieve[0:2] = [False, False]
    primes = []
    for i in range(2, limit + 1):
        if sieve[i]:
            primes.append(i)
            for j in range(i * i, limit + 1, i):
                sieve[j] = False
    return primes


def permutation_with_duplicates(L):
    """
    Generate all unique permutations of L, even if L has duplicates.
    """
    if not L:
        return [[]]
    ret = []
    seen = set()
    for i, val in enumerate(L):
        if val in seen:
            continue
        seen.add(val)
        rest = L[:i] + L[i+1:]
        for perm in permutation_with_duplicates(rest):
            ret.append([val] + perm)
    return ret


if __name__ == "__main__":
    n = int(input())
    # Count factors of 2
    ans2 = 0
    d = 1
    while (1 << d) <= n:
        ans2 += n // (1 << d)
        d += 1

    # Count factors of 5
    ans5 = 0
    d = 1
    while 5**d <= n:
        ans5 += n // (5**d)
        d += 1

    print(min(ans2, ans5))
