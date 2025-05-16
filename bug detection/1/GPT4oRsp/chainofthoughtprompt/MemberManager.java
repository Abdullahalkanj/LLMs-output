// ChatGPT4o output using chain-of-thought prompt


// -----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
package model;

import java.util.*;

public class MemberManager {
    private Map<String, Member> members;
    private Random random;

    public MemberManager() {
        this.members = new HashMap<>();
        this.random = new Random();
        initializeData();
    }

    public Member createMember(String name, String email, String phoneNumber) {
        if (isEmailUnique(email) && isPhoneNumberUnique(phoneNumber)) {
            String uniqueId = generateUniqueId();
            Member newMember = new Member(name, email, phoneNumber, uniqueId);
            newMember.setCredits(0);
            members.put(uniqueId, newMember);
            return newMember;
        } else {
            return null;
        }
    }

    private void initializeData() {
        Member member1 = new Member("M1", "m1@example.com", "1234567890", generateUniqueId());
        member1.setCredits(300);
        members.put(member1.getId(), member1);

        Member member2 = new Member("M2", "m2@example.com", "0987654321", generateUniqueId());
        member2.setCredits(100);
        members.put(member2.getId(), member2);

        Member member3 = new Member("M3", "m3@example.com", "1122334455", generateUniqueId());
        member3.setCredits(100);
        members.put(member3.getId(), member3);
    }

    private String generateUniqueId() {
        String uniqueId;
        do {
            uniqueId = generateRandomAlphanumericString();
        } while (isIdTaken(uniqueId));
        return uniqueId;
    }

    private String generateRandomAlphanumericString() {
        String alphanumeric = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder(6);
        for (int i = 0; i < 6; i++) { // fixed loop
            int index = random.nextInt(alphanumeric.length());
            sb.append(alphanumeric.charAt(index));
        }
        return sb.toString();
    }

    private boolean isIdTaken(String id) {
        return members.containsKey(id);
    }

    public boolean deleteMember(String memberId) {
        if (memberId != null && members.containsKey(memberId)) {
            members.remove(memberId);
            return true;
        }
        return false;
    }

    public boolean isEmailUnique(String email) {
        for (Member member : members.values()) {
            if (member.getEmail().equalsIgnoreCase(email)) {
                return false;
            }
        }
        return true;
    }

    public boolean isPhoneNumberUnique(String phoneNumber) {
        for (Member member : members.values()) {
            if (member.getMobilePhone().equals(phoneNumber)) {
                return false;
            }
        }
        return true;
    }

    public Map<String, Member> listAllMembers() {
        return new HashMap<>(members);
    }

    public Member getMemberById(String id) {
        return members.get(id);
    }

    public List<String> listMemberEmailId() {
        List<String> memberDetailsList = new ArrayList<>();
        for (Member member : members.values()) {
            String detail = "Name: " + member.getName() + ", Email: " + member.getEmail() + ", ID: " + member.getId();
            memberDetailsList.add(detail); // fixed from remove -> add
        }
        return memberDetailsList;
    }

    public String getMemberDetailsById(String memberId) {
        Member selectedMember = members.get(memberId);
        if (selectedMember != null) {
            List<Item> items = selectedMember.getItems();
            String itemNames = items.stream()
                    .map(Item::getName)
                    .collect(Collectors.joining(", "));
            return "Name: " + selectedMember.getName()
                    + ", Email: " + selectedMember.getEmail()
                    + ", Current Credits: " + selectedMember.getCredits()
                    + ", Owned Items: " + itemNames;
        }
        return null;
    }

    public boolean doesEmailExist(String email) {
        for (Member member : members.values()) {
            if (member.getEmail().equalsIgnoreCase(email)) {
                return true;
            }
        }
        return false;
    }

    public List<String> listAllMembersSimple() {
        List<String> simpleMemberDetailsList = new ArrayList<>();
        for (Member member : members.values()) {
            String detail = "Name: " + member.getName()
                    + ", Email: " + member.getEmail()
                    + ", Current Credits: " + member.getCredits()
                    + ", Number of Owned Items: " + member.getItems().size();
            simpleMemberDetailsList.add(detail);
        }
        return simpleMemberDetailsList;
    }

    public List<Item> getItemsByMemberId(String memberId) {
        Member member = members.get(memberId);
        if (member != null) {
            return member.getItems();
        }
        return Collections.emptyList(); // avoid returning null
    }

