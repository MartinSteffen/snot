package absynt;
import java.io.Serializable;


/**
 * Class for sfc actions. An action consists of a qualifier
 * and a program to be executed.
 *
 * @author Initially provided by Martin Steffen.
 * @version $Id: Action.java,v 1.1 2001-05-02 05:09:34 swprakt Exp $
 */


public class Action  extends Absynt implements Serializable { 
  public ActionQualifier qualifier;
  public StmtList        sap;    // simple assignement program 
  
}




//----------------------------------------------------------------------
//	Abstract syntax for Snot programs
//	------------------------------------
//
//	$Id: Action.java,v 1.1 2001-05-02 05:09:34 swprakt Exp $
//
//	$Log: not supported by cvs2svn $
//	
//---------------------------------------------------------------------

