
---

## **List of bugs in `HANOI`**

---

**B1 – `hanoi()` method**
**Description:** Records an incorrect move from `start` to `helper` instead of from `start` to `end` when moving the largest disk. This violates the Tower of Hanoi rules and causes the entire sequence to be incorrect.
**Buggy Code Line / Issue:**

```java
steps.add(new Pair<Integer,Integer>(start, helper));
```

should be

```java
steps.add(new Pair<Integer,Integer>(start, end));
```

**Detected by Test(s):**

* `test_0`: expected single move is from `1 → 3`, but buggy code produces `1 → 2`
* `test_1`: expected `[[1,2],[1,3],[2,3]]`, but gets wrong middle move
* `test_2`, `test_3` – fail: complex sequences built from incorrect base cases
* `test_4`: incorrect intermediate move
* `test_5`: unusual edge case where `start == end`, still mishandled
* `test_6`: start from `3` to `1` results in wrong order
