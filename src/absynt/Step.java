package absynt;
import java.io.Serializable;


/**
 * A step of a Snot-program.
 * 
 * @author Initially provided by Martin Steffen.
 * @version $Id: Step.java,v 1.3 2001-05-04 09:27:29 swprakt Exp $
 */


public class Step extends Absynt implements Serializable { 
  public String name;
  public ActionList actions;
  //-------------------------------
  public Step (String _name, ActionList _actions) {
    name = _name;
    actions = _actions;
  }
}




//----------------------------------------------------------------------
//	Abstract syntax for Snot programs
//	------------------------------------
//
//	$Id: Step.java,v 1.3 2001-05-04 09:27:29 swprakt Exp $
//
//	$Log: not supported by cvs2svn $
//	Revision 1.2  2001/05/02 07:03:37  swprakt
//	Abstract syntax compiles.
//	
//	Revision 1.1  2001/04/27 08:52:38  swprakt
//	OK
//	
//	
//---------------------------------------------------------------------

