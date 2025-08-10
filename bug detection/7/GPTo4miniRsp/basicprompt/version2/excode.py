from bisect import bisect_left as bl

g = lambda: map(int, input().split())
N, M = g()

sortx, sorty = [], []
y_line, x_line = [], []

# read first N lines
for _ in range(N):
    a, b, c = g()
    sortx += [a, b]
    sorty.append(c)
    y_line.append((a, b, c))

# read next M lines
for _ in range(M):
    d, e, f = g()
    sortx.append(d)
    sorty += [e, f]
    x_line.append((d, e, f))

# dedupe & sort coordinates
sortx = sorted(set(sortx))
sorty = sorted(set(sorty))

# remap to compressed indices
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

W = len(sortx) * 2 - 1
H = len(sorty) * 2 - 1

# initialize grid G: 1=wall, 0=open
G = [1] * (W * H)

def make_graph(i, j, s=0):
    G[i + j*W] = s

# carve out walls/lines
for a, b, c in zip_y_line:
    for i in range(2*a, 2*b + 1):
        make_graph(i, 2*c, 0)

for d, e, f in zip_x_line:
    for j in range(2*e, 2*f + 1):
        make_graph(2*d, j, 0)

def get_score(i, j):
    if i<=0 or j<=0 or i>=W-1 or j>=H-1:
        return float('inf')
    if i%2==1 and j%2==1:
        dx = sortx[(i+1)//2] - sortx[(i-1)//2]
        dy = sorty[(j+1)//2] - sorty[(j-1)//2]
        return dx * dy
    return 0

dic = [(-1,0),(1,0),(0,1),(0,-1)]

def search(i, j):
    res = []
    for dx, dy in dic:
        ni, nj = i+dx, j+dy
        if 0 <= ni < W and 0 <= nj < H and G[ni + nj*W] == 1:
            res.append((ni, nj))
    return res

# start at (0,0) compressed
start_x = 2*bl(sortx, 0) - 1
start_y = 2*bl(sorty, 0) - 1

stack = [(start_x, start_y)]
res = 0

while stack:
    x, y = stack.pop()
    if G[x + y*W] != 1:
        continue
    res += get_score(x, y)
    for nbr in search(x, y):
        stack.append(nbr)
    make_graph(x, y, 0)

print(res if res < float('inf') else 'INF')
