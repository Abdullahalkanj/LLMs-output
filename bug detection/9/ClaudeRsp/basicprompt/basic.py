# Claude output using basic prompt


# ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
# I'll analyze this code to find and fix the bug. The code appears to be calculating the largest power of 10 that divides the factorial of n, which is determined by counting the number of trailing zeros in n!.
# The bug is in the calculation of ans1 and ans2. Let's look at the relevant part:

pythonn = int(input())
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

# The issue is in the formulas within the while loops. When calculating the number of trailing zeros in a factorial, we need to count the number of factors of 2 and 5, and since there are always more factors of 2 than 5 in a factorial, the limiting factor is the number of 5s.
# Here's the fix:

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
sys.setrecursionlimit(10 ** 7)

class UnionFind():
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
        return '\n'.join('{}: {}'.format(r, self.members(r)) for r in self.roots())

def dijkstra_heap(s, edge, n):
    d = [10 ** 20] * n
    used = [True] * n
    d[s] = 0
    used[s] = False
    edgelist = []
    for a, b in edge[s]:
        heapq.heappush(edgelist, a * (10 ** 6) + b)
    while len(edgelist):
        minedge = heapq.heappop(edgelist)
        if not used[minedge % (10 ** 6)]:
            continue
        v = minedge % (10 ** 6)
        d[v] = minedge // (10 ** 6)
        used[v] = False
        for e in edge[v]:
            if used[e[1]]:
                heapq.heappush(edgelist, (e[0] + d[v]) * (10 ** 6) + e[1])
    return d

def factorization(n):
    arr = []
    temp = n
    for i in range(2, int(-(-(n ** 0.5) // 1)) + 1):
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

n = int(input())
ans1 = 0
ans2 = 0
d1 = 1
d2 = 1
while 2 ** d1 <= n:  # Fixed: removed the *2
    ans1 += n // (2 ** d1)  # Fixed: removed the *2
    d1 += 1
while 5 ** d2 <= n:  # Fixed: removed the *2
    ans2 += n // (5 ** d2)  # Fixed: removed the *2
    d2 += 1
ans = min(ans1, ans2)
print(ans)

# The bug was in the calculation of factors of 2 and 5 in n!. In the original code, there were extra multiplications by 2 in both while loop conditions and in the division:

while 2 ** d1 * 2 <= n:  # Bug: extra *2
    ans1 += n // (2 ** d1 * 2)  # Bug: extra *2

# and
while 5 ** d2 * 2 <= n:  # Bug: extra *2
    ans2 += n // (5 ** d2 * 2)  # Bug: extra *2

# The correct formula to count how many factors of a prime p are in n! is:
Σ⌊n/p^i⌋ for i from 1 to ∞ (or until p^i > n)

# So the fixed code uses:

while 2 ** d1 <= n:
    ans1 += n // (2 ** d1)
    d1 += 1
while 5 ** d2 <= n:
    ans2 += n // (5 ** d2)
    d2 += 1

# Since the trailing zeros in n! are determined by the number of pairs of factors 2 and 5, and there are always more factors of 2 than 5 in a factorial, the minimum of these two counts (which will be ans2) gives the number of trailing zeros.
# ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


# Research Team:
# the output

# ================== SUMMARY ==================
# Program: buggy_50.py
# Tests passed: 3/15
# Pass rate: 20.00%
# =============================================