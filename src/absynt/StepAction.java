package absynt;
import java.io.Serializable;


/**
 * Class for sfc actions. An action consists of a qualifier
 * and a program to be executed.
 *
 * @author Initially provided by Martin Steffen.
 * @version $Id: StepAction.java,v 1.1 2001-05-04 15:10:19 swprakt Exp $
 */


public class StepAction  extends Absynt implements Serializable { 
  public ActionQualifier qualifier;
  public String          a_name;    // simple assignement program 

  public StepAction (ActionQualifier _qualifier, String _a_name) {
    qualifier = _qualifier;
    a_name    = _a_name;
  }
  
}




//----------------------------------------------------------------------
//	Abstract syntax for Snot programs
//	------------------------------------
//
//	$Id: StepAction.java,v 1.1 2001-05-04 15:10:19 swprakt Exp $
//
//	$Log: not supported by cvs2svn $
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

