package absynt;
import java.io.Serializable;
import java.util.LinkedList;


/**
 * Class for sfc actions. An action consists of a qualifier
 * and a program to be executed.
 *
 * @author Initially provided by Martin Steffen.
 * @version $Id: Action.java,v 1.4 2001-05-23 14:56:32 swprakt Exp $
 */


public class Action  extends Absynt implements Serializable { 
  public String   a_name;
  public LinkedList sap;    // simple assignement program 

  public Action (String _a_name, LinkedList _sap) {
    a_name = _a_name;
    sap    = _sap;
  }
  
}




//----------------------------------------------------------------------
//	Abstract syntax for Snot programs
//	------------------------------------
//
//	$Id: Action.java,v 1.4 2001-05-23 14:56:32 swprakt Exp $
//
//	$Log: not supported by cvs2svn $
//	Revision 1.3  2001/05/04 09:50:38  swprakt
//	*** empty log message ***
//	
//	Revision 1.2  2001/05/02 05:11:25  swprakt
//	I added the standard constructor. [Steffen]
//	
//	Revision 1.1  2001/05/02 05:09:34  swprakt
//	First proposal according to the req. spec.
//	
//	I decided to replace the class Sap directly by a
//	statement list.
//	
//	[Steffen]
//	
//	
//---------------------------------------------------------------------

