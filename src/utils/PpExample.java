
package utils;

import utils.*;
import absynt.*;


/**
   Beispiel fuer die Verwendung des Pretty Printers.
 * @author Initially provided by Martin Steffen.
 * @version  $Id: PpExample.java,v 1.1 2001-05-04 15:11:09 swprakt Exp $    
 */


public class PpExample {
    public static void main(String argv[]) {
	PrettyPrint pp = new PrettyPrint();
	SFC sfc1 = Example.getExample1();
	StepActionList sal1 = Example.getExampleStepActionList1();
	//System.out.println("*** StepActionList1 ***");
	//pp.print(sal1);
	System.out.println("*** SFC1 ***");
	pp.print(sfc1);
    }
}


