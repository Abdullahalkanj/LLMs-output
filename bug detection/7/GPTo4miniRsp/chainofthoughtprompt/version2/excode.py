from bisect import bisect_left as bl

def main():
    # 1. Read inputs
    g = lambda: map(int, input().split())
    N, M = g()

    # 2. Gather raw coordinates and segments
    sortx, sorty = [], []
    y_line, x_line = [], []

    for _ in range(N):
        a, b, c = g()
        sortx.extend([a, b])
        sorty.append(c)
        y_line.append((a, b, c))

    for _ in range(M):
        d, e, f = g()
        sortx.append(d)
        sorty.extend([e, f])
        x_line.append((d, e, f))

    # 3. Coordinate‐compression
    sortx = sorted(set(sortx))
    sorty = sorted(set(sorty))

    zip_y_line = [
        (bl(sortx, a), bl(sortx, b), bl(sorty, c))
        for a, b, c in y_line
    ]
    zip_x_line = [
        (bl(sortx, d), bl(sorty, e), bl(sorty, f))
        for d, e, f in x_line
    ]

    # 4. Build the grid
    W = len(sortx)*2 - 1
    H = len(sorty)*2 - 1
    G = [1] * (W * H)     # exactly W*H cells

    def mark(i, j, val=0):
        G[i + j*W] = val

    # 5. Carve out walls
    for a, b, c in zip_y_line:
        y = 2*c
        for i in range(2*a, 2*b + 1):
            mark(i, y)

    for d, e, f in zip_x_line:
        x = 2*d
        for j in range(2*e, 2*f + 1):
            mark(x, j)

    # 6. Scoring function
    def get_score(i, j):
        # If we're on a boundary‐edge of the grid, it's unbounded:
        if i <= 0 or j <= 0 or i >= W-1 or j >= H-1:
            return float('inf')
        # Only count “cell centers” (odd,odd)
        if i % 2 == 1 and j % 2 == 1:
            dx = sortx[(i+1)//2] - sortx[(i-1)//2]
            dy = sorty[(j+1)//2] - sorty[(j-1)//2]
            return dx * dy
        return 0

    # 7. Flood‐fill
    dirs = [(-1,0),(1,0),(0,1),(0,-1)]
    def neighbors(i, j):
        result = []
        for dx, dy in dirs:
            ni, nj = i+dx, j+dy
            if 0 <= ni < W and 0 <= nj < H and G[ni + nj*W] == 1:
                result.append((ni, nj))
        return result

    # Start from the cell containing (0,0)
    sx = 2*bl(sortx, 0) - 1
    sy = 2*bl(sorty, 0) - 1

    stack = [(sx, sy)]
    total_area = 0

    while stack:
        x, y = stack.pop()
        if G[x + y*W] != 1:
            continue
        # mark visited immediately
        mark(x, y, val=0)
        score = get_score(x, y)
        if score == float('inf'):
            print("INF")
            return
        total_area += score
        stack.extend(neighbors(x, y))

    print(total_area)


if __name__ == "__main__":
    main()
