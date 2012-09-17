package com.danielbchapman.calendars;

import java.util.Calendar;

public class TestMonths
{
  public static void main(String ... args)
  {
    for(int i = 0; i < 3; i++)
      System.out.println(new Month<String>(2012, i));
    
    System.out.println("---------------------------------------\n");
    
    Month<String> aug2012 = new Month<String>(2012, Calendar.AUGUST);
    for(int i = 0; i < 28; i++)
      if(i % 3 == 0)
      {
        Calendar local = Calendar.getInstance();
        local.set(Calendar.YEAR, 2012);
        local.set(Calendar.MONTH, Calendar.AUGUST);
        local.set(Calendar.DAY_OF_MONTH, i);
        
        aug2012.set(local.getTime(), local.getTime().toString());
      }
    
    System.out.println(aug2012);
    
    for(int i = 0; i < 28; i++)
    {
      Calendar local = Calendar.getInstance();
      local.set(Calendar.YEAR, 2012);
      local.set(Calendar.MONTH, Calendar.AUGUST);
      local.set(Calendar.DAY_OF_MONTH, i);
      
      System.out.print(i);
      System.out.print(" << ");
      System.out.println(aug2012.get(local.getTime()));
    }
  }
}
