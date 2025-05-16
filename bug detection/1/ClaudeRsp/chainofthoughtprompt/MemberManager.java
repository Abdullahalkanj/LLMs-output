// Claude output using Chain-of-thought prompt


// ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
// I'll analyze the MemberManager class to identify and fix issues in the code. Let's go through it step by step.
// Code Analysis
// 1. Constructor and Member Creation

// The createMember method creates a new Item object instead of a Member object, which is inconsistent with the method's purpose.
// The method returns the incorrect type (Item instead of Member).

// 2. Random ID Generation

// In generateRandomAlphanumericString(), the for loop runs 0 times (for (int i = 0; i < 0; i++)) so it creates an empty string.
// The sb.append operation inside the loop is never reached.

// 3. Boolean Logic Issues

// Several boolean methods return the opposite of what they should:

// isEmailUnique checks if the name matches the email and returns false when unique
// isPhoneNumberUnique returns true when the number already exists
// doesEmailExist returns false when the email exists
// itemExists returns false when found
// itemIsAvailable returns false when available



// 4. Parameter Inconsistencies

// The itemIsAvailable method has a syntax error with an extra comma in its parameters

// 5. Comparison Issues

// In borrowerHasEnoughCredits, it uses equality (==) instead of inequality (>=) to check if credits are sufficient

// 6. Null Checks

// Several methods have backwards logic in null checks:

// deleteMember removes a member if the ID is null, which is incorrect
// updateMemberInformation proceeds only if the member is null, which is backwards



// 7. Collections Handling

// listMemberEmailId calls remove() instead of add() on the list
// itemContracts is checked with itemContracts == null && !itemContracts.isEmpty() which is contradictory

// 8. Other Logic Issues

// updateMemberInformation sets credits to the ID instead of the actual credits value
// addItemToMember and removeItemFromMember return false on success, which is counterintuitive

// The Fix

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

// Key Fixes Made

// Fixed createMember method:

// Changed from creating an Item to creating a Member
// Added the new member to the members map


// Fixed ID generation:

// Changed loop from for (int i = 0; i < 0; i++) to for (int i = 0; i < 6; i++)
// Added the missing charAt(index) to append a character to the string builder


// Fixed boolean logic issues:

// Corrected isEmailUnique to check email (not name) and return proper boolean
// Corrected isPhoneNumberUnique to return false when number exists
// Fixed doesEmailExist to return true when email exists
// Fixed itemExists to return true when found
// Fixed itemIsAvailable to return true when available


// Fixed syntax errors:

// Removed extra comma in itemIsAvailable parameters


// Fixed comparison issues:

// Changed == to >= in borrowerHasEnoughCredits
// Fixed calculation of total cost using multiplication instead of addition


// Fixed null checks:

// Inverted condition in deleteMember to check memberId != null
// Inverted condition in updateMemberInformation to proceed when member exists


// Fixed collections handling:

// Changed remove() to add() in listMemberEmailId
// Fixed contradictory null check in verbose details methods


// Fixed other logic issues:

// Updated updateMemberInformation to set credits properly from old member
// Made addItemToMember and other similar methods return true on success



// These changes ensure the MemberManager class will function correctly according to its intended purpose.
// ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
