# Claude output using basic prompt


# ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
# # I'll analyze this code carefully and find the bug. Looking at the indentation and logic flow, there are several issues that need to be fixed.

# The main bugs I found and fixed:

# Indentation issues: There were severe indentation problems throughout the code, particularly in the dijkstra function, which were causing logical errors.
# Early termination check: The original code had a flawed early termination mechanism that was checking all nodes at each step, which is inefficient and potentially incorrect. I replaced it with a standard Dijkstra optimization that skips processing if we've already found a better path.
# Battery charging logic: The charging logic had incorrect indentation and was missing a check to update the used array. I fixed this to properly track the state.
# Variable scoping: Added better variable naming (new_cost, new_hold) to make the logic clearer and less prone to errors.
# Min function for charging: Added the min function when charging to ensure we don't exceed the maximum battery capacity.

# These changes maintain the core algorithm - a Dijkstra's shortest path with the additional dimension of battery charge level - while fixing the implementation issues that were causing bugs.

import heapq

N, M, S = map(int, input().split())
Cost = [[[-1, -1] for _ in range(N)] for _ in range(N)]
Change = [[0] * 2 for _ in range(N)]
A_m = 0

for i in range(M):
    u, v, a, b = map(int, input().split())
    A_m = max(A_m, a)
    Cost[u - 1][v - 1][0] = a
    Cost[u - 1][v - 1][1] = b
    Cost[v - 1][u - 1][0] = a
    Cost[v - 1][u - 1][1] = b

for i in range(N):
    c, d = map(int, input().split())
    Change[i][0] = c
    Change[i][1] = d

if S > A_m * N:
    S = A_m * N

def dijkstra(N, start, Cost, Change):
    Ans = [10 ** 12] * N
    Ans[start] = 0
    minHeap = []
    used = [[10 ** 12] * (A_m * N + 1) for _ in range(N)]
    used[start][S] = 0
    heapq.heappush(minHeap, (0, S, start))
    
    while minHeap:
        (c, hold, current) = heapq.heappop(minHeap)
        
        # Early termination check - this was improperly indented and had a logic error
        if c > used[current][hold]:
            continue
            
        for road in range(N):
            if Cost[current][road][0] != -1:
                if hold >= Cost[current][road][0]:
                    new_cost = c + Cost[current][road][1]
                    new_hold = hold - Cost[current][road][0]
                    
                    if used[road][new_hold] > new_cost:
                        used[road][new_hold] = new_cost
                        heapq.heappush(minHeap, (new_cost, new_hold, road))
                        if Ans[road] > new_cost:
                            Ans[road] = new_cost
        
        # Check if we can charge at the current node
        new_hold = min(hold + Change[current][0], A_m * N)
        new_cost = c + Change[current][1]
        
        if used[current][new_hold] > new_cost:
            used[current][new_hold] = new_cost
            heapq.heappush(minHeap, (new_cost, new_hold, current))
    
    return Ans

T = dijkstra(N, 0, Cost, Change)
for i in T[1:]:
    print(i)
# ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


# Research Team:
# ================== SUMMARY ==================
# Program: buggy_4.py
# Tests passed: 26/27
# Pass rate: 96.30%
# =============================================