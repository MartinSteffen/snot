package absynt;
import java.io.Serializable;


/**
 * Variables of the simple assignement language.
 * They are very simply implemented as strings
 * 
 * @author Initially provided by Martin Steffen.
 * @version $Id: Variable.java,v 1.2 2001-05-02 05:52:04 swprakt Exp $	
 */


public class Variable extends Expr implements Serializable { 
  public String name;
  public Type   type;

  public Variable (String s) {
    name = s;
    type = nil;
  };

  public Variable (String s, Type _t) {
    name = s;
    type  = _t};
};




//----------------------------------------------------------------------
//	Abstract syntax for Snot programs
//	------------------------------------
//
//	$Id: Variable.java,v 1.2 2001-05-02 05:52:04 swprakt Exp $
//
//	$Log: not supported by cvs2svn $
//	Revision 1.1  2001/05/02 05:47:32  swprakt
//	First proposal according to the req. spec.
//	
//	[Steffen]
//	
//	
//---------------------------------------------------------------------

