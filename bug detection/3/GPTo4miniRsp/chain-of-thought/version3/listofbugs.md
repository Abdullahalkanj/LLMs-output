### Bug List

**B1 – `sieve()` method**
**Description:** Uses `any()` instead of `all()` to determine if `n` is not divisible by any known primes, which results in **non-prime numbers being incorrectly added** to the list of primes.
**Buggy Code Line / Issue:**

```java
if (any(list_comp(n, primes))) {
```

**Detected by Test(s):**

* `test_0` – **Passes** (expected `[]`; no primes ≤ 1, bug doesn't affect outcome)
* `test_1` – **Passes** (expected `[2]`; 2 is first prime, no filtering occurs yet)
* `test_2` – **Fails** (expected `[2, 3]`; incorrect primes such as `4` may be included)
* `test_3` – **Fails** (expected `[2, 3, 5, 7]`; non-primes may be included)
* `test_4` – **Fails** (expected `[2, 3, 5, 7, 11, 13, 17, 19]`)
* `test_5` – **Fails** (expected `[2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47]`)
