from bisect import bisect_left as bl
import sys

def read_ints():
    return map(int, sys.stdin.readline().split())

# Read N (horizontal walls) and M (vertical walls)
N, M = read_ints()

sortx, sorty = [], []
y_line, x_line = [], []

# Horizontal segments: y is constant, spans from x=a to x=b at y=c
for _ in range(N):
    a, b, c = read_ints()
    sortx.extend([a, b])
    sorty.append(c)
    y_line.append((a, b, c))

# Vertical segments: x is constant, spans from y=e to y=f at x=d
for _ in range(M):
    d, e, f = read_ints()
    sortx.append(d)
    sorty.extend([e, f])
    x_line.append((d, e, f))

# Coordinate‐compress
sortx = sorted(set(sortx))
sorty = sorted(set(sorty))

zip_y_line = []
for a, b, c in y_line:
    ai = bl(sortx, a)
    bi = bl(sortx, b)
    ci = bl(sorty, c)
    zip_y_line.append((ai, bi, ci))

zip_x_line = []
for d, e, f in x_line:
    di = bl(sortx, d)
    ei = bl(sorty, e)
    fi = bl(sorty, f)
    zip_x_line.append((di, ei, fi))

# Build grid dimensions
W = len(sortx) * 2 - 1
H = len(sorty) * 2 - 1

# G[k] == 1 means free cell/edge, 0 means blocked
G = [1] * (W * H)

def make_graph(i, j, s=0):
    """Set G[i,j] = s (0=block, 1=free)."""
    G[i + j * W] = s

# Block out all the walls in G
for a, b, c in zip_y_line:
    yj = 2 * c
    for xi in range(2 * a, 2 * b + 1):
        make_graph(xi, yj, 0)

for d, e, f in zip_x_line:
    xi = 2 * d
    for yj in range(2 * e, 2 * f + 1):
        make_graph(xi, yj, 0)

def get_score(i, j):
    # If we're on the outer border, it's infinite
    if i <= 0 or j <= 0 or i >= W - 1 or j >= H - 1:
        return float('inf')
    # Only at cell-centers (both odd indices) do we accrue area
    if i % 2 == 1 and j % 2 == 1:
        dx = sortx[(i + 1) // 2] - sortx[(i - 1) // 2]
        dy = sorty[(j + 1) // 2] - sorty[(j - 1) // 2]
        return dx * dy
    return 0

# 4-connected neighbors
dirs = [(-1, 0), (1, 0), (0, -1), (0, 1)]

def neighbors(i, j):
    res = []
    for dx, dy in dirs:
        ni, nj = i + dx, j + dy
        if 0 <= ni < W and 0 <= nj < H and G[ni + nj * W] == 1:
            res.append((ni, nj))
    return res

# Start from the cell that contains (0,0).
# We need to see where 0 would insert in sortx/sorty:
sx = bl(sortx, 0) * 2 - 1
sy = bl(sorty, 0) * 2 - 1

stack = [(sx, sy)]
total = 0

while stack:
    x, y = stack.pop()
    if G[x + y * W] != 1:
        continue
    # Mark visited so we don’t revisit
    make_graph(x, y, 0)

    # Add the area of this cell if it’s a cell-center
    score = get_score(x, y)
    if score == float('inf'):
        total = float('inf')
        break
    total += score

    # Push unvisited, free neighbors
    for ni, nj in neighbors(x, y):
        stack.append((ni, nj))

print(total if total < float('inf') else 'INF')
