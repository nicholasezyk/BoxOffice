/**
 * A simple balanced ternary class to determine true (+1), false (-1), or untested/untestable (0) assertions.
 * 
 * @author Nicholas Ezyk
 * 
 */

public class BTernary {
	public int v;
	
	public BTernary(int val)
	{
		if (val == 1) v = 1;
		else if (val == -1) v = -1;
		else v = 0;
	}
	
	public void set(int val)
	{
		if (val == 1) v = 1;
		else if (val == -1) v = -1;
		else v = 0;
	}
	
	public void set(String s)
	{
		s = s.toUpperCase();
		if (s.equals("T") || s.equals("TRUE")) v = 1;
		else if (s.equals("F") || s.equals("FALSE")) v = -1;
		else if (s.equals("N") || s.equals("NO DATA")) v = 0;
		
	}
	
	public int get()
	{
		return v;
	}
}
