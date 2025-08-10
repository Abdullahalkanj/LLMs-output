package main;

import main.Boat.BoatType;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Handles file operations for storing and retrieving members and their boats.
 */
public class FileManager {
  private String filename;

  /**
  * Initializes a new FileManager instance.
  *
  * @param filename The name of the file to manage.
  */
  public FileManager(String filename) {
    this.filename = filename;
    File file = new File(this.filename);
    if (!file.exists()) {
      try {
        file.createNewFile();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public String getFilename() {
    return filename;
  }

  public void setFilename(String filename) {
    this.filename = filename;
  }

  /**
  * Loads members into the provided registry from the file.
  *
 * @param registry The registry to load members into.
 */
  public void loadMembersIntoRegistry(Registry registry) {
    File file = new File(filename);
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file),
                          StandardCharsets.UTF_8))) {
      String line;
      Member currentMember = null;
      while ((line = reader.readLine()) != null) {
        String[] parts = line.split(":");
        if (parts[0].equals("MEMBER")) {
          currentMember = new Member(parts[1], parts[2]);
          currentMember.setMemberId(parts[3]);
          registry.addMember(currentMember);
        } else if (parts[0].equals("BOAT") && currentMember != null) {
          BoatType type = BoatType.valueOf(parts[2].toUpperCase());
          Boat boat = new Boat(parts[1], type, currentMember, Integer.parseInt(parts[3]),
                                     parts.length > 4 ? Integer.parseInt(parts[4]) : 0,
                                     parts.length > 5 ? Integer.parseInt(parts[5]) : 0);
          currentMember.addBoat(boat);
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
  * Saves the provided list of members to the file.
  *
  * @param members The list of members to save.
  */
  public void saveToFile(List<Member> members) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
      for (Member member : members) {
        writer.write("MEMBER:" + member.getName() + ":" 
                  + (member.getEmail() != null ? member.getEmail() : "") + ":" + member.getMemberId());
        writer.newLine();
  
        // Write the boats owned by this member
        for (Boat boat : member.getBoats()) {
          writer.write("BOAT:" + boat.getName() + ":" 
                        + boat.getType().toString().toLowerCase() + ":" + boat.getLength());
  
          switch (boat.getType()) {
            case SAILBOAT:
              writer.write(":" + boat.getDepth() + "");
              break;
            case MOTORBOAT:
              writer.write(":" + boat.getEnginePower() + "");
              break;
            case MOTORSAILER:
              writer.write(":" + boat.getDepth() + ":" + boat.getEnginePower() + "");
              break;
            case CANOE:
              // No additional information for canoe
              break;
            default:
              break;
          }
          writer.newLine();
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
