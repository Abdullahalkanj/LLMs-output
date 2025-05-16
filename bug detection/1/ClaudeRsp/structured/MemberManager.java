// Claude output using Structured prompt


// ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
// # Code Analysis

// I've reviewed the Java code for the `MemberManager` class, and I found several bugs and issues that need fixing. Let's go through them systematically.

// ## Major Bugs and Issues

// 1. **Creation of new member** 
//    - Line 23-24: The `createMember` method tries to create a `Member` object but incorrectly creates it as an `Item` object. It's also missing a semicolon.

// 2. **ID Generation** 
//    - Line 52-55: The loop in `generateRandomAlphanumericString()` runs 0 times because the loop condition is `i < 0`.
//    - Line 54: The code is generating the index but not using it to append anything to the StringBuilder.

// 3. **Deletion logic** 
//    - Line 63-68: The `deleteMember` method is removing the member only if `memberId` is null and always returns true regardless of success.

// 4. **Boolean logic reversals**
//    - Line 71-77: The `isEmailUnique` method checks against `getName()` instead of `getEmail()` and always returns false.
//    - Line 80-86: The `isPhoneNumberUnique` method returns true when a phone number match is found (should be false).

// 5. **Missing operations**
//    - Line 95-98: The `listMemberEmailId` method is removing items from the list instead of adding them.

// 6. **Parameter and variable inconsistencies**
//    - Line 133: Missing parameter in method signature for `itemIsAvailable` (there's an extra comma).
//    - Line 174: Incorrect null check (`itemContracts == null && !itemContracts.isEmpty()`).

// 7. **Credits comparison error**
//    - Line 143-146: The `borrowerHasEnoughCredits` method uses `==` instead of `>=` to check sufficient credits.

// 8. **Incorrect arguments**
//    - Line 192: `setCredits` is being called with `oldMember.getId()` instead of `oldMember.getCredits()`.

// 9. **Object creation and validation**
//    - Line 269-274: The `addItemToMember` method returns `false` when it should return `true`.

// ## Fix Implementation

// Here's my implementation with all the bugs fixed:

