# Claude output using Structured prompt


# ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
# I'll analyze the provided code to identify and fix the bug. Let's break it down step by step.
# Code Analysis
# This code appears to be solving a problem related to calculating trailing zeros in factorials. It includes several utility functions including:

# Union-Find data structure
# Dijkstra's algorithm
# Prime factorization
# GCD and LCM calculation
# Prime number checking
# Permutation generation

# The main part of the code is at the end where it:

# Takes an input number n
# Calculates two values (ans1 and ans2) which track how many multiples of powers of 2 and powers of 5 are in the range up to n
# Returns the minimum of those two values

# Bug Identification
# The bug is in the indentation of the second while loop. In the current code:

while 2 ** d1 * 2 <= n :
ans1 += n // ( 2 ** d1 * 2 )
    d1 += 1
    while 5 ** d2 * 2 <= n :
ans2 += n // ( 5 ** d2 * 2 )
    d2 += 1

# The second while loop (for powers of 5) is incorrectly indented, making it execute only once after the first loop completes, rather than as an independent loop. This is incorrect because the two loops should run independently.
# Bug Fix
# Here's the corrected code for this section:

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

# The fix is to properly indent the second while loop so it runs independently of the first loop. This ensures that both calculations are performed completely and independently.

# Explanation of the Algorithm
# This algorithm appears to be calculating the number of trailing zeros in a factorial or a similar mathematical problem. It's doing this by:

# Counting how many pairs of factors 2 and 5 appear in numbers up to n
# Since a trailing zero is created by each factor of 10 (which is 2×5), the limiting factor will be whichever appears fewer times (typically the factor of 5)
# The solution uses the min of ans1 and ans2 because trailing zeros are determined by the minimum number of pairs that can be formed
# The bug prevented the full calculation of ans2 (the count of factors of 5), which would likely result in an incorrect answer since in most cases, the number of factors of 5 (not 2) determines the number of trailing zeros.

# i asked the whole code
# the respond 
# Here's the corrected version of the code:

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

while 2 ** d1 * 2 <= n:
    ans1 += n // (2 ** d1 * 2)
    d1 += 1

while 5 ** d2 * 2 <= n:
    ans2 += n // (5 ** d2 * 2)
    d2 += 1

ans = min(ans1, ans2)
print(ans)

# The main fix was correcting the indentation of the second while loop. In the original code, the second loop was incorrectly indented, making it run only once after the first loop completed. By fixing the indentation, both loops now run independently as intended.
# The algorithm calculates the number of trailing zeros by counting the number of factor pairs (2 and 5) that can be formed from the numbers up to n. Since each trailing zero requires both a factor of 2 and a factor of 5, the result is determined by whichever factor occurs less frequently (typically the factor of 5).
# ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


# Research Team:
#the output

# ================== SUMMARY ==================
# Program: buggy_50.py
# Tests passed: 13/15
# Pass rate: 86.67%
# =============================================