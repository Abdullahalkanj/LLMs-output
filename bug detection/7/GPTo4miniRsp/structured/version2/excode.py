from bisect import bisect_left

# ---------- Input ----------
def read_ints():
    return list(map(int, input().split()))

N, M = read_ints()

h_segs = []  # horizontal: (x1, x2, y)
v_segs = []  # vertical: (x, y1, y2)
xs = set()
ys = set()

for _ in range(N):
    a, b, c = read_ints()
    if b < a:  # normalize
        a, b = b, a
    h_segs.append((a, b, c))
    xs.add(a); xs.add(b)
    ys.add(c)

for _ in range(M):
    d, e, f = read_ints()
    if f < e:
        e, f = f, e
    v_segs.append((d, e, f))
    xs.add(d)
    ys.add(e); ys.add(f)

# Make sure origin is represented so we can locate start cell.
xs.add(0)
ys.add(0)

# Sort & index maps
xs = sorted(xs)
ys = sorted(ys)
xi = {x:i for i,x in enumerate(xs)}
yi = {y:i for i,y in enumerate(ys)}

# Grid doubling: cells live on odd coordinates, edges on even.
# For L unique coords we have (L-1) intervals; with doubling: size = 2*L-1
W = 2*len(xs) - 1
H = 2*len(ys) - 1

# State grids
# passable[i][j] == True means you can step *onto* that lattice point/cell;
# We'll instead track visited cells; walls represented by blocking movement between neighbors.
# Because we move in 4 dirs, it's easiest to pre-build a wall grid:
blocked = [[False]*H for _ in range(W)]  # we'll only set for edge locations
# NOTE: cells are (odd,odd); vertical edges (even,odd); horizontal edges (odd,even)

# Mark horizontal walls
for x1,x2,y in h_segs:
    yi_c = yi[y]
    j = 2*yi_c  # edge row (even)
    # edges span from compressed x1 to x2-1 across cells; but in doubled grid we mark all between
    i1 = 2*xi[x1]
    i2 = 2*xi[x2]
    # include entire closed span: mark all edge slots between those compressed positions
    for i in range(min(i1,i2), max(i1,i2)+1):
        blocked[i][j] = True

# Mark vertical walls
for x,y1,y2 in v_segs:
    xi_c = xi[x]
    i = 2*xi_c  # edge column (even)
    j1 = 2*yi[y1]
    j2 = 2*yi[y2]
    for j in range(min(j1,j2), max(j1,j2)+1):
        blocked[i][j] = True

# Helper: get area of a cell at (i,j); only valid for odd,odd
def cell_area(i,j):
    # convert doubled index back to coordinate interval
    x_idx = (i-1)//2
    y_idx = (j-1)//2
    dx = xs[x_idx+1] - xs[x_idx]
    dy = ys[y_idx+1] - ys[y_idx]
    return dx*dy

# Movement test: from cell (i,j) to neighbor (ni,nj) crosses an edge at midpoint.
def can_move(i,j,ni,nj):
    mid_i = (i+ni)//2
    mid_j = (j+nj)//2
    return not blocked[mid_i][mid_j]

# Locate start cell containing origin (0,0).
# If origin lies exactly on a wall line, the "cell" below/left is used.
xo = bisect_left(xs, 0)
yo = bisect_left(ys, 0)
# If 0 is not last coord, we start in interval before next coord; otherwise region unbounded
if xo == len(xs) or xs[xo] != 0:
    # insertion index -> cell to the left; so xo-1 is lower bound
    xo -= 1
if yo == len(ys) or ys[yo] != 0:
    yo -= 1
if xo < 0 or yo < 0 or xo >= len(xs)-1 or yo >= len(ys)-1:
    # no enclosed cell in compressed grid -> definitely infinite
    print("INF")
    raise SystemExit

start_i = 2*xo + 1  # odd
start_j = 2*yo + 1  # odd

# Flood fill
visited = [[False]*H for _ in range(W)]
stack = [(start_i,start_j)]
area = 0
touches_border = False

dirs = [(1,0),(-1,0),(0,1),(0,-1)]

while stack:
    i,j = stack.pop()
    if visited[i][j]:
        continue
    visited[i][j] = True

    # If this cell lies on exterior frame, region may be unbounded beyond; mark.
    # Border cells correspond to i==1? Actually cell indices run 1..W-2 step2.
    # If i==W-2 or j==H-2 reachable and outside no wall, we are at bounding box boundary.
    if i <= 0 or j <= 0 or i >= W-1 or j >= H-1:
        touches_border = True
    # Only add area for proper cells (odd,odd) inside bounds
    if 0 < i < W-1 and 0 < j < H-1 and i%2==1 and j%2==1:
        area += cell_area(i,j)

    for dx,dy in dirs:
        ni = i+dx
        nj = j+dy
        if 0 <= ni < W and 0 <= nj < H and not visited[ni][nj]:
            if can_move(i,j,ni,nj):
                stack.append((ni,nj))

print("INF" if touches_border else area)
