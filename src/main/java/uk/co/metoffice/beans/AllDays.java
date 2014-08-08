package uk.co.metoffice.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>[Pattern] to provide the following to {@link Object}:</p>
 * <ul>
 * <li></li>
 * </ul>
 * <p>Example:</p>
 * <pre>
 * </pre>
 *
 * @since 0.0.1
 *  
 */
public enum AllDays {
  Monday,
  Tuesday,
  Wednesday,
  Thursday,
  Friday;

  /**
   * Returns a String array of days.
   */
  public static List<String> getAllDays(){
      List<String> dayList = new ArrayList<>();
      for(AllDays temp : AllDays.values()){
        dayList.add(temp.name());
      }
    return dayList;
   }
}
