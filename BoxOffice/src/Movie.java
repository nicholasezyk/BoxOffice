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
	public boolean offset;
	
	public String title;
	public int ID = -100;
	
	public int debutYear;
	public int debutWeek;
	
	public String dayRange;
	
	public Vector<String> date = new Vector<String>(10, 10);
	public Vector<Integer> gross = new Vector<Integer>(10, 10);
	public Vector<Integer> aggregate = new Vector<Integer>(10, 10);
	public Vector<Integer> theatres = new Vector<Integer>(10, 10);
	
	public int RTcriticScore = -100;
	public int RTaudienceScore = -100;
	public int RTnumCritics = -100;
	
	public int budget = -100;
	
	public BTernary flop = new BTernary(0);
	
	public String studio;
	
	public Movie()
	{
		nonInit = true;
	}
	
	public Movie (String n, int y, int w, String s, String r)
	{
		nonInit = false;
		
		title = n;
		
		debutYear = y;
		debutWeek = w;
		
		dayRange = r;
		
		IDsearch:
			for (int i = 1; i < 100; i++)
			{
				if (w < 10 && i < 10)
				{
					String sample = y + "0" + w + "0" + i;
					int find = Integer.parseInt(sample);
					if (Dataset.catalogTitles.containsKey(find) == true)
					{
						ID = find;
						break IDsearch;
					}
				}
				else if (w < 10 && i >= 10)
				{
					String sample = y + "0" + w + i;
					int find = Integer.parseInt(sample);
					if (Dataset.catalogTitles.containsKey(find) == true)
					{
						ID = find;
						break IDsearch;
					} 
				}
				else
				{
					String sample = y + w + "" + i;
					int find = Integer.parseInt(sample);
					if (Dataset.catalogTitles.containsKey(find) == true)
					{
						ID = find;
						break IDsearch;
					}
					
				}
				if (ID == -100)
				{
					System.out.println("ID assignment error for movie " + n + ".");
				}
			}
		
		if (ID != -100)
		{
			Dataset.catalogTitles.put(ID, n);
			Dataset.catalogMovies.put(ID, this);
		}
		
		studio = s;
	}
	
	public void push(int pl, int y, int w, int a, int g, int t)
	{
		String yearWeek = "";
		if (w < 10)
		{
			yearWeek = y + "-0" + w;
		}
		else
		{
			yearWeek = y + "-" + w;
		}
		if (pl > this.date.capacity())
		{
			this.date.setSize(pl + 1);
			this.gross.setSize(pl + 1);
			this.theatres.setSize(pl + 1);
		}
		this.date.setElementAt(yearWeek, pl);
		this.gross.setElementAt(g, pl);
		this.aggregate.setElementAt(a, pl);
		this.theatres.setElementAt(t, pl);
	}
	
	public int ID()
	{
		return ID;
	}
	
	public void setBudget(int _budget)
	{
		budget = _budget;
	}
	
	
}
