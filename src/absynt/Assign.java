package absynt;
import java.io.Serializable;


/**
 * An assignement-statement as part of the simple assignement language
 * 
 * @author Initially provided by Martin Steffen.
 * @version $Id: Assign.java,v 1.1 2001-05-02 05:39:21 swprakt Exp $
 */


public class Assign extends Action implements Serializable { 
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
//	$Id: Assign.java,v 1.1 2001-05-02 05:39:21 swprakt Exp $
//
//	$Log: not supported by cvs2svn $
//---------------------------------------------------------------------

