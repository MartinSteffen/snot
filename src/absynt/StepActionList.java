package absynt;
import java.io.Serializable;


/**
 * A list implementation of a set of actions.
 * 
 * To iterate through the list, the steps adhere to the
 * ``Enumaration''-interface
 * @author Initially provided by Martin Steffen.
 * @version $Id: StepActionList.java,v 1.2 2001-05-22 06:12:31 swprakt Exp $
 */


public class StepActionList 
  extends Absynt implements java.util.Enumeration, Serializable { 
  public StepAction      head;
  public StepActionList  next;

  public StepActionList (StepAction s, StepActionList sl) {
    head = s;
    next = sl;
  };

  public boolean hasMoreElements (){ // required by ``Enumeration''
    return next != null;
  }
  
  public Object nextElement () { // required by ``Enumeration''
    // to iterate through the list.
    return next;
  }
}




//----------------------------------------------------------------------
//	Abstract syntax for Snot programs
//	------------------------------------
//
//	$Id: StepActionList.java,v 1.2 2001-05-22 06:12:31 swprakt Exp $
//
//	$Log: not supported by cvs2svn $
//	Revision 1.1  2001/05/04 15:10:20  swprakt
//	changed actions in steps
//	
//	Revision 1.2  2001/05/02 07:03:36  swprakt
//	Abstract syntax compiles.
//	
//	Revision 1.1  2001/05/02 06:36:47  swprakt
//	First proposal according to the requirement specification.
//	
//	[Steffen]
//	
//	
//---------------------------------------------------------------------

