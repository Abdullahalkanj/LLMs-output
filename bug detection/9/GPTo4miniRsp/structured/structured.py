# ChatGPT4omini output using Structured prompt


# ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
# 1. High-Level Overview
# This script is very messy, but at the bottom, it reads an integer n, and then computes two values ans1 and ans2 through loops involving powers of 2 and 5.
# Finally, it prints min(ans1, ans2).

# Clearly, the final part is trying to compute something related to powers of 2 and 5 — likely about trailing zeros in a factorial or similar counting.

# 2. Where is the bug?
# Let's zoom into the important part:

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

# Problem:
# 2 ** d1 * 2 and 5 ** d2 * 2 inside the loops look suspicious.

# Normally, if you want to count the number of times a number is divisible by powers of 2 or 5, you don't multiply by an extra 2.

# The multiplication * 2 is incorrect — it offsets the powers.

# You should be checking 2 ** d1 <= n and 5 ** d2 <= n, not 2 ** d1 * 2 <= n.

# Thus:

# 2 ** d1 * 2 = 2 ** (d1 + 1), meaning it's shifting the check to one power higher, but the division logic remains as 2 ** d1.

# Mismatch!

# This is the bug.

# 3. How to fix it
# Correct Version:

n = int(input())
ans1 = 0
ans2 = 0
d1 = 1
d2 = 1
while 2 ** d1 <= n:
    ans1 += n // (2 ** d1)
    d1 += 1
while 5 ** d2 <= n:
    ans2 += n // (5 ** d2)
    d2 += 1
ans = min(ans1, ans2)
print(ans)

# Removed the * 2 inside both while conditions and the divisions.

# 4. Summary
# Aspect	Buggy code	Correct code
# While condition	while 2**d1 * 2 <= n:	while 2**d1 <= n:
# Division inside loop	n // (2**d1 * 2)	n // (2**d1)
# While condition (5's loop)	while 5**d2 * 2 <= n:	while 5**d2 <= n:
# Division inside loop	n // (5**d2 * 2)	n // (5**d2)
# 5. Extra note
# This seems like it's trying to find the number of trailing zeros in some modified factorial or combination, because trailing zeros are determined by the number of factors of 2 and 5, and you take the minimum between the two.

# Would you like me to also rewrite the full corrected version of the entire script for you (cleaned, indented properly)? 🚀
# It would make it much more readable!

# i asked 
# give me the code 
# i got 
# Of course! Here's the corrected and properly formatted version of your script:

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
        return '\n'.join('{} : {}'.format(r, self.members(r)) for r in self.roots())

def dijkstra_heap(s, edge, n):
    d = [10**20] * n
    used = [True] * n
    d[s] = 0
    used[s] = False
    edgelist = []
    for a, b in edge[s]:
        heapq.heappush(edgelist, a * (10**6) + b)
    while len(edgelist):
        minedge = heapq.heappop(edgelist)
        if not used[minedge % (10**6)]:
            continue
        v = minedge % (10**6)
        d[v] = minedge // (10**6)
        used[v] = False
        for e in edge[v]:
            if used[e[1]]:
                heapq.heappush(edgelist, (e[0] + d[v]) * (10**6) + e[1])
    return d

def factorization(n):
    arr = []
    temp = n
    for i in range(2, int(-( -n ** 0.5 // 1)) + 1):
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
        if p ** 2 > n:
            break
        if n % p == 0:
            return False
        p += 1
    return True

def eratosthenes(limit):
    A = [i for i in range(2, limit + 1)]
    P = []
    while True:
        prime = min(A)
        if prime > sqrt(limit):
            break
        P.append(prime)
        i = 0
        while i < len(A):
            if A[i] % prime == 0:
                A.pop(i)
                continue
            i += 1
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

# ----------------
# Corrected logic
# ----------------

n = int(input())
ans1 = 0
ans2 = 0
d1 = 1
d2 = 1
while 2 ** d1 <= n:
    ans1 += n // (2 ** d1)
    d1 += 1
while 5 ** d2 <= n:
    ans2 += n // (5 ** d2)
    d2 += 1
ans = min(ans1, ans2)
print(ans)

# ✅ Changes I made:

# Fixed indentation everywhere.

# Removed wrong * 2 from the bottom calculation.

# Cleaned up spacing and formatting for clarity.

# Would you also like a smaller cleaned version containing only the needed parts (without all the unused imports and classes)? It would be much shorter! 🚀
# ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


# Research Team:
# ================== SUMMARY ==================
# Program: buggy_50.py
# Tests passed: 6/15
# Pass rate: 40.00%
# =============================================
