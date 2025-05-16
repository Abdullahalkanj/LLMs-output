// ChatGPT4o output using Structured prompt


//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
package model;

import java.util.*;
import java.util.stream.Collectors;

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
            members.put(uniqueId, newMember);
            return newMember;
        }
        return null;
    }

    private void initializeData() {
        createMember("M1", "m1@example.com", "1234567890").setCredits(300);
        createMember("M2", "m2@example.com", "0987654321").setCredits(100);
        createMember("M3", "m3@example.com", "1122334455").setCredits(100);
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
        for (int i = 0; i < 6; i++) {
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
        return members.values().stream()
                .noneMatch(member -> member.getEmail().equalsIgnoreCase(email));
    }

    public boolean isPhoneNumberUnique(String phoneNumber) {
        return members.values().stream()
                .noneMatch(member -> member.getMobilePhone().equals(phoneNumber));
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
            memberDetailsList.add(detail);
        }
        return memberDetailsList;
    }

    public boolean doesEmailExist(String email) {
        return members.values().stream()
                .anyMatch(member -> member.getEmail().equalsIgnoreCase(email));
    }

    public List<String> listAllMembersSimple() {
        return members.values().stream()
                .map(member -> "Name: " + member.getName() +
                        ", Email: " + member.getEmail() +
                        ", Current Credits: " + member.getCredits() +
                        ", Number of Owned Items: " + member.getItems().size())
                .collect(Collectors.toList());
    }

    public List<Item> getItemsByMemberId(String memberId) {
        Member member = members.get(memberId);
        return member != null ? member.getItems() : Collections.emptyList();
    }

    public boolean updateMemberInformation(String memberId, String newName, String newEmail, String newPhoneNumber) {
        Member oldMember = members.get(memberId);
        if (oldMember == null) return false;

        if (newName.isEmpty()) newName = oldMember.getName();
        if (newEmail.isEmpty()) newEmail = oldMember.getEmail();
        if (newPhoneNumber.isEmpty()) newPhoneNumber = oldMember.getMobilePhone();

        Member updatedMember = new Member(newName, newEmail, newPhoneNumber, memberId);
        updatedMember.setItems(oldMember.getItems());
        updatedMember.setCredits(oldMember.getCredits());

        members.put(memberId, updatedMember);
        return true;
    }

    public String getIdByEmail(String email) {
        return members.entrySet().stream()
                .filter(entry -> entry.getValue().getEmail().equalsIgnoreCase(email))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
    }

    public boolean itemExists(String itemName) {
        return members.values().stream()
                .flatMap(member -> member.getItems().stream())
                .anyMatch(item -> item.getName().equalsIgnoreCase(itemName));
    }

    public boolean itemIsAvailable(String itemName, int startDate, int endDate) {
        return members.values().stream()
                .flatMap(member -> member.getItems().stream())
                .anyMatch(item -> item.getName().equalsIgnoreCase(itemName) && item.isItemAvailable(startDate, endDate));
    }

    public boolean borrowerHasEnoughCredits(String lenderId, String borrowerId, String itemName, int startDate, int endDate) {
        Member borrower = members.get(borrowerId);
        Item item = findItemByName(itemName);
        if (borrower == null || item == null) return false;

        int days = endDate - startDate + 1;
        int totalCost = days * item.getCostPerDay();
        return borrower.getCredits() >= totalCost;
    }

    public List<Member> getAllMembers() {
        return new ArrayList<>(members.values());
    }

    public List<String> getAllMembersVerboseDetails() {
        List<String> detailsList = new ArrayList<>();
        for (Member member : members.values()) {
            StringBuilder sb = new StringBuilder();
            sb.append("Name: ").append(member.getName())
                    .append(", Email: ").append(member.getEmail());
            List<Item> items = member.getItems();
            if (items != null && !items.isEmpty()) {
                sb.append(", Owned Items:");
                for (Item item : items) {
                    sb.append("\n\tItem Name: ").append(item.getName())
                      .append(", Description: ").append(item.getDescription());
                    List<Contract> contracts = item.getContracts();
                    if (contracts != null && !contracts.isEmpty()) {
                        for (Contract contract : contracts) {
                            sb.append("\n\t\tLent to: ").append(contract.getBorrower().getEmail())
                              .append(", From: ").append(contract.getStartDate())
                              .append(", To: ").append(contract.getEndDate());
                        }
                    }
                }
            } else {
                sb.append(", No owned items");
            }
            detailsList.add(sb.toString());
        }
        return detailsList;
    }

    public boolean addItemToMember(String memberId, String category, String name, String description, int costPerDay) {
        Member owningMember = getMemberById(memberId);
        if (owningMember == null) return false;
        Item newItem = new Item(category, name, description, costPerDay);
        owningMember.addItem(newItem);
        return true;
    }

    public boolean memberExists(String memberId) {
        return members.containsKey(memberId);
    }

    public boolean removeItemFromMember(String memberId, String itemName) {
        Member member = getMemberById(memberId);
        return member != null && member.removeItemByName(itemName);
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
        if (!validateMemberAndItem(borrowerEmail, lenderEmail, itemName, startDate, endDate)) return null;
        Member lender = members.values().stream().filter(m -> m.getEmail().equalsIgnoreCase(lenderEmail)).findFirst().orElse(null);
        Member borrower = members.values().stream().filter(m -> m.getEmail().equalsIgnoreCase(borrowerEmail)).findFirst().orElse(null);
        Item item = findItemByName(itemName);
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


// Alright, let's dive into this carefully.

// Your code has **multiple bugs and logical flaws**, which I'll explain **one by one**, then provide **corrected versions** or advice on how to fix them.

// ---

// # 1. **Syntax Error in `createMember` Method**
// ```java
// Item newMember = new Item(phoneNumber, name, uniqueId, 0)
// ```
// - **Bug**: 
//   - Missing semicolon `;` at the end.
//   - Creating an `Item`, not a `Member` (wrong class).
// - **Fix**:
// ```java
// Member newMember = new Member(name, email, phoneNumber, uniqueId);
// ```
// *(You must return a `Member`, not an `Item`.)*

// ---

// # 2. **Bug in `generateRandomAlphanumericString` Method**
// ```java
// for (int i = 0; i < 0; i++) { ... }
// ```
// - **Bug**:
//   - Loop **never runs** because `i < 0` is immediately false.
// - **Fix**:
// ```java
// for (int i = 0; i < 6; i++) { 
//     int index = random.nextInt(alphanumeric.length());
//     sb.append(alphanumeric.charAt(index));
// }
// ```
// *(Generate a 6-character random string.)*

// ---

// # 3. **Bug in `deleteMember` Method**
// ```java
// if (memberId == null) {
//     members.remove(memberId);
//     return true;
// }
// return true;
// ```
// - **Bug**:
//   - Always returns `true`, even when `memberId == null`.
//   - **Also trying to remove null**, which makes no sense.
// - **Fix**:
// ```java
// if (memberId != null && members.containsKey(memberId)) {
//     members.remove(memberId);
//     return true;
// }
// return false;
// ```
// ---

// # 4. **Bug in `isEmailUnique` Method**
// ```java
// return false;
// ```
// - **Bug**:
//   - Always returns `false`, even if the email is unique.
// - **Fix**:
// ```java
// for (Member member : members.values()) {
//     if (member.getEmail().equalsIgnoreCase(email)) {
//         return false;
//     }
// }
// return true;
// ```

// ---

// # 5. **Bug in `isPhoneNumberUnique` Method**
// ```java
// if (member.getMobilePhone().equals(phoneNumber)) {
//     return true;
// }
// ```
// - **Bug**:
//   - Wrong logic: you return `true` when the phone number **already exists**, but it should be **unique**!
// - **Fix**:
// ```java
// for (Member member : members.values()) {
//     if (member.getMobilePhone().equals(phoneNumber)) {
//         return false;
//     }
// }
// return true;
// ```

// ---

// # 6. **Bug in `listMemberEmailId` Method**
// ```java
// memberDetailsList.remove(detail);
// ```
// - **Bug**:
//   - You **remove** details instead of **adding** them.
// - **Fix**:
// ```java
// memberDetailsList.add(detail);
// ```

// ---

// # 7. **Bug in `doesEmailExist` Method**
// ```java
// if (member.getEmail().equalsIgnoreCase()) {
// ```
// - **Bug**:
//   - `equalsIgnoreCase()` expects a parameter, but none is passed.
// - **Fix**:
// ```java
// if (member.getEmail().equalsIgnoreCase(email)) {
//     return true;
// }
// ```
// *(And correct return values: `true` if exists.)*

// ---

// # 8. **Syntax Error in `itemIsAvailable` Method**
// ```java
// public boolean itemIsAvailable(String itemName,, int startDate, int endDate)
// ```
// - **Bug**:
//   - Double comma `,,` in parameters.
// - **Fix**:
// ```java
// public boolean itemIsAvailable(String itemName, int startDate, int endDate)
// ```

// ---

// # 9. **Bug in `borrowerHasEnoughCredits` Method**
// ```java
// int totalCost = (endDate + startDate + 1) + item.getCostPerDay();
// return availableCredits == totalCost;
// ```
// - **Bug**:
//   - Formula for cost **makes no logical sense**.
//   - Should be something like:
// ```java
// int days = endDate - startDate + 1;
// int totalCost = days * item.getCostPerDay();
// return availableCredits >= totalCost;
// ```

// ---

// # 10. **Bug in `updateMemberInformation` Method**
// ```java
// if (oldMember == null) { 
//     //...
//     return false;
// }
// ```
// - **Bug**:
//   - You check if `oldMember == null`, then try to access its fields, causing **NullPointerException**!
// - **Fix**:
// ```java
// if (oldMember == null) {
//     return false;
// }
// ```
// Then continue updating if `oldMember` is not null.

// ---

// # 11. **Bug in `getAllMembersVerboseDetails`**
// ```java
// if (itemContracts == null && !itemContracts.isEmpty()) 
// ```
// - **Bug**:
//   - If `itemContracts == null`, `!itemContracts.isEmpty()` will **throw** an error.
// - **Fix**:
// ```java
// if (itemContracts != null && !itemContracts.isEmpty())
// ```

// ---

// # 12. **Bug in `addItemToMember` Method**
// ```java
// if (owningMember == null) {
//     Item newItem = new Item(...);
//     owningMember.addItem(newItem);
//     return false;
// }
// ```
// - **Bug**:
//   - If `owningMember == null`, then you **cannot call** `owningMember.addItem()`.
// - **Fix**:
// ```java
// if (owningMember == null) {
//     return false;
// }
// Item newItem = new Item(category, name, description, costPerDay);
// owningMember.addItem(newItem);
// return true;
// ```

// ---

// # 13. **Bug in `itemExists` Method**
// ```java
// return false;
// ```
// - **Bug**:
//   - Always returns `false`, even if the item is found.
// - **Fix**:
// ```java
// return true;
// ```
// inside the if condition.

// ---

// # General Observations:
// - **Member and Item are mixed up** sometimes.
// - **Randomness** is not properly implemented (in ID generation).
// - **Validation methods** return wrong values.
// - **Null checks** are misplaced.
// - **Error-prone copy-paste mistakes** (especially with empty method parameters, wrong loops).

// ---

// # Short Summary:

// | Problem Area                    | Issue                                          | Fix Summary                                  |
// |----------------------------------|------------------------------------------------|----------------------------------------------|
// | Object Creation                  | Created `Item` instead of `Member`             | Create correct object (`Member`)             |
// | Loop / Random ID                 | Loop doesn't run                              | Fix loop range in `generateRandomAlphanumericString` |
// | Delete Logic                     | Deletes `null` ID                             | Check ID first                               |
// | Validation Methods (`isUnique`)  | Wrong return values                           | Correct returns (`true/false`)               |
// | Email Checking                   | `equalsIgnoreCase` missing parameter          | Pass correct parameter                      |
// | Cost Calculation                 | Formula wrong                                 | Fix day * costPerDay                         |
// | Update Member                    | Null check wrong                              | Fix condition                               |
// | Verbose Details                  | Null check wrong                              | Reverse condition                           |
// | Item Management                  | Wrong logic when `owningMember == null`        | Fix branching logic                         |

// ---

// Would you like me to also **show you a full corrected version** of this class for you to directly use or study? 🚀
// (If yes, I can provide a full polished `MemberManager.java` for you!)
//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------