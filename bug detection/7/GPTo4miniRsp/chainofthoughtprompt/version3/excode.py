from bisect import bisect_left
import sys
sys.setrecursionlimit(10**7)

def read_ints():
    return map(int, sys.stdin.readline().split())

def main():
    N, M = read_ints()

    # 1) Read input segments
    horiz = []  # (x1, x2, y)
    vert  = []  # (x, y1, y2)
    xs = set([0])
    ys = set([0])

    for _ in range(N):
        x1, x2, y = read_ints()
        if x2 < x1:
            x1, x2 = x2, x1
        horiz.append((x1, x2, y))
        xs.update([x1, x2])
        ys.add(y)

    for _ in range(M):
        x, y1, y2 = read_ints()
        if y2 < y1:
            y1, y2 = y2, y1
        vert.append((x, y1, y2))
        xs.add(x)
        ys.update([y1, y2])

    # 2) Coordinate compression
    xs = sorted(xs)
    ys = sorted(ys)
    W = len(xs)*2 - 1
    H = len(ys)*2 - 1

    # 3) Mark walls on grid G (1=free, 0=wall)
    G = [1] * (W * H)

    def block(i, j):
        """ Set cell (i,j) to wall """
        G[j*W + i] = 0

    # Horizontal segments block edges along y-lines:
    for x1, x2, y in horiz:
        i1 = bisect_left(xs, x1)*2
        i2 = bisect_left(xs, x2)*2
        j  = bisect_left(ys, y)*2
        for i in range(i1, i2+1):
            block(i, j)

    # Vertical segments block edges along x-lines:
    for x, y1, y2 in vert:
        i  = bisect_left(xs, x)*2
        j1 = bisect_left(ys, y1)*2
        j2 = bisect_left(ys, y2)*2
        for j in range(j1, j2+1):
            block(i, j)

    # 4) Flood-fill from the cell containing (0,0)
    start_i = bisect_left(xs, 0)*2 - 1
    start_j = bisect_left(ys, 0)*2 - 1

    visited = [False] * (W * H)
    stack = [(start_i, start_j)]
    area = 0
    INF = float('inf')

    def cell_area(i, j):
        """ Cell-centered nodes only: both i,j odd => real cell. """
        if i <= 0 or j <= 0 or i >= W-1 or j >= H-1:
            return INF
        if i%2==1 and j%2==1:
            dx = xs[(i+1)//2] - xs[(i-1)//2]
            dy = ys[(j+1)//2] - ys[(j-1)//2]
            return dx * dy
        return 0

    # 4-direction offsets
    dirs = [(-1,0),(1,0),(0,-1),(0,1)]

    while stack:
        i,j = stack.pop()
        idx = j*W + i
        if visited[idx] or G[idx]==0:
            continue
        visited[idx] = True

        score = cell_area(i,j)
        if score == INF:
            area = INF
            break
        area += score

        for di,dj in dirs:
            ni, nj = i+di, j+dj
            if 0 <= ni < W and 0 <= nj < H:
                stack.append((ni,nj))

    # 5) Output
    if area == INF:
        print("INF")
    else:
        print(area)

if __name__ == "__main__":
    main()
