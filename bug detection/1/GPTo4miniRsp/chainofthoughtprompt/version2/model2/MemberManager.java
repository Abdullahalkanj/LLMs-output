package model2;

import java.util.*;
import java.util.stream.Collectors;

public class MemberManager {
  private Map<String, Member> members = new HashMap<>();
  private Random random = new Random();

  public MemberManager() {
    initializeData();
  }

  /** 1. Create and register a new member if email & phone are unique */
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

  /** 2. Seed initial demo data */
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

  /** 3. Generate a collision-resistant 6-char ID */
  private String generateUniqueId() {
    String id;
    do {
      id = generateRandomAlphanumericString(6);
    } while (members.containsKey(id));
    return id;
  }

  /** 4. Build a random alphanumeric string of given length */
  private String generateRandomAlphanumericString(int length) {
    String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    StringBuilder sb = new StringBuilder(length);
    for (int i = 0; i < length; i++) {
      sb.append(chars.charAt(random.nextInt(chars.length())));
    }
    return sb.toString();
  }

  /** 5. Delete an existing member by ID */
  public boolean deleteMember(String memberId) {
    if (memberId != null && members.containsKey(memberId)) {
      members.remove(memberId);
      return true;
    }
    return false;
  }

  /** 6. Email must not exist in ANY member */
  public boolean isEmailUnique(String email) {
    return members.values().stream()
        .noneMatch(m -> m.getEmail().equalsIgnoreCase(email));
  }

  /** 7. Phone must not exist in ANY member */
  public boolean isPhoneNumberUnique(String phoneNumber) {
    return members.values().stream()
        .noneMatch(m -> m.getMobilePhone().equals(phoneNumber));
  }

  /** 8. List all members (copy of the map) */
  public Map<String, Member> listAllMembers() {
    return new HashMap<>(members);
  }

  /** 9. Get member or null if missing */
  public Member getMemberById(String id) {
    return members.get(id);
  }

  /** 10. Return each member’s name/email/id */
  public List<String> listMemberEmailId() {
    return members.values().stream()
      .map(m -> "Name: " + m.getName()
             + ", Email: " + m.getEmail()
             + ", ID: " + m.getId())
      .collect(Collectors.toList());
  }

  /** 11. Detailed string by ID */
  public String getMemberDetailsById(String memberId) {
    Member m = members.get(memberId);
    if (m == null) return null;
    String items = m.getItems().stream()
                    .map(Item::getName)
                    .collect(Collectors.joining(", "));
    return String.format(
      "Name: %s, Email: %s, Credits: %d, Owned Items: [%s]",
      m.getName(), m.getEmail(), m.getCredits(), items);
  }

  /** 12. Check if any member has this email */
  public boolean doesEmailExist(String email) {
    return members.values().stream()
        .anyMatch(m -> m.getEmail().equalsIgnoreCase(email));
  }

  /** 13. Simple summary of all members */
  public List<String> listAllMembersSimple() {
    return members.values().stream()
      .map(m -> String.format(
        "Name: %s, Email: %s, Credits: %d, #Items: %d",
        m.getName(), m.getEmail(), m.getCredits(), m.getItems().size()))
      .collect(Collectors.toList());
  }

  /** 14. Get items or empty list */
  public List<Item> getItemsByMemberId(String memberId) {
    Member m = members.get(memberId);
    return m != null ? new ArrayList<>(m.getItems()) : Collections.emptyList();
  }

  /** 15. Update member fields; if a new value is empty, keep old */
  // public boolean updateMemberInformation(String memberId,
  //     String newName, String newEmail, String newPhoneNumber) {
  //   Member old = members.get(memberId);
  //   if (old == null) return false;
  //   if (!newName.isBlank()) old.setName(newName);
  //   if (!newEmail.isBlank() && isEmailUnique(newEmail)) old.setEmail(newEmail);
  //   if (!newPhoneNumber.isBlank() && isPhoneNumberUnique(newPhoneNumber))
  //     old.setMobilePhone(newPhoneNumber);
  //   return true;
  // }

  /** 16. Email→ID lookup */
  public String getIdByEmail(String email) {
    return members.entrySet().stream()
        .filter(e -> e.getValue().getEmail().equalsIgnoreCase(email))
        .map(Map.Entry::getKey)
        .findFirst().orElse(null);
  }

