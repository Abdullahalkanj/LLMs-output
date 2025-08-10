import heapq

# Read basic input
N, M, S = map(int, input().split())

# Build adjacency list: graph[u] = list of (v, resource_cost, time_cost)
graph = [[] for _ in range(N)]
A_max = 0
for _ in range(M):
    u, v, a, b = map(int, input().split())
    u -= 1; v -= 1
    graph[u].append((v, a, b))
    graph[v].append((u, a, b))
    A_max = max(A_max, a)

# Read per-node resource–time exchange data
# Change[i] = (resource_gain, time_cost)
Change = [None]*N
for i in range(N):
    c, d = map(int, input().split())
    Change[i] = (c, d)

# Clamp starting resource S to the theoretical maximum
max_hold = A_max * N
if S > max_hold:
    S = max_hold

def dijkstra(N, start, S, graph, Change):
    INF = 10**18
    # best_time[u] := best time found to reach u (over all resource states)
    best_time = [INF]*N
    best_time[start] = 0

    # used[u][r] := minimum time to reach (u, remaining_resource=r)
    used = [ [INF]*(max_hold+1) for _ in range(N) ]
    used[start][S] = 0

    heap = [(0, S, start)]  # (time_so_far, resource_remaining, node)

    while heap:
        time_so_far, res, u = heapq.heappop(heap)
        # If we've already found a better way to this state, skip
        if time_so_far > used[u][res]:
            continue

        # Record best time to reach u
        if time_so_far < best_time[u]:
            best_time[u] = time_so_far

        # Option 1: buy more resource at node u
        gain, cost_time = Change[u]
        new_res = min(max_hold, res + gain)
        new_time = time_so_far + cost_time
        if used[u][new_res] > new_time:
            used[u][new_res] = new_time
            heapq.heappush(heap, (new_time, new_res, u))

        # Option 2: traverse each outgoing edge
        for v, req_res, edge_time in graph[u]:
            if res >= req_res:
                nr = res - req_res
                nt = time_so_far + edge_time
                if used[v][nr] > nt:
                    used[v][nr] = nt
                    heapq.heappush(heap, (nt, nr, v))

    return best_time

# Run from node 0 (which represents input node 1)
T = dijkstra(N, start=0, S=S, graph=graph, Change=Change)
# Output answers for nodes 2..N
for t in T[1:]:
    print(t)