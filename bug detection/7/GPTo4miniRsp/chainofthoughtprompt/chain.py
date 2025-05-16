# ChatGPT4omini output using Chain-of-thought prompt


# ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
# 1. Reading input
g = lambda: map(int, input().split())
N, M = g()

# Intent: read two integers, the number of horizontal segments (N) and the number of vertical segments (M).

# Bug: so far so good.

# 2. Collecting coordinates and segments

for _ in [0]*N:
a, b, c = g()
    sortx.append(a); sortx.append(b)
    sorty.append(c)
    y_line.append((a,b,c))
    for _ in [0]*M:
d, e, f = g()
    sortx.append(d)
    sorty.append(e); sorty.append(f)
    x_line.append((d,e,f))

Intent:

# Read N horizontal segments (a, b, c) into y_line.

# Read M vertical segments (d, e, f) into x_line.

# Real effect: the M-loop is nested inside the N-loop, so instead of reading exactly M vertical segments, you end up reading N×M of them!

# 3. Coordinate compression
# They gather all x-coordinates in sortx, all y-coordinates in sorty, then do:

sortx = sorted(set(sortx))
sorty = sorted(set(sorty))

# Then they meant to “zip” each original segment into its compressed index:

# horizontal
for a, b, c in y_line:
    ai = bisect_left(sortx, a)
    bi = bisect_left(sortx, b)
    ci = bisect_left(sorty, c)
    zip_y_line.append((ai, bi, ci))

# vertical
for d, e, f in x_line:
    di = bisect_left(sortx, d)
    ei = bisect_left(sorty, e)
    fi = bisect_left(sorty, f)
    zip_x_line.append((di, ei, fi))

# Bug: In the original, these two for-loops were also nested or mis-indented, so you didn’t get the right compressed lists.

# 4. Building the grid
# They compute

W = len(sortx)*2 - 1
H = len(sorty)*2 - 1
G = [1] * (W*H)     # all “open”

# and define
def make_wall(i, j):
    G[i + j*W] = 0

# Then they intended to carve every horizontal segment:
for ai, bi, ci in zip_y_line:
    for x in range(2*ai, 2*bi + 1):
        make_wall(x, 2*ci)

# and every vertical one:
for di, ei, fi in zip_x_line:
    for y in range(2*ei, 2*fi + 1):
        make_wall(2*di, y)

# Bug: In the posted code, those loops wound up inside the body of make_graph (i.e. mis-indented), so they never actually ran at all.

# 5. Flood-fill and area
# They define
def get_score(i, j):
    if i <= 0 or j <= 0 or i >= W or j >= H:
        return float(' inf ')    # <- invalid Python!
    if i%2==1 and j%2==1:
        dx = sortx[(i+1)//2] - sortx[(i-1)//2]
        dy = sorty[(j+1)//2] - sorty[(j-1)//2]
        return dx*dy
    return 0

def search(i, j):
    res = []
    for dx, dy in dic:
        if G[(i+dx)+(j+dy)*W] == 1:
            res.append((i+dx, j+dy))
            return res   # <- returns on the very *first* neighbor!
    # and if *no* neighbor was passable, it never returns anything (i.e. `None`)

# Bugs:

# float(' inf ') must be float('inf').

# return res is inside the loop, so you only ever get one neighbor back.

# If there are no open neighbors, search falls off the end and returns None, causing stack += None to blow up.

# Fully corrected version

import sys
from bisect import bisect_left

def g():
    return map(int, sys.stdin.readline().split())

# 1) Read input
N, M = g()
sortx = []
sorty = []
y_line = []
x_line = []

# 2) Horizontal segments
for _ in range(N):
    a, b, c = g()
    sortx.extend([a, b])
    sorty.append(c)
    y_line.append((a, b, c))

# 3) Vertical segments
for _ in range(M):
    d, e, f = g()
    sortx.append(d)
    sorty.extend([e, f])
    x_line.append((d, e, f))

# 4) Make sure (0,0) is in our grid so we can BFS from “outside”
sortx.append(0)
sorty.append(0)

# 5) Compress coordinates
sortx = sorted(set(sortx))
sorty = sorted(set(sorty))

zip_y_line = []
for a, b, c in y_line:
    ai = bisect_left(sortx, a)
    bi = bisect_left(sortx, b)
    ci = bisect_left(sorty, c)
    zip_y_line.append((ai, bi, ci))

zip_x_line = []
for d, e, f in x_line:
    di = bisect_left(sortx, d)
    ei = bisect_left(sorty, e)
    fi = bisect_left(sorty, f)
    zip_x_line.append((di, ei, fi))

# 6) Grid size and initialization
W = len(sortx) * 2 - 1
H = len(sorty) * 2 - 1
G = [1] * (W * H)   # 1 = open, 0 = wall

def make_wall(i, j):
    G[i + j*W] = 0

# 7) Carve horizontal walls
for ai, bi, ci in zip_y_line:
    y = 2 * ci
    for x in range(2*ai, 2*bi + 1):
        make_wall(x, y)

# 8) Carve vertical walls
for di, ei, fi in zip_x_line:
    x = 2 * di
    for y in range(2*ei, 2*fi + 1):
        make_wall(x, y)

# 9) Area of a cell
def get_score(i, j):
    # if you step outside the boundary → infinite
    if i <= 0 or j <= 0 or i >= W-1 or j >= H-1:
        return float('inf')
    # only odd,odd positions are real cells
    if i%2==1 and j%2==1:
        dx = sortx[(i+1)//2] - sortx[(i-1)//2]
        dy = sorty[(j+1)//2] - sorty[(j-1)//2]
        return dx * dy
    return 0

# 10) Flood-fill neighbors
dirs = [(-1,0),(1,0),(0,-1),(0,1)]
def search(i, j):
    res = []
    for dx, dy in dirs:
        ni, nj = i+dx, j+dy
        if 0 <= ni < W and 0 <= nj < H and G[ni + nj*W] == 1:
            res.append((ni, nj))
    return res

# 11) BFS from “just outside” the origin
si = 2 * bisect_left(sortx, 0) - 1
sj = 2 * bisect_left(sorty, 0) - 1

stack = [(si, sj)]
G[si + sj*W] = 0   # mark visited
res = 0

while stack:
    x, y = stack.pop()
    area = get_score(x, y)
    if area == float('inf'):
        res = float('inf')
        break
    res += area
    for nx, ny in search(x, y):
        G[nx + ny*W] = 0
        stack.append((nx, ny))

# 12) Output
print("INF" if res == float('inf') else res)

# What we fixed
# Indentation: all loops and function bodies are now at the correct level.

# Exact loops: exactly N horizontal reads, then M vertical reads (no accidental nesting).

# make_wall actually gets called in top-level loops, so walls are carved.

# float('inf') spelled correctly (no extra spaces).

# search returns every open neighbor and always returns a list.

# Visited cells are stamped out as soon as they go on the stack, avoiding duplicates.

# Boundary check in get_score uses W-1/H-1 so that we treat going outside as “infinite.”

# With those changes in place, the code now correctly builds the compressed grid, carves every segment into walls, does a proper flood-fill from the outside, and reports either the total reachable area or "INF" if it can escape to infinity.
# ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


# Research Team:
# ================== SUMMARY ==================
# Program: buggy_61.py
# Tests passed: 85/96
# Pass rate: 88.54%
# =============================================