
package io;


import java.io.IOException;
import java.io.*;
import utils.*;
import absynt.*;


/**
 * Standalone file to test the parser on input files
 * @author Martin Steffen, Karsten Stahl
 * @version $Id: Main.java,v 1.4 2001-07-10 14:28:06 swprakt Exp $
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
      PrettyPrint pp = new PrettyPrint();
      SFC sfc1 =  parser.parseFile(datei);
      System.out.println("*** Test: pretty print the result of parsing ***");
      pp.print(sfc1);      
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






