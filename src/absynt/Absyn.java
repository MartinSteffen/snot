package absynt;
import java.io.Serializable;



/**
 * Abstract class to provide coordinates and locations.
 * Um sp"ater das Parsen zu erleichten, ist ein Feld
 * vorgesehen, welches zumindest die Zeilennummber aufnehmen kann.
 * Alle syntaktischen konstrukte, bei denen Zeileninformation
 * sinnvoll ist, sollen hiervon erben.
 *
 * The classes of this package
 * are a straighforward implmementation of the EBNF definition,
 * so in most cases, the code is self-explanatory.
 * 
 * @author Initially provided by Martin Steffen.
 * @version  $Id: Absyn.java,v 1.1 2001-04-19 12:21:13 swprakt Exp $
 */



abstract public class Absyn implements Serializable {
    public Location location;
};





//----------------------------------------------------------------------
//	Abstract Syntax for Mist programs
//	------------------------------------
//
//	$Id: Absyn.java,v 1.1 2001-04-19 12:21:13 swprakt Exp $
//
//	$Log: not supported by cvs2svn $
//---------------------------------------------------------------------
