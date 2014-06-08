import java.net.URL;
import java.util.Calendar;
import java.util.Vector;

/**
 * 
 * @author nicholasezyk
 * @todo Put in some sort of boolean mechanism to
 * 		account for the fact that some films debut
 * 		with week "1" and some debut with a "-" then
 * 		"1" after that.
 *
 */

public class Movie {
	public boolean nonInit;
	
	public static int count;
	public int ID;
	
	public String title;
	
	public Calendar dataCurrentTo;
	
	public Calendar releaseDate;
	public Calendar closeDate;
	
	//public String dayRange; I don't even remember why this is here
	
	public Vector<String> date = new Vector<String>(100, 1);
	public Vector<Integer> gross = new Vector<Integer>(100, 1);
	public Vector<Integer> theatres = new Vector<Integer>(100, 1);
	
	public URL summaryPage;
	public Vector<URL> releases = new Vector<URL>(100, 1);
	
	public int RTcriticScore = -100;
	public int RTaudienceScore = -100;
	public int RTnumCritics = -100;
	
	public long nominalGross;
	public long realGross;
	public long nominalLifetimeGross;
	public long realLifetimeGross;
	public long nominalWorldwideGross;
	public long realWorldwideGross;
	
	public Vector<String> genres = new Vector<String>(6, 1);
	
	public int NominalBudget;
	public int realBudget;
	
	public int runtime;
	
	public BTernary flop = new BTernary(0);
	
	public String distributor;
	
	public Movie()
	{
		nonInit = true;
	}
	
	public Movie (String n)
	{
		nonInit = false;
		
		title = n;
		
		count++;
		this.ID = count;
		
		Dataset.catalogMovies.put(n, this);
		System.out.println("Added to library: " + n);
	}
	
	public void setGross(int g, Calendar c)
	{
		this.nominalGross = g;
		if (c.YEAR > 1982 && c.YEAR < 2014)
		{
			this.realGross = (int) (g * Inflator.inflate(c.YEAR));
		}
	}
	
	public void setLifetimeGross(int l, Calendar c)
	{
		this.nominalLifetimeGross = l;
		if (c.YEAR > 1982 && c.YEAR < 2014)
		{
			this.realGross = (int) (l * Inflator.inflate(c.YEAR));
		}
	}
	
	public void setWorldwideGross(int w, Calendar c)
	{
		this.nominalWorldwideGross = w;
		if (c.YEAR > 1982 && c.YEAR < 2014)
		{
			this.realGross = (int) (w * Inflator.inflate(c.YEAR));
		}
	}
	
	public void setDistributor(String d)
	{
		this.distributor = d;
	}

	
	
}
