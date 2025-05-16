# ChatGPT4omini output using Structured prompt


# ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
# The root of the problem is that your input‐reading loop and your Dijkstra “return” are both mis-indented, and you even accidentally shadow your outer loop variable inside that inner loop. Concretely:

# Reading the “Change” array is nested inside your edge‐reading loop.

for i in range(M):
    …
    for i in range(N):           # <<< THIS IS INSIDE the M-loop by accident
        c, d = …
        Change[i][0] = c
        Change[i][1] = d

# That means you try to read the N lines of (c, d) once per edge, and you clobber your loop counter i each time, so the rest of your code works with completely the wrong data.

# You never update your used[][] table when you “refill” (the Change step).
# In your “pay cost d to gain c fuel” branch you do:

if used[current][new_hold] > c + d:
    heapq.heappush(…)
    # but you never do
    used[current][new_hold] = c + d

# which means you’ll re-push the same state over and over.

# Your return Ans is indented inside the while loop, so you bail out after the very first iteration.

# Here’s a minimal “diff” of the fix:

- for i in range(M):
+ for _ in range(M):
      u, v, a, b = map(int, input().split())
      A_m = max(A_m, a)
      Cost[u-1][v-1][0] = a
      Cost[u-1][v-1][1] = b
      Cost[v-1][u-1][0] = a
      Cost[v-1][u-1][1] = b

-     for i in range(N):     # <<< wrong indentation, and re-uses `i`
+ for i in range(N):         # <<< now OUTSIDE the M-loop
      c, d = map(int, input().split())
      Change[i][0] = c
      Change[i][1] = d

-    if S > A_m * N:
- S = A_m * N
+ if S > A_m * N:
+     S = A_m * N

  def dijkstra(...):
      …
      while minHeap:
          …
-         if hold + Change[current][0] <= A_m * N:
-             if used[current][hold + Change[current][0]] > c + Change[current][1]:
-                 heapq.heappush(…)        # <<< pushes but never updates `used[...]`
+         # --- REFILL STEP (must update `used` before pushing) ---
+         c_inc, d_cost = Change[current]
+         new_hold = hold + c_inc
+         if new_hold <= A_m * N:
+             new_cost = c + d_cost
+             if used[current][new_hold] > new_cost:
+                 used[current][new_hold] = new_cost
+                 heapq.heappush(minHeap, (new_cost, new_hold, current))
+
      # --- the `return` must be *after* the while, not indented under it! ---
-     return Ans
+     return Ans

# Here’s the fully‐corrected version

import heapq

# read N = #nodes, M = #edges, S = initial fuel
N, M, S = map(int, input().split())

# Cost[u][v] = [fuel_needed, time_cost]  (–1 means “no edge”)
Cost = [[[ -1, -1 ] for _ in range(N)] for _ in range(N)]
# Change[i] = [fuel_gain, time_cost] at node i
Change = [[0, 0] for _ in range(N)]
A_m = 0

# read edges
for _ in range(M):
    u, v, a, b = map(int, input().split())
    u -= 1; v -= 1
    A_m = max(A_m, a)
    Cost[u][v] = [a, b]
    Cost[v][u] = [a, b]

# read per-node refill info
for i in range(N):
    c, d = map(int, input().split())
    Change[i] = [c, d]

# cap initial fuel to the maximum possible
if S > A_m * N:
    S = A_m * N

def dijkstra(start):
    INF = 10**15
    # best time to reach each node (ignoring fuel-state)
    Ans = [INF] * N
    Ans[0] = 0

    # used[u][f] = best time to reach node u with f units of fuel left
    used = [[INF] * (A_m * N + 1) for _ in range(N)]
    used[start][S] = 0

    minHeap = [(0, S, start)]  # (time_so_far, fuel_left, node)

    while minHeap:
        time_so_far, fuel_left, u = heapq.heappop(minHeap)

        # stale‐state check
        if time_so_far > used[u][fuel_left]:
            continue

        # (optional) early exit if no improvement possible
        if all(Ans[v] <= time_so_far for v in range(N)):
            break

        # -- try travelling along each edge --
        for v in range(N):
            a_req, b_cost = Cost[u][v]
            if a_req >= 0 and fuel_left >= a_req:
                new_time = time_so_far + b_cost
                new_fuel = fuel_left - a_req
                if used[v][new_fuel] > new_time:
                    used[v][new_fuel] = new_time
                    heapq.heappush(minHeap, (new_time, new_fuel, v))
                    Ans[v] = min(Ans[v], new_time)

        # -- try refilling at u --
        c_gain, d_time = Change[u]
        new_fuel = fuel_left + c_gain
        if new_fuel <= A_m * N:
            new_time = time_so_far + d_time
            if used[u][new_fuel] > new_time:
                used[u][new_fuel] = new_time        # <<< **don’t forget this line**
                heapq.heappush(minHeap, (new_time, new_fuel, u))

    return Ans

T = dijkstra(0)
# print answers for nodes 2..N
for t in T[1:]:
    print(t)

# What changed
# Input loops un-nested: we now read all M edges first, then all N refills.

# Loop variables renamed (_ for the edges loop) so we don’t shadow.

# if S > A_m * N is now properly indented so it runs once, after we know A_m.

# In Dijkstra’s refill branch we both push the new state and update used[][].

# The return Ans sits outside the while so you don’t bail out prematurely.

# With exactly those four fixes applied, your algorithm will both read its input correctly and explore all of the fuel‐state graph exactly once per state, and you’ll get the right shortest times.
# ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


# Research Team:
# the output
# ================== SUMMARY ==================
# Program: buggy_4.py
# Tests passed: 27/27
# Pass rate: 100.00%
# =============================================