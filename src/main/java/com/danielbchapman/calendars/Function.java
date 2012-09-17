package com.danielbchapman.calendars;

public interface Function<Args, Value>
{
  public Value call(Args args);
}
