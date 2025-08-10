package assignment4;

import assignment4.Boat.BoatType;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;


/**
 * Represents a Registry that manages a list of Members.
 * The Registry class also handles interactions with a file for persistent storage of membership information.
 */

public class Registry {
  private List<Member> members;
  private Scanner scanner;
  private FileManager fileManager;
  private String filePath;

  /**
  * Initializes a new Registry instance.
  *
  * @param filePath The path to the file for storing and retrieving member data.
  */
  public Registry(String filePath) {
    this.filePath = filePath;
    this.fileManager = new FileManager(filePath);
    members = new ArrayList<>();
    scanner = new Scanner(System.in, StandardCharsets.UTF_8);
  }

  public List<Member> getMembers() {
    return new ArrayList<>(members);
  }

  public String getFilePath() {
    return filePath;
  }

  public void setFilePath(String filePath) {
    this.filePath = filePath;
  }

  private String generateUniqueMemberId() {
    String alphanumeric = "abcdefghijklmnopqrstuvwxyz0123456789";
    Random rnd = new Random();
    StringBuilder sb = new StringBuilder(6);

    while (true) {
      for (int i = 0; i < 6; i++) {
        sb.append(alphanumeric.charAt(rnd.nextInt(alphanumeric.length())));
      }
      String candidateId = sb.toString();
      if (!memberIdExists(candidateId)) {
        return candidateId;
      }
      sb.setLength(0); // Clear the StringBuilder to generate a new candidate
    }
  }

