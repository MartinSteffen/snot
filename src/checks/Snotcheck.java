package checks;

import java.util.*;
import java.awt.*;
import java.lang.*;
import absynt.*;

/**
 *Diese Klasse macht eine ganze Menge, n"amlich zum Beispiel:
 *checken von SFCs
 *@author Dimitri Schultheis, Tobias Pugatschov
 *@version: $Id: Snotcheck.java,v 1.18 2001-06-07 11:24:00 swprakt Exp $
 *
 */

public class Snotcheck{



    /**Diese Funktion prueft Deklarationsliste
     */


    private static boolean isDeclarationOk(SFC aSFCObject) throws DecListFailure {
	//Diese Methode prueft, ob in den Dekarationen keine NULL-Werte stehen, ob keine Variable doppelt deklariert ist und ob der in der Keklaration angegebene Typ gleich dem Typ der Variable ist.
	
	
	//Pruefung auf NULL-Werte:
	
	LinkedList decList = aSFCObject.declist;
	Declaration decl,decl2;
	Variable var,var2;
	
	if (decList != null){
	    for (int i=0; i < decList.size(); i++){
		decl = (Declaration)decList.get(i);
		if (decl.var == null || decl.type == null || decl.val == null){
		    //Wenn einer dieser Eintr"age fehlt, dann werfe eine Exception (DeclarationFailure) mit der entsprechenden Declaration als Argument
		    throw new DecListFailure(decl, "Missing argument(s) in declaration.");
			}
	    }
	}
	
	
	//Pr"ufung, ob Variable doppelt vorkommt:
	
	if (decList != null){
	    for (int i=0; i < decList.size(); i++){
		decl = (Declaration)decList.get(i);                          //durchgehen aller Deklarationen
		var = decl.var;                                 //auslesen der entsprechenden Variable
		if (var != null){
		    for (int j=i+1; j < decList.size(); j++){
			decl2 = (Declaration)decList.get(j);                 //durchgehen aller nachfolgenden Deklarationen
			var2 = decl2.var;
			if (var.name == var2.name){            //vergleich aller Variablennamen
			    throw new DecListFailure(decl, "This variable is declared twice!");
			}
		    }
		}
	    }	
	}
	
	
	//Pr"ufung auf Typgleichheit:
	
	if (decList != null){
	    for (int i=1; i < decList.size(); i++){
		decl = (Declaration)decList.get(i);
		var = decl.var;
		if (var != null){
		    if (decl.type != var.type){
			throw new DecListFailure(decl, "Type Error!");}   //Der Typ der Variable stimmt nicht mit dem Typ der Deklaration "uberein
		}
	    }
	}
	
	
	
	
	
	return true;	
	
    }
    
    







    /**Diese Funktion prueft, ob alle Actions vollstaendig und korrekt sind
     */
    private static boolean isAllActionOk(SFC aSFCObject) throws ActionFailure {
	
	LinkedList actionList = aSFCObject.actions;
	Action action;
	
	for (int i=0; i < actionList.size(); i++){
	    action = (Action)actionList.get(i);
	    if (action.sap == null){throw new ActionFailure(action, "Missing sap in this Action.");}
	    if (action.a_name == null){throw new ActionFailure(action, "Action without a name.");}
	}


	return true;
    }










    /**Diese Funktion prueft alle Steps(vollstaendig, korrekt)
     */
    private static boolean isAllStepOk(SFC aSFCObject) throws StepFailure {
	//Quellcode fehlt
	return true;
    }

    /**Diese Funktion prueft, ob der istep vorhanden ist
     */
    private static boolean isThereAnIStep(SFC aSFCObject) throws IStepException{
	if (aSFCObject.istep == null) {return false;}
	return true;
    }

    /**Diese Funktion prueft, ob alle Transitionen vollstaendig und korrekt sind
     *
     */
    private static boolean isAllTransitionOk(SFC aSFCObject) throws TransitionFailure{
	//Bevor diese Methode aufgerufen wird, ist zu pruefen, ob die Transitionsliste=null ist.
	//Wenn die Liste nicht existiert, arbeitet diese Methode m"oglicherweise nicht korrekt.

	LinkedList translist = aSFCObject.transs;
	Transition trans;
	
	if (translist!=null){
	    for (int i=0; i < translist.size(); i++){
		trans = (Transition)translist.get(i);
		if (trans.source == null || trans.target == null || trans.guard == null){
		    //Wenn einer der Werte fehlt, dann werfe eine Exception (TransitionFailure) mit der entsprechenden Transition als Argument
		    throw new TransitionFailure(trans, "Missing argument(s) in transition.");
		}
	    }
	}
	
	
	
	return true;
	
    }





    /**Diese Funktion prueft, ob nur boolsche Variablen vorkommen.
     */
    public static boolean isOnlyBool(SFC aSFCObject){
	return true;
    }



    /**Diese Funktion prueft, ob der angegebene SFC korrekt ist
       
     */

    public static boolean isWellDefined(SFC aSFCObject) throws CheckException {

	if (!isThereAnIStep( aSFCObject ) ){throw new IStepException("There is no I-Step!!");}
	boolean nurBool,test;

	test = isDeclarationOk(aSFCObject);
	test = isAllActionOk(aSFCObject);
	test = isAllStepOk(aSFCObject);
	test = isAllTransitionOk(aSFCObject);
//	nurBool = isOnlyBool(aSFCObject);
//	if (!nurBool) throw new BoolException("Es kamen auch Integervariablen vor!");
	return true;
    }


}


//----------------------------------------------------------------------
//	package checks for Snot programs
//	------------------------------------
//
//	$Id: Snotcheck.java,v 1.18 2001-06-07 11:24:00 swprakt Exp $
//
//	$Log: not supported by cvs2svn $
//	Revision 1.17  2001/06/07 11:00:12  swprakt
//	*** empty log message ***
//	
//	Revision 1.16  2001/06/05 14:42:17  swprakt
//	*** empty log message ***
//	
//	Revision 1.15  2001/06/05 14:33:52  swprakt
//	*** empty log message ***
//	
//	Revision 1.14  2001/06/05 14:17:34  swprakt
//	*** empty log message ***
//	
//	Revision 1.13  2001/06/05 13:21:31  swprakt
//	*** empty log message ***
//	
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
