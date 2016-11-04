import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Programming Assignment 2 Solution
 * @author Mohmad Tamam
 * Copyright 2016
 * @version 1.0
 *
 */

/**
 * Represents a Calendar that can hold events and print them to console in either a day or month view,
 * print all events as a list, delete all events from a particular day or delete all events in Calendar.
 *
 */
public class MyCalendar extends GregorianCalendar {
	
	private Calendar calendar;
	private TreeSet<Event> ts = new TreeSet<Event>(new 
			Comparator<Event>()
			{
				public int compare(Event e1, Event e2) 
				{
					return e1.getDate().compareTo(e2.getDate());
				}
			}); // make EventComparator an Anonymous class

	/**
	 * Class constructor that creates a Gregorian calendar with Pacific Standard Time time zone 
	 * and setting up rules for Daylight Saving Time.
	 */
	public MyCalendar()
	{
		// get the supported ids for GMT-08:00 (Pacific Standard Time)
		String[] ids = TimeZone.getAvailableIDs(-8 * 60 * 60 * 1000);
		// if no ids were returned, something is wrong. get out.
		if (ids.length == 0)
			System.exit(0);
	
		// create a Pacific Standard Time time zone
		SimpleTimeZone pdt = new SimpleTimeZone(-8 * 60 * 60 * 1000, ids[0]);
	
		// set up rules for Daylight Saving Time
		pdt.setStartRule(Calendar.APRIL, 1, Calendar.SUNDAY, 2 * 60 * 60 * 1000);
		pdt.setEndRule(Calendar.OCTOBER, -1, Calendar.SUNDAY, 2 * 60 * 60 * 1000);
	
		// create a GregorianCalendar with the Pacific Daylight time zone
		// and the current date and time
		calendar = new GregorianCalendar(pdt);
		calendar.setTime(new Date());
	}

	
	/**
	 * Prints Day View of the Calendar for a given date. It first prints date and then all the events scheduled for this date. 
	 * @param date date for which to show Day View
	 */
	public void getDayView(Date date) 
	{
		SimpleDateFormat header = new SimpleDateFormat("EEEE, MMM d, y");
		System.out.println(header.format(date)); // print date that the view is from
		SimpleDateFormat startTime = new SimpleDateFormat("HH:mm"); // for printing start time of event
		SimpleDateFormat compare = new SimpleDateFormat("yyyyMMdd"); // for comparing two events by only the date
		
		for(Event e: ts)
		{
			if( compare.format(e.getDate()).equals(compare.format(date)) )
			{
				//System.out.println("it is equal");
				if(!e.getEndTime().equals(""))
					System.out.printf("%s %s - %s%n", e.getTitle(), startTime.format(e.getDate()), e.getEndTime());
				else
					System.out.printf("%s %s%n", e.getTitle(), startTime.format(e.getDate()));
			}
		}
	}

	/**
	 * Prints Month View of Calendar by calling helper method to print current month to console highlighting days with scheduled events.
	 */
	public void getMonthView() 
	{
		getMonthView(false, true);		
	}
	
	/**
	 * Prints Month View of Calendar by calling the helper method to print current month to console with today's date highlighted.
	 * @param highlight_today if true, today's date is highlighted; otherwise, today's date is not highlighted
	 */
	public void getMonthView(boolean highlight_today) 
	{
		getMonthView(highlight_today, false);		
	}
	
	/**
	 * Helper method that prints name of current month on console and calls another method to print actual days in the month with given parameters.
	 * @param highlight_today if true, today's date is highlighted; otherwise, today's date is not highlighted
	 * @param showEvents if true, events scheduled in this month are highlighted; otherwise, events are not highlighted
	 */
	private void getMonthView(boolean highlight_today, boolean showEvents) 
	{
		System.out.println(getDisplayName(Calendar.MONTH, Calendar.LONG,
				Locale.US) + " " + get(Calendar.YEAR));
		
		int x = get(Calendar.DAY_OF_MONTH);
		
		set(Calendar.DAY_OF_MONTH, 1);
		int firstWeekdayOfMonth = get(Calendar.DAY_OF_WEEK);
		set(Calendar.DAY_OF_MONTH, x);

		int numberOfMonthDays = getActualMaximum(Calendar.DAY_OF_MONTH);

		printCalendar(numberOfMonthDays, firstWeekdayOfMonth, highlight_today, showEvents);		
	}
	
