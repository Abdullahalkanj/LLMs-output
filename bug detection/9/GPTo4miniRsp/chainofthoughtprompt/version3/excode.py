import sys
import heapq
import math
from functools import reduce

sys.setrecursionlimit(10**7)
INF = 1 << 60
MOD = 10**9 + 7

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
        # union by size
        if self.parents[x] > self.parents[y]:
            x, y = y, x
        self.parents[x] += self.parents[y]
        self.parents[y] = x

    def size(self, x):
        return -self.parents[self.find(x)]

    def same(self, x, y):
        return self.find(x) == self.find(y)

def dijkstra(s, adj, n):
    dist = [math.inf]*n
    dist[s] = 0
    pq = [(0, s)]
    while pq:
        d, u = heapq.heappop(pq)
        if d > dist[u]:
            continue
        for w, v in adj[u]:
            nd = d + w
            if nd < dist[v]:
                dist[v] = nd
                heapq.heappush(pq, (nd, v))
    return dist

def factorization(n):
    res = []
    temp = n
    for p in range(2, int(math.isqrt(n)) + 1):
        if temp % p == 0:
            cnt = 0
            while temp % p == 0:
                temp //= p
                cnt += 1
            res.append((p, cnt))
    if temp > 1:
        res.append((temp, 1))
    if not res:
        res.append((n, 1))
    return res

def eratosthenes(limit):
    sieve = [True]*(limit+1)
    sieve[0:2] = [False, False]
    for i in range(2, int(math.sqrt(limit)) + 1):
        if sieve[i]:
            for j in range(i*i, limit+1, i):
                sieve[j] = False
    return [i for i, is_prime in enumerate(sieve) if is_prime]

def trailing_zeros_double_factorial(n):
    """Count factors of 10 in 2*4*6*...*n (n even), i.e. min(e₂,e₅)."""
    if n % 2 == 1:
        return 0
    # exponent of 2 in product is huge; only 5 matters
    count5 = 0
    power = 5
    while 2*power <= n:
        count5 += n // (2*power)
        power *= 5
    return count5

if __name__ == "__main__":
    n = int(sys.stdin.readline())
    print(trailing_zeros_double_factorial(n))
