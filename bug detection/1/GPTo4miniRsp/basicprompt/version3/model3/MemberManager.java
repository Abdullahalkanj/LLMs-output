package model3;

import java.util.ArrayList;
import java.util.Collections;
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
//new
  public boolean deleteMember(String memberId) {
        if (memberId != null && members.containsKey(memberId)) {
            members.remove(memberId);
            return true;
        }
        return false;
  }
//new
  public boolean isEmailUnique(String email) {
        return members.values().stream()
            .noneMatch(m -> m.getEmail().equalsIgnoreCase(email));
  }
//new
  public boolean isPhoneNumberUnique(String phoneNumber) {
        return members.values().stream()
            .noneMatch(m -> m.getMobilePhone().equals(phoneNumber));
  }
//new
  public Map<String, Member> listAllMembers() {
        return new HashMap<>(members);
  }
//new
  public Member getMemberById(String id) {
        return members.get(id);
  }
//new
  public List<String> listMemberEmailId() {
        List<String> list = new ArrayList<>();
        for (Member m : members.values()) {
            String detail = String.format("Name: %s, Email: %s, ID: %s",
                                           m.getName(), m.getEmail(), m.getId());
            list.add(detail);
        }
        return list;
  }
//new 
  public String getMemberDetailsById(String memberId) {
        Member m = members.get(memberId);
        if (m == null) return null;

        String items = getItemsByMemberId(memberId).stream()
            .map(Item::getName)
            .collect(Collectors.joining(", "));
        return String.format("Name: %s, Email: %s, Credits: %d, Items: %s",
                             m.getName(), m.getEmail(), m.getCredits(),
                             items.isEmpty() ? "none" : items);
  }
//new
  public boolean doesEmailExist(String email) {
        return members.values().stream()
            .anyMatch(m -> m.getEmail().equalsIgnoreCase(email));
  }
//new
  public List<String> listAllMembersSimple() {
        List<String> list = new ArrayList<>();
        for (Member m : members.values()) {
            list.add(String.format("Name: %s, Email: %s, Credits: %d, #Items: %d",
                    m.getName(), m.getEmail(), m.getCredits(), m.getItems().size()));
        }
        return list;
  }
//new
  public List<Item> getItemsByMemberId(String memberId) {
        Member m = members.get(memberId);
        return m != null
            ? new ArrayList<>(m.getItems())
            : Collections.emptyList();
  }
//new
  // public boolean updateMemberInformation(String memberId,
  //                                          String newName,
  //                                          String newEmail,
  //                                          String newPhoneNumber) {
  //       Member old = members.get(memberId);
  //       if (old == null) return false;

  //       if (newName != null && !newName.isEmpty()) {
  //           old.setName(newName);
  //       }
  //       if (newEmail != null && !newEmail.isEmpty() && isEmailUnique(newEmail)) {
  //           old.setEmail(newEmail);
  //       }
  //       if (newPhoneNumber != null && !newPhoneNumber.isEmpty() && isPhoneNumberUnique(newPhoneNumber)) {
  //           old.setMobilePhone(newPhoneNumber);
  //       }
  //       return true;
  // }
//new
  public String getIdByEmail(String email) {
        return members.entrySet().stream()
            .filter(e -> e.getValue().getEmail().equalsIgnoreCase(email))
            .map(Map.Entry::getKey)
            .findFirst()
            .orElse(null);
  }
//new
  public boolean itemExists(String itemName) {
        return members.values().stream()
            .flatMap(m -> m.getItems().stream())
            .anyMatch(item -> item.getName().equalsIgnoreCase(itemName));
  }
//new
  public boolean itemIsAvailable(String itemName, int startDate, int endDate) {
        Item item = findItemByName(itemName);
        return item != null && item.isItemAvailable(startDate, endDate);
  }
//new
  public boolean borrowerHasEnoughCredits(String borrowerId,
                                            String itemName,
                                            int startDate,
                                            int endDate) {
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
        if (owner == null) return false;
        Item newItem = new Item(category, name, description, costPerDay);
        owner.addItem(newItem);
        return true;
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

  // public boolean validateMemberAndItem(String borrowerEmail,
  //     String lenderEmail, String itemName, int startDate, int endDate) {
  //   String borrowerId = getIdByEmail(borrowerEmail);
  //   String lenderId = getIdByEmail(lenderEmail);
  //   Item item = findItemByName(itemName);
  //   return item != null
  //       && item.isItemAvailable(startDate, endDate)
  //       && borrowerHasEnoughCredits(lenderId, borrowerId, itemName, startDate, endDate);
  // }

  // public Contract createNewContract(String lenderEmail, String borrowerEmail,
  //     String itemName, int startDate, int endDate, Timee timee) {

  //   Member lender = members.values().stream()
  //       .filter(member -> member.getEmail().equalsIgnoreCase(lenderEmail))
  //       .findFirst()
  //       .orElse(null);
  //   Member borrower = members.values().stream()
  //       .filter(member -> member.getEmail().equalsIgnoreCase(borrowerEmail))
  //       .findFirst()
  //       .orElse(null);
  //   Item item = findItemByName(itemName);

  //   if (!validateMemberAndItem(borrowerEmail, lenderEmail, itemName, startDate, endDate)) {
  //     return null;
  //   }

  //   Contract newContract = new Contract(lender, borrower, item, startDate, endDate, timee);
  //   newContract.addContractToItem(item);

  //   return newContract;
  // }
//new
  public Item findItemByName(String itemName) {
        for (Member m : members.values()) {
            for (Item item : m.getItems()) {
                if (item.getName().equalsIgnoreCase(itemName)) {
                    return item;
                }
            }
        }
        return null;
  }

}
