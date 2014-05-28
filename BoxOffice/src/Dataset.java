import java.util.Vector;
import java.util.Map;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

import org.apache.commons.collections4.bidimap.TreeBidiMap;

/**
 * @author Nicholas Ezyk, 2014
 * 
 * This and all code in this project are Creative Commons licensed,
 * CC-BY-NC 3.0. It may be shared and adapted, but only with attribution
 * and only for non-commercial purposes.
 */


public class Dataset {
	
	/**
	 * @todo This needs to be revamped almost wholly.
	 * The Dataset class will contain maps for title to movie, title to debut yearweek, and title to
	 * 		URL String.
	 * The parse will go by week, dipping into movies by their URLs to track their weekly progress.
	 * 
	 * The existence of lifetime grosses over multiple releases will necessitate a means of counting and
	 * inflation adjusting over multiple releases--perhaps an array linking to alternate releases?
	 * 
	 * There will be a passthrough so that when the theatrical run history of the Movie is populated,
	 * the Week position in which it resides will be filled as well.
	 * 
	 */
	
	public static TreeBidiMap<Integer, String> catalogTitles = new TreeBidiMap();
	public static Map<Integer, Movie>  catalogMovies;
	
	public static boolean running = true;
	
	public static void main(String[] args)
	{
		Scanner sc = new Scanner(System.in);
		System.out.println("Select year");
		int year = sc.nextInt();
		while (year < 1982 || year > 2014)
		{
			System.out.println("Invalid. Please select a year between 1982 and 2014, inclusive.");
			year = sc.nextInt();
		}
		System.out.println("Select week");
		Scanner wk = new Scanner(System.in);
		int week = wk.nextInt();
		//System.out.println(week);
		while (week < 1 || week > 52)
		{
			System.out.println("Invalid. Please select a week between 1 and 52, inclusive.");
			week = wk.nextInt();
		}
	search:	
		while (running == true)
		{
			String page = "";
			
			if (week == 0) week = 52;
			if (week < 10) System.out.println("Preparing " + year + "-" + "0" + week);
			else System.out.println("Preparing " + year + "-" + week);
			
			if (week < 10)
			{
				try
				{
					page = GrabHTML.pull(new URL("http://boxofficemojo.com/weekly/chart/?yr=" + year + "&wk=0" + week + "&p=.htm"));
				}
				catch (Exception e)
				{
					
				}
			}
			else
			{
				try
				{
					page = GrabHTML.pull(new URL("http://boxofficemojo.com/weekly/chart/?yr=" + year + "&wk=" + week + "&p=.htm"));
				}
				catch (Exception e)
				{
					
				}
			}
			
			if (page.indexOf("No Weekly Data Found") > -1)
			{
				System.out.println("Data break at year " + year + ", week " + week + ".");
				break search;
			}
			
			//the rest of the parse goes here
			page = page.substring(page.indexOf("<h1>Weekly Box Office</h1>"));
			page = page.substring(page.indexOf("<h2>") + ("<h2>").length());
			
			String range = page.substring(0, page.indexOf('<'));
			page = page.substring(page.indexOf("<td align=\"center\"><font size=\"2\">") + ("<td align=\"center\"><font size=\"2\">").length());
			
			int count = Integer.parseInt(page.substring(0, 1));
			if (count != 1) System.out.println("Count error");
			
			collect:
				while(running = true)
				{
					page = page.substring(page.indexOf("><b>") + "><b>".length());
					String title = page.substring(0, page.indexOf('<'));
					
					page = page.substring(page.indexOf(".htm\">") + ".htm\">".length());
					String studio = page.substring(0, page.indexOf('<'));
					
					page = page.substring(page.indexOf("<font size=\"2\">") + "<font size=\"2\">".length());
					String dollars = page.substring(0, page.indexOf('<'));
					dollars = dollars.substring(1);
					do
					{
						dollars = dollars.substring(0, dollars.indexOf(',')) + dollars.substring(dollars.indexOf(',') + 1);
					} while (dollars.indexOf(',') != -1);
					int weekGross = Integer.parseInt(dollars);
					
					page = page.substring(page.indexOf("<font size=\"2\">") + "<font size=\"2\">".length());
					page = page.substring(page.indexOf("<font size=\"2\">") + "<font size=\"2\">".length());
					String cinemas = page.substring(0, page.indexOf('<'));
					int theaterCount = Integer.parseInt(cinemas);
					
					page = page.substring(page.indexOf("<font size=\"2\">") + "<font size=\"2\">".length());
					page = page.substring(page.indexOf("<font size=\"2\">") + "<font size=\"2\">".length());
					page = page.substring(page.indexOf("<font size=\"2\">") + "<font size=\"2\">".length());
					String aggregateGross = page.substring(0, page.indexOf('<'));
					aggregateGross = aggregateGross.substring(1);
					
					do
					{
						aggregateGross = aggregateGross.substring(0, aggregateGross.indexOf(',')) + aggregateGross.substring(aggregateGross.indexOf(',') + 1);
					} while (aggregateGross.indexOf(',') != -1);
					
					int cumulative = Integer.parseInt(aggregateGross);
					page = page.substring(page.indexOf("<font size=\"2\">") + "<font size=\"2\">".length());
					String _budget = page.substring(0, page.indexOf('<'));
					int budget = -100;
					if (_budget.equals("-") == false)
					{
						budget = Integer.parseInt(_budget);
					}
					page = page.substring(page.indexOf("<font size=\"2\">") + "<font size=\"2\">".length());
					int thisWeek = 0;
					if (page.substring(0, page.indexOf('<')) == "-")
					{
						thisWeek = 0;
					}
					else
					{
						thisWeek = Integer.parseInt(page.substring(0, page.indexOf('<')));
					}
					
					if (catalogTitles.containsValue(title) == false)
					{
						Movie m = new Movie(title, year, week, studio, range);
						m.push(thisWeek, year, week, cumulative, weekGross, theaterCount);
						if (budget != -100)
						{
							budget *= 1000000;
							m.setBudget(budget);
						}
					}
					else
					{
						int ident = catalogTitles.getKey(title);
						catalogMovies.get(ident).push(thisWeek, year, week, cumulative, weekGross, theaterCount);
						if (_budget.equals("-") == false && catalogMovies.get(ident).budget != -100)
						{
							catalogMovies.get(ident).setBudget(budget);
						}
					}
					
					
					
					page = page.substring(page.indexOf("</tr>") + "</tr>".length());
					
					/**
					 * @todo account for new movies <tr bgcolor="#ffff99">
					 */
					
					if ((count + 1) % 2 == 0)
					{
						if (page.indexOf("<tr bgcolor=\"#f4f4ff\">") == -1)
						{
							break collect;
						}
						else
						{
							page = page.substring(page.indexOf("<tr bgcolor=\"#f4f4ff\">") + "<tr bgcolor=\"#f4f4ff\">".length());
						}
					}
					else
					{
						if (page.indexOf("<tr bgcolor=\"#ffffff\">") == -1)
						{
							break collect;
						}
						else
						{
							page = page.substring(page.indexOf("<tr bgcolor=\"#ffffff\">") + "<tr bgcolor=\"#ffffff\">".length());
						}
					}
					
					count++;
					
					
					
				}
			
			
			char c = ' ';
			while(!(c == 'n' || c == 'N' || c == 'y' || c == 'Y'))
			{
				System.out.println(year + " " + week + " completed. Continue? y/n");
				Scanner ou = new Scanner(System.in);
				c = ou.nextLine().toCharArray()[0];
			}
			if (c == 'n' || c == 'N')
			{
				System.out.println("Collection terminated at week " + week + " of year " + year + ".");
				break search;
			}
			else if (c == 'y' || c == 'Y')
			{
				if (week == 52)
				{
					week = 1;
					year++;
				}
				else week++;
			}
		}
	}
	
	
}
