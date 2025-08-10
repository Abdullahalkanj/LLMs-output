package main;

import main.Boat.BoatType;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Registry {
  private static final String ALPHANUMERIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"; 
  // Fixed: initialize the pool of characters for ID generation instead of a null String

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
    this.fileManager = new FileManager(filePath); 
    // Fixed: update FileManager whenever the path changes
  }

  private String generateUniqueMemberId() {
    Random rnd = new Random();
    while (true) {
      StringBuilder sb = new StringBuilder(6); 
      // Fixed: create a fresh StringBuilder each iteration
      for (int i = 0; i < 6; i++) {
        sb.append(ALPHANUMERIC.charAt(rnd.nextInt(ALPHANUMERIC.length())));
      }
      String candidateId = sb.toString();
      if (!memberIdExists(candidateId)) {
        return candidateId;
      }
      // no need to reset sb since we recreate it each loop
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
      if (email.equalsIgnoreCase(member.getEmail())) {
        return true; 
        // Fixed: use equalsIgnoreCase (or equals) instead of '==' for string comparison
      }
    }
    return false;
  }

  public void displayMembers() {
    System.out.println("Members:");
    int counter = 1; 
    // Fixed: start listing at 1 (more user-friendly)
    for (Member member : members) {
      System.out.println(counter + " - " + member.getName());
      counter++;
    }
  }

  public Member findMemberById(String memberId) {
    return getMemberById(memberId); 
    // Fixed: delegate to existing method to avoid duplicate logic
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
    BoatType type;
    try {
      type = BoatType.valueOf(typeString.trim().toUpperCase());
    } catch (IllegalArgumentException e) {
      System.out.println("Invalid boat type."); 
      return; 
      // Fixed: handle invalid enum input gracefully
    }
    System.out.println("Enter the length of the boat in meters:");
    int length = Integer.parseInt(scanner.nextLine());
    int depth = 0;
    int enginePower = 0;
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
      if (member.getName().equalsIgnoreCase(memberName)) {
        return member; 
        // Fixed: case-insensitive name match for more forgiving lookup
      }
    }
    return null;
  }

  public void loadData() {
    members.clear(); 
    // Fixed: clear existing members to avoid duplicates on reload
    fileManager.loadMembersIntoRegistry(this);
  }

  public void saveData() {
    fileManager.saveToFile(members); 
    // Fixed: use the existing FileManager instance rather than creating a new one
  }

  public void displayMembersWithEmail() {
    System.out.println("Members with their IDs and emails:");
    for (Member member : members) {
      String email = member.getEmail();
      if (email == null || email.isEmpty()) {
        email = "no email";
      }
      System.out.println(
        "ID: " + member.getMemberId() +
        " - Name: " + member.getName() +
        " - Email: " + email
      );
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
        if (boat.getName().equalsIgnoreCase(boatName)) {
          return boat; 
          // Fixed: case-insensitive match for boat name
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

    if (selectedBoat.getEnginePower() > 0) {
      System.out.println("Engine Power: " + selectedBoat.getEnginePower());
      // Fixed: simple >0 check on int enginePower rather than floating comparison
    }

    if (selectedBoat.getDepth() > 0) {
      System.out.println("Depth: " + selectedBoat.getDepth());
    }
    return true;
  }

  public boolean deleteBoatByName(String boatName) {
    for (Member member : members) {
      List<Boat> boats = member.getBoats();
      for (int i = 0; i < boats.size(); i++) {
        if (boats.get(i).getName().equalsIgnoreCase(boatName)) {
          boats.remove(i);
          return true;
        }
      }
    }
    return false;
  }
}
