import heapq

def dijkstra(N, start, S, Cost, Change, cap):
    # Ans[i]: best time to reach node i (ignoring resource leftover)
    Ans = [10**18] * N
    Ans[start] = 0

    # used[node][res] = best time to reach this (node, res) state
    used = [[10**18] * (cap + 1) for _ in range(N)]
    used[start][S] = 0

    minHeap = []
    heapq.heappush(minHeap, (0, S, start))

    while minHeap:
        c_time, res, node = heapq.heappop(minHeap)

        # If we have already a better time for this exact state, skip
        if c_time > used[node][res]:
            continue

        # Relax every neighbor
        for nbr in range(N):
            a_cost, t_cost = Cost[node][nbr]
            if a_cost < 0:   # no edge
                continue

            if res >= a_cost:
                new_res = res - a_cost
                new_time = c_time + t_cost
                if new_time < used[nbr][new_res]:
                    used[nbr][new_res] = new_time
                    heapq.heappush(minHeap, (new_time, new_res, nbr))
                    # update best time to reach nbr
                    if new_time < Ans[nbr]:
                        Ans[nbr] = new_time

        # Recharge at current node
        delta_res, delta_time = Change[node]
        new_res = min(res + delta_res, cap)
        new_time = c_time + delta_time
        if new_time < used[node][new_res]:
            used[node][new_res] = new_time
            heapq.heappush(minHeap, (new_time, new_res, node))
            # no change to Ans[node], because we’re still at same node

    return Ans

def main():
    N, M, S = map(int, input().split())

    # 1) Read edges
    Cost = [[[ -1, -1 ] for _ in range(N)] for _ in range(N)]
    A_m = 0
    for _ in range(M):
        u, v, a, b = map(int, input().split())
        u -= 1; v -= 1
        A_m = max(A_m, a)
        Cost[u][v] = [a, b]
        Cost[v][u] = [a, b]

    # 2) Read per-node recharge info
    Change = [None] * N
    for i in range(N):
        c, d = map(int, input().split())
        Change[i] = (c, d)

    # 3) Cap the starting resource
    cap = A_m * N
    if S > cap:
        S = cap

    # 4) Run Dijkstra variant
    dist = dijkstra(N, start=0, S=S, Cost=Cost, Change=Change, cap=cap)

    # 5) Print results
    for t in dist[1:]:
        print(t)

if __name__ == "__main__":
    main()
