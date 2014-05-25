/**
 * @author JavaPF of Javaprogrammingforums.com, 2008
 * @author Nicholas Ezyk, 2014
 */

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
 
public class GrabHTML {
 
 public static String pull(URL url) throws Exception{
 
  //Set URL
  //URL url = new URL("http://cumtd.com");
  URLConnection spoof = url.openConnection();
 
  //Spoof the connection so we look like a web browser
  spoof.setRequestProperty( "User-Agent", "Mozilla/4.0 (compatible; MSIE 5.5; Windows NT 5.0; H010818)" );
  BufferedReader in = new BufferedReader(new InputStreamReader(spoof.getInputStream()));
  String strLine = "";
  String output = "";
 
  //Loop through every line in the source
  while ((strLine = in.readLine()) != null){
 
   //Prints each line to the console
  // System.out.println(strLine);
   output += strLine + "\n";
  }
 
  //System.out.println("End of page.");
  //System.out.println(output);
  return output;
 }
 
 public static void main(String[] args){
 
    //URL 1994_08 = new URL("http://boxofficemojo.com/weekly/chart/?yr=1994&wk=08&p=.htm");
   
    
    
  try{
   //Calling the Connect method
   
     pull(new URL("http://google.com"));
   //Connect();
  }catch(Exception e){
 
  }
 }
}

