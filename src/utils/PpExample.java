
package utils;

import utils.*;
import absynt.*;


/**
   Beispiel fuer die Verwendung des Pretty Printers.
 * @author Initially provided by Martin Steffen.
 * @version  $Id: PpExample.java,v 1.2 2001-05-23 14:55:38 swprakt Exp $    
 */


public class PpExample {
    public static void main(String argv[]) {
	PrettyPrint pp = new PrettyPrint();
	SFC sfc1 = Example.getExample1();
	//StepActionList sal1 = Example.getExampleStepActionList1();
	//System.out.println("*** StepActionList1 ***");
	//pp.print(sal1);
	System.out.println("*** SFC1 ***");
	pp.print(sfc1);
    }
}


