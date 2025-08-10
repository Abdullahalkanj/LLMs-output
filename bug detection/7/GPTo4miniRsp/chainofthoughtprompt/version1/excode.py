import sys
from bisect import bisect_left

input = sys.stdin.readline

def readints():
    return map(int, input().split())

N, M = map(int, input().split())

h_segments = []  # horizontal walls (a,b,c)  x-range [a,b), at y=c
v_segments = []  # vertical walls (d,e,f)    y-range [e,f), at x=d
xs = []
ys = []

for _ in range(N):
    a, b, c = readints()
    if a > b:
        a, b = b, a
    h_segments.append((a, b, c))
    xs.extend([a, b])
    ys.append(c)

for _ in range(M):
    d, e, f = readints()
    if e > f:
        e, f = f, e
    v_segments.append((d, e, f))
    xs.append(d)
    ys.extend([e, f])

# --- padding so we can detect escape to infinity ---
# choose values strictly outside given coord ranges
INF_PAD = 10**10  # sufficiently large
xs.extend([-INF_PAD, INF_PAD])
ys.extend([-INF_PAD, INF_PAD])

xs = sorted(set(xs))
ys = sorted(set(ys))

# coordinate -> compressed index
def idx_x(x): return bisect_left(xs, x)
def idx_y(y): return bisect_left(ys, y)

# compress segments
h_comp = [(idx_x(a), idx_x(b), idx_y(c)) for a,b,c in h_segments]
v_comp = [(idx_x(d), idx_y(e), idx_y(f)) for d,e,f in v_segments]

# doubled grid sizes
W = len(xs)*2 - 1
H = len(ys)*2 - 1

# grid meaning:
# even index = line (coordinate)
# odd index  = cell gap between coordinates
OPEN = 1
BLOCKED = 0
grid = [OPEN] * (W * H)

def block(i, j):
    grid[i + j*W] = BLOCKED

# lay horizontal walls
for a, b, c in h_comp:
    y = 2*c
    for i in range(2*a, 2*b + 1):
        block(i, y)

# lay vertical walls
for d, e, f in v_comp:
    x = 2*d
    for j in range(2*e, 2*f + 1):
        block(x, j)

# area lookup for cell center at odd,odd index
def cell_area(i, j):
    # if outside (shouldn't happen if we check) treat as 0
    if not (0 <= i < W and 0 <= j < H):
        return 0
    if (i & 1) and (j & 1):
        # cell between coords
        xi = (i-1)//2
        xj = (i+1)//2
        yi = (j-1)//2
        yj = (j+1)//2
        dx = xs[xj] - xs[xi]
        dy = ys[yj] - ys[yi]
        return dx * dy
    return 0

# find start cell containing (0,0)
sx = idx_x(0)
sy = idx_y(0)

# sx is index of coord >=0; if xs[sx] == 0:
# the cell immediately to its left is between sx-1 and sx
# choose the odd index representing that gap.
# We must be sure sx>0 and sy>0 because we padded with -INF_PAD.
start_i = 2*sx - 1
start_j = 2*sy - 1

# flood fill
stack = [(start_i, start_j)]
area = 0
escaped = False

while stack:
    i,j = stack.pop()
    if not (0 <= i < W and 0 <= j < H):
        # walked off allocated grid; treat as escape
        escaped = True
        continue
    idx = i + j*W
    if grid[idx] != OPEN:
        continue
    grid[idx] = BLOCKED  # mark visited

    # if we are in the outermost frame cell that touches ±INF_PAD coords,
    # we consider that escaped. Outer frame corresponds to cells adjacent to index 0 or last.
    if i <= 0 or j <= 0 or i >= W-1 or j >= H-1:
        escaped = True

    area += cell_area(i,j)

    # neighbors
    if i > 0:   # left
        if grid[idx-1] == OPEN: stack.append((i-1,j))
    if i+1 < W: # right
        if grid[idx+1] == OPEN: stack.append((i+1,j))
    if j > 0:   # up
        if grid[idx-W] == OPEN: stack.append((i,j-1))
    if j+1 < H: # down
        if grid[idx+W] == OPEN: stack.append((i,j+1))

print("INF" if escaped else area)