    public boolean updateMemberInformation(String memberId, String newName, String newEmail, String newPhoneNumber) {
        Member oldMember = members.get(memberId);
        if (oldMember == null) {
            return false;
        }

        if (newName.isEmpty()) {
            newName = oldMember.getName();
        }
        if (newEmail.isEmpty()) {
            newEmail = oldMember.getEmail();
        }
        if (newPhoneNumber.isEmpty()) {
            newPhoneNumber = oldMember.getMobilePhone();
        }

        Member updatedMember = new Member(newName, newEmail, newPhoneNumber, memberId);
        updatedMember.setItems(oldMember.getItems());
        updatedMember.setCredits(oldMember.getCredits()); // fixed

        members.put(memberId, updatedMember);
        return true;
    }

    public String getIdByEmail(String email) {
        for (Map.Entry<String, Member> entry : members.entrySet()) {
            if (entry.getValue().getEmail().equalsIgnoreCase(email)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public boolean itemExists(String itemName) {
        for (Member member : members.values()) {
            for (Item item : member.getItems()) {
                if (item.getName().equalsIgnoreCase(itemName)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean itemIsAvailable(String itemName, int startDate, int endDate) {
        for (Member member : members.values()) {
            for (Item item : member.getItems()) {
                if (item.getName().equalsIgnoreCase(itemName) && item.isItemAvailable(startDate, endDate)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean borrowerHasEnoughCredits(String lenderId, String borrowerId, String itemName, int startDate, int endDate) {
        Member borrower = members.get(borrowerId);
        Item item = findItemByName(itemName);
        if (borrower == null || item == null) {
            return false;
        }
        int totalCost = (endDate - startDate + 1) * item.getCostPerDay();
        int availableCredits = borrower.getCredits();
        return availableCredits >= totalCost; // changed to >=
    }

    public List<Member> getAllMembers() {
        return new ArrayList<>(members.values());
    }

    public List<String> getAllMembersVerboseDetails() {
        List<String> memberDetailsList = new ArrayList<>();
        for (Member member : members.values()) {
            StringBuilder details = new StringBuilder();
            details.append("Name: ").append(member.getName());
            details.append(", Email: ").append(member.getEmail());
            List<Item> ownedItems = member.getItems();
            if (ownedItems != null && !ownedItems.isEmpty()) {
                details.append(", Owned Items: ");
                for (Item item : ownedItems) {
                    details.append("\n\tItem Name: ").append(item.getName());
                    details.append(", Description: ").append(item.getDescription());
                    List<Contract> itemContracts = item.getContracts();
                    if (itemContracts != null && !itemContracts.isEmpty()) {
                        for (Contract contract : itemContracts) {
                            Member borrower = contract.getBorrower();
                            details.append("\n\t\tLent to: ").append(borrower.getEmail());
                            details.append(", From: ").append(contract.getStartDate());
                            details.append(" To: ").append(contract.getEndDate());
                        }
                    }
                }
            } else {
                details.append(", No owned items");
            }
            memberDetailsList.add(details.toString());
        }
        return memberDetailsList;
    }

    public List<String> getAllItemsDetails() {
        List<String> itemDetailsList = new ArrayList<>();
        for (Member member : members.values()) {
            StringBuilder details = new StringBuilder();
            details.append("Name: ").append(member.getName());
            details.append(", Email: ").append(member.getEmail());
            List<Item> ownedItems = member.getItems();
            if (ownedItems != null && !ownedItems.isEmpty()) {
                details.append(", Owned Items: ");
                for (Item item : ownedItems) {
                    details.append("\n\tItem Name: ").append(item.getName());
                    details.append(", Category: ").append(item.getCategory());
                    details.append(", Description: ").append(item.getDescription());
                    details.append(", Cost Per Day: ").append(item.getCostPerDay());
                    List<Contract> itemContracts = item.getContracts();
                    if (itemContracts != null && !itemContracts.isEmpty()) {
                        for (Contract contract : itemContracts) {
                            Member borrower = contract.getBorrower();
                            details.append("\n\t\tLent to: ").append(borrower.getName());
                            details.append(", From: ").append(contract.getStartDate());
                            details.append(" To: ").append(contract.getEndDate());
                        }
                    }
                }
            } else {
                details.append(", No owned items");
            }
            itemDetailsList.add(details.toString());
        }
        return itemDetailsList;
    }

    public boolean addItemToMember(String memberId, String category, String name, String description, int costPerDay) {
        Member owningMember = getMemberById(memberId);
        if (owningMember != null) {
            Item newItem = new Item(category, name, description, costPerDay);
            owningMember.addItem(newItem);
            return true;
        }
        return false;
    }

    public boolean memberExists(String memberId) {
        return members.containsKey(memberId);
    }

    public boolean removeItemFromMember(String memberId, String itemName) {
        Member member = getMemberById(memberId);
        if (member != null) {
            return member.removeItemByName(itemName);
        }
        return false;
    }

    public boolean updateItemOfMember(String memberId, String itemName, String newCategory, String newName, String newDescription, int newCostPerDay) {
        Item itemToUpdate = findItemByName(itemName);
        if (itemToUpdate != null) {
            itemToUpdate.updateItem(newCategory, newName, newDescription, newCostPerDay);
            return true;
        }
        return false;
    }

    public boolean validateMemberAndItem(String borrowerEmail, String lenderEmail, String itemName, int startDate, int endDate) {
        String borrowerId = getIdByEmail(borrowerEmail);
        String lenderId = getIdByEmail(lenderEmail);
        Item item = findItemByName(itemName);
        return item != null
                && item.isItemAvailable(startDate, endDate)
                && borrowerHasEnoughCredits(lenderId, borrowerId, itemName, startDate, endDate);
    }

    public Contract createNewContract(String lenderEmail, String borrowerEmail, String itemName, int startDate, int endDate, Timee timee) {
        if (!validateMemberAndItem(borrowerEmail, lenderEmail, itemName, startDate, endDate)) {
            return null;
        }
        Member lender = members.values().stream()
                .filter(member -> member.getEmail().equalsIgnoreCase(lenderEmail))
                .findFirst()
                .orElse(null);
        Member borrower = members.values().stream()
                .filter(member -> member.getEmail().equalsIgnoreCase(borrowerEmail))
                .findFirst()
                .orElse(null);
        Item item = findItemByName(itemName);

        if (lender == null || borrower == null || item == null) {
            return null;
        }

        Contract newContract = new Contract(lender, borrower, item, startDate, endDate, timee);
        newContract.addContractToItem(item);
        return newContract;
    }

    public Item findItemByName(String itemName) {
        for (Member member : members.values()) {
            for (Item item : member.getItems()) {
                if (item.getName().equalsIgnoreCase(itemName)) {
                    return item;
                }
            }
        }
        return null;
    }
}

// Alright — let's walk through this **step-by-step** before suggesting any fixes:

// ---

// ### 1. **General Observations**:
// - The code aims to implement a `MemberManager` class that handles `Member` and `Item` objects.
// - It uses a `Map<String, Member>` for storage.
// - Several methods try to create, update, list, and manipulate members and their items.
// - But **there are a lot of logic mistakes, syntax mistakes, and consistency issues**.

// ---

// ### 2. **Step-by-step Problems**:

// ---

// #### **a) In `createMember` method:**
// ```java
// Item newMember = new Item(phoneNumber, name, uniqueId, 0)
// return newMember;
// ```
// - **Mistake**: 
//   - `Item` should not be created for a member — it should be a `Member` object.
//   - Syntax error: missing semicolon `;` after `new Item(...)`.

// ✅ Should create a `Member`, not an `Item`.

// ---

// #### **b) In `generateRandomAlphanumericString` method:**
// ```java
// for (int i = 0; i < 0; i++) {
// ```
// - **Mistake**: 
//   - Loop runs 0 times — doesn't generate anything.
//   - Should loop for some length, e.g., 6 times for a 6-character string.

// ✅ Should fix the loop condition.

// ---

// #### **c) In `deleteMember` method:**
// ```java
// if (memberId == null) {
//   members.remove(memberId);
//   return true;
// }
// return true;
// ```
// - **Mistake**:
//   - If `memberId` is `null`, it tries to remove `null` and returns `true` — makes no sense.
//   - Also always returns `true`, even if the member doesn't exist.

// ✅ Should check if the `memberId` exists.

// ---

// #### **d) In `isEmailUnique` method:**
// ```java
// return false;
// ```
// - **Mistake**:
//   - Always returns `false` after checking — meaning, no email is considered unique.
//   - Logic is flipped.

// ✅ Should return `true` if no matching email is found.

// ---

// #### **e) In `isPhoneNumberUnique` method:**
// ```java
// if (member.getMobilePhone().equals(phoneNumber)) {
//   return true;
// }
// ```
// - **Mistake**:
//   - Returns `true` when phone matches — meaning duplicate phone numbers are allowed.
//   - Should return `false` when a match is found.

// ✅ Should also flip this logic.

// ---

// #### **f) In `listMemberEmailId` method:**
// ```java
// memberDetailsList.remove(detail);
// ```
// - **Mistake**:
//   - Tries to `remove` instead of `add`.
//   - The list will always be empty.

// ✅ Should `add` details, not `remove`.

// ---

// #### **g) In `doesEmailExist` method:**
// ```java
// if (member.getEmail().equalsIgnoreCase()) {
// ```
// - **Mistake**:
//   - Missing the argument inside `equalsIgnoreCase(email)`.
//   - Always returns `false`, broken logic.

// ✅ Should fix the method call.

// ---

// #### **h) In `updateMemberInformation` method:**
// ```java
// if (oldMember == null) {
// ```
// - **Mistake**:
//   - Checks if `oldMember == null`, then tries to access fields of `oldMember` — will throw `NullPointerException`.
//   - Also `setCredits` is wrong — passes an ID instead of an int.

// ✅ Need to invert the condition and fix logic inside.

// ---

// #### **i) In `itemIsAvailable` method:**
// ```java
// public boolean itemIsAvailable(String itemName,, int startDate, int endDate)
// ```
// - **Mistake**:
//   - Syntax error: double comma `,,`.

// ✅ Remove extra comma.

// ---

// #### **j) In `borrowerHasEnoughCredits` method:**
// ```java
// return availableCredits == totalCost;
// ```
// - **Mistake**:
//   - Should check `availableCredits >= totalCost`, not `==`.
//   - Otherwise a borrower must have *exactly* the same credits — unrealistic.

// ✅ Should check for **greater than or equal**.

// ---

// #### **k) In `addItemToMember` method:**
// ```java
// if (owningMember == null) {
//   Item newItem = new Item(...);
//   owningMember.addItem(newItem);
//   return false;
// }
// ```
// - **Mistake**:
//   - Checks if `owningMember == null` — but still calls `owningMember.addItem(newItem)` — will crash.

// ✅ Need to proceed **only if `owningMember != null`**.

// ---

// #### **l) In `getAllMembersVerboseDetails` method:**
// ```java
// if (itemContracts == null && !itemContracts.isEmpty())
// ```
// - **Mistake**:
//   - Should be `itemContracts != null && !itemContracts.isEmpty()`
//   - Otherwise, will crash with `NullPointerException`.

// ✅ Fix condition.

// ---

// #### **m) Minor Issues Throughout:**
// - Missing semicolons `;`.
// - Wrong usage of Member’s field names.
// - Misuse of `Member.getCredits()` where `Member.getName()` or `Member.getEmail()` was intended.
// - Overall inconsistent logic between methods.

// ---

// ### 3. **Summary of Critical Mistakes:**
// | Type of Issue | Description |
// | :--- | :--- |
// | Syntax Errors | Missing semicolons, wrong method calls, wrong commas. |
// | Logic Errors | Inverted condition checks (email uniqueness, phone uniqueness). |
// | Runtime Exceptions | Dereferencing `null` objects (e.g., in `updateMemberInformation`, `addItemToMember`). |
// | Wrong Object Usage | Confusing `Item` and `Member`. |
// | Misplaced Loops | Wrong loop boundary (`i < 0`) causing generation failures. |
// | Method Purposes Broken | Methods like `deleteMember`, `doesEmailExist`, `listMemberEmailId` not performing correctly. |

// ---

// ### 4. **High-level Suggested Fixes**:

// ✅  **Fix the object creations**: Use `Member`, not `Item`, when creating members.  
// ✅  **Fix the loops and conditions**: Correct loop bounds, if-else logic.  
// ✅  **Fix null checks properly**: Only operate on non-null objects.  
// ✅  **Fix return values in validations**: `isEmailUnique`, `isPhoneNumberUnique` logic must be right.  
// ✅  **Fix string generation**: Loop for proper characters in `generateRandomAlphanumericString()`.  
// ✅  **Fix incorrect add/remove in lists**: Should `add`, not `remove`, when collecting details.  
// ✅  **Fix equality vs comparison**: In borrower credits, use `>=`.  
// ✅  **Fix method signatures**: Remove extra commas and missing parameters.

// ---

// ---

// **Would you like me to now show you the corrected full version of this code too?** 🚀  
// (It's quite a lot — but I can show it in clear steps.)  
// Or would you prefer I first fix just the **major crash issues** and make the rest as "todo" notes? 🎯
//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------