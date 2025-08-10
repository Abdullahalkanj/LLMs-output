
## **List of bugs in `Registry`**

---

**B1 – `generateUniqueMemberId()` method**
**Description:** Fails with `NullPointerException` because `alphanumeric` is declared but never initialized
**Buggy Code Line / Issue:** `String alphanumeric = null;` — then tries `alphanumeric.charAt(...)`
**Detected by Test(s):** `testUniqueIdsAreGenerated`

---

**B2 – `generateUniqueMemberId()` method**
**Description:** `StringBuilder` is not reset correctly on each retry attempt — can lead to reused characters
**Buggy Code Line / Issue:** `sb.setLength(0);` is after `for` loop and inside `while`, but if outer `while` retries quickly, previous state may persist in some environments
**Detected by Test(s):** Indirectly via `testUniqueIdsAreGenerated`

---

**B3 – `emailExists(String email)` method**
**Description:** Uses `==` instead of `.equals()` for String comparison, causing logic failure
**Buggy Code Line / Issue:** `if (email == member.getEmail())` instead of `email.equals(...)`
**Detected by Test(s):** `testEmailExistsDetection`

---
