# Claude output using Chain-of-thought prompt


# ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
# I'll analyze this code step by step to identify and fix any bugs. Let's start by understanding what the code is trying to do.
# Understanding the Code
# The core problem appears to be a function that calculates the number of trailing zeros in the factorial of a number. This is done by calculating:

# The number of factors of 2 in the factorial
# The number of factors of 5 in the factorial

# The minimum of these two values determines the number of trailing zeros, as each trailing zero requires both a factor of 2 and a factor of 5.
# Looking at the main part of the code:

n = int(input())
ans1 = 0  # Counting factors of 2
ans2 = 0  # Counting factors of 5
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

# Finding the Bug
# I've identified several issues:

# Logical Bug: The formula for counting factors of 2 and 5 is incorrect. The standard approach is to count how many times n is divisible by 2, then by 2², then by 2³, etc. Same for powers of 5.
# Mathematical Error: The expression 2 ** d1 * 2 should just be 2 ** d1. Similarly, 5 ** d2 * 2 should be 5 ** d2.
# Indentation Issue: In the provided code, it appears the second while loop might be improperly indented, but this could just be a display issue in the pasted code.

# The Fix
# Here's the corrected version of the main calculation:

n = int(input())
ans1 = 0  # Counting factors of 2
ans2 = 0  # Counting factors of 5
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

# However, there's a further optimization. Since factors of 2 will always be more than factors of 5 in factorial calculations (there are many more even numbers than multiples of 5), we could just calculate factors of 5:

n = int(input())
ans = 0
i = 5
while i <= n:
    ans += n // i
    i *= 5
print(ans)

# This computes the number of multiples of 5, then 25, then 125, etc., up to n, which gives us the exact count of factors of 5 in n!, which equals the number of trailing zeros.
# Final Fix
# Based on the original code structure and intention, here's my suggested fix:

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

# This corrects the calculations for counting factors of 2 and 5 in n!, and then prints the minimum of these counts, which gives the number of trailing zeros in n!.
    
# i asked 
# give me the code
# the responds 

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
    for i in range(2, int(-(-n ** 0.5 // 1)) + 1):
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
while 2 ** d1 <= n:
    ans1 += n // (2 ** d1)
    d1 += 1
while 5 ** d2 <= n:
    ans2 += n // (5 ** d2)
    d2 += 1
ans = min(ans1, ans2)
print(ans)
# ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


# Research Team:
# the output

# ================== SUMMARY ==================
# Program: buggy_50.py
# Tests passed: 3/15
# Pass rate: 20.00%
# =============================================