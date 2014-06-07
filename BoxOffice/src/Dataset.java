import java.util.Calendar;
import java.util.Vector;
import java.util.Map;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
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
	 * There needs to be a check in the Summary page for limited opening weekends.
	 * 
	 */
	
	//public static TreeBidiMap<Integer, String> catalogTitles = new TreeBidiMap();
	public static Map<String, Movie>  catalogMovies;
	
	public static boolean running = true;
	
	public static void main(String[] args)
	{
		
		
		
		/********************************
		 * deprecated parse algorithm
		 * to be deleted upon replacement
		 */
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
						Movie m = new Movie(title);
						//m.push(thisWeek, year, week, cumulative, weekGross, theaterCount);
						if (budget != -100)
						{
							budget *= 1000000;
							m.budget = budget;
						}
					}
					else
					{
						int ident = catalogTitles.getKey(title);
						//catalogMovies.get(ident).push(thisWeek, year, week, cumulative, weekGross, theaterCount);
						if (_budget.equals("-") == false && catalogMovies.get(ident).budget != -100)
						{
							catalogMovies.get(ident).budget = budget;
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
	
	public void pullFilmData(String filmURL)
	//to be changed to Movie return type
	{
		String bom = "http://boxofficemojo.com";
		String bomMovies = bom + "/movies/";
		
		String raw = "";
		try
		{
			raw = GrabHTML.pull(new URL(filmURL));
		} catch (MalformedURLException we)
		{
			
		} catch (Exception me)
		{
			
		}
		
		raw = snip(raw, "<!--------------------------Site Body---------------------------------->");
		raw = snip(raw, "<font face=\"Verdana\" size=\"6\"><b>");
		
		String filmTitle; 
		filmTitle = mine(raw, "</b>");
		
		Movie mov = new Movie(filmTitle);
		raw = snip(raw, "Domestic Total Gross: <b>");
		String nGross = mine(raw, "</b>");
		int _nominalGross = parseDollarAmount(nGross);
		String dLGross = "";
		String releasesLink = "";
		int _domesticLifetimeGross = 0;
		if (raw.indexOf("Domestic Lifetime Gross:") != -1)
		{
			raw = snip(raw, "<a href=\"");
			releasesLink = mine(raw, "\"");
			raw = snip(raw, "Domestic Lifetime Gross: $");
			dLGross = mine(raw, "</b>");
			_domesticLifetimeGross = parseDollarAmount(dLGross);
		}
		
		raw = snip(raw, "Distributor: <b><a href=\"");
		String distributorLink = mine(raw, "\">");
		String _distributor = mine(raw, "</a>");
		raw = snip(raw, ".htm\">");
		String rDate = mine(raw, "</a>");
		Calendar releaseDate = parseDate(rDate);
		
		raw = snip(raw, "Genre: <b>");
		Vector<String> genreList = new Vector<String>(6, 1);
		String genre1 = mine(raw, "</b>");
		genreList.set(1, genre1);
		
		raw = snip(raw, "Runtime: <b>");
		String runtimeString = mine(raw, "</b>");
		int runtime = getRuntime(runtimeString);
		
		raw = snip(raw, "MPAA Rating: <b>");
		String MPAArating = mine(raw, "</b>");
		
		raw = snip(raw, "Production Budget: <b>");
		String productionBudget = mine(raw, "</b>");
		int budget;
		if (productionBudget.equals("N/A")) budget = -100;
		else parseBudget(productionBudget);
		
		raw = snip(raw, "Weekend</a></li>");
		raw = snip(raw, "<a href=\"");
		String weeklyLink = mine(raw, "\">");
		weeklyLink = bom + weeklyLink;
		
		String releasesList = "";
		String foreignList = "";
		if (raw.indexOf("Releases</a>") != -1 && raw.indexOf("Foreign</a></li>") != -1)
		{
			raw = snip(raw, "Weekly</a></li>");
			raw = snip(raw, "<li><a href=\"");
			releasesList = bom + mine(raw, "\">Releases</a></li>");
			
			raw = snip(raw, "Releases</a>");
			raw = snip(raw, "<li><a href=\"");
			foreignList = bom + mine(raw, "\">Foreign</a></li>");
		}
		else if (raw.indexOf("Releases</a>") != -1)
		{
			raw = snip(raw, "Weekly</a></li>");
			raw = snip(raw, "<li><a href=\"");
			releasesList = bom + mine(raw, "\">Releases</a></li>");
		}
		else if (raw.indexOf("Foreign</a></li>") != -1)
		{
			raw = snip(raw, "Weekly</a></li>");
			raw = snip(raw, "<li><a href=\"");
			foreignList = bom + mine(raw, "\">Foreign</a></li>");
		}
		
		raw = snip(raw, "<td width=\"35%\" align=\"right\">&nbsp;<b>");
		String domestic = mine(raw, "</b>");
		int domesticGross = parseDollarAmount(domestic);
		
		raw = snip(raw, "&nbsp;$");
		String foreign = mine(raw, "</b></td>");
		int foreignGross = parseDollarAmount(foreign);
		
		raw = snip(raw, "Domestic Summary</b></div>");
		raw = snip(raw, "<td align=\"center\"><a href=\"");
		
		String openingWeekendLink = "";
		openingWeekendLink = bom + mine(raw, "\"");
		String debutWeek = getWeek(openingWeekendLink);
		String debutYear = getYear(openingWeekendLink);
		
		String genreSec = mine(raw, "</table></div></div><div class=\"mp_box\">");
		genreSec = snip(genreSec, "<tr><th>Genre</th>");
		genreSec = snip(genreSec, "<th>Rank</th></tr>");
		while (genreSec.indexOf("<font size=\"2\">") != -1)
		{
			genreSec = snip(genreSec, "<font size=\"2\">");
		}
		
		
		
		
		
		
		
		
		
		
		
		
	}
	
	public String snip(String set, String place)
	{
		if (set.indexOf(place) != -1)
		{
			return set = set.substring(set.indexOf(place) + place.length());
		}		
		else return set;
	}
	
	public String mine(String set, String stop)
	{
		String out = set.substring(0, set.indexOf(stop));
		snip(set, stop);
		return out;
	}
	
	public int parseDollarAmount(String dollarAmount)
	{
		if (dollarAmount.indexOf("$") > -1) dollarAmount = snip(dollarAmount, "$");
		while (dollarAmount.indexOf(",") != -1)
		{
			int pt = dollarAmount.indexOf(",");
			dollarAmount = dollarAmount.substring(0, pt) + dollarAmount.substring(pt + 1);
		}
		return Integer.parseInt(dollarAmount);
	}
	
	
	public Calendar parseDate(String date)
	{
		int m = 0;
		int d = 0;
		int y = 0;
		date = date.toLowerCase();
		if (date.indexOf("january") != -1)
		{
			m = Integer.parseInt("1");
			date = snip(date, " ");
		}
		else if (date.indexOf("february") != -1)
		{
			m = Integer.parseInt("2") ; date = snip(date, " ");
		}
		else if (date.indexOf("march") != -1)
		{
			m = Integer.parseInt("3"); date=snip(date," ");
		}
		else if (date.indexOf("april") != -1)
		{
			m = Integer.parseInt("4");
			date = snip(date, " ");
		}
		else if (date.indexOf("may") != -1)
		{
			m = Integer.parseInt("5");
			
			date =snip(date, " ");
		}
		else if (date.indexOf("june") != -1)
		{
			m = Integer.parseInt("6");
			
			date= snip(date, " ");
		}
		else if (date.indexOf("july") != -1)
		{
			m = Integer.parseInt("7");
			date = snip(date, " ");
		}
		else if (date.indexOf("august") != -1)
		{
			m = Integer.parseInt("8");
			date = snip(date, " ");
		}
		else if (date.indexOf("september") != -1)
		{
			m = Integer.parseInt("9");
			date = snip(date, " ");
		}
		else if (date.indexOf("october") != -1)
		{
			m = Integer.parseInt("10");
			date = snip(date, " ");
		}
		else if (date.indexOf("november") != -1)
		{
			m = Integer.parseInt("11");
			date = snip(date, " ");
		}
		else if (date.indexOf("december") != -1)
		{
			m = Integer.parseInt("12");
			date = snip(date, " ");
		}
		
		d = Integer.parseInt(mine(date, ","));
		date = snip(date, " ");
		
		y = Integer.parseInt(date);
		
		Calendar cal = Calendar.getInstance();
		cal.set(y, m, d);
		return cal;
	}
	
	public int getRuntime(String runtime)
	{
		if (runtime.indexOf("hr") == -1 || runtime.indexOf("min") == -1) return -1;
		else
		{
			int totalMins = 0;
			String hours = runtime.substring(runtime.indexOf("hr") - 2, runtime.indexOf(" "));
			int hrs = Integer.parseInt(hours);
			totalMins += hrs*60;
			
			runtime = snip(runtime, "hrs. ");
			
			String mins = mine(runtime, " ");
			int minutes = Integer.parseInt(mins);
			totalMins += minutes;
			
			return totalMins;
		}
	}
	
	public int parseBudget(String bdgt)
	{
		if (bdgt.indexOf("$") > -1) bdgt = snip(bdgt, "$");
		String num = mine(bdgt, " ");
		int budget = Integer.parseInt(num);
		return budget * 1000000;
	}
	
	public String getYear(String link)
	{
		if (link.indexOf("?yr=") == -1) return "1889";
		else
		{
			link = snip(link, "?yr=");
			return mine(link, "&");
			
		}
	}
	
	public String getWeek(String link)
	{
		if (link.indexOf("?wknd=") == -1) return "01";
		else
		{
			link = snip(link, "?wknd=");
			return mine(link, "&");
		}
	}
}
