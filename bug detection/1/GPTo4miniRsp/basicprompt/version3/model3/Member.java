package model3;

import java.util.ArrayList;
import java.util.List;

/**
 * Member represents an individual member with various attributes such as ID, name, email, and mobile phone.
 * Each member also has an associated creation date, a credit balance, and a list of items.
 */
public class Member {
  private String id;
  private String name;
  private String email;
  private String mobilePhone;
  private int credits;
  private List<Item> items;

  /**
 * Initializes a new instance of the Member class with the specified parameters.
 * Automatically assigns the creation date to the current date and sets the default credits to 0.
 *
 * @param name The name of the member.
 * @param email The email address of the member.
 * @param mobilePhone The mobile phone number of the member.
 * @param id The unique identifier for the member.
 */
  public Member(String name, String email, String mobilePhone, String id) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.mobilePhone = mobilePhone;
    setCredits(0);
    this.items = new ArrayList<>();
  }
  
  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getEmail() {
    return email;
  }

  public String getMobilePhone() {
    return mobilePhone;
  }

  public int getCredits() {
    return credits;
  }

  protected void setCredits(int credits) {
    this.credits = credits;
  }

  public List<Item> getItems() {
    return new ArrayList<>(items);
  }

  public void setItems(List<Item> items) {
    this.items = new ArrayList<>(items);
  }

  /**
  * Adds an Item to the member's list of items and updates credits and ownership accordingly.
  *
  * @param item The Item object to be added. This should not be null.
  */
  public void addItem(Item item) {
    this.items.add(item);
    this.addCredits(100); // Add 100 credits to this member
  }
  
  /**
  * Removes an item from the member's list of items based on the item's name.
  *
  * @param itemName The name of the item to be removed. This should not be null or empty.
  * @return Returns true if the item is successfully removed, 
       false otherwise (e.g., if no item with the specified name is found).
  */
  public boolean removeItemByName(String itemName) {
    for (Item item : this.items) {
      if (item.getName().equals(itemName)) {
        this.items.remove(item);
        return true;
      }
    }
    return false;
  }

  /**
  * Adds a specified amount of credits to the member's current total, if the amount is positive.
  *
  * @param credits The amount of credits to be added. This should be a positive integer.
  */
  public void addCredits(int credits) {
    if (credits > 0) {
      this.credits += credits;
    }
  }

  /**
  * Deducts a specified amount of credits from the member's current total, without going below zero.
  *
  * @param amount The amount of credits to be deducted. 
       Deduction will only occur if the resulting total is non-negative.
  */
  public void deductCredits(int amount) {
    int newCredits = this.credits - amount; 
    if (newCredits >= 0) {
      this.credits = newCredits;
    } 
  }
}

