package main;

/**
 * Represents a Boat with attributes such as name, type, length, depth, and engine power.
 * Provides methods to get and set these attributes.
 */

public class Boat {
  private String name;
  private BoatType type;
  private int length;
  private int depth;
  private int enginePower;
  private Member owner;

  /**
  * Constructs a new Boat instance with the provided parameters.
  *
  * @param name The name of the boat.
  * @param type The type of the boat. 
  * @param owner The owner of the boat as a Member object.
  * @param length The length of the boat.
  * @param depth The depth of the boat.
  * @param enginePower The engine power of the boat.
  */
  public Boat(String name, BoatType type, Member owner, int length, int depth, int enginePower) {
    setName(name);
    setType(type);
    setLength(length);
    setDepth(depth);
    setEnginePower(enginePower);
    this.owner = new Member(owner);
  }

  /**
   * Enumerates the types of boats that can be represented by the Boat class.
   */

  public enum BoatType {
    SAILBOAT, MOTORBOAT, MOTORSAILER, CANOE
  }

  public String getName() {
    return name;
  }

  public BoatType getType() {
    return type;
  }

  public int getLength() {
    return length;
  }

  public int getDepth() {
    return depth;
  }

  public int getEnginePower() {
    return enginePower;
  }

  public Member getOwner() {
    return new Member(owner);
  }
  
  private void setName(String name) {
    this.name = name;
  }

  private void setType(BoatType type) {
    this.type = type;
  }

  private void setLength(int length) {
    this.length = length;
  }

  private void setDepth(int depth) {
    this.depth = depth;
  }

  private void setEnginePower(int enginePower) {
    this.enginePower = enginePower;
  }

  protected void setOwner(Member owner) {
    this.owner = new Member(owner);
  }
}

