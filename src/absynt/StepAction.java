package absynt;
import java.io.Serializable;


/**
 * Class for sfc actions associated with a step. An action consists of a
 * qualifier and the name of a program to be executed.
 *
 * @author Initially provided by Martin Steffen.
 * @version $Id: StepAction.java,v 1.4 2001-05-10 06:59:52 swprakt Exp $
 */


public class StepAction  extends Absynt implements Serializable { 
  public ActionQualifier qualifier;
  public String          a_name;    // name of program 

  public StepAction (ActionQualifier _qualifier, String _a_name) {
    qualifier = _qualifier;
    a_name    = _a_name;
  }
}




//----------------------------------------------------------------------
//	Abstract syntax for Snot programs
//	------------------------------------
//
//	$Id: StepAction.java,v 1.4 2001-05-10 06:59:52 swprakt Exp $
//
//	$Log: not supported by cvs2svn $
//	Revision 1.3  2001/05/05 10:14:21  swprakt
//	Requirement spec. adapted to the implementation
//	
//	[Steffen]
//	
//	Revision 1.2  2001/05/05 09:45:56  swprakt
//	I adapted the comment [Steffen]
//	
//	Revision 1.1  2001/05/04 15:10:19  swprakt
//	changed actions in steps
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

