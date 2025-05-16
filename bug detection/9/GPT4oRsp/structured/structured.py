# ChatGPT4o output with structured prompt


#----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
# Alright, let's go through this carefully.

# First, high-level overview:

# The code includes a ton of utility functions (Union-Find, Dijkstra, prime checking, etc.).

# But the main logic is only at the very bottom, starting from:

n = int(input())
ans1 = 0
ans2 = 0
d1 = 1
d2 = 1
while 2 ** d1 * 2 <= n:
    ans1 += n // (2 ** d1 * 2)
    d1 += 1
while 5 ** d2 * 2 <= n:
    ans2 += n // (5 ** d2 * 2)
    d2 += 1
ans = min(ans1, ans2)
print(ans)

# Main Bug Analysis:

# The loops:
while 2 ** d1 * 2 <= n:

# and

while 5 ** d2 * 2 <= n:

# are WRONG.

# Conceptually, it looks like this is trying to compute something related to trailing zeros in factorials or something similar, maybe powers of 2 and 5.
# Normally, when dealing with trailing zeros of n! (factorial), you count the powers of 2 and 5 in the factorization, but you don't write 2 ** d1 * 2.

# Also, you would usually do something like:

# n // 2, n // 4, n // 8, n // 16, ..., not multiply by 2 extra.

# 2 ** d1 * 2 is equivalent to 2 ** (d1 + 1), which is messy.

# Additionally, you shouldn't be counting powers like this — the standard method is:

# For number p, the number of times p divides into n! is:

n // p + n // p**2 + n // p**3 + ... until p^k > n

# Specific Bugs:
# Wrong multiplication (* 2) after 2 ** d1 and 5 ** d2.

# Wrong loop conditions.

# Wrong counting methodology.

# Correct Code (Fix):
# Here's the corrected bottom part:

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
MOD = 1000000007
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
        return "\n".join("{}: {}".format(r, self.members(r)) for r in self.roots())

def dijkstra_heap(s, edge, n):
    d = [10**20] * n
    used = [True] * n
    d[s] = 0
    used[s] = False
    edgelist = []
    for a, b in edge[s]:
        # Push (weight, node)
        heapq.heappush(edgelist, (a, b))
    while edgelist:
        weight, v = heapq.heappop(edgelist)
        if not used[v]:
            continue
        d[v] = weight
        used[v] = False
        for e in edge[v]:
            if used[e[1]]:
                heapq.heappush(edgelist, (e[0] + d[v], e[1]))
    return d

def factorization(n):
    arr = []
    temp = n
    for i in range(2, int(n**0.5) + 1):
        if temp % i == 0:
            cnt = 0
            while temp % i == 0:
                cnt += 1
                temp //= i
            arr.append([i, cnt])
    if temp != 1:
        arr.append([temp, 1])
    if arr == []:
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
    while True:
        if p * p > n:
            break
        if n % p == 0:
            return False
        p += 1
    return True

def eratosthenes(limit):
    A = [i for i in range(2, limit + 1)]
    P = []
    while A:
        prime = min(A)
        if prime > sqrt(limit):
            break
        P.append(prime)
        A = [a for a in A if a % prime != 0]
    for a in A:
        P.append(a)
    return P

def permutation_with_duplicates(L):
    if L == []:
        return [[]]
    else:
        ret = []
        S = sorted(set(L))
        for i in S:
            data = L[:]
            data.remove(i)
            for j in permutation_with_duplicates(data):
                ret.append([i] + j)
        return ret

# Main Code:
# This part computes the number of trailing zeros in n! by counting factors of 2 and 5
n = int(input())

ans1 = 0
ans2 = 0

# Count factors of 2 in n!:
d1 = 2
while d1 <= n:
    ans1 += n // d1
    d1 *= 2

# Count factors of 5 in n!:
d2 = 5
while d2 <= n:
    ans2 += n // d2
    d2 *= 5

# The number of trailing zeros is the minimum of these counts:
ans = min(ans1, ans2)
print(ans)
# --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


# Research Team:
# ================== SUMMARY ==================
# Program: buggy_50.py
# Tests passed: 3/15
# Pass rate: 20.00%
# =============================================
