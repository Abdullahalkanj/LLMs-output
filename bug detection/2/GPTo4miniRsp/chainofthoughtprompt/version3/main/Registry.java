package main;

import main.Boat.BoatType;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class Registry {
    private static final String ID_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private List<Member> members = new ArrayList<>();
    private Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8);
    private FileManager fileManager;
    private String filePath;
//updated
  public Registry(String filePath) {
      this.filePath = filePath;
      this.fileManager = new FileManager(filePath);
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
  private String generateUniqueMemberId() {
        StringBuilder sb = new StringBuilder(6);
        // Try up to, say, 1000 times to avoid infinite loops:
        for (int attempt = 0; attempt < 1000; attempt++) {
            sb.setLength(0);
            for (int i = 0; i < 6; i++) {
                int idx = ThreadLocalRandom.current().nextInt(ID_CHARS.length());
                sb.append(ID_CHARS.charAt(idx));
            }
            String candidate = sb.toString();
            if (!memberIdExists(candidate)) {
                return candidate;
            }
        }
        throw new IllegalStateException("Unable to generate unique member ID after many attempts.");
  }
//updated
  private boolean memberIdExists(String candidateId) {
        return members.stream()
                      .anyMatch(m -> candidateId.equals(m.getMemberId()));
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
  public void addNewMember(String name, String email) {
        if (emailExists(email)) {
            throw new IllegalArgumentException("This email address is already used.");
        }
        Member newMember = new Member(name, email);
        newMember.setMemberId(generateUniqueMemberId());
        members.add(newMember);
  }   
//updated
  public void addMemberFromUserInput() {
        System.out.print("Enter name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Enter email: ");
        String email = scanner.nextLine().trim();
        try {
            addNewMember(name, email);
            System.out.println("Member added.");
        } catch (IllegalArgumentException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }
//updated
  private boolean emailExists(String email) {
        if (email == null || email.isBlank()) return false;
        return members.stream()
                      .map(Member::getEmail)
                      .anyMatch(e -> e != null && e.equalsIgnoreCase(email));
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
  public void addBoatFromUserInput(String memberId) {
        Member owner = getMemberById(memberId);
        if (owner == null) {
            System.out.println("Member not found.");
            return;
        }
        try {
            System.out.print("Boat name: ");
            String name = scanner.nextLine().trim();
            System.out.print("Type (sailboat, motorboat, motorsailer, canoe): ");
            BoatType type = BoatType.valueOf(scanner.nextLine().trim().toUpperCase());
            System.out.print("Length (m): ");
            int length = Integer.parseInt(scanner.nextLine().trim());

            int depth = 0, power = 0;
            if (type == BoatType.SAILBOAT || type == BoatType.MOTORSAILER) {
                System.out.print("Depth (m): ");
                depth = Integer.parseInt(scanner.nextLine().trim());
            }
            if (type == BoatType.MOTORBOAT || type == BoatType.MOTORSAILER) {
                System.out.print("Engine power (HP): ");
                power = Integer.parseInt(scanner.nextLine().trim());
            }

            Boat boat = new Boat(name, type, owner, length, depth, power);
            owner.addBoat(boat);
            System.out.println("Boat added.");
        } catch (IllegalArgumentException | NullPointerException ex) {
            System.out.println("Invalid boat type.");
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
//new
  public void close() {
        scanner.close();
  }
}