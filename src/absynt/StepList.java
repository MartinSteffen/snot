package absynt;
import java.io.Serializable;


/**
 * A list implementation of a set of states.
 * 
 * To iterate through the list, the steps adhere to the
 * ``Enumaration''-interface
 * @author Initially provided by Martin Steffen.
 * @version $Id: <dollar>	
 */


public class StepList 
  extends Absynt implements java.util.Enumeration, Serializable { 
  public Step      head;
  public StepList  next;

  public StepList (Step s, StepList sl) {
    head = s;
    tail = sl;
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
//	$Id: StepList.java,v 1.1 2001-05-02 06:32:37 swprakt Exp $
//
//	$Log: not supported by cvs2svn $
//	
//---------------------------------------------------------------------

