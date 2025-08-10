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
        return [i for i, x in enumerate(self.parents) if x < 0]

    def group_count(self):
        return len(self.roots())

    def all_group_members(self):
        return {r: self.members(r) for r in self.roots()}

    def __str__(self):
        return "\n".join(f"{r}: {self.members(r)}" for r in self.roots())

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
    for i in range(2, int(sqrt(n)) + 1):
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
    while A:
        prime = A[0]
        if prime * prime > limit:
            P.extend(A)
            break
        P.append(prime)
        A = [a for a in A if a % prime != 0]
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

if __name__ == "__main__":
    n = int(input())

    # count exponent of 2 in n!
    ans1 = 0
    power = 2
    while power <= n:
        ans1 += n // power
        power *= 2

    # count exponent of 5 in n!
    ans2 = 0
    power = 5
    while power <= n:
        ans2 += n // power
        power *= 5

    # trailing zeros = min(exponent_of_2, exponent_of_5)
    ans = min(ans1, ans2)
    print(ans)
