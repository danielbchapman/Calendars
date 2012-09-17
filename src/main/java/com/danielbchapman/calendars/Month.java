package com.danielbchapman.calendars;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import lombok.Getter;


/**
 * <Class Definitions>
 *
 ***************************************************************************
 * @author Daniel B. Chapman 
 * <br /><i><b>Light Assistant</b></i> copyright Daniel B. Chapman
 * @since Sep 14, 2012
 * @version 2 Development
 * @link http://www.lightassistant.com
 ***************************************************************************
 */
public class Month<T>
{
  @Getter
  private TimeZone timeZone = TimeZone.getDefault();
  private ArrayList<Pair<Date, T>> dates;
  private int startIndex;
  @Getter
  private Date dayFirst;
  @Getter
  private Date dayLast;
  @Getter
  private Date absoluteFirst;
  @Getter
  private Date absoluteLast;
  
  public Month(Date date)
  {
    this(date, TimeZone.getDefault());
  }
  
  public Month(Date date, TimeZone timeZone)
  {
    this(help(date, timeZone, Calendar.YEAR), help(date, timeZone, Calendar.MONTH), timeZone);
  }
  
  public Month(int year, int month)
  {
    this(year, month, TimeZone.getDefault());
  }
  
  public Month(int year, int month, TimeZone timeZone)
  {
    this.timeZone = timeZone;
    Calendar cal = Calendar.getInstance(timeZone);
    cal.set(year, month, 1);
    dayFirst = cal.getTime();
    int startDate = cal.get(Calendar.DAY_OF_YEAR);
    int startOffset = cal.get(Calendar.DAY_OF_WEEK) - Calendar.SUNDAY;
    
    cal.add(Calendar.MONTH, 1);
    cal.add(Calendar.DAY_OF_YEAR, -1);
    dayLast = cal.getTime();
    int endOffset = Calendar.SATURDAY - cal.get(Calendar.DAY_OF_WEEK);
    int endDate = cal.get(Calendar.DAY_OF_YEAR);
    
    /* Force a filler week at the beginning or end if needed*/
    if(startOffset == 0) 
      startOffset = 7;
    
    if(endOffset == 0)
      endOffset = 7;

    int totals = startOffset + endOffset + endDate + 1 - startDate;
    
    if(totals <= 35)
      endOffset += 7;
    
    Date[] pre = new Date[startOffset];
    Date[] post = new Date[endOffset];
    
    for(int i = 0; i < post.length; i++)
    {
      cal.add(Calendar.DAY_OF_YEAR, 1);
      post[i] = cal.getTime();
    }
    
    cal.setTime(dayFirst);
    for(int i = pre.length; i > 0; i--)
    {
      cal.add(Calendar.DAY_OF_YEAR, -1);
      pre[i -1] = cal.getTime();
    }
      
    dates = new ArrayList<Pair<Date, T>>();
    cal.setTime(dayFirst);
    
    for(Date d : pre)
      dates.add(new Pair<Date, T>(d, null));
    
    startIndex = dates.size() -1;
    for(int i = 0; i < endDate - startDate + 1; i++)
    {
      dates.add(new Pair<Date, T>(cal.getTime(), null));
      cal.add(Calendar.DAY_OF_YEAR, 1);
    }
    
    for(Date d : post)
      dates.add(new Pair<Date, T>(d, null));
    
    absoluteFirst = dates.get(0).getFirst();
    absoluteLast = dates.get(dates.size() -1).getFirst();
  }
  
  public void set(Date date, T data)
  {  
    dates.set(index(date), new Pair<Date, T>(date, data));
  }
  
  public T get(Date date)
  {
    return dates.get(index(date)).getSecond();
  }
  
  public Pair<Date, T> get(int index)
  {
    return dates.get(index);
  }
  
  private int index(Date date)
  {
    Calendar cal = Calendar.getInstance(timeZone);
    cal.setTime(date);
    int index = cal.get(Calendar.DAY_OF_MONTH) + startIndex - 1;
    if(index < 0 || index >= dates.size())
      throw new IndexOutOfBoundsException("The date " + date + " is an invalid date for the month of " + toString());    
    return index;
  }
  
  /**
   * @return a calendar synchronized with the time zone
   * used for this instance  
   */
  public Calendar getCalendarInstance()
  {
    return Calendar.getInstance(timeZone);
  }
  
  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  public String toString()
  {
    StringBuilder builderOne = new StringBuilder();
    StringBuilder builderTwo = new StringBuilder();
    SimpleDateFormat sdf = new SimpleDateFormat("MMMMM',' yyyy");
    SimpleDateFormat day = new SimpleDateFormat("dd");
    builderOne.append("Month of " + sdf.format(dayFirst) + " \n\t");
    builderTwo.append("\t");
    int count = 1;
    
    for(Pair<Date, T> pair : dates)
    {
      
      if(count % 8 == 0)
      {
        count = 1;
        builderOne.append("\n\t");
        builderTwo.append("\n\t");
      }
      count++;
      builderOne.append("[");
      builderTwo.append("[");
      
      builderOne.append(day.format(pair.getFirst()));
      builderTwo.append(pair.getSecond());
      
      builderOne.append("]\t");
      builderTwo.append("]\t");
    }
    
    builderOne.append("\n\n");
    builderOne.append(builderTwo.toString());
    
    return builderOne.toString();
  }
  
  public int size()
  {
    return dates.size();
  }
  
  private static int help(Date date, TimeZone timeZone, int field)
  {
    Calendar cal = Calendar.getInstance(timeZone);
    cal.setTime(date);
    return cal.get(field);
  }
}
