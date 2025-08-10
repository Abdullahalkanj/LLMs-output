package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * The MemberManager class manages member-related operations such as creating, updating, deleting,
 * listing, and retrieving member information. It also provides methods to check the uniqueness of
 * email addresses and phone numbers among members.
 */
public class MemberManager {
  private Map<String, Member> members; // Stores members with their ID as the key
  private Random random;
  
  /**
  * Default constructor for MemberManager.
  * Initializes members as a new HashMap and random as a new Random instance.
  * Calls the initializeData method to populate the members map.
  */ 
  public MemberManager() {
    this.members = new HashMap<>();
    this.random = new Random();
    initializeData();
  }

  /**
  * Creates a new Member object with the specified parameters and adds it to the members map.
  * A unique ID is generated for each new member.
  *
  * @param name The name of the new member.
  * @param email The email address of the new member.
  * @param phoneNumber The phone number of the new member.
  */
  //bug1,2,3 claude
  public Member createMember(String name, String email, String phoneNumber) {
    if (isEmailUnique(email) && isPhoneNumberUnique(phoneNumber)) {
      String uniqueId = generateUniqueId();
      Member newMember = new Member(name, email, phoneNumber, uniqueId); //1
      members.put(uniqueId, newMember); //2
      return newMember;
    } else {
      return null;
    }
    
  }

  private void initializeData() {
    // create first member with 500 credits
    Member member1 = new Member("M1", "m1", "1234567890", generateUniqueId());
    member1.setCredits(300); // assuming you have a method in Member class to set credits
    members.put(member1.getId(), member1); // assuming getId() method exists in Member class

    // create second member with 100 credits
    Member member2 = new Member("M2", "m2", "0987654321", generateUniqueId());
    member2.setCredits(100);
    members.put(member2.getId(), member2);

    // create third member with 100 credits
    Member member3 = new Member("M3", "m3", "1122334455", generateUniqueId());
    member3.setCredits(100);
    members.put(member3.getId(), member3);
  }

  // Method to generate a unique ID
  private String generateUniqueId() {
    String uniqueId;
    do {
      uniqueId = generateRandomAlphanumericString();
    } while (isIdTaken(uniqueId));
    return uniqueId;
  }
// bug 2,3 claude
  // Helper method to generate a random alpha-numeric string of length 6
  private String generateRandomAlphanumericString() {
    String alphanumeric = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    StringBuilder sb = new StringBuilder(6);
    for (int i = 0; i < 6; i++) { //3
      int index = random.nextInt(alphanumeric.length());
      sb.append(alphanumeric.charAt(index)); //4
    }
    return sb.toString();
  }

  // Method to check if an ID is already taken
  private boolean isIdTaken(String id) {
    return members.containsKey(id);
  }

  /**
  * Deletes a member from the collection of members using the provided member ID.
  * The method will only proceed if the provided member ID is not null.
  *
  * @param memberId The unique identifier of the member to be deleted. Should not be null.
  * @return Returns true if the member ID is not null (indicating a deletion attempt was made), 
  *         false otherwise. Note that this does not guarantee the member was found and deleted.
  */
  //bug3,4 claude
  public boolean deleteMember(String memberId) {
    if (memberId != null) { //5
      members.remove(memberId);
      return true;
    }
    return false; //6
  }

  /**
  * Checks if the given email address is unique among all members.
  *
  * @param email The email address to check for uniqueness.
  * @return true if the email is unique, false otherwise.
  */
  //bug4,5 claude
  public boolean isEmailUnique(String email) {
    for (Member member : members.values()) {
      if (member.getEmail().equalsIgnoreCase(email)) { //7
        return false;
      }
    }
    return true; //8
  }

  /**
  * Checks if the given phone number is unique among all members.
  *
  * @param phoneNumber The phone number to check for uniqueness.
  * @return true if the phone number is unique, false otherwise.
  */
  //bug5 claude
  public boolean isPhoneNumberUnique(String phoneNumber) {
    for (Member member : members.values()) {
      if (member.getMobilePhone().equals(phoneNumber)) {
        return false; //9
      }
    }
    return true;
  }


  public Map<String, Member> listAllMembers() {
    return new HashMap<>(members);
  }

  // Method to get full information about a specific member by ID
  public Member getMemberById(String id) {
    return members.get(id);
  }

  /**
  * Retrieves details of all members in the form of a list of strings.
  * Each string contains the name, email, and ID of a member.
  *
  * @return A list of strings, each representing the details of a member.
  */
  //bug 6 claude
  public List<String> listMemberEmailId() {
    List<String> memberDetailsList = new ArrayList<>();
    for (Member member : members.values()) {
      String detail = "Name: " + member.getName() + ", Email: " + member.getEmail() + ", ID: " + member.getId();
      memberDetailsList.add(detail); //10
    }
    return memberDetailsList;
  }

  /**
  * Retrieves details of a member by their unique ID.
  *
  * @param memberId The unique identifier of the member.
  * @return A string containing the member's name, email, current credits, and owned items,
  *         or null if no member with the specified ID is found.
  */
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

