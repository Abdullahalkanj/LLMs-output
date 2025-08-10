package model2;

import java.util.ArrayList;
import java.util.List;

/**
 * Item represents an individual item with various attributes such as name, category, description, and cost per day.
 * Each item also has an associated creation date.
 */
public class Item {
  private String name;
  private String category;
  private String description;
  private int costPerDay;
  private List<Contract> contracts = new ArrayList<>();
  private boolean isAvailable;

  /**
  * Initializes a new instance of the Item class with the specified parameters.
  *
  * @param category The category of the item.
  * @param name The name of the item.
  * @param description A description of the item.
  * @param costPerDay The cost per day for renting or using the item.
  */
  public Item(String category, String name, String description, int costPerDay) {
    setName(name);
    setCategory(category);
    setDescription(description);
    setCostPerDay(costPerDay);
  }

  public String getName() {
    return name;
  }  

  private void setName(String name) {
    this.name = name;
  }

  public String getCategory() {
    return category;
  }

  private void setCategory(String category) {
    this.category = category;
  }

  public String getDescription() {
    return description;
  }

  private void setDescription(String description) {
    this.description = description;
  }

  public int getCostPerDay() {
    return costPerDay;
  }

  private void setCostPerDay(int costPerDay) {
    this.costPerDay = costPerDay;
  }  

  /**
 * Checks if an item is available for a given date range by comparing it with existing contracts.
 *
 * @param startDate The start date for checking availability, 
 *     represented as an integer. The start date should be less than or equal to the end date.
 * @param endDate The end date for checking availability, represented as an integer.
 * @return Returns true if the item is available for the entire date range specified; false otherwise.
 */
  public boolean isItemAvailable(int startDate, int endDate) {
    if (startDate > endDate) { 
      return false;
    }
    for (Contract contract : contracts) {
      if (!(endDate < contract.getStartDate() || startDate > contract.getEndDate())) {
        return false;
      }
    }
    return true;
  }
  
  public void setAvailable(boolean isAvailable) {
    this.isAvailable = isAvailable;
  }

  public boolean canBeLent() {
    return isAvailable && contracts.isEmpty(); // or any other condition you want to check
  }

  /**
 * Updates the item with the given details. 
 *  Each parameter is optional; if a parameter is null or invalid (e.g., an empty string for strings,
 *  or a non-positive integer for costPerDay), the corresponding attribute of the item is not updated.
 *
 * @param category The new category of the item. If null or empty, the category is not updated.
 * @param name The new name of the item. If null or empty, the name is not updated.
 * @param description The new description of the item. If null or empty, the description is not updated.
 * @param costPerDay The new cost per day of the item. If less than or equal to zero, the cost per day is not updated.
 */
  public void updateItem(String category, String name, String description, int costPerDay) {
    if (category != null && !category.isEmpty()) {
      this.category = category;
    }
    if (name != null && !name.isEmpty()) {
      this.name = name;
    }
    if (description != null && !description.isEmpty()) {
      this.description = description;
    }
    if (costPerDay > 0) {
      this.costPerDay = costPerDay;
    }
  }

  public void addContract(Contract contract) {
    contracts.add(contract);
  }

  public List<Contract> getContracts() {
    return new ArrayList<>(contracts);
  }
}

