package checks;

import java.lang.*;
import absynt.*;

/**
 *Diese Klasse macht eine ganze Menge, n"amlich zum Beispiel:
 *-checken von SFCs
 *-checken von SFCs
 *-und nat"urlich auch checken von SFCs
 *@author Dimitri Schultheis, Tobias Pugatschov
 *@version: $Id: Snotcheck.java,v 1.9 2001-05-22 14:50:36 swprakt Exp $
 *
 */


public class Snotcheck{



    public boolean isWellDefine(SFC aSFCObject) throws Exception {

	if (false) {
	    throw new Exception("Es ist etwas schief gelaufen!");
	}
	boolean nurBool;
	nurBool = onlyBool(aSFCObject);
	if (nurBool) {
	    return false;
	} else {
	    throw new Exception("Es ist etwas schief gelaufen!");
	}
	return true;
    }

    public boolean onlyBool(SFC aSFCObject) throws Exception {
	return false;
    }

}


//----------------------------------------------------------------------
//	package checks for Snot programs
//	------------------------------------
//
//	$Id: Snotcheck.java,v 1.9 2001-05-22 14:50:36 swprakt Exp $
//
//	$Log: not supported by cvs2svn $
//	Revision 1.8  2001/05/22 14:49:31  swprakt
//	alles ok
//	
//	Revision 1.7  2001/05/22 14:41:53  swprakt
//	*** empty log message ***
//	
//	Revision 1.6  2001/05/22 14:31:47  swprakt
//	*** empty log message ***
//	
//	Revision 1.5  2001/05/22 14:28:04  swprakt
//	*** empty log message ***
//	
//	Revision 1.4  2001/05/22 14:25:23  swprakt
//	ok
//	
//	
//--------------------------------------------------------------------
