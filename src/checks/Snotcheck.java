package checks;

import java.lang.*;
import absynt.*;

/**
 *Diese Klasse macht eine ganze Menge, n"amlich zum Beispiel:
 *-checken von SFCs
 *-checken von SFCs
 *-und nat"urlich auch checken von SFCs
 *@author Dimitri Schultheis, Tobias Pugatschov
 *@version: $Id: Snotcheck.java,v 1.11 2001-05-29 15:08:01 swprakt Exp $
 *
 */


public class Snotcheck{

    private static boolean isThereAnIStep(SFC aSFCObject){
	if (aSFCObject.istep == null) {return false;}
	return true;
    }

    public static boolean isWellDefined(SFC aSFCObject) throws Exception {

	if (!isThereAnIStep( aSFCObject ) ){throw new NoIStepException();}

	if (false) {
	    throw new Exception("Es ist voll 'was schief gelaufen!");
	}
	boolean nurBool;
	nurBool = isOnlyBool(aSFCObject);
	if (nurBool) {
	    return true;
	} else {
	    throw new Exception("Es ist etwas schief gelaufen!");
	}
    }

    public static boolean isOnlyBool(SFC aSFCObject) throws Exception {
	return false;
    }
    public static void main(String[] args){
	SFC test= new SFC(null,null,null,null,null);
	boolean a;
	try{
	a=isWellDefined(test);
	}catch (Exception e){};
    }

}


//----------------------------------------------------------------------
//	package checks for Snot programs
//	------------------------------------
//
//	$Id: Snotcheck.java,v 1.11 2001-05-29 15:08:01 swprakt Exp $
//
//	$Log: not supported by cvs2svn $
//	Revision 1.10  2001/05/22 14:54:59  swprakt
//	*** empty log message ***
//	
//	Revision 1.9  2001/05/22 14:50:36  swprakt
//	*** empty log message ***
//	
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
