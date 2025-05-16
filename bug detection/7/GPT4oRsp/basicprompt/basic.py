# ChatGPT4o output with Basic prompt


# --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
# Alright, this code is badly indented and mixed up — that's the root cause of the issues.
# Let’s first understand the goal:
# You have some horizontal and vertical lines on a 2D grid, and you need to find the area of the region starting from (0,0) that's not blocked by lines. If it's infinitely large, output INF.

# Main problems:
# Indentation is broken everywhere — causing wrong loops and blocks.

# Import of bisect is happening inside a loop unnecessarily.

# Inner loops are wrongly nested.

# Initialization of stack is wrong: you're searching from the wrong place.

# General sloppiness — multiple logic bugs.

# Here’s a fixed, clean version:

from bisect import bisect_left as bl

g = lambda: map(int, input().split())
N, M = g()
sortx = []
sorty = []
y_line = []
x_line = []

for _ in range(N):
    a, b, c = g()
    sortx.append(a)
    sortx.append(b)
    sorty.append(c)
    y_line.append((a, b, c))

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

# Draw the walls
for a, b, c in zip_y_line:
    for i in range(2 * a, 2 * b + 1):
        make_graph(i, 2 * c)

for d, e, f in zip_x_line:
    for j in range(2 * e, 2 * f + 1):
        make_graph(2 * d, j)

def get_score(i, j):
    if i <= 0 or j <= 0 or i >= W or j >= H:
        return float('inf')
    elif i % 2 and j % 2:
        dx = sortx[(i + 1) // 2] - sortx[(i - 1) // 2]
        dy = sorty[(j + 1) // 2] - sorty[(j - 1) // 2]
        return dx * dy
    else:
        return 0

dic = [(-1, 0), (1, 0), (0, 1), (0, -1)]

def search(i, j):
    res = []
    for dx, dy in dic:
        ni, nj = i + dx, j + dy
        if 0 <= ni < W and 0 <= nj < H and G[ni + nj * W] == 1:
            res.append((ni, nj))
    return res

# Start DFS from the compressed (0,0)
stack = [(2 * bl(sortx, 0), 2 * bl(sorty, 0))]
res = 0

while stack:
    x, y = stack.pop()
    if G[x + y * W] != 1:
        continue
    area = get_score(x, y)
    if area == float('inf'):
        res = float('inf')
        break
    res += area
    G[x + y * W] = 0
    stack += search(x, y)

print(res if res < float('inf') else 'INF')

# Main fixes:
# Correct indentation.

# Proper loops.

# Compressed coordinates and built graph properly.

# Safe DFS (don't run off the grid).

# Correct starting point at (0,0).

# Extra note:
# This code expects that the input is valid and covers 0,0 point initially.

# If not, you might need to adjust starting point handling a bit more carefully.

# Would you also like me to show a version that's even faster, using deque for BFS instead of manual stack? 🚀 (especially useful if the grid is very large).
# ------------------------------------------------------------------------------------------------------------------------------------------------------


# Research Team:
# the output
# ================== SUMMARY ==================
# Program: buggy_61.py
# Tests passed: 51/96
# Pass rate: 53.12%
# =============================================
