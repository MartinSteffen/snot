package checks;

import java.lang.*;
import absynt.*;


public class TransitionFailure extends CheckException{
    private
	Transition trans;

    public TransitionFailure(Transition aTransition, String aString){
	nachricht = aString;
	trans = aTransition;
    }

    public Transition get_Trans(){
	return trans;
    }
}
