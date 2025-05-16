// ChatGPT4o output using basic prompt


// ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
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

    // Create a new member if email and phone are unique
    public Member createMember(String name, String email, String phoneNumber) {
        if (isEmailUnique(email) && isPhoneNumberUnique(phoneNumber)) {
            String uniqueId = generateUniqueId();
            Member newMember = new Member(name, email, phoneNumber, uniqueId);
            members.put(uniqueId, newMember);
            return newMember;
        }
        return null;
    }

    // Initialize sample data
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
            uniqueId = generateRandomAlphanumericString(6);
        } while (members.containsKey(uniqueId));
        return uniqueId;
    }

    private String generateRandomAlphanumericString(int length) {
        String alphanumeric = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(alphanumeric.length());
            sb.append(alphanumeric.charAt(index));
        }
        return sb.toString();
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
        List<String> emails = new ArrayList<>();
        for (Member member : members.values()) {
            emails.add(member.getEmail());
        }
        return emails;
    }

    public boolean updateMemberInformation(String memberId, String newName, String newEmail, String newPhoneNumber) {
        Member existingMember = members.get(memberId);
        if (existingMember != null) {
            if (!newName.isEmpty()) existingMember.setName(newName);
            if (!newEmail.isEmpty() && isEmailUnique(newEmail)) existingMember.setEmail(newEmail);
            if (!newPhoneNumber.isEmpty() && isPhoneNumberUnique(newPhoneNumber)) existingMember.setMobilePhone(newPhoneNumber);
            return true;
        }
        return false;
    }

    public String getIdByEmail(String email) {
        for (Member member : members.values()) {
            if (member.getEmail().equalsIgnoreCase(email)) {
                return member.getId();
            }
        }
        return null;
    }

    public boolean memberExists(String memberId) {
        return members.containsKey(memberId);
    }

    public boolean addItemToMember(String memberId, String category, String name, String description, int costPerDay) {
        Member member = members.get(memberId);
        if (member != null) {
            Item newItem = new Item(category, name, description, costPerDay);
            member.addItem(newItem);
            return true;
        }
        return false;
    }

    public boolean removeItemFromMember(String memberId, String itemName) {
        Member member = members.get(memberId);
        if (member != null) {
            return member.removeItemByName(itemName);
        }
        return false;
    }

    public boolean updateItemOfMember(String memberId, String itemName, String newCategory, String newName, String newDescription, int newCostPerDay) {
        Member member = members.get(memberId);
        if (member != null) {
            Item item = member.getItemByName(itemName);
            if (item != null) {
                item.updateItem(newCategory, newName, newDescription, newCostPerDay);
                return true;
            }
        }
        return false;
    }

    public List<String> listAllMembersSimple() {
        List<String> list = new ArrayList<>();
        for (Member member : members.values()) {
            list.add("Name: " + member.getName() + ", Email: " + member.getEmail() + ", Credits: " + member.getCredits());
        }
        return list;
    }

    public List<Item> getItemsByMemberId(String memberId) {
        Member member = members.get(memberId);
        if (member != null) {
            return member.getItems();
        }
        return Collections.emptyList();
    }

    public boolean itemExists(String itemName) {
        return findItemByName(itemName) != null;
    }

    public boolean itemIsAvailable(String itemName, int startDate, int endDate) {
        Item item = findItemByName(itemName);
        return item != null && item.isItemAvailable(startDate, endDate);
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

    public boolean borrowerHasEnoughCredits(String lenderId, String borrowerId, String itemName, int startDate, int endDate) {
        Member borrower = members.get(borrowerId);
        Item item = findItemByName(itemName);
        if (borrower == null || item == null) {
            return false;
        }
        int days = endDate - startDate + 1;
        int totalCost = days * item.getCostPerDay();
        return borrower.getCredits() >= totalCost;
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
                .filter(m -> m.getEmail().equalsIgnoreCase(lenderEmail))
                .findFirst()
                .orElse(null);

        Member borrower = members.values().stream()
                .filter(m -> m.getEmail().equalsIgnoreCase(borrowerEmail))
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
}
// Alright, let’s go over what this `MemberManager` Java class **is supposed to do** — and also highlight major **issues** in it.

// ---

// ## **Purpose:**
// This class is intended to **manage members** and their **owned items** in some system (like a lending library or sharing platform). It lets you:
// - Create, update, delete members.
// - Add, remove, update items that members own.
// - List members and items with details.
// - Handle **contracts** for lending items between members.
// - Validate that users and items are available and meet conditions before creating a contract.

// ---

// ## **High-level Responsibilities:**
// 1. **Member Management:**
//    - Add new members (`createMember`).
//    - Delete members (`deleteMember`).
//    - Update member info (`updateMemberInformation`).
//    - Check if a member exists by ID or email.
//    - List all members.

// 2. **Item Management:**
//    - Add new items to members (`addItemToMember`).
//    - Remove or update items.
//    - Find items by name.

// 3. **Contract Management:**
//    - Create contracts between lender and borrower.
//    - Check item availability and borrower credits before creating contracts.

// 4. **Helper Methods:**
//    - Validate email/phone uniqueness.
//    - Generate unique IDs.
//    - Retrieve detailed information about members and their items.

// ---

// ## **BUT there are MANY bugs and issues:**

// Here’s a detailed breakdown:

// | Problem Area | Issue | Consequence |
// |:---|:---|:---|
// | **Typos** | Extra commas (`itemIsAvailable(String itemName,, int startDate, int endDate)`), missing semicolons, missing `;` after `Item newMember = new Item(...)` | Code won't compile. |
// | **Logic Errors** | `isEmailUnique()` and `isPhoneNumberUnique()` logic are inverted (wrong return values). | Wrong validation — allows duplicates. |
// | **Incorrect Loops** | `for (int i = 0; i < 0; i++)` in `generateRandomAlphanumericString()` will never generate anything. | Always empty ID strings. |
// | **Incorrect Deletion** | In `deleteMember()`, it deletes **null** instead of actual memberId. | Nothing meaningful gets deleted. |
// | **Wrong Checks** | `doesEmailExist()` checks `member.getEmail().equalsIgnoreCase()` without passing a string to compare. | Always returns wrong result. |
// | **Wrong Control Flow** | In `updateMemberInformation()`, if the member is `null`, you proceed to access it anyway (`oldMember.getName()`). | Will cause a `NullPointerException`. |
// | **Logical Confusion** | Adding item to member: `if (owningMember == null)` then trying to `owningMember.addItem(...)`. | Will crash if member is not found. |
// | **Other Mistakes** | Borrower credits checking (`borrowerHasEnoughCredits()`) — wrong formula `(endDate + startDate + 1) + item.getCostPerDay()`. Should be duration \* cost per day. | Incorrect credit calculations. |
// | **Redundant/Dead Code** | `listMemberEmailId()` removes details instead of adding them. | Method is broken and always returns empty list. |
// | **Misuse of Contracts** | In `getAllMembersVerboseDetails()`, you check `itemContracts == null && !itemContracts.isEmpty()`. | This will always skip valid contracts. |
// | **Consistency Problems** | Sometimes method names suggest different behavior than actual code (e.g., `listAllMembersSimple()`). | Confusing for future developers. |

// ---

// ## **What the corrected structure should look like:**
// You would need to:
// - Fix all syntax errors.
// - Correct the logic in each method (especially validations, loops, condition checks).
// - Standardize true/false returns (some methods wrongly return false when success).
// - Make consistent naming and validation strategies.

// ---

// ## **Short Summary:**
// >  
// ✅ Intended as a **member-item-contract manager** for a borrowing/lending app.  
// ❌ Full of **syntax errors, logic bugs, missing checks**, and **incorrect returns** that would prevent it from working correctly.  
// 🔧 Needs serious **refactoring, error handling, and testing** before use.

// ---

// Would you like me to **show a corrected and working version** of this class for you? 🚀 It might make it clearer how it *should* look!  
// (And I can also explain how it could be better structured.)  
// Would you like me to?

//--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------