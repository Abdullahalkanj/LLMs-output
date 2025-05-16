# ChatGPT4omini output using Structured prompt


# ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
# Here’s what was going wrong and how to fix it:

# You were reading the vertical segments inside the horizontal‐segment loop.
# That makes you read N × M lines instead of N + M, garbling both your coordinate lists and your graph.

# You never added the origin (0) into your coordinate lists.
# When you later do

si = 2*bl(sortx, 0) - 1
sj = 2*bl(sorty, 0) - 1

# if 0 isn’t in sortx (or sorty), bisect_left returns an insertion‐point that makes si (or sj) come out –1 or W—i.e. outside your grid.

# Your search() only ever returned the first neighbor, because the return was indented inside the for loop.

from bisect import bisect_left as bl

g = lambda: map(int, input().split())
N, M = g()

# include the origin so we can start there
sortx = [0]
sorty = [0]
y_line = []
x_line = []

# 1) read N horizontal segments
for _ in range(N):
    a, b, c = g()
    sortx.extend([a, b])
    sorty.append(c)
    y_line.append((a, b, c))

# 2) read M vertical segments
for _ in range(M):
    d, e, f = g()
    sortx.append(d)
    sorty.extend([e, f])
    x_line.append((d, e, f))

# coordinate-compress
sortx = sorted(set(sortx))
sorty = sorted(set(sorty))

# re-encode each segment in the compressed grid
zip_y_line = []
for a, b, c in y_line:
    a = bl(sortx, a)
    b = bl(sortx, b)
    c = bl(sorty, c)
    zip_y_line.append((a, b, c))

zip_x_line = []
for d, e, f in x_line:
    d = bl(sortx, d)
    e = bl(sorty, e)
    f = bl(sorty, f)
    zip_x_line.append((d, e, f))

W = len(sortx)*2 - 1
H = len(sorty)*2 - 1

# build graph with a sentinel border of zeros
G = [1] * (W*H) + [0] * (W + 1)

def make_graph(i, j, s=0):
    G[i + j*W] = s

# block all horizontal edges
for a, b, c in zip_y_line:
    for i in range(2*a, 2*b+1):
        make_graph(i, 2*c)

# block all vertical edges
for d, e, f in zip_x_line:
    for j in range(2*e, 2*f+1):
        make_graph(2*d, j)

def get_score(i, j):
    # if on the “outer” boundary, count as infinite
    if i <= 0 or j <= 0 or i >= W or j >= H:
        return float('inf')
    # if both odd, we’re in a cell center → area = dx*dy
    if i % 2 == 1 and j % 2 == 1:
        dx = sortx[(i+1)//2] - sortx[(i-1)//2]
        dy = sorty[(j+1)//2] - sorty[(j-1)//2]
        return dx * dy
    return 0

neighbors = [(-1,0),(1,0),(0,1),(0,-1)]

def search(i, j):
    res = []
    for dx, dy in neighbors:
        ni, nj = i+dx, j+dy
        if G[ni + nj*W] == 1:
            res.append((ni, nj))
    # moved OUTSIDE the loop so *all* valid neighbors are returned
    return res

# start flood-fill at the origin cell
si = 2*bl(sortx, 0) - 1
sj = 2*bl(sorty, 0) - 1

stack = [(si, sj)]
res = 0

while stack:
    x, y = stack.pop()
    if G[x + y*W] != 1:
        continue
    # mark visited
    make_graph(x, y, 0)
    res += get_score(x, y)
    stack.extend(search(x, y))

print(res if res < float('inf') else 'INF')

# What changed?
# Separated the two for-loops so you read exactly N horizontals then M verticals.

# Appended 0 to both sortx and sorty so (0,0) is actually inside your compressed grid.

# Dedented the return in search() so it collects all possible neighbors before returning.

# Kept the sentinel border trick (those extra zeros in G) so you don’t have to do manual bounds-checking when you look up G[...].

# With those three tweaks in place, your flood-fill will start in the right cell, see all its open edges, and correctly sum up the reachable area.
# ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


# Research Team:
#the output
# ================== SUMMARY ==================
# Program: buggy_61.py
# Tests passed: 82/96
# Pass rate: 85.42%
# =============================================
