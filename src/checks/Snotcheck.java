package checks;

import java.util.*;
import java.awt.*;
import java.lang.*;
import absynt.*;

/**
 *Diese Klasse macht eine ganze Menge, n"amlich zum Beispiel:
 *checken von SFCs
 *@author Dimitri Schultheis, Tobias Pugatschov
 *@version: $Id: Snotcheck.java,v 1.23 2001-06-12 14:55:02 swprakt Exp $
 *
 */

public class Snotcheck{



    /**Diese Funktion prueft Deklarationsliste
     */


    private static boolean isDeclarationOk(SFC aSFCObject) throws DecListFailure {
	//Diese Methode prueft, ob in den Dekarationen keine NULL-Werte stehen, ob keine Variable doppelt deklariert ist und ob der in der Deklaration angegebene Typ gleich dem Typ der Variable ist.
	
	
	//Pruefung auf NULL-Werte:
	
	LinkedList decList = aSFCObject.declist;
	Declaration decl,decl2;
	Variable var,var2;
	String className1, className2;
	Class typeClass1, typeClass2;
	

	
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
		typeClass1 = decl.type.getClass();
		className1 = typeClass1.getName();
		if (var != null){
		    typeClass2 = var.type.getClass();
		    className2 = typeClass2.getName();
		    if (className1 != className2){
			throw new DecListFailure(decl, "Type Error!");}   //Der Typ der Variable stimmt nicht mit dem Typ der Deklaration "uberein
		}
	    }
	}
	
	
	
	
	
	return true;	
	
    }
    
    







    /**Diese Funktion prueft, ob alle Actions vollstaendig und korrekt sind
     */
    private static boolean isAllActionOk(SFC aSFCObject) throws ActionFailure {
	//Diese Methode pr"uft zuerst, ob bei den actions aus der actionList keine Null-Werte stehen. Anschliessend wird noch geprueft, ob die Namen eindeutig, d.h. nicht doppelt, sind.

	LinkedList actionList = aSFCObject.actions;
	String name1,name2;
	Action action,action2;
	
	if (actionList = null){return true;}    //wenn in der actionList keine Eintraege vorhanden sind, kann auch kein Eintrag fehlerhaft sein, also sind alle Eintraege korrekt

	//Pruefung auf Null-Werte
	for (int i=0; i < actionList.size(); i++){
	    action = (Action)actionList.get(i);
	    if (action.sap == null){throw new ActionFailure(action, "Missing sap in this Action.");}
	    if (action.a_name == null){throw new ActionFailure(action, "Action without a name.");}
	}

	//Namenspruefung
	for (int i=0; i < actionList.size(); i++){
	    action = (Action)actionList.get(i);
	    name1 = action.a_name;
	    for (int j=i; j < actionList.size(); j++){
		action2 = (Action)actionList.get(j);
		name2 = action2.a_name;
		if (name1 == name2){throw new ActionFailure(action, "Action with a name, which is already used.");}
	    }
	    
	}


	//nun muessen nur noch die einzelnen sap`s ueberprueft werden:
	for (int i=0; i < actionList.size(); i++){
	    action = (Action)actionList.get(i);
	    sap1 = action.sap;                           //auslesen der i-ten sap
	    for (int j=0; j < sap1.size(); j++){
		stmt1 = (Stmt)sap1.get(j);               //auslesen des j-ten Statements aus der sap
		class1 = stmt.getClass();
		class1Name = class1.getName();
		if (class1Name == "Assign" || class1Name == "Skip"){
		    if (class1Name == "Assign"){
			//pruefen, ob var und val vernuenftig sind
			if (stmt1.var == null){throw new ActionFailure(action, "This statement has no var.")}
			if (stmt1.val == null){throw new ActionFailure(action, "This statement has no val.")}
		    }
		} else {throw new ActionFailure(null, "Error, that can`t be solved."}
	    }


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
	if (aSFCObject.istep == null) {throw new IStepException("There is no I-Step!!");}
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

	LinkedList decList = aSFCObject.declist;
	Declaration decl;
	String nameOfClass;
	Class typeClass;
	boolean nurBool = true;
	
	for (int i=0; i < decList.size(); i++){
	    decl = (Declaration)decList.get(i);
	    typeClass = decl.type.getClass();
	    nameOfClass = typeClass.getName();
	    if(nameOfClass != "BoolType"){nurBool = false;}
	}
	return nurBool;
    }





    /**Diese Funktion prueft, ob der angegebene SFC korrekt ist
       
     */

    public static boolean isWellDefined(SFC aSFCObject) throws CheckException {

	if (aSFCObject == null){throw new IStepException("No SFC selected.");}

	boolean nurBool,test;

	test = isThereAnIStep(aSFCObject);
	test = isDeclarationOk(aSFCObject);
	test = isAllActionOk(aSFCObject);
	test = isAllStepOk(aSFCObject);
	test = isAllTransitionOk(aSFCObject);
	return true;
    }


}


//----------------------------------------------------------------------
//	package checks for Snot programs
//	------------------------------------
//
//	$Id: Snotcheck.java,v 1.23 2001-06-12 14:55:02 swprakt Exp $
//
//	$Log: not supported by cvs2svn $
//	Revision 1.21  2001/06/08 09:00:56  swprakt
//	*** empty log message ***
//	
//	Revision 1.20  2001/06/08 08:56:17  swprakt
//	*** empty log message ***
//	
//	Revision 1.19  2001/06/07 12:04:25  swprakt
//	*** empty log message ***
//	
//	Revision 1.18  2001/06/07 11:24:00  swprakt
//	*** empty log message ***
//	
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
