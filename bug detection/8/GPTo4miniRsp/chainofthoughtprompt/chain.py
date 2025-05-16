# ChatGPT4omini output using Chain-of-thought prompt


# ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
# Step 1: Understand what the code is trying to do
# This looks like a graph problem involving:

# Edges: between nodes, with two values per edge (like "cost to traverse" and "time/penalty").

# Resource (like fuel or money): initially S, needed to travel along edges (needs to be ≥ required cost).

# Nodes can "refuel": Each node has a Change (gain some resource with some cost).

# The problem tries to find shortest cost paths from node 0 to others, given initial "resource" S.

# Dijkstra's algorithm is used, but modified to account for resource holding.

# Step 2: Identify problems in indentation and scope
# 2.1 Indentation problems
# The for i in range(M): loop reads edges correctly, but the next loop for i in range(N): (reading Change values) is inside the edge loop, when it should be after reading all edges.

# The def dijkstra function is nested inside the input reading part. It should be top-level (outside).

# The final call T = dijkstra(...) and the print loop are wrongly indented too.

# 👉 Root cause: Wrong indentation causing incorrect code structure.

# Step 3: Find logical issues inside Dijkstra
# 3.1 Wrongly placed "early exit" check
# Inside Dijkstra:
j = 1
for i in range(N):
    if Ans[i] > c:
        j = 0
        break
if j == 1:
    break

# Problem: Breaking the outer Dijkstra loop if all Ans[i] <= c?

# This is wrong: Dijkstra does not "terminate early" like that unless we have proven that all nodes are already finalized. Here, with extra "resource charging" possible, you might find better paths later after recharging!

# Conclusion: This early break logic must be deleted.

# 3.2 Forgetting to update used when doing a "recharge"
# When you recharge (gain resource at a node):

if hold + Change[current][0] <= A_m * N:
    if used[current][hold + Change[current][0]] > c + Change[current][1]:
        heapq.heappush(...)

# Bug: You heappush but forget to update used[current][hold + Change[current][0]]!

# You should update used[current][...] immediately after pushing.

# 👉 Root cause: Missing used[current][new_hold] = new_cost when recharging.

# Step 4: Minor optimization
# Since A_m * N can be huge, setting S = min(S, A_m * N) is fine (already done), no issue there.

# Step 5: Plan the fixes
# Summarized Fix Plan:

# Fix indentation for reading inputs, function definitions, and function calls.

# Remove the weird "early exit" j = 1 check inside Dijkstra.

# Update used[current][new_hold] properly after recharging.

# (Optional) Clean formatting a bit.

# Would you like me to now apply these fixes and show you the corrected full code? 🚀
# (It's a bit long but I'll format it cleanly.)

# i asked to : give me the code 
# the respond was
# Of course!
# Here is the corrected full code after applying the step-by-step reasoning:

# Problem ID: p02703_s941258054
# Corrected version
import heapq

N, M, S = map(int, input().split())

Cost = [[[ -1, -1 ] for _ in range(N)] for _ in range(N)]
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

def dijkstra(N, start, Cost, Change):
    Ans = [10**12] * N
    Ans[start] = 0
    minHeap = []
    used = [[10**12] * (A_m * N + 1) for _ in range(N)]
    used[start][S] = 0
    heapq.heappush(minHeap, (0, S, start))

    while minHeap:
        c, hold, current = heapq.heappop(minHeap)

        if used[current][hold] < c:
            continue

        for road in range(N):
            if Cost[current][road][0] != -1:
                need = Cost[current][road][0]
                cost = Cost[current][road][1]
                if hold >= need:
                    if used[road][hold - need] > c + cost:
                        used[road][hold - need] = c + cost
                        heapq.heappush(minHeap, (c + cost, hold - need, road))
                        if Ans[road] > c + cost:
                            Ans[road] = c + cost

        # Try to recharge
        if hold + Change[current][0] <= A_m * N:
            if used[current][hold + Change[current][0]] > c + Change[current][1]:
                used[current][hold + Change[current][0]] = c + Change[current][1]
                heapq.heappush(minHeap, (c + Change[current][1], hold + Change[current][0], current))

    return Ans

T = dijkstra(N, 0, Cost, Change)
for i in T[1:]:
    print(i)

# 🔥 Main Fixes Applied:
# Fixed all indentations.

# Moved def dijkstra outside the input part.

# Removed wrong "early exit" condition.

# Updated used properly when recharging.

# Cleaned formatting to make it readable.

# Would you also like me to show you a small example input you could use to test this corrected code easily? 📄🚀
# It'll help you check it very quickly!
# ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


# Research Team:
# the output
# ================== SUMMARY ==================
# Program: buggy_4.py
# Tests passed: 26/27
# Pass rate: 96.30%
# =============================================
