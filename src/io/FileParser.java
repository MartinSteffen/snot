package io;

import java.io.IOException;
import java.io.*;


/**
 * To test the parser
 * @author Martin Steffe
 * @version $Id: FileParser.java,v 1.2 2001-06-30 15:03:21 swprakt Exp $
 */
class FileParser{
 
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