package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
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
    } else {
      return null;
    }
  }

  private void initializeData() {
    Member member1 = new Member("M1", "m1", "1234567890", generateUniqueId());
    member1.setCredits(300);
    members.put(member1.getId(), member1);

    Member member2 = new Member("M2", "m2", "0987654321", generateUniqueId());
    member2.setCredits(100);
    members.put(member2.getId(), member2);

    Member member3 = new Member("M3", "m3", "1122334455", generateUniqueId());
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
    if (memberId != null) {
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
      memberDetailsList.add(detail);
    }
    return memberDetailsList;
  }

  public String getMemberDetailsById(String memberId) {
    Member selectedMember = members.get(memberId);
    if (selectedMember != null) {
      List<Item> items = getItemsByMemberId(memberId);
      String itemNames = items.stream()
          .map(Item::getName)
          .collect(Collectors.joining(", "));

      return "Name: " + selectedMember.getName()
          +
          ", Email: " + selectedMember.getEmail()
          +
          ", Current Credits: " + selectedMember.getCredits()
          +
          ", Owned Items: " + itemNames;
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
          +
          ", Email: " + member.getEmail()
          +
          ", Current Credits: " + member.getCredits()
          +
          ", Number of Owned Items: " + member.getItems().size();
      simpleMemberDetailsList.add(detail);
    }
    return simpleMemberDetailsList;
  }

  public List<Item> getItemsByMemberId(String memberId) {
    Member member = members.get(memberId);
    if (member != null) {
      return member.getItems();
    }
    return null;
  }

  public boolean updateMemberInformation(String memberId, String newName, String newEmail, String newPhoneNumber) {
    Member oldMember = members.get(memberId);
    if (oldMember != null) {
      if (newName.isEmpty()) {
        newName = oldMember.getName();
      }
      if (newEmail.isEmpty()) {
        newEmail = oldMember.getEmail();
      }
      if (newPhoneNumber.isEmpty()) {
        newPhoneNumber = oldMember.getMobilePhone();
      }
      Member newMember = new Member(newName, newEmail, newPhoneNumber, memberId);
      newMember.setItems(oldMember.getItems());
      newMember.setCredits(oldMember.getCredits());

      members.put(memberId, newMember);
      return true;
    }
    return false;
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

  public boolean borrowerHasEnoughCredits(String lenderId,
      String borrowerId, String itemName, int startDate, int endDate) {
    Member borrower = members.get(borrowerId);
    Item item = findItemByName(itemName);
    int totalCost = (endDate - startDate + 1) * item.getCostPerDay();
    int availableCredits = borrower.getCredits();
    return availableCredits >= totalCost;
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
      details.append(", Credits: ").append(member.getCredits());
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
      memberDetailsList.add(details.toString());
    }
    return memberDetailsList;
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

  public boolean updateItemOfMember(String memberId,
      String itemName, String newCategory, String newName, String newDescription, int newCostPerDay) {
    Item itemToUpdate = findItemByName(itemName);
    if (itemToUpdate != null) {
      itemToUpdate.updateItem(newCategory, newName, newDescription, newCostPerDay);
      return true;
    }
    return false;
  }

  public boolean validateMemberAndItem(String borrowerEmail,
      String lenderEmail, String itemName, int startDate, int endDate) {
    String borrowerId = getIdByEmail(borrowerEmail);
    String lenderId = getIdByEmail(lenderEmail);
    Item item = findItemByName(itemName);
    return item != null
        && item.isItemAvailable(startDate, endDate)
        && borrowerHasEnoughCredits(lenderId, borrowerId, itemName, startDate, endDate);
  }

  public Contract createNewContract(String lenderEmail, String borrowerEmail,
      String itemName, int startDate, int endDate, Timee timee) {

    Member lender = members.values().stream()
        .filter(member -> member.getEmail().equalsIgnoreCase(lenderEmail))
        .findFirst()
        .orElse(null);
    Member borrower = members.values().stream()
        .filter(member -> member.getEmail().equalsIgnoreCase(borrowerEmail))
        .findFirst()
        .orElse(null);
    Item item = findItemByName(itemName);

    if (!validateMemberAndItem(borrowerEmail, lenderEmail, itemName, startDate, endDate)) {
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


// ```

// ## Detailed Explanation of Fixes

// 1. **Fixed Member Creation (Line 23-26)**
//    - Changed `Item newMember = new Item(...)` to `Member newMember = new Member(...)`
//    - Added the missing semicolon
//    - Added missing code to store the new member in the map

// 2. **Fixed ID Generation (Line 52-56)**
//    - Changed `i < 0` to `i < 6` to actually generate a 6-character ID
//    - Added missing code to append characters to the StringBuilder

// 3. **Fixed Member Deletion (Line 63-68)**
//    - Changed the condition from `if (memberId == null)` to `if (memberId != null)`
//    - Made it return `false` for failure cases

// 4. **Fixed Boolean Logic in Validation Methods**
//    - Fixed `isEmailUnique` to check against `getEmail()` instead of `getName()` and return the correct boolean value
//    - Fixed `isPhoneNumberUnique` to return `false` when a match is found

// 5. **Fixed List Operations**
//    - Changed `memberDetailsList.remove(detail)` to `memberDetailsList.add(detail)` in the `listMemberEmailId` method

// 6. **Fixed Parameter and Variable Issues**
//    - Fixed the parameter list in `itemIsAvailable` by removing the extra comma
//    - Fixed the conditional check in `getAllMembersVerboseDetails` from `itemContracts == null && !itemContracts.isEmpty()` to `itemContracts != null && !itemContracts.isEmpty()`

// 7. **Fixed Credits Comparison**
//    - Changed `==` to `>=` in `borrowerHasEnoughCredits` to properly check if there are enough credits
//    - Fixed the calculation to multiply by cost per day instead of adding

// 8. **Fixed Method Arguments**
//    - Changed `setCredits(oldMember.getId())` to `setCredits(oldMember.getCredits())`

// 9. **Fixed Object Creation and Validation**
//    - Changed `return false` to `return true` in `addItemToMember` when successful
//    - Corrected the null check in the same method to check if member is not null

// 10. **Fixed Logic in Various Methods**
//     - Fixed `doesEmailExist` to return `true` when found
//     - Fixed `itemExists` to return `true` when found
//     - Fixed `itemIsAvailable` to return `true` when found
//     - Fixed `updateMemberInformation` to return `true` when successful

// These fixes address all the bugs and logical errors found in the original code, making the `MemberManager` class function correctly.
// ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
