# Claude output using Chain-of-thought prompt


# ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
# I'll analyze this code step by step to identify bugs and suggest fixes.
# Understanding the Problem
# This appears to be a graph problem where:

# We have N nodes and M edges
# Each edge has two costs: 'a' (coins required) and 'b' (time required)
# Each node has a "change" operation: gain 'c' coins by spending 'd' time
# We start at node 0 with S coins
# We need to find the minimum time to reach each other node

# The code uses Dijkstra's algorithm with a state that includes [position, coins].
# Identifying Issues
# Let me work through the code carefully to find the bugs:
# Issue 1: Indentation Errors
# The code has inconsistent indentation, which would cause syntax errors in Python. Python relies on indentation to define code blocks.
# Issue 2: Early Termination Condition in Dijkstra
# There's a problematic check inside the Dijkstra's algorithm:

j = 1
for i in range(N):
    if Ans[i] > c:
        j = 0
        break
    if j == 1:
        break

# This tries to terminate the algorithm early if all nodes have been "settled" (have final answers), but the logic is flawed. The if j == 1: break is at the wrong indentation level and will break out of the loop after checking just the first node.
# Issue 3: Edge Processing
# When processing edges in Dijkstra's algorithm, the code iterates over all nodes rather than checking actual adjacency:

for road in range(N):
    if Cost[current][road][0] != -1:
        # process edge
# This is inefficient because it tries all possible N destinations for each node.
# Issue 4: Redundant Check for Answer Update
# The code updates Ans[road] inside the edge processing loop, but this update doesn't consider if we're getting a better path to the node. It should compare with the current answer.
# Issue 5: Change Operation Logic
# The "change" operation indentation is wrong and it's only applied once for each node visited in the Dijkstra algorithm.
# Fixed Version
# Here's the corrected code with explanations:

import heapq

# Read input
N, M, S = map(int, input().split())
Cost = [[[-1, -1] for _ in range(N)] for _ in range(N)]
Change = [[0] * 2 for _ in range(N)]

# Maximum coin cost of any edge
A_m = 0

# Read edges
for i in range(M):
    u, v, a, b = map(int, input().split())
    A_m = max(A_m, a)
    Cost[u-1][v-1][0] = a
    Cost[u-1][v-1][1] = b
    Cost[v-1][u-1][0] = a
    Cost[v-1][u-1][1] = b

# Read change operations for each node
for i in range(N):
    c, d = map(int, input().split())
    Change[i][0] = c
    Change[i][1] = d

# Cap the maximum coins to avoid unnecessary states
if S > A_m * N:
    S = A_m * N

def dijkstra(N, start, Cost, Change):
    # Initialize answer array (minimum time to reach each node)
    Ans = [10 ** 12] * N
    Ans[start] = 0  # Time to reach start node is 0
    
    # Initialize min heap with (time, coins, node)
    minHeap = []
    
    # Track minimum time for each state [node][coins]
    used = [[10 ** 12] * (A_m * N + 1) for _ in range(N)]
    used[start][S] = 0
    
    heapq.heappush(minHeap, (0, S, start))
    
    while minHeap:
        # Get the state with minimum time
        (c, hold, current) = heapq.heappop(minHeap)
        
        # Skip if we've found a better way to this state
        if used[current][hold] < c:
            continue
        
        # Process edges from current node
        for road in range(N):
            if Cost[current][road][0] != -1:  # If edge exists
                if hold >= Cost[current][road][0]:  # If we have enough coins
                    new_time = c + Cost[current][road][1]
                    new_coins = hold - Cost[current][road][0]
                    
                    if used[road][new_coins] > new_time:
                        used[road][new_coins] = new_time
                        heapq.heappush(minHeap, (new_time, new_coins, road))
                        
                        # Update answer if we found a better time
                        if Ans[road] > new_time:
                            Ans[road] = new_time
        
        # Try change operation at current node (gain coins by spending time)
        new_coins = hold + Change[current][0]
        if new_coins <= A_m * N:  # Don't exceed max coins
            new_time = c + Change[current][1]
            if used[current][new_coins] > new_time:
                used[current][new_coins] = new_time
                heapq.heappush(minHeap, (new_time, new_coins, current))
    
    return Ans

# Run Dijkstra's algorithm
T = dijkstra(N, 0, Cost, Change)

# Print results (excluding the start node)
for i in T[1:]:
    print(i)

# Key Fixes:

# Fixed indentation throughout the code to maintain consistent Python syntax
# Removed the problematic early termination logic that was incorrectly checking if all nodes were settled
# Added a prune condition in the Dijkstra algorithm to avoid processing states we've already found better paths to
# Fixed "change" operation indentation and logic
# Improved code readability with more descriptive variable names and comments

# The main algorithmic bug was the early termination check in Dijkstra's algorithm, which could cause the algorithm to stop before finding the optimal paths to all nodes. Additionally, the indentation issues would have prevented the code from running properly.
# ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


# Research Team:
# the output
# ================== SUMMARY ==================
# Program: buggy_4.py
# Tests passed: 26/27
# Pass rate: 96.30%
# =============================================