  /**
  * Checks if the provided email address exists in the collection of members.
  *
  * @param email The email address to be checked. This should be a non-null, non-empty string.
  * @return Returns true if an existing member with the provided email address is found, false otherwise.
  */
  //bug7,8 claude
  public boolean doesEmailExist(String email) {
    for (Member member : members.values()) {
      if (member.getEmail().equalsIgnoreCase(email)) {
        return true;  // Email exists //11
      }
    }
    return false;  // Email does not exist
  }

  /**
  * Retrieves a simple list of details for all members.
  *
  * @return A list of strings, each containing the member's name, email, current credits, and the number of owned items.
  */
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

  /**
  * Retrieves a list of items associated with the given member ID.
  *
  * @param memberId The unique identifier of the member whose items are to be retrieved. Should not be null.
  * @return A List of Item objects associated with the given member ID. 
  *         Returns null if there is no member with the provided ID.
  */
  public List<Item> getItemsByMemberId(String memberId) {
    Member member = members.get(memberId);
    if (member != null) {
      return member.getItems();
    }
    return null;
  }

  /**
  * Updates the information of a member identified by the given member ID.
     If any of the new information fields are empty, 
  * the corresponding attribute is not updated.
  *
  * @param memberId The unique identifier of the member to be updated. Should not be null.
  * @param newName The new name to be set for the member. If empty, the member's name is not updated.
  * @param newEmail The new email to be set for the member. If empty, the member's email is not updated.
  * @param newPhoneNumber The new phone number to be set for the member. 
    If empty, the member's phone number is not updated.
  * @return Returns true if a member with the provided ID is found and updated, false otherwise.
  */
  //bug8.9.10 claude
  public boolean updateMemberInformation(String memberId, String newName, String newEmail, String newPhoneNumber) {
    Member oldMember = members.get(memberId);
    if (oldMember != null) { //12
  
      if (newName.isEmpty()) {
        newName = oldMember.getName();
      }
      if (newEmail.isEmpty()) {
        newEmail = oldMember.getEmail();
      }
      if (newPhoneNumber.isEmpty()) {
        newPhoneNumber = oldMember.getMobilePhone();
      }
      Member newmember = new Member(newName, newEmail, newPhoneNumber, memberId);
      newmember.setItems(oldMember.getItems());
      newmember.setCredits(oldMember.getCredits()); //13

      members.put(memberId, newmember);
      return true; //14
    }
    return false;
  }


  /**
  * Retrieves the member ID associated with the given email.
  *
  * @param email The email of the member. Case-insensitive.
  * @return The member ID if a member with the provided email is found, null otherwise.
  */
  public String getIdByEmail(String email) {
    for (Map.Entry<String, Member> entry : members.entrySet()) {
      if (entry.getValue().getEmail().equalsIgnoreCase(email)) {
        return entry.getKey();
      }
    }
    return null;
  }

  /**
  * Checks if an item with the given name exists among all members.
  *
  * @param itemName The name of the item to be searched for. Case-insensitive.
  * @return Returns true if an item with the provided name is found, false otherwise.
  */
  //bug9 claude
  public boolean itemExists(String itemName) {
    for (Member member : members.values()) {
      for (Item item : member.getItems()) {
        if (item.getName().equalsIgnoreCase(itemName)) {
          return true; //15
        }
      }
    }
    return false;
  }

  /**
  * Checks if an item with the given name is available within the specified date range.
  *
  * @param itemName The name of the item to be checked. Case-insensitive.
  * @param startDate The start date of the period for which the item's availability is checked.
  * @param endDate The end date of the period for which the item's availability is checked.
  * @return Returns true if the item with the provided name is available, false otherwise.
  */
  //bug10 ,11 claude
  public boolean itemIsAvailable(String itemName, int startDate, int endDate) {
    for (Member member : members.values()) {
      for (Item item : member.getItems()) {
        if (item.getName().equalsIgnoreCase(itemName) && item.isItemAvailable(startDate, endDate)) {
          return true; //16
        }
      }
    }
    return false; //17
  }

  /**
 * Checks if a borrower has enough credits to borrow a specific item within a given date range.
 *
 * @param lenderId The ID of the lender.
 * @param borrowerId The ID of the borrower.
 * @param itemName The name of the item to be borrowed.
 * @param startDate The start date of the borrowing period.
 * @param endDate The end date of the borrowing period.
 * @return Returns true if the borrower has enough credits, false otherwise.
 */
//bug11,12,13 claude failed detection
  public boolean borrowerHasEnoughCredits(String lenderId, 
                         String borrowerId, String itemName, int startDate, int endDate) {
    Member borrower = members.get(borrowerId);
    Item item = findItemByName(itemName);
    int totalCost = (endDate - startDate + 1) * item.getCostPerDay(); //18,19
    int availableCredits = borrower.getCredits();
    return availableCredits >= totalCost; //20
  }

  public List<Member> getAllMembers() {
    return new ArrayList<>(members.values());
  }

