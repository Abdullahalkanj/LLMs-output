package main;

import main.Boat.BoatType;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Pattern;

public class Registry {
  private static final String ID_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
  private List<Member> members;
  private Scanner scanner;
  private FileManager fileManager;
  private String filePath;
  private static final Pattern EMAIL_PATTERN =
    Pattern.compile("^[\\w.%+\\-]+@[\\w.\\-]+\\.[A-Za-z]{2,6}$");
  
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
//updated
  /** BUGFIX: initialize alphanumeric and clear StringBuilder each loop. */
  private String generateUniqueMemberId() {
    Random rnd = new Random();
    StringBuilder sb = new StringBuilder(6);

    while (true) {
      sb.setLength(0);  // BUGFIX: clear previous candidate
      for (int i = 0; i < 6; i++) {
        sb.append(ID_CHARS.charAt(rnd.nextInt(ID_CHARS.length())));
      }
      String candidateId = sb.toString();
      if (!memberIdExists(candidateId)) {
        return candidateId;
      }
      // loop again if collision
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
//updated
  /** IMPROV: validate email format, reject blank. */
  public void addNewMember(String name, String email) {
    if (email == null || email.isBlank() || !EMAIL_PATTERN.matcher(email).matches()) {
      throw new IllegalArgumentException("Please provide a valid email address.");
    }
    if (emailExists(email)) {
      throw new IllegalArgumentException("This email address is already used.");
    }
    Member newMember = new Member(name, email);
    newMember.setMemberId(generateUniqueMemberId());
    members.add(newMember);
  }
//updated
  public void addMemberFromUserInput() {
    try {
      System.out.print("Enter name: ");
      String name = scanner.nextLine().trim();

      System.out.print("Enter email: ");
      String email = scanner.nextLine().trim();

      addNewMember(name, email);
      System.out.println("New member added successfully.");
    } catch (IllegalArgumentException e) {
      System.out.println("Error: " + e.getMessage());
    }
  }
//updated
  /** BUGFIX: compare strings with equals(), validate non-null/non-empty. */
  private boolean emailExists(String email) {
    if (email == null || email.isBlank()) {
      return false; 
    }
    for (Member m : members) {
      if (email.equalsIgnoreCase(m.getEmail())) {
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
//updated
  /** IMPROV: guard numeric and enum parsing with retries. */
  public void addBoatFromUserInput(String memberId) {
    Member owner = findMemberById(memberId);
    if (owner == null) {
      System.out.println("Member not found.");
      return;
    }

    System.out.print("Boat name: ");
    String name = scanner.nextLine().trim();

    BoatType type = null;
    while (type == null) {
      System.out.print("Boat type (sailboat, motorboat, motorsailer, canoe): ");
      String input = scanner.nextLine().trim().toUpperCase();
      try {
        type = BoatType.valueOf(input);
      } catch (IllegalArgumentException ex) {
        System.out.println("Invalid type; please try again.");
      }
    }

    int length = promptForInt("Length in meters: ");

    Integer depth = null;
    if (type == BoatType.SAILBOAT || type == BoatType.MOTORSAILER) {
      depth = promptForInt("Depth in meters: ");
    }

    Integer enginePower = null;
    if (type == BoatType.MOTORBOAT || type == BoatType.MOTORSAILER) {
      enginePower = promptForInt("Engine power in HP: ");
    }

    Boat newBoat = new Boat(name, type, owner, length,
                            depth != null ? depth : 0,
                            enginePower != null ? enginePower : 0);
    owner.addBoat(newBoat);
    System.out.println("New boat added successfully.");
  }

  /** Helper to keep prompting until a valid integer is entered. */
  private int promptForInt(String prompt) {
    while (true) {
      System.out.print(prompt);
      String line = scanner.nextLine().trim();
      try {
        return Integer.parseInt(line);
      } catch (NumberFormatException e) {
        System.out.println("Please enter a valid integer.");
      }
    }
  }
//new
  public boolean displayDetails(String boatName) {
    Boat b = findBoatByName(boatName);
    if (b == null) {
      System.out.println("No boat found with that name.");
      return false;
    }
    System.out.println("Boat Name: " + b.getName());
    System.out.println("Type: " + b.getType());
    System.out.println("Length: " + b.getLength() + " m");
    // BUGFIX: simply check > 0 for ints
    if (b.getEnginePower() > 0) {
      System.out.println("Engine Power: " + b.getEnginePower() + " HP");
    }
    if (b.getDepth() > 0) {
      System.out.println("Depth: " + b.getDepth() + " m");
    }
    return true;
  }
//new
  /** IMPROV: call at shutdown if desired. */
  public void closeScanner() {
    if (scanner != null) {
      scanner.close();
    }
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