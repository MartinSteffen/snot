
package simulator;

import utils.*;
import absynt.*;


/**
   Beispiel fuer die Verwendung des Simulators.
 * @author Jörn Fiebelkorn
 */


public class SimExample {
	
    public static void main(String argv[]) {
    	
		SFC sfc1 = Example.getExample1(); 		// SFC erzeugen

		Simulator sim = new Simulator(sfc1);	// Simulator erzeugen
		
		sim.Initialize();						// Simulator initialisieren	
		
		System.out.print("Anfangszustand: ");
		sim.PrintConfiguration(System.out);				// Zustand ausgeben
		
                try {

			for (int i=1; i<=15; i++) {
			
              			sim.SingleStep();
			
				System.out.print("nach "+i+". Schritt: ");

				sim.PrintConfiguration(System.out);
			}
		}
		catch (SimException simex) {
			System.out.print("boeser Fehler: " + simex.getMessage() );
		}
		
    }
}