  /**
 * Checks if a given candidate ID exists among the list of members.
 *
 * @param candidateId the ID to check for existence among members
 * @return true if the ID exists, false otherwise
 */
  private boolean memberIdExists(String candidateId) {
    for (Member member : members) {
      if (member.getMemberId().equals(candidateId)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Deletes a specified member from the registry.
   *
   * @param member The member to be deleted.
   */
  public void deleteMember(Member member) {
    members.remove(member);
  }
  
  /**
   * Adds a new member to the registry.
   * The email of the new member should be unique, or else an IllegalArgumentException is thrown.
   *
   * @param member The new member to be added.
   * @throws IllegalArgumentException if a member with the same email already exists.
   */
  public void addMember(Member member) {
    if (emailExists(member.getEmail())) {
      throw new IllegalArgumentException("This email address is already used by another member.");
    }
    members.add(member);
  }


  public List<Member> listMembers() {
    return new ArrayList<>(members);
  }

  /**
   * Returns a member that matches the given id.
   * If no match is found, it returns null.
   *
   * @param id The id to be matched.
   * @return The matched member or null.
   */
  public Member getMemberById(String id) {
    for (Member member : members) {
      if (member.getMemberId().equals(id)) {
        return member;
      }
    }
    return null;
  }

  /**
   * Adds a new member with a given name and email.
   * An IllegalArgumentException is thrown if the email already exists.
   *
   * @param name The name of the new member.
   * @param email The email of the new member.
   * @throws IllegalArgumentException if the email already exists.
   */
  public void addNewMember(String name, String email) {
    if (emailExists(email)) {
      throw new IllegalArgumentException("This email address is already used by another member.");
    }

    // Create a new Member object
    Member newMember = new Member(name, email);
    // Generate and set a unique memberId for this member
    newMember.setMemberId(generateUniqueMemberId());

    // Add the new Member to the list
    members.add(newMember); 
  }

  /**
   * Interacts with the user to get the details of a new member, and adds the member to the registry.
   * If the member's email already exists, an error message is displayed.
   */
  public void addMemberFromUserInput() {
    System.out.println("Enter the name of the new member:");
    String name = scanner.nextLine();
    System.out.println("Enter the email of the new member:");
    String email = scanner.nextLine();

    try {
      addNewMember(name, email);
      System.out.println("New member added successfully.");
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
    }
  }

  /**
   * Checks whether a given email exists in the list of members.
   *
   * @param email The email to be checked.
   * @return True if the email exists, false otherwise.
   */
  private boolean emailExists(String email) {
    if (email == null || email.isEmpty()) {
      return false;
    }
    for (Member member : members) {
      if (email.equals(member.getEmail())) {
        return true;
      }
    }
    return false;
  }

  /**
   * Displays the names of all members in the console.
   * Each member is numbered in the order they are found in the list.
   */
  public void displayMembers() {
    System.out.println("Members:");
    int counter = 1;
    for (Member member : members) {
      System.out.println(counter + " - " + member.getName());
      counter++;
    }
  }

  /**
  * Searches for and returns a member based on the provided member ID.
  *
  * @param memberId The ID of the member to search for.
  * @return The Member object if found, or null if not.
  */
  public Member findMemberById(String memberId) {
    for (Member member : members) {
      if (member.getMemberId().equals(memberId)) {
        return member;
      }
    }
    return null; // Return null if member with the given ID is not found
  }

  /**
  * Adds a new boat to a member based on user input. The user will be prompted
  * to provide boat details such as name, type, length, depth, and engine power.
  *
  * @param memberId The ID of the member to whom the boat will be added.
  */
  public void addBoatFromUserInput(String memberId) {
    Member owner = findMemberById(memberId);
    if (owner == null) {
      System.out.println("Member not found.");
      return;
    }
    System.out.println("Enter the name of the boat:");
    String name = scanner.nextLine();
    System.out.println("Enter the type of the boat (sailboat, motorboat, motorsailer, canoe):");
    String typeString = scanner.nextLine();
    BoatType type = BoatType.valueOf(typeString.toUpperCase());
    int length = 0;
    int depth = 0;
    int enginePower = 0;
    System.out.println("Enter the length of the boat in meters:");
    length = Integer.parseInt(scanner.nextLine());
    if (type == BoatType.SAILBOAT || type == BoatType.MOTORSAILER) {
      System.out.println("Enter the depth of the boat in meters:");
      depth = Integer.parseInt(scanner.nextLine());
    }
    if (type == BoatType.MOTORBOAT || type == BoatType.MOTORSAILER) {
      System.out.println("Enter the engine power of the boat in horsepower:");
      enginePower = Integer.parseInt(scanner.nextLine());
    }
    Boat newBoat = new Boat(name, type, owner, length, depth, enginePower);
    owner.addBoat(newBoat);
    System.out.println("New boat added successfully.");
  }

  /**
  * Finds a member by their name.
  *
  * @param memberName The name of the member to search for
  * @return The member with the given name, or null if not found
  */
  public Member findMemberByName(String memberName) {
    for (Member member : members) {
      if (member.getName().equals(memberName)) {
        return member;
      }
    }
    return null; // Return null if the member with the given name is not found
  }

  public void loadData() {
    fileManager.loadMembersIntoRegistry(this);
  }

  public void saveData() {
    FileManager fileManager = new FileManager(filePath);
    fileManager.saveToFile(members);
  }

  /**
 * Displays a list of all members along with their IDs and emails.
 * If a member does not have an email, it displays "no email".
 */
  public void displayMembersWithEmail() {
    System.out.println("Members with their IDs and emails:");
    for (Member member : members) {
      String email = member.getEmail();
      if (email == null || email.isEmpty()) {
        email = "no email";
      }
      System.out.println("ID: " + member.getMemberId() + " - Name: " + member.getName() + " - Email: " + email);
    }
  }

  /**
  * Lists all the boats for a specified member. If the member does not have any boats,
  * it notifies that the member has no boats.
  *
  * @param member The member whose boats will be listed.
  */
  public void listBoatsForMember(Member member) {
    List<Boat> boats = member.getBoats();
    
    if (boats == null || boats.isEmpty()) {
      System.out.println(member.getName() + " has no boats.");
      return;
    }
    
    System.out.println(member.getName() + "'s boats:");
    for (int i = 0; i < boats.size(); i++) {
      Boat boat = boats.get(i);
      StringBuilder boatDetails = new StringBuilder((i + 1) + ") ");
      boatDetails.append(boat.getName())
                   .append(" - Type: ").append(boat.getType())
                   .append(", Length: ").append(boat.getLength()).append(" meters");

      // If it's a sailboat or motorsailer, display depth
      if (boat.getType() == BoatType.SAILBOAT || boat.getType() == BoatType.MOTORSAILER) {
        boatDetails.append(", Depth: ").append(boat.getDepth()).append(" meters");
      }

      // If it's a motorboat or motorsailer, display engine power
      if (boat.getType() == BoatType.MOTORBOAT || boat.getType() == BoatType.MOTORSAILER) {
        boatDetails.append(", Engine Power: ").append(boat.getEnginePower()).append(" HP");
      }

      System.out.println(boatDetails);
    }
  }

  /**
  * Display the boats owned by the given member.
  *
  * @param member The member whose boats will be displayed.
  */
  public void displayBoatsForMember(Member member) {
    // Check if the member has any boats
    if (member.getBoats().isEmpty()) {
      System.out.println(member.getName() + " has no boats.");
      return;
    }
    System.out.println(member.getName() + "'s Boats:");
    for (Boat boat : member.getBoats()) {
      System.out.println("Name: " + boat.getName());
      System.out.println("Type: " + boat.getType());
      System.out.println("Length: " + boat.getLength() + " meters");

      if (boat.getType() == BoatType.SAILBOAT || boat.getType() == BoatType.MOTORSAILER) {
        System.out.println("Depth: " + boat.getDepth() + " meters");
      }
      
      if (boat.getType() == BoatType.MOTORBOAT || boat.getType() == BoatType.MOTORSAILER) {
        System.out.println("Engine Power: " + boat.getEnginePower() + " horsepower");
      }
      
      System.out.println("------------------------------");
    }
  }

  /**
  * Searches for and returns a boat based on the provided boat name.
  * If there's no boat with the specified name, it returns null.
  *
  * @param boatName The name of the boat to search for.
  * @return The Boat object if found, or null if not.
  */
  public Boat findBoatByName(String boatName) {
    for (Member member : members) {
      for (Boat boat : member.getBoats()) {
        if (boat.getName().equals(boatName)) {
          return boat;
        }
      }
    }
    return null;
  }

  /**
  * Displays detailed information of a boat based on its name.
  *
  * @param boatName The name of the boat whose details are to be displayed.
  * @return true if the boat details were found and displayed, false otherwise.
  */
  public boolean displayDetails(String boatName) {
    Boat selectedBoat = findBoatByName(boatName);
    if (selectedBoat == null) {
      System.out.println("No boat found with the given name.");
      return false;
    }
    System.out.println("Boat Name: " + selectedBoat.getName());
    System.out.println("Boat Type: " + selectedBoat.getType());
    System.out.println("Boat Length: " + selectedBoat.getLength());
    
    if (Math.abs(selectedBoat.getEnginePower() - 0.0) > 1e-9) {
      System.out.println("Engine Power: " + selectedBoat.getEnginePower());
    }

    if (Math.abs(selectedBoat.getDepth() - 0.0) > 1e-9) {
      System.out.println("Depth: " + selectedBoat.getDepth());
    }
    return true;
  }

  /**
  * Deletes a boat based on its name from a member's list of boats.
  *
  * @param boatName The name of the boat to be deleted.
  * @return true if the boat was found and deleted, false otherwise.
  */
  public boolean deleteBoatByName(String boatName) {
    for (Member member : members) {
      List<Boat> boats = member.getBoats();
      for (int i = 0; i < boats.size(); i++) {
        if (boats.get(i).getName().equals(boatName)) {
          boats.remove(i);
          return true;  // return true if boat found and deleted
        }
      }
    }
    return false;  // Return false if boat wasn't found in any member's list
  }
}

  
