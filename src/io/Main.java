package io;

import java.io.IOException;
import java.io.*;


/**
 * Standalone file to test the parser on input files
 * @author Martin Steffen, Karsten Stahl
 * @version $Id: Main.java,v 1.2 2001-06-30 13:57:02 swprakt Exp $
 *
 * ============== //-->
 */
public class Main {
 
  public static void main (String args []) {
	  if (args.length < 1){
		  System.out.println("Usage: ParseTester <filename>");
		  return;
	  }
	  try{
		  File datei = new File(args[0]);

		  io.Parser parser = new io.Parser();
		  parser.parseFile(datei);
	  } catch (ParseException e){
		  System.err.println(e.toString());
		  System.exit(1);
	  } catch (IOException e){
		  System.err.println(e.toString());
		  System.exit(1);
	  } catch (Exception e){
	      System.err.println(e.toString());
	      System.exit(1);
	  }
  }
}






