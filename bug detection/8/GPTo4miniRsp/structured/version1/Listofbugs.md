## List of Bugs

---

**B1 – Input Graph Representation (`Cost`)**
**Description:** `Cost` is initialized as a 3D matrix; inefficient and incorrect for representing a sparse graph.
**Buggy Code Line / Issue:**

```python
Cost = [ [ [ -1 , -1 ] for _ in range ( N ) ] for _ in range ( N ) ]
Cost[u-1][v-1][0] = a
Cost[u-1][v-1][1] = b
```

**Detected by Test(s):** Likely **all** tests involving any connected graph (`random_*.txt`, `sample_*.txt`, `line_*.txt`)

---

**B2 – Recharge Loop Misplacement**
**Description:** Recharge inputs are read **inside** the edge input loop, so values are parsed too many times or overwritten incorrectly.
**Buggy Code Line / Issue:**

```python
for i in range(M):
    ...
    for i in range(N):  # Misplaced
        c, d = ...
```

**Detected by Test(s):** All tests requiring recharge logic (most `random_*.txt`, likely `sample_03.txt`, `sample_04.txt`)

---

**B3 – Incorrect Fuel Cap Limiting Condition Placement**
**Description:** Fuel limit `S` is clamped **inside** recharge input loop instead of globally.
**Buggy Code Line / Issue:**

```python
if S > A_m * N:
    S = A_m * N  # Indented wrong
```

**Detected by Test(s):** Any test where `S` starts large — likely `random_*.txt`, `sample_05.txt`

---

**B4 – Missing `rich[]` Pruning**
**Description:** Without `rich`, same node-fuel combinations may be revisited redundantly, causing inefficiency or incorrect early exits.
**Buggy Code Line / Issue:**
Missing:

```python
if rich[current] and rich[current][-1] >= hold:
    continue
```

**Detected by Test(s):** All tests with long or multiple paths — `random_*.txt`, `line_2.txt`, `sample_04.txt`

---

**B5 – Looping Over All Nodes Instead of Neighbors**
**Description:** Dijkstra loops over all `N` nodes, not just connected neighbors, causing incorrect logic.
**Buggy Code Line / Issue:**

```python
for road in range(N):
    if Cost[current][road][0] != -1:
```

**Detected by Test(s):** All functional tests with disconnected nodes — `random_*.txt`, `sample_01.txt`, `sample_03.txt`

---

**B6 – Incorrect `INF` Value**
**Description:** Uses `10^12` instead of `10^18`, which may cause comparison issues for long paths.
**Buggy Code Line / Issue:**

```python
Ans = [10 ** 12] * N
used = [[10 ** 12] * ... ]
```

**Detected by Test(s):** Any test with high-cost paths — `random_10.txt`, `random_17.txt`, `sample_02.txt`

---

**B7 – Faulty Early Exit Condition in Dijkstra**
**Description:** Faulty logic for early stopping; breaks prematurely if all Ans[i] > c is false.
**Buggy Code Line / Issue:**

```python
j = 1
for i in range(N):
    if Ans[i] > c:
        j = 0
        break
if j == 1:
    break
```

**Detected by Test(s):** Multi-destination tests or where delays matter — `random_05.txt`, `random_09.txt`, `sample_04.txt`

---

**B8 – Missing `used[][]` Update When Recharging**
**Description:** After pushing a recharged state to the heap, the cost is not recorded in `used[][]`, leading to repeated and inefficient state exploration.
**Buggy Code Line / Issue:**

```python
if hold + Change[current][0] <= A_m * N:
    if used[current][hold + Change[current][0]] > c + Change[current][1]:
        heapq.heappush(minHeap, (c + Change[current][1], hold + Change[current][0], current))
        # Missing: used[][] update
```

**Detected by Test(s):** All tests requiring recharge optimization — `random_01.txt`, `random_08.txt`, `sample_03.txt`

---
