import heapq

def dijkstra(N, start, initial_fuel, Cost, Change, A_max):
    """
    Finds minimum cost to reach every node, given fuel constraints.
    - N: number of nodes
    - start: starting node index (0-based)
    - initial_fuel: starting amount of fuel
    - Cost[u][v] = [fuel_cost, money_cost]
    - Change[u] = [fuel_gain, money_cost]
    - A_max: maximum fuel cost on any edge
    """
    INF = 10**12
    # Ans[i]: minimum money cost to reach node i
    Ans = [INF] * N
    Ans[start] = 0

    # used[u][f]: minimum money spent to reach state (u, f)
    max_fuel = A_max * N
    used = [[INF] * (max_fuel + 1) for _ in range(N)]
    used[start][initial_fuel] = 0

    # heap entries: (money_spent, fuel_left, node)
    heap = [(0, initial_fuel, start)]

    while heap:
        money_spent, fuel_left, u = heapq.heappop(heap)

        # If we have already found a cheaper way to (u, fuel_left), skip
        if money_spent > used[u][fuel_left]:
            continue

        # Try buying fuel at u
        gain, cost_to_buy = Change[u]
        new_fuel = min(fuel_left + gain, max_fuel)
        if used[u][new_fuel] > money_spent + cost_to_buy:
            used[u][new_fuel] = money_spent + cost_to_buy
            heapq.heappush(heap, (money_spent + cost_to_buy, new_fuel, u))

        # Try traveling to neighbors
        for v in range(N):
            fuel_req, money_cost = Cost[u][v]
            if fuel_req == -1:
                continue  # no edge
            if fuel_left >= fuel_req:
                remaining = fuel_left - fuel_req
                new_money = money_spent + money_cost
                if used[v][remaining] > new_money:
                    used[v][remaining] = new_money
                    heapq.heappush(heap, (new_money, remaining, v))
                    # Update global best for node v
                    if Ans[v] > new_money:
                        Ans[v] = new_money

    return Ans


def main():
    N, M, S = map(int, input().split())

    # Initialize Cost matrix with [-1, -1]
    Cost = [[[-1, -1] for _ in range(N)] for _ in range(N)]
    A_max = 0

    # Read edges
    for _ in range(M):
        u, v, a, b = map(int, input().split())
        u -= 1  # convert to 0-based
        v -= 1
        A_max = max(A_max, a)              # track maximum fuel cost
        Cost[u][v] = [a, b]
        Cost[v][u] = [a, b]

    # Read Change array
    Change = []
    for _ in range(N):
        c, d = map(int, input().split())
        Change.append([c, d])

    # Cap initial fuel S to the maximum reasonable amount
    max_possible = A_max * N
    if S > max_possible:
        S = max_possible

    # Run the modified Dijkstra
    results = dijkstra(N, start=0, initial_fuel=S,
                       Cost=Cost, Change=Change,
                       A_max=A_max)

    # Print results for nodes 2..N
    for cost in results[1:]:
        print(cost)


if __name__ == "__main__":
    main()