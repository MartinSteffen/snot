package checks;

import java.lang.*;
import absynt.*;

/**
 *Diese Klasse macht eine ganze Menge, n"amlich zum Beispiel:
 *checken von SFCs
 *@author Dimitri Schultheis, Tobias Pugatschov
 *@version: $Id: Snotcheck.java,v 1.13 2001-06-05 13:21:31 swprakt Exp $
 *
 */


public class Snotcheck{

    /**Diese Funktion prueft, ob nur boolsche Variablen vorkommen.
     */
    public static boolean isOnlyBool(SFC aSFCObject){
	return true;
    }



    /**Diese Funktion prueft, ob der istep vorhanden ist
     */
    private static boolean isThereAnIStep(SFC aSFCObject){
	if (aSFCObject.istep == null) {return false;}
	return true;
    }

    /**Diese Funktion prueft, ob alle Transitionen vollstaendig und korrekt sind
     *
     */
    private static boolean isAllTransitionOk(SFC aSFCObject){
	//Quellcode fehlt
	return true;
    }

    /**Diese Funktion prueft, ob alle Actions vollstaendig und korrekt sind
     */
    private static boolean isAllActionOk(SFC aSFCObject){
	//Quellcode fehlt
	return true;
    }

    public static boolean isWellDefined(SFC aSFCObject) throws CheckException {

	if (!isThereAnIStep( aSFCObject ) ){throw new IStepException("Ihr Idioten habt vergessen einen I-Step zu setzen!!");}


	boolean nurBool;
	nurBool = isOnlyBool(aSFCObject);
	if (!nurBool) throw new BoolException("Es kamen auch Integervariablen vor!");
    }


}


//----------------------------------------------------------------------
//	package checks for Snot programs
//	------------------------------------
//
//	$Id: Snotcheck.java,v 1.13 2001-06-05 13:21:31 swprakt Exp $
//
//	$Log: not supported by cvs2svn $
//	Revision 1.12  2001/05/30 14:01:58  swprakt
//	*** empty log message ***
//	
//	Revision 1.11  2001/05/29 15:08:01  swprakt
//	*** empty log message ***
//	
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
