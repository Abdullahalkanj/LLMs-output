## List Of Bugs

---

### **B1 – `main()` method**

**Description:** Uses a manual flag `b` to alternate between `lst1` and `lst2` instead of Python slicing with `V[::2]` and `V[1::2]`.
**Buggy Code Line / Issue:**

```python
b = True
lst1 = []
lst2 = []
for e in lst:
    if b:
        lst1.append(e)
    else:
        lst2.append(e)
    b = not b
```

**Detected by Test(s):** `ALL`

---

### **B2 – `main()` method**

**Description:** Reimplements frequency counting manually with sorting and loops instead of using `collections.Counter().most_common()`.
**Buggy Code Line / Issue:**

```python
lst1.sort()
...
a = 0
for e in lst1:
    ...
```

**Detected by Test(s):** `ALL`

---

### **B3 – `main()` method**

**Description:** Unnecessary and potentially harmful sorting of `lst1` and `lst2`, which affects ordering and frequency grouping.
**Buggy Code Line / Issue:**

```python
lst1.sort()
lst2.sort()
```

**Detected by Test(s):** `ALL`

---

### **B4 – `main()` method**

**Description:** Faulty logic when comparing element frequencies, resulting in incorrect tracking of most and second-most frequent elements.
**Buggy Code Line / Issue:**

```python
if a >= c1[0][1]:
    c1[1] = c1[0][1]
    ...
elif a > c1[1]:
    ...
```

**Detected by Test(s):** `ALL`

---

### **B5 – `main()` method**

**Description:** Fails to handle the case when only one unique element exists in either half; missing fallback return `n // 2`.
**Buggy Code Line / Issue:**

```python
# missing this:
if len(ac) == 1 or len(bc) == 1:
    return n // 2
```

**Detected by Test(s):** Likely triggered in `sample2_iw.txt`, `sample3_1111.txt`, `zoro_0.txt`, etc., and contributes to all failures.

---

### **B6 – `main()` method**

**Description:** Broken logic when both sides have the same most frequent element — the fallback logic for second-best combo is incorrect.
**Buggy Code Line / Issue:**

```python
if c1[0][0] == c2[0][0]:
    if c1[0][1] > c2[0][1] or ...:
        ...
    else:
        ...
```

**Detected by Test(s):** Likely triggered in `top2modoki_*.txt`, `vary_*.txt`, `ababa_*.txt` — contributes to all failures.

---

### **B7 – `main()` method**

**Description:** Risk of crashing when `lst1` or `lst2` is empty, due to direct access like `lst1[0]`.
**Buggy Code Line / Issue:**

```python
last = lst1[0]
...
last = lst2[0]
```

**Detected by Test(s):** Highly likely in `sample2_iw.txt` and possibly others where one half may be empty.

---