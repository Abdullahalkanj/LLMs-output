# ChatGPT4o output with structured prompt


# --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
# Alright, let's dive into this carefully.

# You posted a buggy version of a code with the problem ID p02703_s941258054.
# From a high-level view, the code tries to implement a modified Dijkstra's algorithm, where you manage coins or resources (hold), moving across nodes (N nodes) with both movement costs and resource requirements, and there are also resource recharge stations (Change).

# Now, here are the main issues in the buggy code:
# Indentation is completely broken:

# Inside your loops, conditionals, and function definitions (def), the indentation is wrong. This would immediately throw syntax errors in Python.

# For example:

for i in range(M):
u, v, a, b = map(...)

# needs to be indented:

for i in range(M):
    u, v, a, b = map(...)

# Incorrect location of the second for loop:

# The second loop that reads Change values (after reading M edges) is wrongly placed inside the first for i in range(M) loop.

# It should happen after reading all roads.

# A_m is used inside dijkstra but not passed properly:

# The variable A_m is used inside dijkstra, but it's not passed as a parameter nor declared global.

# This is a scope error.

# Solution: either pass A_m as a parameter to dijkstra or recalculate inside the function.

# Missing update inside recharge (Change) operation:

# After heapq.heappush, the used[current][new_hold] value should be updated, similar to normal moves.

# Wrong start setting in Ans array:

# You initialize Ans[0] = 0 always, but the start might not be node 0 in general. It should be Ans[start] = 0.

# Unnecessary early break inside Dijkstra:

# The code tries to check if all Ans[i] <= c, and then break.

# This is incorrect and unnecessary in a heap-based Dijkstra. It can cause premature termination.

# Now, here’s a fixed version of the code:

import heapq

# Read inputs
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

def dijkstra(N, start, Cost, Change, S, A_m):
    Ans = [10**12] * N
    Ans[start] = 0
    minHeap = []
    used = [[10**12] * (A_m * N + 1) for _ in range(N)]
    used[start][S] = 0
    heapq.heappush(minHeap, (0, S, start))

    while minHeap:
        c, hold, current = heapq.heappop(minHeap)

        if Ans[current] < c:
            continue

        for road in range(N):
            if Cost[current][road][0] != -1:
                cost_need = Cost[current][road][0]
                move_cost = Cost[current][road][1]
                if hold >= cost_need:
                    new_hold = hold - cost_need
                    if used[road][new_hold] > c + move_cost:
                        used[road][new_hold] = c + move_cost
                        heapq.heappush(minHeap, (c + move_cost, new_hold, road))
                        if Ans[road] > c + move_cost:
                            Ans[road] = c + move_cost

        # Recharge at current node
        if hold + Change[current][0] <= A_m * N:
            new_hold = hold + Change[current][0]
            recharge_cost = Change[current][1]
            if used[current][new_hold] > c + recharge_cost:
                used[current][new_hold] = c + recharge_cost
                heapq.heappush(minHeap, (c + recharge_cost, new_hold, current))

    return Ans

T = dijkstra(N, 0, Cost, Change, S, A_m)

for i in T[1:]:
    print(i)


# Summary of the main corrections:
# Proper indentation and logical block separation.

# Move Change input to outside the road input loop.

# Pass S and A_m as parameters to dijkstra.

# Fix updating used array after recharging.

# Remove unnecessary break in Dijkstra.

# Would you also like me to show you a more optimized version of this using priority queues more efficiently? 🚀
# It can make the program faster for bigger inputs!

# --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


# Research Team:
# ================== SUMMARY ==================
# Program: buggy_4.py
# Tests passed: 2/27
# Pass rate: 7.41%
# =============================================