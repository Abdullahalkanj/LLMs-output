from bisect import bisect_left as bl

# Fast input
g = lambda: map(int, input().split())

# Read N (horizontal segments) and M (vertical segments)
N, M = g()

# We'll collect all x- and y-coordinates for compression:
xs, ys = [], []
horiz_segments = []
vert_segments  = []

# Read horizontal ("y_line") segments: given as (x1, x2, y)
for _ in range(N):
    x1, x2, y = g()
    xs += [x1, x2]
    ys.append(y)
    horiz_segments.append((x1, x2, y))

# Read vertical ("x_line") segments: given as (x, y1, y2)
for _ in range(M):
    x, y1, y2 = g()
    xs.append(x)
    ys += [y1, y2]
    vert_segments.append((x, y1, y2))

# Deduplicate and sort for coordinate compression
xs = sorted(set(xs))
ys = sorted(set(ys))

# Remap all segments into compressed grid coordinates
horiz = []
for x1, x2, y in horiz_segments:
    i1 = bl(xs, x1)
    i2 = bl(xs, x2)
    j  = bl(ys, y)
    horiz.append((i1, i2, j))

vert = []
for x, y1, y2 in vert_segments:
    i = bl(xs, x)
    j1 = bl(ys, y1)
    j2 = bl(ys, y2)
    vert.append((i, j1, j2))

# Grid dimensions in the *expanded* graph:
W = len(xs)*2 - 1
H = len(ys)*2 - 1

# G will be a flat list of size W*H; 1 = open, 0 = wall/visited
G = [1] * (W * H)

def set_cell(i, j, val):
    """Set G[j][i] to val (flat-index helper)."""
    G[i + j*W] = val

# Block out the edges of every horizontal segment:
for i1, i2, j in horiz:
    row = 2*j
    for i in range(2*i1, 2*i2 + 1):
        set_cell(i, row, 0)

# Block out the edges of every vertical segment:
for i, j1, j2 in vert:
    col = 2*i
    for j in range(2*j1, 2*j2 + 1):
        set_cell(col, j, 0)

# Precompute the four neighbor offsets:
dirs = [(-1,0),(1,0),(0,1),(0,-1)]

def in_bounds(i, j):
    return 0 <= i < W and 0 <= j < H

def get_score(i, j):
    """Return the area of the cell if (i,j) is a 'face'-center, else 0 or inf outside."""
    if not in_bounds(i, j):
        return float('inf')
    # Only face-centers (odd,odd) contribute:
    if (i % 2 == 1) and (j % 2 == 1):
        dx = xs[(i+1)//2] - xs[(i-1)//2]
        dy = ys[(j+1)//2] - ys[(j-1)//2]
        return dx * dy
    return 0

def neighbors(i, j):
    """Return all open neighbor cells (no premature return!)."""
    result = []
    for di, dj in dirs:
        ni, nj = i+di, j+dj
        if in_bounds(ni, nj) and G[ni + nj*W] == 1:
            result.append((ni, nj))
    return result

def flood_fill(start_i, start_j):
    """DFS from (start_i,start_j), summing scores, marking visited."""
    stack = [(start_i, start_j)]
    total = 0
    while stack:
        i, j = stack.pop()
        if G[i + j*W] != 1:
            continue
        # mark visited
        set_cell(i, j, 0)
        # add area if this is a face
        total += get_score(i, j)
        # push neighbors
        for ni, nj in neighbors(i, j):
            stack.append((ni, nj))
    return total

# Compute the insertion point of 0 in compressed coords.
# If 0 isn't present, we find the closest segment.
si = bl(xs, 0)
sj = bl(ys, 0)
# Map to graph coords
start_i = 2*si
start_j = 2*sj
# If that lands on a wall, we step one back
if not in_bounds(start_i, start_j) or G[start_i + start_j*W] == 0:
    start_i = max(0, min(W-1, start_i-1))
    start_j = max(0, min(H-1, start_j-1))

res = flood_fill(start_i, start_j)

print(res if res < float('inf') else 'INF')