  /**
  * Generates a list of strings with detailed information about members and their owned items.
  *
  * @return List of detailed member information strings.
  */
  //bug12,,13,14,15 Claude Faild
  public List<String> getAllMembersVerboseDetails() {
    List<String> memberDetailsList = new ArrayList<>();

    for (Member member : members.values()) {
      StringBuilder details = new StringBuilder();
      details.append("Name: ").append(member.getName()); //21
      details.append(", Email: ").append(member.getEmail()); //22
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

  /**
  * Retrieves and formats details of all items owned by each member.
  *
  * @return a list of strings with details of items owned by each member
  */
  //bug13,14 claude
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
          details.append(", Category: ").append(item.getCategory()); //23
          details.append(", Description: ").append(item.getDescription()); //24
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

  /**
 * Adds a new item to a member's list of items.
 *
 * @param memberId The unique identifier of the member.
 * @param category The category of the new item.
 * @param name The name of the new item.
 * @param description A description of the new item.
 * @param costPerDay The cost per day to rent the new item.
 * @return true if the item was successfully added, false otherwise.
 */
//bug 2 claude
  public boolean addItemToMember(String memberId, String category, String name, String description, int costPerDay) {
    Member owningMember = getMemberById(memberId);
    if (owningMember != null) { //25
      Item newItem = new Item(category, name, description, costPerDay);
      owningMember.addItem(newItem);
      return true; //26
    }
    return false;
  }

  public boolean memberExists(String memberId) {
    return members.containsKey(memberId);
  }
  
  /**
 * Removes an item from a member's list of items.
 *
 * @param memberId The unique identifier of the member.
 * @param itemName The name of the item to be removed.
 * @return true if the item was successfully removed, false otherwise.
 */
  public boolean removeItemFromMember(String memberId, String itemName) {
    Member member = getMemberById(memberId);
    if (member != null) {
      return member.removeItemByName(itemName);
    }
    return false;
  }

  /**
  * Updates the details of an existing item of a member.
  *
  * @param memberId The unique identifier of the member who owns the item.
  * @param itemName The current name of the item to be updated.
  * @param newCategory The new category to be set for the item.
  * @param newName The new name to be set for the item.
  * @param newDescription The new description to be set for the item.
  * @param newCostPerDay The new cost per day to be set for the item.
  * @return true if the item was successfully updated, false otherwise.
  */
  public boolean updateItemOfMember(String memberId,
          String itemName, String newCategory, String newName, String newDescription, int newCostPerDay) {
    Item itemToUpdate = findItemByName(itemName);
    if (itemToUpdate != null) {
      itemToUpdate.updateItem(newCategory, newName, newDescription, newCostPerDay);
      return true;
    }
    return false;
  }

  /**
  * Validates that both member and item are available for creating a new contract.
  *
  * @param borrowerEmail The email of the member who wishes to borrow the item.
  * @param lenderEmail The email of the member who owns the item.
  * @param itemName The name of the item to be borrowed.
  * @param startDate The start date of the intended borrowing period.
  * @param endDate The end date of the intended borrowing period.
  * @return true if both member and item are valid and available, false otherwise.
  */
  public boolean validateMemberAndItem(String borrowerEmail,
             String lenderEmail, String itemName, int startDate, int endDate) {
    String borrowerId = getIdByEmail(borrowerEmail);
    String lenderId = getIdByEmail(lenderEmail);
    Item item = findItemByName(itemName);
    return item != null 
      && item.isItemAvailable(startDate, endDate) 
      && borrowerHasEnoughCredits(lenderId, borrowerId, itemName, startDate, endDate);
  }

  /**
  * Creates a new contract for the borrowing of an item.
  *
  * @param lenderEmail The email of the member who owns the item.
  * @param borrowerEmail The email of the member who wishes to borrow the item.
  * @param itemName The name of the item to be borrowed.
  * @param startDate The start date of the borrowing period.
  * @param endDate The end date of the borrowing period.
  * @param timee An object representing the time aspect of the contract.
  * @return A new Contract object.
  */
  public Contract createNewContract(String lenderEmail, String borrowerEmail,
      String itemName, int startDate, int endDate, Timee timee) {
  
    // Retrieve the lender and borrower Member objects using their email addresses
    Member lender = members.values().stream()
                           .filter(member -> member.getEmail().equalsIgnoreCase(lenderEmail))
                           .findFirst()
                           .orElse(null);
    Member borrower = members.values().stream()
                             .filter(member -> member.getEmail().equalsIgnoreCase(borrowerEmail))
                             .findFirst()
                             .orElse(null);
    
    // Assuming that findItemByName can be used to find an item from all members and not just one
    Item item = findItemByName(itemName);
    
    if (!validateMemberAndItem(borrowerEmail, lenderEmail, itemName, startDate, endDate)) {
      return null;
    }
    
    // Create a new Contract using the retrieved lender, borrower, and item
    Contract newContract = new Contract(lender, borrower, item, startDate, endDate, timee);
    
    // Associate the contract with the item
    newContract.addContractToItem(item);
    
    return newContract;
  }
    
  /**
   * Searches for an item by its name across all members.
   * The search is case-insensitive, meaning it does not consider case when comparing the item name.
   *
   * @param itemName The name of the item to search for.
   * @return The {@code Item} object if found, {@code null} otherwise.
   */
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

