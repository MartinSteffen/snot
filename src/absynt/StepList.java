package absynt;
import java.io.Serializable;


/**
 * A list implementation of a set of states.
 * 
 * To iterate through the list, the steps adhere to the
 * ``Enumaration''-interface
 * @author Initially provided by Martin Steffen.
 * @version $Id: StepList.java,v 1.4 2001-05-22 05:59:08 swprakt Exp $
 */


public class StepList 
  extends Absynt implements java.util.Enumeration, Serializable { 
  public Step      head;
  public StepList  next;

  public StepList (Step s, StepList sl) {
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
//	$Id: StepList.java,v 1.4 2001-05-22 05:59:08 swprakt Exp $
//
//	$Log: not supported by cvs2svn $
//	Revision 1.3  2001/05/02 07:03:37  swprakt
//	Abstract syntax compiles.
//	
//	Revision 1.2  2001/05/02 06:34:16  swprakt
//	*** empty log message ***
//	
//	Revision 1.1  2001/05/02 06:32:37  swprakt
//	Initial proposal according the the req. spec.
//	
//	
//---------------------------------------------------------------------

