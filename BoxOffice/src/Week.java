import java.lang.Exception;
import java.util.Vector;

public class Week {
	public boolean nonInit;
	
	public int year;
	public int week;
	
	public Vector<Movie> films = new Vector<Movie>(10, 10);
	
	public String start;
	public String end;
	
	public int nominalGross;
	public int realGross;
	
//	public static void main(String[] args)
//	{
//		
//		
//	}
	
	public Week()
	{
		nonInit = true;
	}
	
	public Week(int yr, int wk)
	{
		nonInit = false;
		
		String set = "";
		
		if (yr >= 1982) year = yr;
		if (wk >= 1 && wk <= 52) 
		{
			week = wk;
			if (wk < 10) set = "0";
		}

	}


	
	//public Movie[] rank = new Movie[26];
}
