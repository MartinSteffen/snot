package absynt;
import java.io.Serializable;
import java.util.LinkedList;


/**
 * A step of a Snot-program.
 * 
 * @author Initially provided by Martin Steffen.
 * @version $Id: Step.java,v 1.6 2001-05-23 14:56:34 swprakt Exp $
 */


public class Step extends Absynt implements Serializable { 
  public String name;
  public LinkedList actions;
  public Position pos;
  //-------------------------------
  public Step (String _name) {
    name = _name;
    actions = new LinkedList();
  }
  public Step (String _name, LinkedList _actions) {
    name = _name;
    actions = _actions;
  }
  public Step (String _name, LinkedList _actions, Position _pos) {
    name = _name;
    actions = _actions;
    pos     = _pos;
  }
}




//----------------------------------------------------------------------
//	Abstract syntax for Snot programs
//	------------------------------------
//
//	$Id: Step.java,v 1.6 2001-05-23 14:56:34 swprakt Exp $
//
//	$Log: not supported by cvs2svn $
//	Revision 1.5  2001/05/17 08:30:01  swprakt
//	Added a new constructor
//	
//	Revision 1.4  2001/05/04 15:10:19  swprakt
//	changed actions in steps
//	
//	Revision 1.3  2001/05/04 09:27:29  swprakt
//	removed bug, changed field from `a_name' to `name'
//	
//	Revision 1.2  2001/05/02 07:03:37  swprakt
//	Abstract syntax compiles.
//	
//	Revision 1.1  2001/04/27 08:52:38  swprakt
//	OK
//	
//	
//---------------------------------------------------------------------

