// Claude output using Chain-of-thought prompt


// ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
package assignment4;

import assignment4.Boat.BoatType;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Registry {
  private List<Member> members;
  private Scanner scanner;
  private FileManager fileManager;
  private String filePath;

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
    // Fix: Initialize alphanumeric with valid characters instead of null
    String alphanumeric = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
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
      sb.setLength(0);
    }
  }

  private boolean memberIdExists(String candidateId) {
    for (Member member : members) {
      if (member.getMemberId().equals(candidateId)) {
        return true;
      }
    }
    return false;
  }

  public void deleteMember(Member member) {
    members.remove(member);
  }
  
  public void addMember(Member member) {
    if (emailExists(member.getEmail())) {
      throw new IllegalArgumentException("This email address is already used by another member.");
    }
    members.add(member);
  }

  public List<Member> listMembers() {
    return new ArrayList<>(members);
  }

  public Member getMemberById(String id) {
    for (Member member : members) {
      if (member.getMemberId().equals(id)) {
        return member;
      }
    }
    return null;
  }

  public void addNewMember(String name, String email) {
    if (emailExists(email)) {
      throw new IllegalArgumentException("This email address is already used by another member.");
    }

    Member newMember = new Member(name, email);
    newMember.setMemberId(generateUniqueMemberId());
    members.add(newMember); 
  }

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

  private boolean emailExists(String email) {
    if (email == null || email.isEmpty()) {
      return false;
    }
    for (Member member : members) {
      // Fix: Corrected the comparison (removed extra parenthesis and used equals instead of ==)
      if (email.equals(member.getEmail())) {
        return true;
      }
    }
    return false;
  }

  public void displayMembers() {
    System.out.println("Members:");
    int counter = 0;
    for (Member member : members) {
      System.out.println(counter + " - " + member.getName());
      counter++;
    }
  }

  public Member findMemberById(String memberId) {
    for (Member member : members) {
      if (member.getMemberId().equals(memberId)) {
        return member;
      }
    }
    return null;
  }

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

  public Member findMemberByName(String memberName) {
    for (Member member : members) {
      if (member.getName().equals(memberName)) {
        return member;
      }
    }
    return null;
  }

  public void loadData() {
    fileManager.loadMembersIntoRegistry(this);
  }

  public void saveData() {
    FileManager fileManager = new FileManager(filePath);
    fileManager.saveToFile(members);
  }

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

      if (boat.getType() == BoatType.SAILBOAT || boat.getType() == BoatType.MOTORSAILER) {
        boatDetails.append(", Depth: ").append(boat.getDepth()).append(" meters");
      }

      if (boat.getType() == BoatType.MOTORBOAT || boat.getType() == BoatType.MOTORSAILER) {
        boatDetails.append(", Engine Power: ").append(boat.getEnginePower()).append(" HP");
      }

      System.out.println(boatDetails);
    }
  }

  public void displayBoatsForMember(Member member) {
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

  public boolean deleteBoatByName(String boatName) {
    for (Member member : members) {
      List<Boat> boats = member.getBoats();
      for (int i = 0; i < boats.size(); i++) {
        if (boats.get(i).getName().equals(boatName)) {
          boats.remove(i);
          return true;
        }
      }
    }
    return false;
  }
}


// I'll analyze the code and identify any issues step by step.

// ## Analysis of the Registry Class

// Looking at the provided Java code for a boat registry system, I need to methodically check for issues:

// 1. **Imports**: The imports look correct, including necessary classes for the functionality.

// 2. **Class Structure**: The class maintains a list of members, handles file operations, and provides methods for member and boat management.

// 3. **Constructor**: Properly initializes member variables.

// 4. **Method Review**:
//    - Let me check each method for logical errors, null pointer issues, or syntax problems.

// 5. **Specific Method Analysis**:
//    - First, `generateUniqueMemberId()` - There's a major issue here. The `alphanumeric` variable is initialized to null and then used without assignment in the line `sb.append(alphanumeric.charAt(rnd.nextInt(alphanumeric.length())));`. This would cause a `NullPointerException`.

// 6. **Comparison Logic**:
//    - In `emailExists()` method, there's a syntax error in the comparison: `if (email == member.getEmail()))` has:
//      - An extra closing parenthesis
//      - Uses `==` for string comparison instead of `.equals()`

// 7. **Error Handling**:
//    - The code lacks sufficient error handling in several places, like when parsing integers in `addBoatFromUserInput()`.

// ## Primary Issues:

// 1. **NullPointerException in `generateUniqueMemberId()`**:
//    - The `alphanumeric` variable is null and will cause an exception when accessed.

// 2. **String Comparison Error in `emailExists()`**:
//    - Using `==` for string comparison instead of `.equals()`
//    - Extra closing parenthesis causing a syntax error

// Let me create an artifact with the corrected code for these issues:

// ## Explanation of Issues and Fixes

// After analyzing the code, I found two significant issues that needed to be fixed:

// ### 1. NullPointerException in `generateUniqueMemberId()`

// **Issue:** The `alphanumeric` variable was initialized to `null` and then accessed here:
// ```java
// String alphanumeric = null;
// // Later in the code
// sb.append(alphanumeric.charAt(rnd.nextInt(alphanumeric.length())));
// ```
// This would cause a `NullPointerException` since you can't call methods on a null reference.

// **Fix:** I initialized the `alphanumeric` variable with a string of valid characters:
// ```java
// String alphanumeric = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
// ```
// This provides the characters needed to generate a random alphanumeric ID.

// ### 2. String Comparison Error in `emailExists()`

// **Issue:** There were two problems in this line:
// ```java
// if (email == member.getEmail())) {
// ```
// - An extra closing parenthesis `)` causing a syntax error
// - Using `==` for string comparison, which compares object references, not string contents

// **Fix:** Corrected both issues:
// ```java
// if (email.equals(member.getEmail())) {
// ```
// - Removed the extra closing parenthesis
// - Used `.equals()` for proper string content comparison

// These fixes should resolve the immediate issues in the code. The first fix prevents a runtime NullPointerException, and the second fix ensures proper string comparison and fixes a syntax error.

// Would you like me to identify any other potential issues or improvements for this code?
// ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
