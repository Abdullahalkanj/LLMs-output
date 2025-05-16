# ChatGPT4o output with chain-of-thought


# ----------------------------------------------------------------------------------------------------------------------------------------------------------------------
# Alright, let's reason carefully through this.

# First, what the code is trying to do:

# You are given a graph with N nodes and M edges.

# Each edge has two weights: a resource cost a and a time cost b.

# You start with some initial resource S.

# Besides traveling, at each node you can "replenish" resources at some cost d.

# You must find the minimum time cost to reach each node from node 0.

# The code is supposed to implement Dijkstra's algorithm, but with a resource amount affecting movement.

# Step-by-Step Problems:
# Indentation errors:

# In the for i in range(M) loop, inner code (u, v, a, b = map...) is indented improperly.

# Similarly, in for i in range(N) where change costs are read, it’s wrongly indented under the previous loop.

# Overall, the structure is messed up because of inconsistent indentation.

# Misplaced if S > A_m * N block:

# S is updated to A_m * N if it's larger, but this logic should happen after all input is read.

# Scope error: A_m inside dijkstra:

# The variable A_m is used inside the dijkstra function, but it's defined outside.

# In clean code, it should either be passed as a parameter, or declared global, or recalculated in the function.

# Wrong initialization of Ans inside dijkstra:

# Ans[0] is immediately set to 0. Shouldn’t it be Ans[start] = 0 instead? (Since start may not always be 0.)

# Confusing inner logic:

# Checking if all Ans[i] <= c (variable j) looks pointless and wrong for Dijkstra — this check should not be here.

# Double pushing into heap:

# After moving along a road, both used and Ans are updated. That's fine, but logic is complicated unnecessarily.

# Potential infinite loop:

# If you only ever replenish at a node (without leaving), you could loop forever. However, the heap approach should prevent this.

# Messy return and printing:

# At the end, after calling dijkstra, you loop over T[1:] to print results — correct, but the indentation is wrong again.

# Plan for Fix:
# Fix all indentations.

# Properly separate input reading from logic.

# Pass A_m to dijkstra or recompute inside.

# Remove wrong early stop check (j).

# Use start instead of 0 in initialization.

# Clean up the heap logic a little bit.

# Now, here’s a corrected version:

import heapq

N, M, S = map(int, input().split())
Cost = [[[-1, -1] for _ in range(N)] for _ in range(N)]
Change = [[0] * 2 for _ in range(N)]
A_m = 0

for _ in range(M):
    u, v, a, b = map(int, input().split())
    A_m = max(A_m, a)
    Cost[u-1][v-1][0] = a
    Cost[u-1][v-1][1] = b
    Cost[v-1][u-1][0] = a
    Cost[v-1][u-1][1] = b

for i in range(N):
    c, d = map(int, input().split())
    Change[i][0] = c
    Change[i][1] = d

if S > A_m * N:
    S = A_m * N

def dijkstra(N, start, Cost, Change, A_m, S):
    INF = 10**12
    Ans = [INF] * N
    used = [[INF] * (A_m * N + 1) for _ in range(N)]

    heap = []
    heapq.heappush(heap, (0, S, start))
    used[start][S] = 0

    while heap:
        time_cost, resource, current = heapq.heappop(heap)

        if Ans[current] <= time_cost:
            continue

        Ans[current] = time_cost

        # Try to travel to neighboring nodes
        for next_node in range(N):
            if Cost[current][next_node][0] != -1:
                a, b = Cost[current][next_node]
                if resource >= a:
                    next_resource = resource - a
                    next_time = time_cost + b
                    if used[next_node][next_resource] > next_time:
                        used[next_node][next_resource] = next_time
                        heapq.heappush(heap, (next_time, next_resource, next_node))

        # Try to replenish resource at current node
        add_resource, add_time = Change[current]
        if resource + add_resource <= A_m * N:
            next_resource = resource + add_resource
            next_time = time_cost + add_time
            if used[current][next_resource] > next_time:
                used[current][next_resource] = next_time
                heapq.heappush(heap, (next_time, next_resource, current))

    return Ans

T = dijkstra(N, 0, Cost, Change, A_m, S)

for ans in T[1:]:
    print(ans)

# Summary of changes:
# Fixed indentation.

# Moved all input collection first.

# Made A_m a parameter of dijkstra.

# Removed broken early-termination logic.

# Cleaned up heap pushing.

# Would you like me to also explain why S > A_m * N matters logically? (It’s a smart optimization related to limiting resource amounts.) 🚀

# ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


# Research Team:
# ================== SUMMARY ==================
# Program: buggy_4.py
# Tests passed: 2/27
# Pass rate: 7.41%
# =============================================
