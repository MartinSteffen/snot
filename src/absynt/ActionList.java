package absynt;
import java.io.Serializable;


/**
 * A list implementation of a set of actions.
 * 
 * To iterate through the list, the steps adhere to the
 * ``Enumaration''-interface
 * @author Initially provided by Martin Steffen.
 * @version $Id: ActionList.java,v 1.2 2001-05-02 07:03:36 swprakt Exp $
 */


public class ActionList 
  extends Absynt implements java.util.Enumeration, Serializable { 
  public Action      head;
  public ActionList  next;

  public ActionList (Action s, ActionList sl) {
    head = s;
    next = sl;
  };

  public boolean hasMoreElements (){ // required by ``Enumeration''
    return next != null;
  }
  
  public Object nextElement () { // required byte ``Enumeration''
    // to iterate through the list.
    return next;
  }
}




//----------------------------------------------------------------------
//	Abstract syntax for Snot programs
//	------------------------------------
//
//	$Id: ActionList.java,v 1.2 2001-05-02 07:03:36 swprakt Exp $
//
//	$Log: not supported by cvs2svn $
//	Revision 1.1  2001/05/02 06:36:47  swprakt
//	First proposal according to the requirement specification.
//	
//	[Steffen]
//	
//	
//---------------------------------------------------------------------

