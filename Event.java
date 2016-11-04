import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Programming Assignment 2 Solution
 * @author Mohmad Tamam
 * Copyright 2016
 * @version 1.0
 */

/**
 * Represents an Event to be stored in the Calendar.
 * An Event has a date (consisting of both date and start time of event),
 * title and end time (end time is optional).
 *
 */
public class Event implements java.io.Serializable {
	
	private Date date;
	private String title;
	private String end;
	
	/**
	 * Class constructor with title, date, start time and (optional) end time
	 * @param title	title of the event
	 * @param sdate	date of the event
	 * @param start	start time of the event
	 * @param end	end time of the event
	 * postcondition: Event with given information is created.
	 */
	public Event(String title, String sdate, String start, String end)
	{
		this.title = title;
		this.end = end;
		
		String dateTime = sdate + " " + start;
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm");
		try {

            date = formatter.parse(dateTime);
            //System.out.println(date);
            //System.out.println(formatter.format(date));

        } catch (ParseException e) {
            e.printStackTrace();
        }		
	}
	
	/**
	 * Returns the title of this Event.
	 * @return the title of this Event
	 */
	public String getTitle()
	{
		return title;
	}
	
	/**
	 * Returns the date of this Event.
	 * @return date of this Event
	 */
	public Date getDate()
	{
		return date;
	}
	
	/**
	 * Returns the end time of this Event.
	 * If there is no end time, an empty string is returned. 
	 * @return the end time of the Event
	 */
	public String getEndTime()
	{
		return end;
	}
}