	/**
	 * Helper method to actually print the Calendar month to console with current date 
	 * and/or days with events scheduled highlighted.
	 * @param numberOfMonthDays number of days in this particular month
	 * @param firstWeekdayOfMonth on which day of week 1st of the month falls 
	 * @param highlight_today if true, today's date is highlighted; otherwise, today's date is not highlighted
	 * @param showEvents if true, events scheduled in this month are highlighted; otherwise, events are not highlighted
	 */
	private void printCalendar(int numberOfMonthDays, int firstWeekdayOfMonth, 
			boolean highlight_today, boolean showEvents) 
	{
		int weekdayIndex = 0;
		int highlight = 0;
						
		System.out.println("Su Mo Tu We Th Fr Sa");

		// skip weekdays before the first day of month
		for (int day = 1; day < firstWeekdayOfMonth; day++) 
		{
			System.out.print("   ");
			weekdayIndex++;
		}
		
		if(highlight_today == true)
			highlight = get(Calendar.DATE);
		
		// print the days of month in tabular format.
		for (int day = 1; day <= numberOfMonthDays; day++) 
		{
			if(day == highlight)
				System.out.printf("[%d]", day);
			else if(hasEvent(day, get(Calendar.MONTH)))
				System.out.printf("[%d]", day);
			else
				System.out.printf("%1$2d", day);
			weekdayIndex++;
			
			// if it is the last weekday
			if (weekdayIndex == 7) 
			{
				weekdayIndex = 0;
				System.out.println();
			} 
			else
				System.out.print(" ");
		}
		System.out.println();
	}
	
	
	/**
	 * Helper method to determine if a particular day in the month has any events scheduled.
	 * @param day day in which to check for event(s)
	 * @param month month in which this day is
	 * @return <code>true</code> if an event is scheduled for this day of this month; <code>false</code> otherwise
	 */
	private boolean hasEvent(int day, int month) 
	{
		SimpleDateFormat sdf = new SimpleDateFormat("MMdd"); // to get month and day of any event
		String date = "";
		int thisMonth = -1;
		int thisDay = -1;
		for(Event e: ts)
		{
			date = sdf.format(e.getDate());
			//System.out.println(date);
			thisMonth = Integer.parseInt(date.substring(0, 2));
			thisMonth--;
			thisDay = Integer.parseInt(date.substring(2));
			//System.out.printf("event month: %d: day: %d%n", thisMonth, thisDay);
			if(thisMonth == month)
			{
				if(thisDay == day)
				{
					//System.out.println("it is equal, about to return");
					return true;
				}
			}
		}
		return false;
	}


	/**
	 * Adds a given Event to this Calendar.
	 * @param e event to be added
	 */
	public void addEvent(Event e)
	{
		ts.add(e);
	}

	
	/**
	 * Deletes all events that are scheduled on given date.
	 * @param date date of events to be deleted
	 */
	public void delete(Date date)
	{
		SimpleDateFormat compare = new SimpleDateFormat("yyyyMMdd"); // for comparing two events by only the date
		Iterator<Event> iter = ts.iterator();
		while(iter.hasNext())
		{
			Event e = iter.next();
			if( compare.format(e.getDate()).equals(compare.format(date)) )
				iter.remove();
		}
	}

	/**
	 * Deletes all events from this Calendar making it empty.
	 */
	public void delete() 
	{
		ts.clear();		
	}
	

	/**
	 * Prints the year and all the Events from that year chronologically 
	 * until all events in the calendar are printed.
	 */
	public void printEventList()
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyEEEE MMM dd HH:mm"); // for comparing two events by only the date
		Iterator<Event> iter = ts.iterator();
		String year = "";
		System.out.println("2016");
		//Friday Sep 30 13:15 - 14:00 Dentist
		while(iter.hasNext())
		{
			Event e = iter.next();
			// if event year is anything other than 2016, print it 
			// assumes that a date is not going to be from past years
			year = sdf.format(e.getDate()).substring(0, 4);
			if(!year.equals("2016"))
			{
				System.out.println(year);
			}
			if(!e.getEndTime().equals(""))
				System.out.printf("  %s - %s %s%n", sdf.format(e.getDate()).substring(4), e.getEndTime(), e.getTitle());
			else
				System.out.printf("  %s %s%n", sdf.format(e.getDate()).substring(4), e.getTitle());				
		}
	}
	
	
	/**
	 * Returns the data structure, TreeSet, holding all the events in the Calendar
	 * @return the TreeSet containing all the events in the Calendar 
	 */
	public TreeSet<Event> getTreeSet() 
	{
		return ts;
	}
	
}
