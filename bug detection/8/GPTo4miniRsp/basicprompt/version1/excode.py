import heapq

def dijkstra(N, start, Cost, Change, max_hold, initial_S):
    INF = 10**12
    # best cost to reach each node
    Ans = [INF]*N
    Ans[start] = 0
    # used[u][h] = best cost to be at u with h fuel in tank
    used = [[INF]*(max_hold+1) for _ in range(N)]
    used[start][initial_S] = 0

    heap = [(0, initial_S, start)]
    while heap:
        cost_so_far, fuel, u = heapq.heappop(heap)
        # if we've already found a better way to u, skip
        if cost_so_far > Ans[u]:
            continue

        # 1) Try refueling at u
        add_fuel, add_cost = Change[u]
        new_fuel = min(max_hold, fuel + add_fuel)
        new_cost = cost_so_far + add_cost
        if used[u][new_fuel] > new_cost:
            used[u][new_fuel] = new_cost
            heapq.heappush(heap, (new_cost, new_fuel, u))

        # 2) Try driving from u to each v
        for v in range(N):
            fuel_needed, drive_cost = Cost[u][v]
            if fuel_needed == -1 or fuel < fuel_needed:
                continue
            rem_fuel = fuel - fuel_needed
            tot_cost = cost_so_far + drive_cost
            if used[v][rem_fuel] > tot_cost:
                used[v][rem_fuel] = tot_cost
                heapq.heappush(heap, (tot_cost, rem_fuel, v))
                # record best cost to reach v (regardless of remaining fuel)
                if Ans[v] > tot_cost:
                    Ans[v] = tot_cost

    return Ans

def main():
    import sys
    input = sys.stdin.readline

    N, M, S = map(int, input().split())
    # adjacency: Cost[u][v] = [fuel_cost, drive_cost]
    Cost = [[[ -1, -1 ] for _ in range(N)] for _ in range(N)]
    A_m = 0

    # 1) read edges
    for _ in range(M):
        u, v, a, b = map(int, input().split())
        u -= 1; v -= 1
        A_m = max(A_m, a)
        Cost[u][v] = [a, b]
        Cost[v][u] = [a, b]

    # 2) read refuel info
    Change = [None]*N
    for i in range(N):
        c, d = map(int, input().split())
        Change[i] = (c, d)

    # no point in starting with more fuel than max capacity
    max_hold = A_m * N
    S = min(S, max_hold)

    # run
    T = dijkstra(N, 0, Cost, Change, max_hold, S)

    # print answers for nodes 1..N-1
    out = "\n".join(str(T[i]) for i in range(1, N))
    print(out)

if __name__ == "__main__":
    main()
