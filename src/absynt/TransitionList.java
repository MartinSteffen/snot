package absynt;
import java.io.Serializable;


/**
 * A list implementation of a set of transitions.
 * 
 * To iterate through the list, the steps adhere to the
 * ``Enumaration''-interface
 * @author Initially provided by Martin Steffen.
 * @version $Id: TransitionList.java,v 1.1 2001-05-02 06:35:12 swprakt Exp $
 */


public class TransitionList 
  extends Absynt implements java.util.Enumeration, Serializable { 
  public Transition      head;
  public TransitionList  next;

  public TransitionList (Transition s, TransitionList sl) {
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
//	$Id: TransitionList.java,v 1.1 2001-05-02 06:35:12 swprakt Exp $
//
//	$Log: not supported by cvs2svn $
//	
//---------------------------------------------------------------------

