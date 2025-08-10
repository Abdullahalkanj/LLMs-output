import heapq

def dijkstra(N, start, S, A_m, Cost, Change):
    INF = 10**18
    Ans = [INF] * N
    Ans[start] = 0
    maxHold = A_m * N
    # FIX: clamp S here, after A_m is final
    S = min(S, maxHold)
    used = [[INF] * (maxHold + 1) for _ in range(N)]
    used[start][S] = 0
    heap = [(0, start, S)]
    while heap:
        c, node, hold = heapq.heappop(heap)
        # FIX: skip stale states instead of convoluted j-scan
        if c > used[node][hold]:
            continue

        # traverse edges
        for to in range(N):
            a, b = Cost[node][to]
            if a >= 0 and hold >= a:
                newHold = hold - a
                newCost = c + b
                if newCost < used[to][newHold]:
                    used[to][newHold] = newCost
                    heapq.heappush(heap, (newCost, to, newHold))
                    # FIX: update Ans whenever we find a better cost to 'to'
                    if newCost < Ans[to]:
                        Ans[to] = newCost

        # “change” action at current node
        chA, chB = Change[node]
        if hold + chA <= maxHold:
            newHold = hold + chA
            newCost = c + chB
            # FIX: update used before pushing the new state
            if newCost < used[node][newHold]:
                used[node][newHold] = newCost
                heapq.heappush(heap, (newCost, node, newHold))

    return Ans  # FIX: moved return outside the while loop

def main():
    N, M, S = map(int, input().split())
    # FIX: proper initialization of Cost as N×N matrix of [-1,-1]
    Cost = [[[-1, -1] for _ in range(N)] for _ in range(N)]
    Change = [[0, 0] for _ in range(N)]
    A_m = 0

    # read edges
    for _ in range(M):
        u, v, a, b = map(int, input().split())
        u -= 1; v -= 1
        # FIX: keep track of maximum 'a' across all edges
        A_m = max(A_m, a)
        Cost[u][v] = [a, b]
        Cost[v][u] = [a, b]

    # read per-node “change” data
    for i in range(N):
        c, d = map(int, input().split())
        Change[i] = [c, d]

    # run Dijkstra from node 0
    T = dijkstra(N, start=0, S=S, A_m=A_m, Cost=Cost, Change=Change)
    # FIX: properly indent printing under the loop
    for dist in T[1:]:
        print(dist)

if __name__ == "__main__":
    main()
