package absynt;
import java.io.Serializable;


/**
 * A list implementation of a set of statements.
 * 
 * To iterate through the list, the statements adhere to the
 * ``Enumeration''-interface
 * @author Initially provided by Martin Steffen.
 * @version $Id: StmtList.java,v 1.2 2001-05-02 07:03:37 swprakt Exp $
 */


public class StmtList 
  extends Absynt implements java.util.Enumeration, Serializable { 
  public Stmt      head;
  public StmtList  next;

  public StmtList (Stmt s, StmtList sl) {
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
//	$Id: StmtList.java,v 1.2 2001-05-02 07:03:37 swprakt Exp $
//
//	$Log: not supported by cvs2svn $
//	Revision 1.1  2001/05/02 06:47:47  swprakt
//	*** empty log message ***
//	
//---------------------------------------------------------------------

