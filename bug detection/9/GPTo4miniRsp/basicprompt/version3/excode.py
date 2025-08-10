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
        self.parents = [-1] * n

    def find(self, x):
        if self.parents[x] < 0:
            return x
        else:
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
        return '\n'.join(
            '{}: {}'.format(r, self.members(r))
            for r in self.roots()
        )


def dijkstra_heap(s, edge, n):
    d = [INF] * n
    used = [True] * n
    d[s] = 0
    used[s] = False
    edgelist = []
    for a, b in edge[s]:
        heapq.heappush(edgelist, a * 10**6 + b)

    while edgelist:
        minedge = heapq.heappop(edgelist)
        v = minedge % 10**6
        if not used[v]:
            continue
        d[v] = minedge // 10**6
        used[v] = False
        for cost, to in edge[v]:
            if used[to]:
                heapq.heappush(edgelist, (cost + d[v]) * 10**6 + to)
    return d


def factorization(n):
    arr = []
    temp = n
    limit = int(n**0.5) + 1
    for i in range(2, limit):
        if temp % i == 0:
            cnt = 0
            while temp % i == 0:
                cnt += 1
                temp //= i
            arr.append([i, cnt])
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
    while A and A[0] ** 2 <= limit:
        prime = A[0]
        P.append(prime)
        A = [x for x in A if x % prime != 0]
    P.extend(A)
    return P


def permutation_with_duplicates(L):
    if not L:
        return [[]]
    ret = []
    for i in sorted(set(L)):
        rest = L[:]
        rest.remove(i)
        for perm in permutation_with_duplicates(rest):
            ret.append([i] + perm)
    return ret


def main():
    n = int(input())
    # Count factors of 2 in n!
    ans2 = 0
    d = 1
    while 2**d <= n:
        ans2 += n // (2**d)
        d += 1

    # Count factors of 5 in n!
    ans5 = 0
    d = 1
    while 5**d <= n:
        ans5 += n // (5**d)
        d += 1

    # Number of trailing zeros is min(count2, count5)
    print(min(ans2, ans5))


if __name__ == '__main__':
    main()
