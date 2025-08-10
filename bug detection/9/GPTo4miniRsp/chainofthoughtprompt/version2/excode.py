import sys
import heapq
from math import gcd, sqrt
from functools import reduce

INF = 1 << 60
MOD = 10**9 + 7
sys.setrecursionlimit(10**7)

class UnionFind:
    def __init__(self, n):
        self.n = n
        self.parents = [-1]*n

    def find(self, x):
        if self.parents[x] < 0:
            return x
        self.parents[x] = self.find(self.parents[x])
        return self.parents[x]

    def union(self, x, y):
        x = self.find(x); y = self.find(y)
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
            f"{r}: {self.members(r)}"
            for r in self.roots()
        )

def dijkstra_heap(s, edges, n):
    dist = [INF]*n
    dist[s] = 0
    pq = [(0, s)]
    while pq:
        d, v = heapq.heappop(pq)
        if d > dist[v]:
            continue
        for w, to in edges[v]:
            nd = d + w
            if nd < dist[to]:
                dist[to] = nd
                heapq.heappush(pq, (nd, to))
    return dist

def factorization(n):
    arr = []
    temp = n
    i = 2
    while i*i <= temp:
        if temp % i == 0:
            cnt = 0
            while temp % i == 0:
                cnt += 1
                temp //= i
            arr.append([i, cnt])
        i += 1
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
    p = 2
    while p*p <= n:
        if n % p == 0:
            return False
        p += 1
    return True

def eratosthenes(limit):
    sieve = [True]*(limit+1)
    sieve[0] = sieve[1] = False
    for i in range(2, int(sqrt(limit))+1):
        if sieve[i]:
            for j in range(i*i, limit+1, i):
                sieve[j] = False
    return [i for i, is_p in enumerate(sieve) if is_p]

def permutation_with_duplicates(L):
    if not L:
        return [[]]
    ret = []
    seen = set()
    for i, x in enumerate(L):
        if x in seen:
            continue
        seen.add(x)
        rest = L[:i] + L[i+1:]
        for perm in permutation_with_duplicates(rest):
            ret.append([x] + perm)
    return ret

def func(n):
    if n % 2 == 1:
        return 0
    ans2 = 0
    k = n // 2
    # exponent of 5 in (n/2)!  → floor(k/5) + floor(k/25) + ...
    power = 5
    while power <= k:
        ans2 += k // power
        power *= 5
    # exponent of 2 in n!! = k (one 2 per term) + exponent of 2 in k!
    ans1 = k
    power = 2
    while power <= k:
        ans1 += k // power
        power *= 2
    return min(ans1, ans2)

if __name__ == "__main__":
    n = int(input().strip())
    print(func(n))
