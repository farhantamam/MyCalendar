import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

/**
 * Programming Assignment 2 Solution
 * @author Mohmad Tamam
 * Copyright 2016
 * @version 1.0
 *
 */

/**
 * Represents a Calendar application where a user can add events to a calendar from console or from a file,
 * delete events from a particular day or all events from entire Calendar, and view scheduled events in either
 * day or month view and view next or previous day/month; it stores all events to a file when it is closed.
 *
 */
public class MyCalendarTester {

	/**
	 * This is the main method of the program that takes user input to add/delete events to/from a Calendar
	 * and can load events from a file.
	 * @param args command line arguments
	 */
	public static void main(String[] args) {

		MyCalendar calendar = new MyCalendar();
		Scanner scan = new Scanner(System.in);
		File file = new File("events.txt");

		String title = "", sdate = "", start = "", end = "";

		calendar.getMonthView(true);
		System.out.println("\nSelect one of the following options:\n"
				+"[L]oad   [V]iew by  [C]reate, [G]o to [E]vent list [D]elete  [Q]uit");


		String typed = "";
		while(scan.hasNext())
		{
			typed = scan.nextLine();
			if(typed.equalsIgnoreCase("q")) // Quit
			{
				try {
			         FileOutputStream fileOut = new FileOutputStream(file);
			         ObjectOutputStream out = new ObjectOutputStream(fileOut);
			         for(Event e: calendar.getTreeSet())
						{
							out.writeObject(e);
						}
			         out.close();
			         fileOut.close();
			      }catch (FileNotFoundException fnf) {
						//e.printStackTrace();
						try {
							// creates the empty file
							file.createNewFile();
						}  catch (Exception x) {
							// Some other sort of failure, such as permissions.
							x.printStackTrace();
							System.err.format("createFile error: %s%n", x);
						}
					}catch(IOException i) {
			         i.printStackTrace();
			      }

				break;
			}

			if(typed.equalsIgnoreCase("l")) // Load
			{
				FileInputStream fileIn;
				ObjectInputStream in;
				Event e;
				try {
					fileIn = new FileInputStream(file);
					in = new ObjectInputStream(fileIn);
					try{
						while(true)
						{
							e = (Event) in.readObject();
							calendar.addEvent(e);
						}
					}
					catch ( EOFException eof ) {
						// ObjectInputStream doesn't have anything similar to hasNext()
						// therefore, relying on EOF to stop
						in.close();
						fileIn.close();
					} catch (ClassNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}catch (FileNotFoundException fnf) {
					//e.printStackTrace();
					System.out.println("This is the First run.");
				}catch(IOException i) {
					i.printStackTrace();
					return;
				}
			}


			else if(typed.equalsIgnoreCase("v")) // View by
			{
				System.out.println("[D]ay view or [M]view ?");
				if(scan.hasNext())
					typed = scan.nextLine();
				if(typed.equalsIgnoreCase("d")) // View by Day
				{
					calendar.getDayView(calendar.getTime());
					while(!typed.equalsIgnoreCase("m"))
					{
						System.out.println("\n[P]revious or [N]ext or [M]ain menu ?");
						if(scan.hasNext())
							typed = scan.nextLine();
						if(typed.equalsIgnoreCase("p"))
						{
							calendar.add(Calendar.DATE, -1);
							calendar.getDayView(calendar.getTime());
						}
						else if(typed.equalsIgnoreCase("n"))
						{
							calendar.add(Calendar.DATE, 1);
							calendar.getDayView(calendar.getTime());
						}
					}
					calendar.setTime(new Date()); //reset calendar to current date
				}
				else if(typed.equalsIgnoreCase("m")) // View by Month
				{
					calendar.getMonthView();
					System.out.println("[P]revious or [N]ext or [M]ain menu ?");
					if(scan.hasNext())
						typed = scan.nextLine();
					while(!typed.equalsIgnoreCase("m"))
					{

						if(typed.equalsIgnoreCase("p"))
						{
							calendar.add(Calendar.MONTH, -1);
							calendar.getMonthView();
						}
						else if(typed.equalsIgnoreCase("n"))
						{
							calendar.add(Calendar.MONTH, 1);
							calendar.getMonthView();
						}
						System.out.println("[P]revious or [N]ext or [M]ain menu ?");
						if(scan.hasNext())
							typed = scan.nextLine();
					}
					calendar.setTime(new Date()); //reset calendar to current date
				}
			} //end of View (needs to be fixed)

			else if(typed.equalsIgnoreCase("c")) // Create event
			{
				System.out.print("Title: ");
				if(scan.hasNext())
					title = scan.nextLine();
				System.out.print("date (MM/DD/YYYY): ");
				if(scan.hasNext())
					sdate = scan.nextLine();
				System.out.print("Start time (in 24 hour clock): ");
				if(scan.hasNext())
					start = scan.nextLine();
				System.out.print("End time (in 24 hour clock): ");
				//if(scan.hasNext())
					end = scan.nextLine();
				Event e = new Event(title, sdate, start, end);
				calendar.addEvent(e);
			} // end of Create

			else if(typed.equalsIgnoreCase("g")) // Go to
			{
				System.out.print("date (MM/DD/YYYY): ");
				if(scan.hasNext())
					sdate = scan.nextLine();
				calendar.getDayView( getDate(sdate));
			} // end of Go to

			else if(typed.equalsIgnoreCase("e")) // Event List
			{
				calendar.printEventList();
			}

			else if(typed.equalsIgnoreCase("d")) // Delete
			{
				System.out.println("[S]elected or [A]ll ?");
				if(scan.hasNext())
					typed = scan.nextLine();
				if(typed.equalsIgnoreCase("s"))
				{
					System.out.print("Enter the date (MM/DD/YYYY): ");
					if(scan.hasNext())
						sdate = scan.nextLine();
					calendar.delete( getDate(sdate) );
				}
				else if(typed.equalsIgnoreCase("a")) // delete All
					calendar.delete();

			} // end of Delete
			System.out.println("\nSelect one of the following options:\n"
					+"[L]oad   [V]iew by  [C]reate, [G]o to [E]vent list [D]elete  [Q]uit");
		}
		scan.close();

	}

	/**
	 * Returns a Date object from a string that represents a date.
	 * @param sdate string to be converted to Date object
	 * @return the Date object that represents the given string
	 */
	private static Date getDate(String sdate)
	{
		Date date = null;
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		try {
			date = formatter.parse(sdate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

}
