package absynt;
import java.io.Serializable;


/**
 * An assignment-statement as part of the simple assignment language
 * 
 * @author Initially provided by Martin Steffen.
 * @version $Id: Assign.java,v 1.3 2001-05-03 13:31:29 swprakt Exp $
 */


public class Assign extends Stmt implements Serializable { 
  public Variable var;
  public Expr     val;


  public Assign (Variable x, Expr e) {
    var = x;
    val = e;
  };
}




//----------------------------------------------------------------------
//	Abstract syntax for Snot programs
//	------------------------------------
//
//	$Id: Assign.java,v 1.3 2001-05-03 13:31:29 swprakt Exp $
//
//	$Log: not supported by cvs2svn $
//	Revision 1.2  2001/05/02 07:03:36  swprakt
//	Abstract syntax compiles.
//	
//	Revision 1.1  2001/05/02 05:39:21  swprakt
//	First proposal
//	
//---------------------------------------------------------------------

