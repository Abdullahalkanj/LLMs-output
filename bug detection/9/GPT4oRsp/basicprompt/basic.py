# ChatGPT4o output with Basic prompt


# -----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
# Yes, your code has several issues, mainly wrong indentation and logical mistakes at the end in the main part (where you compute ans1 and ans2).

# Let's go through it carefully:

# 1. Indentation Issues
# All methods inside the UnionFind class are incorrectly indented.

# Also, many utility functions like dijkstra_heap, factorization, lcm, etc., should be defined at the global level, but now they are wrongly nested inside each other.

# 2. Logical Bug in Main Calculation
# The original problem seems to ask:
# "How many trailing zeros are there in n!?"

# The number of trailing zeros in n! is determined by how many times 5 divides into the factors, because 2s are abundant compared to 5s.

# Thus, only 5 matters, not 2!

# In your code:

while 2 ** d1 * 2 <= n:
    ans1 += n // (2 ** d1 * 2)
    d1 += 1
while 5 ** d2 * 2 <= n:
    ans2 += n // (5 ** d2 * 2)
    d2 += 1
ans = min(ans1, ans2)
print(ans)

# This is wrong:

# It incorrectly tries to compute based on powers of 2 and powers of 5 separately and takes min(ans1, ans2).

# You should ONLY count how many multiples of 5, 25, 125, etc., fit into n.

# The correct logic is:

n = int(input())
ans = 0
while n:
    n //= 5
    ans += n
print(ans)

# 3. Fixed version of your code
# Here is the corrected and properly indented version:

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
        if p * p > n:
            break
        if n % p == 0:
            return False
        p += 1
    return True

def eratosthenes(limit):
    A = [i for i in range(2, limit + 1)]
    P = []
    while True:
        if not A:
            break
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

# Main logic
n = int(input())
ans = 0
while n:
    n //= 5
    ans += n
print(ans)


# ✅ Summary of the bugs fixed:
# Corrected indentation everywhere.

# Fixed main computation for trailing zeros (only count factors of 5, not both 2 and 5).

# Cleaned some minor typos (e.g., wrong placement of return, wrong popping from empty list).

# Would you also like me to show you a faster or shorter version of just the relevant part (without including all those utility functions)? 🚀

# --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


# Research Team:
# the output
# ================== SUMMARY ==================
# Program: buggy_50.py
# Tests passed: 3/15
# Pass rate: 20.00%
# =============================================