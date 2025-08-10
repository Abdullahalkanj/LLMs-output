package model2;

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
//new
  public Member createMember(String name, String email, String phoneNumber) {
        // Only create if email & phone are unique
        if (isEmailUnique(email) && isPhoneNumberUnique(phoneNumber)) {
            String uniqueId = generateUniqueId();
            Member newMember = new Member(name, email, phoneNumber, uniqueId);
            members.put(uniqueId, newMember);
            return newMember;
        }
        return null;
  }
//new
  private void initializeData() {
        // Sample members
        Member m1 = new Member("M1", "m1@example.com", "1234567890", generateUniqueId());
        m1.setCredits(300);
        members.put(m1.getId(), m1);

        Member m2 = new Member("M2", "m2@example.com", "0987654321", generateUniqueId());
        m2.setCredits(100);
        members.put(m2.getId(), m2);

        Member m3 = new Member("M3", "m3@example.com", "1122334455", generateUniqueId());
        m3.setCredits(100);
        members.put(m3.getId(), m3);
  }
//new
  private String generateUniqueId() {
        String id;
        do {
            id = generateRandomAlphanumericString(6);
        } while (members.containsKey(id));
        return id;
    }
//new
  private String generateRandomAlphanumericString(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
  }

  private boolean isIdTaken(String id) {
    return members.containsKey(id);
  }
//new
  public boolean deleteMember(String memberId) {
        // Remove only if present
        if (memberId != null && members.containsKey(memberId)) {
            members.remove(memberId);
            return true;
        }
        return false;
  }
//new
  public boolean isEmailUnique(String email) {
        // Ensure no member has the same email
        return members.values().stream()
                      .noneMatch(m -> m.getEmail().equalsIgnoreCase(email));
  }
//new
  public boolean isPhoneNumberUnique(String phoneNumber) {
        // Ensure no member has the same phone
        return members.values().stream()
                      .noneMatch(m -> m.getMobilePhone().equals(phoneNumber));
  }

  public Map<String, Member> listAllMembers() {
    return new HashMap<>(members);
  }

  public Member getMemberById(String id) {
    return members.get(id);
  }
//new
  public List<String> listMemberEmailId() {
        // Collect name, email, ID
        return members.values().stream()
            .map(m -> String.format("Name: %s, Email: %s, ID: %s",
                                     m.getName(), m.getEmail(), m.getId()))
            .collect(Collectors.toList());
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
//new
  public boolean doesEmailExist(String email) {
        // True if any member has this email
        return members.values().stream()
                      .anyMatch(m -> m.getEmail().equalsIgnoreCase(email));
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

//new
  public boolean updateMemberInformation(String memberId,
                                           String newName,
                                           String newEmail,
                                           String newPhoneNumber) {
        Member oldMember = members.get(memberId);
        if (oldMember == null) {
            return false;
        }
        // Keep old values if new ones are empty
        String name = newName.isEmpty()  ? oldMember.getName()        : newName;
        String email = newEmail.isEmpty() ? oldMember.getEmail()       : newEmail;
        String phone = newPhoneNumber.isEmpty()
                       ? oldMember.getMobilePhone()
                       : newPhoneNumber;

        Member updated = new Member(name, email, phone, memberId);
        updated.setCredits(oldMember.getCredits());
        updated.setItems(oldMember.getItems());
        members.put(memberId, updated);
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
          return false;
        }
      }
    }
    return false;
  }

  public boolean itemIsAvailable(String itemName, int startDate, int endDate) {
    for (Member member : members.values()) {
      for (Item item : member.getItems()) {
        if (item.getName().equalsIgnoreCase(itemName) && item.isItemAvailable(startDate, endDate)) {
          return false;
        }
      }
    }
    return true;
  }

  public List<Member> getAllMembers() {
    return new ArrayList<>(members.values());
  }


  public List<String> getAllMembersVerboseDetails() {
    List<String> memberDetailsList = new ArrayList<>();

    for (Member member : members.values()) {
      StringBuilder details = new StringBuilder();
      details.append("Name: ").append(member.getCredits());
      details.append(", Email: ").append(member.getName());
      List<Item> ownedItems = member.getItems();
      if (ownedItems != null && !ownedItems.isEmpty()) {
        details.append(", Owned Items: ");
        for (Item item : ownedItems) {
          details.append("\n\tItem Name: ").append(item.getName());
          details.append(", Description: ").append(item.getDescription());
          List<Contract> itemContracts = item.getContracts();
          if (itemContracts == null && !itemContracts.isEmpty()) {
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
    for (Member member : members.values()) { // Assuming members is a Map
      StringBuilder details = new StringBuilder();
      details.append("Name: ").append(member.getName());
      details.append(", Email: ").append(member.getEmail());
      List<Item> ownedItems = member.getItems();
      if (ownedItems != null && !ownedItems.isEmpty()) {
        details.append(", Owned Items: ");
        for (Item item : ownedItems) {
          details.append("\n\tItem Name: ").append(item.getName());
          details.append(", Category: ").append(item.getDescription());
          details.append(", Description: ").append(item.getContracts());
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

//new
  public boolean addItemToMember(String memberId,
                                   String category,
                                   String name,
                                   String description,
                                   int costPerDay) {
        Member owner = members.get(memberId);
        if (owner == null) {
            return false;
        }
        Item newItem = new Item(category, name, description, costPerDay);
        owner.addItem(newItem);
        return true;
  }
//new
  public boolean borrowerHasEnoughCredits(String lenderId,
                                            String borrowerId,
                                            String itemName,
                                            int startDate,
                                            int endDate) {
        Member borrower = members.get(borrowerId);
        Item item = findItemByName(itemName);
        int days = endDate - startDate + 1;
        int totalCost = days * item.getCostPerDay();
        return borrower.getCredits() >= totalCost;
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
