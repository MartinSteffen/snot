package absynt;
import java.io.Serializable;


/**
 * A list implementation of a set of declarations.
 * 
 * To iterate through the list, the steps adhere to the
 * ``Enumaration''-interface
 * @author Initially provided by Karsten Stahl.
 * @version $Id: DeclarationList.java,v 1.1 2001-05-10 06:59:51 swprakt Exp $
 */


public class DeclarationList 
  extends Absynt implements java.util.Enumeration, Serializable { 
  public Declaration      head;
  public DeclarationList  next;

  public DeclarationList (Declaration d, DeclarationList dl) {
    head = d;
    next = dl;
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
//	$Id: DeclarationList.java,v 1.1 2001-05-10 06:59:51 swprakt Exp $
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

