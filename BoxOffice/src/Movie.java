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
	
	public String title;
	
	public int debutYear;
	public int debutWeek;
	
	public String dataCurrentTo;
	
	public String releaseDate;
	public String closeDate;
	
	public String dayRange;
	
	public Vector<String> date = new Vector<String>(100, 1);
	public Vector<Integer> gross = new Vector<Integer>(100, 1);
	public Vector<Integer> theatres = new Vector<Integer>(100, 1);
	
	public int RTcriticScore = -100;
	public int RTaudienceScore = -100;
	public int RTnumCritics = -100;
	
	public int nominalGross;
	public int realGross;
	public int nominalLifetimeGross;
	public int realLifetimeGross;
	public int nominalWorldwideGross;
	public int realWorldwideGross;
	
	public Vector<String> genres = new Vector<String>(6, 1);
	
	public int budget = -100;
	
	public int runtime;
	
	public BTernary flop = new BTernary(0);
	
	public String studio;
	
	public Movie()
	{
		nonInit = true;
	}
	
	public Movie (String n)
	{
		nonInit = false;
		
		title = n;
		
		Dataset.catalogMovies.put(n, this);
		System.out.println("Added to library: " + n);
	}

	
	
}
