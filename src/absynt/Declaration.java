package absynt;
import java.io.Serializable;


/**
 * Declaration of a variables.
 * 
 * @author Initially provided by Karsten Stahl.
 * @version $Id: Declaration.java,v 1.1 2001-05-10 06:59:50 swprakt Exp $
 */


public class Declaration extends Absynt implements Serializable { 
  public Variable var;
  public Type     type;
  public Constval val;

  public Declaration (Variable _var, Type _type, Constval _val) {
    var  = _var;
    type = _type;
    val  = _val;
  }
}


//----------------------------------------------------------------------
//	Abstract syntax for Snot programs
//	------------------------------------
//
//	$Id: Declaration.java,v 1.1 2001-05-10 06:59:50 swprakt Exp $
//
//	$Log: not supported by cvs2svn $
//	
//	
//---------------------------------------------------------------------

