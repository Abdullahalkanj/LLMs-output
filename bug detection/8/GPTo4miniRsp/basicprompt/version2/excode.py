import heapq

# Read parameters
N, M, S = map(int, input().split())

# Prepare data structures
Cost = [[[ -1, -1 ] for _ in range(N)] for _ in range(N)]
Change = [[0, 0] for _ in range(N)]
A_m = 0

# Read edges
for _ in range(M):
    u, v, a, b = map(int, input().split())
    A_m = max(A_m, a)
    u -= 1
    v -= 1
    Cost[u][v] = [a, b]
    Cost[v][u] = [a, b]

# Read change parameters per node
for i_node in range(N):
    c, d = map(int, input().split())
    Change[i_node][0] = c
    Change[i_node][1] = d

# Cap starting resource
if S > A_m * N:
    S = A_m * N

def dijkstra(N, start, Cost, Change):
    INF = 10**12
    Ans = [INF] * N
    Ans[start] = 0

    # used[node][hold] = best cost found so far reaching `node` with `hold` resource
    used = [[INF] * (A_m * N + 1) for _ in range(N)]
    used[start][S] = 0

    minHeap = []
    heapq.heappush(minHeap, (0, S, start))

    while minHeap:
        c, hold, current = heapq.heappop(minHeap)

        # If this cost is already worse than best-known, skip
        if c > used[current][hold]:
            continue

        # If we've already finalized all nodes at cost ≤ c, we could break—
        # but a safer check is just to process until the heap is empty.

        # 1) Try moving along each road
        for road in range(N):
            cost_a, cost_b = Cost[current][road]
            if cost_a == -1:
                continue
            if hold >= cost_a:
                new_hold = hold - cost_a
                new_cost = c + cost_b
                if new_cost < used[road][new_hold]:
                    used[road][new_hold] = new_cost
                    heapq.heappush(minHeap, (new_cost, new_hold, road))
                    Ans[road] = min(Ans[road], new_cost)

        # 2) Try charging at current node
        add_hold, add_cost = Change[current]
        if hold + add_hold <= A_m * N:
            new_hold = hold + add_hold
            new_cost = c + add_cost
            if new_cost < used[current][new_hold]:
                used[current][new_hold] = new_cost
                heapq.heappush(minHeap, (new_cost, new_hold, current))

    return Ans

# Run and print
T = dijkstra(N, start=0, Cost=Cost, Change=Change)
for answer in T[1:]:
    print(answer)
