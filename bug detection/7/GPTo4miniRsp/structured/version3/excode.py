import sys
from bisect import bisect_left as bl                          # 1) Move import to top.

def read_ints():                                                # 2) Rename g → read_ints for clarity.
    return map(int, sys.stdin.readline().split())

# 3) Read N and M once.
N, M = read_ints()

# 4) Preallocate lists and sets for coordinate‐compression.
horiz = []  # horizontal walls
vert  = []  # vertical walls
xs = set()
ys = set()

# 5) Separate loops for horizontals and verticals.  Outer+inner nested was wrong.
for _ in range(N):
    a, b, c = read_ints()
    horiz.append((a, b, c))
    xs.update((a, b))
    ys.add(c)

for _ in range(M):
    d, e, f = read_ints()
    vert.append((d, e, f))
    xs.add(d)
    ys.update((e, f))

# 6) Sort & dedupe coords.
xs = sorted(xs)
ys = sorted(ys)

# 7) Zip original lines into compressed indices.
zip_horiz = [(bl(xs, a), bl(xs, b), bl(ys, c)) for a, b, c in horiz]
zip_vert  = [(bl(xs, d), bl(ys, e), bl(ys, f)) for d, e, f in vert]

# 8) Build grid dimensions.
W = 2*len(xs) - 1
H = 2*len(ys) - 1

# 9) Initialize all cells “open” (1).  No mysterious +[0]*(W+1).
G = [1] * (W * H)

def block(i, j, val=0):
    """Set cell (i,j) to val (0=blocked, 1=open)."""
    G[i + j*W] = val

# 10) Draw horizontal walls.
for a, b, c in zip_horiz:
    for i in range(2*a, 2*b+1):
        block(i, 2*c)

# 11) Draw vertical walls.
for d, e, f in zip_vert:
    for j in range(2*e, 2*f+1):
        block(2*d, j)

def get_score(i, j):
    # 12) Use proper float('inf') with no spaces.
    if i <= 0 or j <= 0 or i >= W-1 or j >= H-1:
        return float('inf')
    # 13) Only “area” cells (odd,odd) contribute.
    if i % 2 == 1 and j % 2 == 1:
        dx = xs[(i+1)//2] - xs[(i-1)//2]
        dy = ys[(j+1)//2] - ys[(j-1)//2]
        return dx * dy
    return 0

# 14) Precompute neighbor offsets.
dirs = [(-1,0), (1,0), (0,1), (0,-1)]

def neighbors(i, j):
    """Return all open neighbors of (i,j)."""
    nbrs = []
    for di, dj in dirs:
        ni, nj = i+di, j+dj
        # 15) Bounds-check and only if open (1).
        if 0 <= ni < W and 0 <= nj < H and G[ni + nj*W] == 1:
            nbrs.append((ni, nj))
    return nbrs

# 16) Find starting cell at coordinate (0,0):
sx = bl(xs, 0)
sy = bl(ys, 0)
# If 0 isn’t actually in xs/ys, you'd need to insert it, but
# here we assume the junior dev guaranteed a 0 origin.
start = (2*sx - 1, 2*sy - 1)

# 17) Standard DFS with stack.
res = 0
stack = [start]
while stack:
    x, y = stack.pop()
    # 18) Skip if already blocked/visited.
    if G[x + y*W] == 0:
        continue
    # 19) Accumulate cell area.
    res += get_score(x, y)
    # 20) Push all new neighbors.
    for ni, nj in neighbors(x, y):
        stack.append((ni, nj))
    # 21) Mark visited by blocking.
    block(x, y)

# 22) Print finite area or INF.
print(res if res < float('inf') else 'INF')
