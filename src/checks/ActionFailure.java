package checks;

import java.lang.*;
import absynt.*;


/**
 *Diese Klasse enth"alt die Spezifikation von ActionFailure
 *@author Dimitri Schultheis, Tobias Pugatschov
 *@version: $Id: ActionFailure.java,v 1.2 2001-06-05 14:33:52 swprakt Exp $
 *
 */


public class ActionFailure extends CheckException{
    private
	Action act;
    
    public ActionFailure(Action a, String aString){
	nachricht = aString;
	act = a;
    };

    public Action get_Action(){
	return act;
    };
}
