# Claude output using basic prompt


# ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
# I'll analyze and fix the bug in this code. Let's go through it step by step.
# After examining the code, I've found the main issue: there's an indentation problem causing some for loops to be improperly nested. This affects how the code processes the input data and builds the graph.
# Here's the fixed version:

g = lambda : map(int, input().split())
N, M = g()
sortx = []
sorty = []
y_line = []
x_line = []
for _ in [0] * N:
    a, b, c = g()
    sortx.append(a)
    sortx.append(b)
    sorty.append(c)
    y_line.append((a, b, c))
for _ in [0] * M:
    d, e, f = g()
    sortx.append(d)
    sorty.append(e)
    sorty.append(f)
    x_line.append((d, e, f))
from bisect import bisect_left as bl
sortx = sorted(list(set(sortx)))
sorty = sorted(list(set(sorty)))
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
G = [1] * (W * H) + [0] * (W + 1)
def make_graph(i, j, s=0):
    G[i + j * W] = s
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
        if G[(i + dx) + (j + dy) * W] == 1:
            res.append((i + dx, j + dy))
    return res
stack = [(2 * bl(sortx, 0) - 1, 2 * bl(sorty, 0) - 1)]
res = 0
while stack:
    x, y = stack.pop()
    if G[x + y * W] != 1:
        continue
    res += get_score(x, y)
    stack += search(x, y)
    make_graph(x, y)
print(res if res < float('inf') else 'INF')

# The key fixes I made:

# Fixed indentation for all loops - especially the second for-loop for reading M lines of input, which was incorrectly indented in the original code.
# Fixed the indentation of the for-loops that apply make_graph to zip_y_line and zip_x_line. These were incorrectly nested inside the make_graph function in the original code.
# Ensured proper parentheses around W * H in the initialization of G to make array size calculation clearer.
# Kept consistent indentation throughout the code (4 spaces per level) to improve readability.

# These fixes should resolve the execution flow issues in the original code, allowing it to properly process the input, build the graph, and compute the final result.
# ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


# Research Team:
# ================== SUMMARY ==================
# Program: buggy_61.py
# Tests passed: 81/96
# Pass rate: 84.38%
# =============================================