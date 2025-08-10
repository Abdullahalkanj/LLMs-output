package model1;

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
  // 1. Check uniqueness
  if (isEmailUnique(email) && isPhoneNumberUnique(phoneNumber)) {
    String uniqueId = generateUniqueId();
    // 2. Construct a Member, not an Item
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
//new
  private String generateRandomAlphanumericString() {
    String alphanumeric = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    StringBuilder sb = new StringBuilder(6);
    // 3. Loop should run 6 times, appending one random char each iteration
    for (int i = 0; i < 6; i++) {
      int index = random.nextInt(alphanumeric.length());
      sb.append(alphanumeric.charAt(index));
    }
    return sb.toString();
  }

  private boolean isIdTaken(String id) {
    return members.containsKey(id);
  }
//new
  public boolean deleteMember(String memberId) {
  // 5. Remove when id is non-null and present
  if (memberId != null && members.containsKey(memberId)) {
    members.remove(memberId);
    return true;
  }
  return false;
  }
//new
  public boolean isEmailUnique(String email) {
  for (Member member : members.values()) {
    // 4. Compare to member.getEmail(), not getName()
    if (member.getEmail().equalsIgnoreCase(email)) {
      return false;
    }
  }
  return true;
  }
//new
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
//new
  public List<String> listMemberEmailId() {
    List<String> memberDetailsList = new ArrayList<>();
    for (Member member : members.values()) {
      String detail = "Name: " + member.getName()
                  + ", Email: " + member.getEmail()
                  + ", ID: " + member.getId();
      // 6. Add, not remove
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
//new
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
//new
  public boolean updateMemberInformation(String memberId, String newName, String newEmail, String newPhoneNumber) {
    Member oldMember = members.get(memberId);
    // 7. Check for non-null oldMember
    if (oldMember != null) {
      // only overwrite if non-empty
      if (newName == null || newName.isEmpty())      newName = oldMember.getName();
      if (newEmail == null || newEmail.isEmpty())    newEmail = oldMember.getEmail();
      if (newPhoneNumber == null || newPhoneNumber.isEmpty()) newPhoneNumber = oldMember.getMobilePhone();

      Member updated = new Member(newName, newEmail, newPhoneNumber, memberId);
      updated.setItems(oldMember.getItems());
      updated.setCredits(oldMember.getCredits());
      members.put(memberId, updated);
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
//new
  public boolean borrowerHasEnoughCredits(String lenderId,
    String borrowerId, String itemName, int startDate, int endDate) {
    Member borrower = members.get(borrowerId);
    Item item = findItemByName(itemName);
    if (borrower == null || item == null) return false;

    // 8. Correct cost calculation: days * costPerDay
    int days = endDate - startDate + 1;
    int totalCost = days * item.getCostPerDay();
    int availableCredits = borrower.getCredits();
    // allow equal or greater
    return availableCredits >= totalCost;
  }

  public List<Member> getAllMembers() {
    return new ArrayList<>(members.values());
  }
//new
  public List<String> getAllMembersVerboseDetails() {
    List<String> memberDetailsList = new ArrayList<>();
    for (Member member : members.values()) {
      StringBuilder details = new StringBuilder();
      // 10. Fix field order: show Name, Email, Credits
      details.append("Name: ").append(member.getName());
      details.append(", Email: ").append(member.getEmail());
      details.append(", Credits: ").append(member.getCredits());

      List<Item> ownedItems = member.getItems();
      if (ownedItems != null && !ownedItems.isEmpty()) {
        details.append(", Owned Items:");
        for (Item item : ownedItems) {
          details.append("\n\tItem Name: ").append(item.getName());
          details.append(", Description: ").append(item.getDescription());
          List<Contract> itemContracts = item.getContracts();
          // only iterate when non-null AND non-empty
          if (itemContracts != null && !itemContracts.isEmpty()) {
            for (Contract contract : itemContracts) {
              Member borrower = contract.getBorrower();
              details.append("\n\t\tLent to: ").append(borrower.getEmail());
              details.append(", From: ").append(contract.getStartDate())
                     .append(" To: ").append(contract.getEndDate());
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
  public List<String> getAllItemsDetails() {
    List<String> detailsList = new ArrayList<>();
    for (Member member : members.values()) {
      StringBuilder details = new StringBuilder();
      details.append("Owner: ").append(member.getName())
             .append(", Email: ").append(member.getEmail());

      List<Item> items = member.getItems();
      if (items != null && !items.isEmpty()) {
        details.append(", Items:");
        for (Item item : items) {
          // 11. Fix category vs description, and iterate contracts
          details.append("\n\tName: ").append(item.getName());
          details.append(", Category: ").append(item.getCategory());
          details.append(", Description: ").append(item.getDescription());
          details.append(", Cost/Day: ").append(item.getCostPerDay());
          List<Contract> contracts = item.getContracts();
          if (contracts != null && !contracts.isEmpty()) {
            for (Contract c : contracts) {
              details.append("\n\t\tLent to: ").append(c.getBorrower().getName())
                     .append(", From: ").append(c.getStartDate())
                     .append(" To: ").append(c.getEndDate());
            }
          }
        }
      } else {
        details.append(", No items");
      }
     detailsList.add(details.toString());
    }
    return detailsList;
  }
//new..
  public boolean addItemToMember(String memberId, String category, String name, String description, int costPerDay) {
    Member owningMember = getMemberById(memberId);
    // 9. Only add if the member exists
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
