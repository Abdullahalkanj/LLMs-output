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
MOD = 1_000_000_007
sys.setrecursionlimit(10**7)


class UnionFind:
    def __init__(self, n):
        self.n = n
        self.parents = [-1] * n

    def find(self, x):
        if self.parents[x] < 0:
            return x
        self.parents[x] = self.find(self.parents[x])
        return self.parents[x]

    def union(self, x, y):
        x = self.find(x)
        y = self.find(y)
        if x == y:
            return
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
            f"{r} : {self.members(r)}" for r in self.roots()
        )


def dijkstra_heap(s, edge, n):

    d = [INF] * n
    used = [True] * n
    d[s] = 0
    used[s] = False
    pq = []  # (distance_so_far, vertex)
    for cost, v in edge[s]:
        heapq.heappush(pq, (cost, v))

    while pq:
        dist, v = heapq.heappop(pq)
        if not used[v]:
            continue
        used[v] = False
        d[v] = dist
        for c, to in edge[v]:
            if used[to]:
                heapq.heappush(pq, (dist + c, to))
    return d


def factorization(n):

    arr = []
    temp = n
    i = 2
    while i * i <= temp:
        if temp % i == 0:
            cnt = 0
            while temp % i == 0:
                temp //= i
                cnt += 1
            arr.append([i, cnt])
        i += 1
    if temp != 1:
        arr.append([temp, 1])
    if not arr:
        arr.append([n, 1])
    return arr


def lcm(x, y):
    return (x * y) // gcd(x, y)


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
    A = list(range(2, limit + 1))
    P = []
    while A:
        prime = A.pop(0)
        P.append(prime)
        A = [x for x in A if x % prime != 0]
    return P


def permutation_with_duplicates(L):
    if not L:
        return [[]]
    ret = []
    for x in sorted(set(L)):
        rest = L.copy()
        rest.remove(x)
        for p in permutation_with_duplicates(rest):
            ret.append([x] + p)
    return ret


def count_factors(n, p):
    """Count how many times prime p divides n!"""
    cnt = 0
    while n:
        n //= p
        cnt += n
    return cnt


def main():
    n = int(sys.stdin.readline().strip())
    twos = count_factors(n, 2)
    fives = count_factors(n, 5)
    print(min(twos, fives))


if __name__ == "__main__":
    main()
