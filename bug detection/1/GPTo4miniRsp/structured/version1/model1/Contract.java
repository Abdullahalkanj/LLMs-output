package model1;

/**
 * The Contract class represents a lending agreement, encapsulating details like lender, borrower, item, 
 * start and end dates, and total cost. It supports operations like cost calculation, contract finalization, 
 * and status checking, interacting with MemberManager and Timee instances as needed.
 */
public class Contract {
  private Member lender;
  private Member borrower;
  private int startDate; 
  private int endDate;    
  private int totalCost;
  private Item item;
  private Timee timee;
  private boolean isFinalized = false;

  /**
 * Constructor for the Contract class.
 *
 * @param lender    The unique identifier of the lender.
 * @param borrower  The unique identifier of the borrower.
 * @param item        The item that is being lent/borrowed.
 * @param startDate   The start date of the contract, represented as an integer.
 * @param endDate     The end date of the contract, represented as an integer.
 * @param timee       An instance of Timee class related to the contract.
 */
  public Contract(Member lender, Member borrower, Item item, int startDate, int endDate, Timee timee) {
    // this.lenderId = lenderId;
    // this.borrowerId = borrowerId;
    this.lender = lender;
    this.borrower = borrower;
    this.startDate = startDate;
    this.endDate = endDate;
    this.item = new Item(item.getCategory(), item.getName(), 
               item.getDescription(), item.getCostPerDay()); // Copying item;
    this.totalCost = calculateTotalCost();
    this.timee = timee;
  }

  public Member getBorrower() {
    return borrower;
  }
  
  public Member getLender() {
    return lender;
  }

  public int getStartDate() {
    return startDate;
  }


  public int getEndDate() {
    return endDate;
  }


  public int getTotalCost() {
    return totalCost;
  }

  public Item getItem() {
    return new Item(item.getCategory(), item.getName(),
           item.getDescription(), item.getCostPerDay()); // Returning a copy of item;
  }

  private int calculateTotalCost() {
    int durationInDays = (endDate - startDate) + 1; 
    return durationInDays * item.getCostPerDay();
  }

  public void addContractToItem(Item item) {
    
    item.addContract(this);
  }

  /**
 * Checks the contract status and finalizes it if the current day is greater or equal to the end date. 
 * Deducts credits from the borrower and adds credits to the lender upon finalization.
 *
 * @param memberManager An instance of MemberManager that allows access to members by their IDs.
 */ 
  public void checkAndFinalizeContract(MemberManager memberManager) {
    if (!isFinalized && timee.getCurrentDay() >= endDate) {
      if (lender != null && borrower != null) {
        borrower.deductCredits(totalCost); 
        lender.addCredits(totalCost);      
      }
      isFinalized = true;
    }
  }

  public boolean isFinalized() {
    return isFinalized;
  }
}
