import heapq

def read_input():
    N, M, S = map(int, input().split())
    # Read roads
    Cost = [[(-1, -1) for _ in range(N)] for _ in range(N)]
    A_m = 0
    for _ in range(M):
        u, v, a, b = map(int, input().split())
        u -= 1; v -= 1
        A_m = max(A_m, a)
        Cost[u][v] = (a, b)
        Cost[v][u] = (a, b)
    # Read city refill info
    Change = []
    for _ in range(N):
        c, d = map(int, input().split())
        Change.append((c, d))
    # Cap initial fuel
    S = min(S, A_m * N)
    return N, M, S, A_m, Cost, Change

def dijkstra(N, start, S, A_m, Cost, Change):
    INF = 10**18
    # Best time to reach city i (over all fuel levels)
    best_time = [INF] * N
    best_time[start] = 0

    # best_state[v][f] = best time to reach (v,f)
    max_fuel = A_m * N
    best_state = [ [INF] * (max_fuel + 1) for _ in range(N) ]
    best_state[start][S] = 0

    heap = [(0, start, S)]  # (time, city, fuel)
    while heap:
        time, city, fuel = heapq.heappop(heap)
        # If this state is stale, skip
        if time > best_state[city][fuel]:
            continue

        # Explore roads
        for nei in range(N):
            a, b = Cost[city][nei]
            if a >= 0 and fuel >= a:
                new_fuel = fuel - a
                new_time = time + b
                if new_time < best_state[nei][new_fuel]:
                    best_state[nei][new_fuel] = new_time
                    heapq.heappush(heap, (new_time, nei, new_fuel))
                    best_time[nei] = min(best_time[nei], new_time)

        # Explore refill
        gain, t_cost = Change[city]
        new_fuel = min(fuel + gain, max_fuel)
        new_time = time + t_cost
        if new_time < best_state[city][new_fuel]:
            best_state[city][new_fuel] = new_time
            heapq.heappush(heap, (new_time, city, new_fuel))

    return best_time

def main():
    N, M, S, A_m, Cost, Change = read_input()
    dist = dijkstra(N, 0, S, A_m, Cost, Change)
    # Print unreachable as -1 if you like, or INF
    for d in dist[1:]:
        print(d if d < 10**17 else -1)

if __name__ == "__main__":
    main()
