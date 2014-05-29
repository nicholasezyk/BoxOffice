import java.lang.Exception;
import java.util.Vector;

import org.apache.commons.collections4.bidimap.TreeBidiMap;

public class Week {
	public boolean nonInit;
	
	public int year;
	public int week;
	
	public TreeBidiMap<Integer, Integer> grossNominal = new TreeBidiMap();
	public TreeBidiMap<Integer, Integer> grossReal = new TreeBidiMap();
	public TreeBidiMap<Integer, Integer> theatres = new TreeBidiMap();
	
	public String start;
	public String end;
	
	public int nominalGross;
	public int realGross;
	public int nominalWorldwideGross;
	public int realWorldwideGross;
	
	public Vector<String> genres = new Vector<String>(6, 1);
	
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
	
	public void plop(int position, int nominal, int theatres)
	{
		grossNominal.put(position, nominal);
		grossReal.put(position, (int) (nominal*Inflator.inflate(this.year)));
	}


	
	//public Movie[] rank = new Movie[26];
}