  /** 17. Does any member own an item by that name? */
  public boolean itemExists(String itemName) {
    return members.values().stream()
      .flatMap(m -> m.getItems().stream())
      .anyMatch(i -> i.getName().equalsIgnoreCase(itemName));
  }

  /** 18. Is the named item free between dates? */
  public boolean itemIsAvailable(String itemName, int startDate, int endDate) {
    Item i = findItemByName(itemName);
    return i != null && i.isItemAvailable(startDate, endDate);
  }

  /** 19. Borrower has ≥ required credits for the rental */
  public boolean borrowerHasEnoughCredits(String borrowerId,
      String itemName, int startDate, int endDate) {
    Member b = members.get(borrowerId);
    Item i = findItemByName(itemName);
    if (b == null || i == null) return false;
    int days = endDate - startDate + 1;
    int cost = days * i.getCostPerDay();
    return b.getCredits() >= cost;
  }

  /** 20. All members as list */
  public List<Member> getAllMembers() {
    return new ArrayList<>(members.values());
  }

  /** 21. Verbose details (fixed field order & null checks) */
  public List<String> getAllMembersVerboseDetails() {
    List<String> out = new ArrayList<>();
    for (Member m : members.values()) {
      StringBuilder sb = new StringBuilder();
      sb.append("Name: ").append(m.getName())
        .append(", Email: ").append(m.getEmail())
        .append(", Credits: ").append(m.getCredits());
      if (m.getItems().isEmpty()) {
        sb.append(", No owned items");
      } else {
        sb.append(", Owned Items:");
        for (Item it : m.getItems()) {
          sb.append("\n\t").append(it);
        }
      }
      out.add(sb.toString());
    }
    return out;
  }

  /** 22. All items detail */
  public List<String> getAllItemsDetails() {
    List<String> out = new ArrayList<>();
    for (Member m : members.values()) {
      for (Item it : m.getItems()) {
        out.add(String.format(
          "Owner: %s, Item: %s, Desc: %s, Cost/day: %d, Contracts: %d",
          m.getName(), it.getName(), it.getDescription(),
          it.getCostPerDay(), it.getContracts().size()
        ));
      }
    }
    return out;
  }

  /** 23. Add a new item to an existing member */
  public boolean addItemToMember(String memberId,
      String category, String name, String description, int costPerDay) {
    Member m = members.get(memberId);
    if (m == null) return false;
    Item newItem = new Item(category, name, description, costPerDay);
    m.addItem(newItem);
    return true;
  }

  /** 24. Remove item by name */
  public boolean removeItemFromMember(String memberId, String itemName) {
    Member m = members.get(memberId);
    return m != null && m.removeItemByName(itemName);
  }

  /** 25. Update an item’s fields */
  public boolean updateItemOfMember(String memberId,
      String itemName, String newCategory,
      String newName, String newDescription, int newCostPerDay) {
    Item it = findItemByName(itemName);
    if (it == null) return false;
    it.updateItem(newCategory, newName, newDescription, newCostPerDay);
    return true;
  }

  /** 26. Validation before contract */
  public boolean validateMemberAndItem(String borrowerEmail,
      String lenderEmail, String itemName, int startDate, int endDate) {
    String bId = getIdByEmail(borrowerEmail);
    String lId = getIdByEmail(lenderEmail);
    return bId != null && lId != null
        && itemIsAvailable(itemName, startDate, endDate)
        && borrowerHasEnoughCredits(bId, itemName, startDate, endDate);
  }

  /** 27. Create & attach a new contract */
  public Contract createNewContract(String lenderEmail, String borrowerEmail,
      String itemName, int startDate, int endDate, Timee timee) {

    if (!validateMemberAndItem(borrowerEmail, lenderEmail, itemName, startDate, endDate))
      return null;

    Member lender = members.values().stream()
        .filter(m -> m.getEmail().equalsIgnoreCase(lenderEmail))
        .findFirst().orElse(null);
    Member borrower = members.values().stream()
        .filter(m -> m.getEmail().equalsIgnoreCase(borrowerEmail))
        .findFirst().orElse(null);
    Item item = findItemByName(itemName);

    Contract c = new Contract(lender, borrower, item, startDate, endDate, timee);
    item.addContract(c);
    return c;
  }

  /** 28. Find first matching item by name */
  public Item findItemByName(String itemName) {
    for (Member m : members.values()) {
      for (Item it : m.getItems()) {
        if (it.getName().equalsIgnoreCase(itemName)) {
          return it;
        }
      }
    }
    return null;
  }
}
