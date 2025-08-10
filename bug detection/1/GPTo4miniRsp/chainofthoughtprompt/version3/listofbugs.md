##  ** List of bugs in  `MemberManager`**

---

**B1 – `createMember()` method**
**Description:** Creates an `Item` instead of a `Member` and returns `null`
**Buggy Code Line / Issue:** `Item newMember = ...` and `return null`
**Detected by Test(s):** `testCreateMember_success`

---

**B2 – `generateRandomAlphanumericString()` method**
**Description:** Loop runs `0` times (never builds string), and no character appended
**Buggy Code Line / Issue:** `for (int i = 0; i < 0; i++)`, missing `sb.append(...)`
**Detected by Test(s):** Indirectly via `testCreateMember_success` (member ID will be blank)

---

**B2.1 – `generateRandomAlphanumericString()` method**
**Description:** Generates `random.nextInt(...)` but doesn't use the index
**Buggy Code Line / Issue:** `int index = random.nextInt(...)` without `sb.append(...)`
**Detected by Test(s):** Indirectly by same test

---

**B3 – `deleteMember()` method**
**Description:** Deletes member when ID is `null`, and always returns `true`
**Buggy Code Line / Issue:** `if (memberId == null) { members.remove...; return true; }` and final `return true`
**Detected by Test(s):** `testDeleteMember_validId`, `testDeleteMember_nullId`

---

**B4 – `isEmailUnique()` method**
**Description:** Compares `email` to `member.getName()` instead of `getEmail()`, and always returns `false`
**Buggy Code Line / Issue:** `member.getName().equalsIgnoreCase(email)`, final `return false;`
**Detected by Test(s):** `testIsEmailUnique_true`

---

**B5 – `isPhoneNumberUnique()` method**
**Description:** Reversed logic: returns `true` if number **exists**, and always returns `true`
**Buggy Code Line / Issue:** `if (...equals(...)) return true;`
**Detected by Test(s):** `testIsPhoneNumberUnique_false`

---

**B6 – `listMemberEmailId()` method**
**Description:** Incorrect logic: uses `memberDetailsList.remove(...)` instead of `.add(...)`, so list stays empty
**Buggy Code Line / Issue:** `memberDetailsList.remove(detail)`
**Detected by Test(s):** `testListMemberEmailId_contentsMatch`

---

**B7 – `doesEmailExist()` method**
**Description:** Returns `false` even when email is found (logic inverted)
**Buggy Code Line / Issue:** `if (...) return false;` and final `return false;`
**Detected by Test(s):** `testDoesEmailExist_true`

---

**B8 – `updateMemberInformation()` method**
**Description:** Logic inside `if (oldMember == null)` is incorrect; should only proceed if `oldMember != null`
**Buggy Code Line / Issue:** Whole logic block mistakenly under `if (oldMember == null)`
**Detected by Test(s):** `testUpdateMemberInformation_success`, `testUpdateMemberInformation_emptyFields`, `testUpdateMemberInformation_invalidId`

---

**B8.1 – `updateMemberInformation()` method**
**Description:** Always returns `false` even when update succeeds
**Buggy Code Line / Issue:** Final `return false`
**Detected by Test(s):** Same tests as B8

---

**B8.2 – `updateMemberInformation()` method**
**Description:** Logic misplacement: moves values from `oldMember` to `newMember`, but places under wrong condition
**Buggy Code Line / Issue:** `if (oldMember == null)` instead of `if (oldMember != null)`
**Detected by Test(s):** Same tests as B8

---

**B9 – `itemExists()` method**
**Description:** Returns `false` when item is found (logic inverted)
**Buggy Code / Line:** `return false;` instead of `return true;`
**Detected by Test(s):** `testItemExists_true`

---

**B10 – `itemIsAvailable()` method**
**Description:** Returns `false` when item is available (logic inverted)
**Buggy Code / Line:** `return false` in match case
**Detected by Test(s):** `testItemIsAvailable_true`, `testItemIsAvailable_false_unavailableRange`

---

**B11 – `borrowerHasEnoughCredits()` method**
**Description:** Incorrect cost calculation: `(endDate + startDate + 1) + item.getCostPerDay()`
**Buggy Code / Line:** Wrong formula used
**Detected by Test(s):** `testBorrowerHasEnoughCredits_true`, `testBorrowerHasEnoughCredits_false`

---

**B12 – `getAllMembersVerboseDetails()` method**
**Description:** Prints `member.getCredits()` instead of name, and `member.getName()` instead of email
**Buggy Code / Line:** `details.append("Name: ").append(member.getCredits())...`
**Detected by Test(s):** `testGetAllMembersVerboseDetails_containsExpectedData`

---

**B13 – `getAllMembersVerboseDetails()` method**
**Description:** Fails `itemContracts != null && !itemContracts.isEmpty()` due to wrong condition
**Buggy Code / Line:** `if (itemContracts == null && !itemContracts.isEmpty())`
**Detected by Test(s):** Same test as B12

---

**B14 – `getAllItemsDetails()` method**
**Description:** Uses wrong fields: prints `item.getContracts()` instead of `item.getDescription()`
**Buggy Code / Line:** `append(... item.getContracts())`
**Detected by Test(s):** `testGetAllItemsDetails_containsItemInfo`

---

**B15 – `getAllItemsDetails()` method**
**Description:** Omits `item.getCategory()` and misuses `getDescription()` / `getContracts()`
**Buggy Code / Line:** Misordered or missing fields
**Detected by Test(s):** Same test as B14

---

**B16 – `addItemToMember()` method**
**Description:** Reversed logic: proceeds when `owningMember == null`, fails always
**Buggy Code / Line:** Executes when `null`, returns `false` anyway
**Detected by Test(s):** `testAddItemToMember_success`, `testAddItemToMember_invalidMember`

---

