import heapq

def dijkstra(N, start, S, Cost, Change, A_m):
    INF = 10**18
    max_hold = A_m * N
    # Ans[u] = best time to reach u (over any hold)
    Ans = [INF] * N
    Ans[start] = 0
    # used[u][h] = best time to reach (u, h)
    used = [[INF] * (max_hold + 1) for _ in range(N)]
    used[start][S] = 0

    heap = [(0, S, start)]  # (time_so_far, hold, node)
    while heap:
        time_so_far, hold, u = heapq.heappop(heap)
        # stale entry?
        if time_so_far > used[u][hold]:
            continue

        # 1) Try every outgoing edge u→v
        for v in range(N):
            a, b = Cost[u][v]
            if a >= 0 and hold >= a:
                new_hold = hold - a
                new_time = time_so_far + b
                if new_time < used[v][new_hold]:
                    used[v][new_hold] = new_time
                    heapq.heappush(heap, (new_time, new_hold, v))
                    # update best answer for v
                    if new_time < Ans[v]:
                        Ans[v] = new_time

        # 2) Try recharging at u
        add_hold, add_time = Change[u]
        new_hold = min(hold + add_hold, max_hold)
        new_time = time_so_far + add_time
        if new_time < used[u][new_hold]:
            used[u][new_hold] = new_time
            heapq.heappush(heap, (new_time, new_hold, u))

    return Ans


def main():
    N, M, S = map(int, input().split())

    # cost[u][v] = (a, b), or (-1,-1) if no edge
    Cost = [[(-1, -1) for _ in range(N)] for _ in range(N)]
    Change = [None] * N

    A_m = 0
    for _ in range(M):
        u, v, a, b = map(int, input().split())
        u -= 1; v -= 1
        A_m = max(A_m, a)
        Cost[u][v] = (a, b)
        Cost[v][u] = (a, b)

    for i in range(N):
        c, d = map(int, input().split())
        Change[i] = (c, d)

    # You never need more than A_m * N holds
    S = min(S, A_m * N)

    T = dijkstra(N, 0, S, Cost, Change, A_m)
    # print answers for nodes 2…N
    for dist in T[1:]:
        print(dist)


if __name__ == '__main__':
    main()
