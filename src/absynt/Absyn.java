package absynt;
import java.io.Serializable;



/**
 * Abstract class to provide coordinates and locations.

 * The classes of this package
 * are a straighforward implemementation of the EBNF definition,
 * so in most cases, the code is self-explanatory.
 * 
 * @author Initially provided by Martin Steffen.
 * @version  $Id: Absyn.java,v 1.2 2001-04-20 05:36:14 swprakt Exp $
 */



abstract public class Absyn implements Serializable {
    public Location location;
};





//----------------------------------------------------------------------
//	Abstract Syntax for Mist programs
//	------------------------------------
//
//	$Id: Absyn.java,v 1.2 2001-04-20 05:36:14 swprakt Exp $
//
//	$Log: not supported by cvs2svn $
//	Revision 1.1  2001/04/19 12:21:13  swprakt
//	*** empty log message ***
//	
//---------------------------------------------------------------------
