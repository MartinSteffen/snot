package absynt;
import java.io.Serializable;


/**
 * Constant values as expressions.
 * Supported are integers and booleans.
 * 
 * @author Initially provided by Martin Steffen.
 * @version $Id: Constval.java,v 1.1 2001-05-02 06:01:10 swprakt Exp $
 */


public class Constval  extends Expr implements Serializable { 
  /**
     Constant values are integers and booleans; they are directly
     implemented using the corresponding data types from Java.
   */

  public Object val;
  
  /**
   * 2 overloaded constructors. The value is stored as ``Object''
   */
  public Constval (boolean v) {
    val = new Boolean (v);}


  public Constval (int v) {
    val = new Integer (v);}
}




//----------------------------------------------------------------------
//	Abstract syntax for Snot programs
//	------------------------------------
//
//	$Id: Constval.java,v 1.1 2001-05-02 06:01:10 swprakt Exp $
//
//	$Log: not supported by cvs2svn $
//	
//---------------------------------------------------------------------

