package absynt;
import java.io.Serializable;
import java.util.LinkedList;


/**
 * SFC.java
 * Class for sfc-programs, the top level syntactic construct,
 * i.e., the entry point.
 * @author Initially provided by Martin Steffen.
 * @version $Id: SFC.java,v 1.6 2001-06-03 15:46:34 swprakt Exp $
 */


public class SFC extends Absynt implements Serializable{ 
  public Step istep;
  public LinkedList steps;
  public LinkedList transs;
  public LinkedList actions;
  public LinkedList declist;
  // -------------------
  /** 
   * Constructor just stores the arguments into the fields
   **/
  public SFC (Step _istep,
	      LinkedList _steps, 
	      LinkedList _transs,
	      LinkedList _actions,
	      LinkedList _declist) {
    istep  = _istep;
    steps  = _steps;
    transs = _transs;
    actions = _actions;
    declist = _declist;
  }
  
  /** 
   * Constructor creates empty LinkedLists
   **/
  public SFC() {
    istep = null;
    steps  = new LinkedList();
    transs = new LinkedList();
    actions = new LinkedList();
    declist = new LinkedList();      
  }
}




//----------------------------------------------------------------------
//	Abstract Syntax for Snot Programs
//	------------------------------------
//
//	$Id: SFC.java,v 1.6 2001-06-03 15:46:34 swprakt Exp $
//
//	$Log: not supported by cvs2svn $
//	Revision 1.5  2001/05/23 14:56:33  swprakt
//	Die Listenimplementierung wurde veraendert, es wird nun LinkedList verwendet
//	
//	Revision 1.4  2001/05/10 06:59:52  swprakt
//	added a declaration list to the SFC's
//	
//	Revision 1.3  2001/05/02 07:03:37  swprakt
//	Abstract syntax compiles.
//	
//	Revision 1.2  2001/05/02 04:40:19  swprakt
//	ok
//	
//	
//---------------------------------------------------------------------

