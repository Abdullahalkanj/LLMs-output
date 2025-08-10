## List Of Bugs

### **B1 – Input Parsing and Line Storage**

**Description:** The buggy code incorrectly reuses loop variables and nesting, leading to misplaced `for` loops and incorrect population of `x_line` and `y_line`. It appends tuples directly and redundantly to lists instead of using indexed assignments. The indentation structure is also broken.

**Buggy Code Line / Issue:**

```python
for _ in [ 0 ] * N :
a , b , c = g ( )
    ...
    for _ in [ 0 ] * M :
d , e , f = g ( )
    ...
```

* The nested loop for M lines should be outside the N-loop.
* Uses `append` where indexed assignment is required for further reference and modification.

**Detected by Test(s):** *ALL*

---

### **B2 – `make_graph()` Function Misplacement and Usage**

**Description:** In the buggy code, `make_graph()` is called **before** any of the helper validation logic (like checking boundaries or visited status) is defined, and is misused recursively in the main loop without such checks. In the fixed version, `make_graph()` only sets a grid cell, while the recursion is handled in `search()`.

**Buggy Code Line / Issue:**

```python
def make_graph ( i , j , s = 0 ) :
G [ i + j * W ] = s

...

while stack :
    ...
    make_graph ( x , y )
```

* Buggy use of `make_graph()` acts as recursive traversal without boundary or revisit checks.
* Correct code separates traversal into `search()` and uses `make_graph()` only to set visited state.

**Detected by Test(s):** *ALL*

---

### **B3 – `G` Grid Initialization Overflow**

**Description:** In the buggy code, the grid `G` is initialized with more elements than needed by adding an extra row (`+ [0] * (W + 1)`), which causes index mismatch and unnecessary space allocation. The fixed version correctly allocates `W * H` elements only.

**Buggy Code Line / Issue:**

```python
G = [ 1 ] * W * H + [ 0 ] * ( W + 1 )
```

* Causes indexing issues when computing positions like `x + y * W`.
* May lead to silent logic bugs or false positives in edge cases.

**Detected by Test(s):** *ALL*

---

### **B4 – `search()` Function Lacks Boundary Checks**

**Description:** In the buggy version, the `search()` function directly accesses neighbors in the grid without verifying bounds, leading to potential index errors or incorrect behavior. The fixed version uses `isOK()` and `isSoto()` to ensure bounds and traversal logic are correct.

**Buggy Code Line / Issue:**

```python
def search ( i , j ) :
    res = [ ]
    for dx , dy in dic :
        if G [ ( i + dx ) + ( j + dy ) * W ] == 1 :
            res . append ( ( i + dx , j + dy ) )
    return res
```

* No check for out-of-bound indices like `0 <= x < W` and `0 <= y < H`.
* Can cause incorrect traversal or crash.

**Detected by Test(s):** *ALL*

---

### **B5 – Incorrect Stack Initialization for Flood Fill**

**Description:** The starting point for flood fill (`stack`) is incorrectly set in the buggy version. It uses:

```python
stack = [ ( 2 * bl ( sortx , 0 ) - 1 , 2 * bl ( sorty , 0 ) - 1 ) ]
```

without checking adjacent tiles or adding diagonals like in the fixed version. This may result in missed regions or incorrect traversal start.

**Buggy Code Line / Issue:**

```python
stack = [ ( 2 * bl ( sortx , 0 ) - 1 , 2 * bl ( sorty , 0 ) - 1 ) ]
```

* No attempt to explore from neighboring outside points.
* In the fixed version:

```python
for dx , dy in [ ( -1, -1 ), ( -1, 1 ), (1, 1), (1, -1) ]:
    if isOK(x0 + dx, y0) or isOK(x0, y0 + dy):
        stack.append((x0 + dx, y0 + dy))
```

**Detected by Test(s):** *ALL*

---

### **B6 – Incorrect Loop Nesting and Indentation**

**Description:** The buggy code contains incorrect indentation, causing the loop that populates `x_line` to be **nested inside** the loop that populates `y_line`. This leads to incorrect input parsing and misassignment of segments.

**Buggy Code Line / Issue:**

```python
for _ in [ 0 ] * N :
    a , b , c = g ( )
    ...
    for _ in [ 0 ] * M :
        d , e , f = g ( )
        ...
```

* This means the M-segment data is read and appended M times for **every** N-segment.
* Causes data structure corruption and wrong geometry.

**Detected by Test(s):** *ALL*

---

### **B7 – Unused and Incorrect Duplicate Zip Lists**

**Description:** The buggy code creates new lists `zip_x_line` and `zip_y_line` instead of updating the original `x_line` and `y_line`. These zip lists are then used inconsistently and created redundantly, leading to confusion and divergence from the final processed data.

**Buggy Code Line / Issue:**

```python
zip_x_line = [ ]
zip_y_line = [ ]
...
zip_y_line.append((a, b, c))
...
zip_x_line.append((d, e, f))
```

* These lists are **used**, but in the fixed version, the original `x_line` and `y_line` lists are **updated in-place**.
* This avoids unnecessary duplication and confusion over which list is current.

**Detected by Test(s):** *ALL*

---

### **B8 – Missing Boundary Checks in `get_score()` Call**

**Description:** The buggy version calls `get_score(x, y)` **before** checking whether the cell is part of the grid or not. This may cause infinite or invalid area calculations for out-of-bounds cells.

**Buggy Code Line / Issue:**

```python
while stack :
    ...
    res += get_score(x, y)
    stack += search(x, y)
    make_graph(x, y)
```

* No `isOK()` / `isSoto()` checks like in the fixed version.
* In the fixed code:

```python
if isSoto(x, y):
    res += get_score(x, y)
    break
if not isOK(x, y):
    continue
```

* This ensures `get_score()` is only called on valid or designated external tiles.

**Detected by Test(s):** *ALL*

---

### **B9 – Incorrect Flood Fill Termination Condition**

**Description:** The buggy code continues the flood fill even after detecting an out-of-bounds (external) cell, whereas the correct logic **terminates immediately** once a border (external face) is detected.

**Buggy Code Line / Issue:**

```python
while stack :
    ...
    if G [ x + y * W ] != 1 :
        continue
    ...
    res += get_score(x, y)
    ...
```

* Fails to **break** early when an outer area is reached.
* In the fixed version:

```python
if isSoto(x, y):
    res += get_score(x, y)
    break
```

* This distinction prevents overcounting and ensures the correct "infinite" case is recognized.

**Detected by Test(s):** *ALL*

---

### **B10 – Incorrect Grid Cell Coordinate Mapping Logic**

**Description:** The coordinate mapping of input values to grid indices is broken in the buggy version due to inconsistent and misaligned application of `bisect_left` (`bl`) on the values and lack of structured index updates. The correct code uses in-place updates with `bl(...)` in a structured, looped assignment.

**Buggy Code Line / Issue:**

```python
for a , b , c in y_line :
    a = bl ( sortx , a )
    b = bl ( sortx , b )
    c = bl ( sorty , c )
    zip_y_line . append ( ( a , b , c ) )

for d , e , f in x_line :
    d = bl ( sortx , d )
    e = bl ( sorty , e )
    f = bl ( sorty , f )
    zip_x_line .append ( ( d , e , f ) )
```

* Doesn't update original `x_line` and `y_line` structures, leading to mismatches.
* The fixed version does:

```python
for i in range(N):
    a, b, c = y_line[i]
    ...
    y_line[i] = (a, b, c)
```

* Ensures that the transformed coordinates are used consistently in later steps.

**Detected by Test(s):** *ALL*

---






