package absynt;
import java.io.Serializable;


/**
 * Variables of the simple assignement language.
 * They are very simply implemented as strings
 * 
 * @author Initially provided by Martin Steffen.
 * @version $Id: Variable.java,v 1.1 2001-05-02 05:47:32 swprakt Exp $	
 */


public class Variable extends Expr implements Serializable { 
  public String name;

  public Variable (String s) {
    name = s;};
};




//----------------------------------------------------------------------
//	Abstract syntax for Snot programs
//	------------------------------------
//
//	$Id: Variable.java,v 1.1 2001-05-02 05:47:32 swprakt Exp $
//
//	$Log: not supported by cvs2svn $
//	
//---------------------------------------------------------------------

