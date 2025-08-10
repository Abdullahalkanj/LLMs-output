package main;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Member of a boating club or organization.
 * Each member has a unique identifier, name, email, and a list of boats.
 */

public class Member {
  private String memberId;
  private String name;
  private String email;
  private List<Boat> boats;

  /**
   * Creates a new Member object.
   *
   * @param name The name of the member.
   * @param email The email of the member.
   */

  public Member(String name, String email) {
    this.boats = new ArrayList<>();
    setName(name);
    setEmail(email);
  }
  
  public Member(Member owner) {
  }

  public String getMemberId() {
    return memberId;
  }

  public String getName() {
    return name;
  }
  
  public String getEmail() {
    return email;
  }

  /**
  * Retrieves the list of boats. 
  * If boats are not initialized, returns an empty list.
  *
  * @return A list of boats or an empty list if boats are null.
  */
  public List<Boat> getBoats() {
    if (this.boats == null) {
      return new ArrayList<>();
    }
    return this.boats;
  }

  private void setName(String name) {
    this.name  = name;
  }
  
  private void setEmail(String email) {
    this.email = email;
  }

  protected void setMemberId(String memberId) {
    this.memberId = memberId;
  }

  public void addBoat(Boat boat) {
    boats.add(boat);
  }
}
