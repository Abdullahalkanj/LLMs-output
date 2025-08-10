import heapq

def dijkstra(N, S, Cost, Change, A_m):
    INF = 10**18
    max_hold = A_m * N
    # Ans[i] = best time to reach node i (over all resource levels)
    Ans = [INF] * N
    Ans[0] = 0

    # used[u][r] = best time to reach (u, r)
    used = [[INF] * (max_hold + 1) for _ in range(N)]
    used[0][S] = 0

    # heap entries are (time_so_far, resource_left, node)
    heap = [(0, S, 0)]

    while heap:
        time, hold, u = heapq.heappop(heap)
        # stale state?
        if time > used[u][hold]:
            continue

        # optional early exit if all Ans[] ≤ time
        if all(Ans[v] <= time for v in range(N)):
            break

        # 1) travel along edges
        for v in range(N):
            a, b = Cost[u][v]
            if a >= 0 and hold >= a:
                nt = time + b
                nr = hold - a
                if used[v][nr] > nt:
                    used[v][nr] = nt
                    heapq.heappush(heap, (nt, nr, v))
                    if Ans[v] > nt:
                        Ans[v] = nt

        # 2) recharge at u
        c, d = Change[u]
        nr = min(max_hold, hold + c)
        nt = time + d
        if used[u][nr] > nt:
            used[u][nr] = nt
            heapq.heappush(heap, (nt, nr, u))

    return Ans

def main():
    N, M, S = map(int, input().split())
    Cost = [[[-1, -1] for _ in range(N)] for _ in range(N)]
    Change = [[0, 0] for _ in range(N)]

    A_m = 0
    # 1) read edges
    for _ in range(M):
        u, v, a, b = map(int, input().split())
        u -= 1; v -= 1
        A_m = max(A_m, a)
        Cost[u][v] = [a, b]
        Cost[v][u] = [a, b]

    # 2) read recharge info
    for i in range(N):
        c, d = map(int, input().split())
        Change[i] = [c, d]

    # 3) cap initial resource
    S = min(S, A_m * N)

    # 4) run Dijkstra and print
    Ans = dijkstra(N, S, Cost, Change, A_m)
    for t in Ans[1:]:
        print(t)

if __name__ == "__main__":
    main()
