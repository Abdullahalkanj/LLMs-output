# ChatGPT4o output with Chain-of-thought

# Step 1: Understanding the problem
# From the pattern, it looks like the code is trying to solve a 2D area coverage or graph traversal problem with obstacles based on lines (either vertical or horizontal).

# Inputs:

# N lines aligned with the y-axis (meaning vertical segments).

# M lines aligned with the x-axis (meaning horizontal segments).

# The lines partition the 2D plane into regions.

# The goal is probably to compute the area reachable from the origin (0, 0) unless blocked by the lines.

# Step 2: What the code tries to do
# Reads input into two lists: y_line (vertical walls) and x_line (horizontal walls).

# Coordinates are compressed (i.e., sorted and deduplicated, mapped to indices).

# Constructs a 2D grid (G) where:

# 1 represents empty or traversable area.

# 0 will represent walls (obstacles).

# Walls are drawn into the graph (G) by setting 0s where obstacles exist.

# Traverses using DFS from the origin, accumulating the area, where each cell has a weight given by get_score.

# Step 3: Major issues detected
# (A) Indentation Errors

# The structure is severely broken — loops and function definitions are wrongly indented.

# Python needs proper nesting; otherwise, for, def, and if blocks will not work.

# Examples:

for _ in [0] * N:
a, b, c = g()
    sortx.append(a)  # wrong indent

# or

def make_graph(i, j, s=0):
G[i + j * W] = s  # wrong indent

# (B) Misplacement of Imports

# from bisect import bisect_left as bl should not be buried inside loops.

# It should be at the top of the file.

# (C) Poor Graph Initialization

# G is created with an extra width (W + 1). It's unclear why — should be checked.

# (D) DFS Stack Incorrect Initialization

# The starting point for DFS is based on the compressed index of 0:

stack = [(2 * bl(sortx, 0) - 1, 2 * bl(sorty, 0) - 1)]

# This likely tries to locate the position corresponding to coordinate (0, 0), but it needs careful validation.

# (E) Misnaming / Overlapping variable res

# res is both:

# A list (res = []) inside search.

# An integer (res = 0) for total area.

# This is confusing and dangerous.

# (F) Logic in make_graph and search

# The walls are painted into the graph twice (zip_y_line and zip_x_line).

# make_graph and search are not clearly separated in terms of purpose (marking walls vs traversal).

# Step 4: Plan to fix
# Fix indentation of the entire code.

# Move imports (bisect) to the top.

# Carefully initialize the graph dimensions.

# Separate concerns:

# First paint the walls.

# Then perform DFS to compute area.

# Fix the naming of variables (res) to avoid confusion.

# Step 5: Fixed version of the code (suggestion)

from bisect import bisect_left as bl

# Input reader
g = lambda: map(int, input().split())

N, M = g()
sortx = []
sorty = []
y_line = []
x_line = []

# Read vertical lines
for _ in range(N):
    a, b, c = g()
    sortx.append(a)
    sortx.append(b)
    sorty.append(c)
    y_line.append((a, b, c))

# Read horizontal lines
for _ in range(M):
    d, e, f = g()
    sortx.append(d)
    sorty.append(e)
    sorty.append(f)
    x_line.append((d, e, f))

# Coordinate compression
sortx = sorted(set(sortx))
sorty = sorted(set(sorty))

zip_x_line = []
zip_y_line = []

for a, b, c in y_line:
    a = bl(sortx, a)
    b = bl(sortx, b)
    c = bl(sorty, c)
    zip_y_line.append((a, b, c))

for d, e, f in x_line:
    d = bl(sortx, d)
    e = bl(sorty, e)
    f = bl(sorty, f)
    zip_x_line.append((d, e, f))

W = len(sortx) * 2 - 1
H = len(sorty) * 2 - 1
G = [1] * (W * H)

def make_graph(i, j, s=0):
    G[i + j * W] = s

# Draw vertical walls
for a, b, c in zip_y_line:
    for i in range(2 * a, 2 * b + 1):
        make_graph(i, 2 * c)

# Draw horizontal walls
for d, e, f in zip_x_line:
    for j in range(2 * e, 2 * f + 1):
        make_graph(2 * d, j)

def get_score(i, j):
    if i <= 0 or j <= 0 or i >= W or j >= H:
        return float('inf')
    elif i % 2 == 1 and j % 2 == 1:
        dx = sortx[(i + 1) // 2] - sortx[(i - 1) // 2]
        dy = sorty[(j + 1) // 2] - sorty[(j - 1) // 2]
        return dx * dy
    else:
        return 0

# Directions for DFS
dic = [(-1, 0), (1, 0), (0, 1), (0, -1)]

def search(i, j):
    res = []
    for dx, dy in dic:
        ni, nj = i + dx, j + dy
        if 0 <= ni < W and 0 <= nj < H and G[ni + nj * W] == 1:
            res.append((ni, nj))
    return res

start_x = 2 * bl(sortx, 0) - 1
start_y = 2 * bl(sorty, 0) - 1
stack = [(start_x, start_y)]

total_area = 0
while stack:
    x, y = stack.pop()
    if G[x + y * W] != 1:
        continue
    total_area += get_score(x, y)
    G[x + y * W] = 0  # mark visited
    stack += search(x, y)

print(total_area if total_area < float('inf') else 'INF')

# Step 6: Summary of what was fixed
# Proper indentation everywhere.

# Import moved to the top.

# Separated wall drawing and traversal logic.

# Avoided variable name collisions.

# Corrected graph initialization and DFS traversal.

# Would you like me to also show a visual diagram of how the compressed grid works, if that would help further? 📈
# --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


# Research Team: 
# the output
# ================== SUMMARY ==================
# Program: buggy_61.py
# Tests passed: 87/96
# Pass rate: 90.62%
# =============================================
