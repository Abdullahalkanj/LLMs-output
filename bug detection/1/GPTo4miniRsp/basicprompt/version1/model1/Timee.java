package model1;

import java.util.ArrayList;
import java.util.List;

/**
 * The Timee class manages the progression of time within the application, maintaining a counter 
 * representing the current day. It supports operations to retrieve the current day and advance time 
 * by a specified number of days.
 */
public class Timee {
  private int currentDay = 0;


  public int getCurrentDay() {
    return currentDay;
  }

  /**
  * Advances the current day by the specified number of days. 
  * If the specified number is not positive, the method does nothing.
  *
  * @param days The number of days to advance. This should be a positive integer.
  */
  public void advanceDay(int days, MemberManager memberManager) {
    if (days > 0) {
      currentDay += days;
      finalizeContracts(memberManager);
    }
  }

  private void finalizeContracts(MemberManager memberManager) {
    List<Item> allItems = new ArrayList<>();
    List<Member> allMembers = memberManager.getAllMembers(); 
    for (Member member : allMembers) {
      allItems.addAll(member.getItems());
    }
    for (Item item : allItems) {
      List<Contract> contracts = item.getContracts();
      for (Contract contract : contracts) {
        contract.checkAndFinalizeContract(memberManager);
      }
    }
  }
}