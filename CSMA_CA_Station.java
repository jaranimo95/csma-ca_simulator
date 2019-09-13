import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.Semaphore;

public class CSMA_CA_Station implements Runnable {

	private static int t_tot = 0;		// Total simulation time elapsed
	private static int n	 = 8;		// Number of stations
	private int t_s      	 = 10; 		// Time unit (milliseconds)
	private int t_d      	 = 240*t_s;	// Sleep duration between ready checks
	private double p;					// Probability for station to have message 	(0 < p < 1)
	private int t_p     	 = t_d/2;	// Single packet transmission time		 	(0.3*t_d < t_p < 0.6*t_d)
	private int t_difs  	 = 30*t_s;	// Duration of DIFS 					 	(20*t_s < t_difs < 40*t_s)
	private int t_ifs   	 = 10*t_s;	// Duration to receive ACK packet
	private int w 			 = n*t_s;	// Default congestion window
	private int k			 = 1;
	private int t_cw;
	private int m 	   		 = 3;		// Number of packets

	private boolean dataToSend = true;

	private Thread station;
	private String stationName;

	private static Medium medium = new Medium();

	public CSMA_CA_Station(String stationName) {
		this.stationName = stationName;
		p = (double) ThreadLocalRandom.current().nextInt(0, 100) / 100;
	}

	public void start() {
		if(station == null) {
			station = new Thread(this, this.stationName);
			station.start();
		}
	}

	public void run() {
		while (m > 0) {					// While still packets to send..
			boolean hasMessage = false;
			double messageChance;			// Will represent the probability that the station has received a message upon waking up
			while (!hasMessage) {
				try {
					station.sleep(t_d);			// Put message to sleep for t_d time
					messageChance = (double) ThreadLocalRandom.current().nextInt(0, 100) / 100;
					if (messageChance < p) hasMessage = true;
					else 				   hasMessage = false;
				} catch (InterruptedException e) { System.out.println(stationName + ": interrupted"); }
			}

			while (medium.inUse()) { //!lock.tryAcquire()
				try {
					station.sleep(t_s);		// Have thread wait for t_s time
					t_tot += t_s;			// Add wait time to total time 
				} catch (InterruptedException e) { System.out.println(stationName + ": interrupted"); }
			}

			  try 						   { station.sleep(t_difs); }	// Wait for t_difs
			catch (InterruptedException e) { System.out.println(stationName + ": interrupted"); }

			t_tot += t_difs;			// Add wait time to total time
			k = 1;						// Set k = 1
			t_cw = k*w;					// Set t_cw = k*w

			while (true) {	// Wait loop
				  try 						   { station.sleep(t_cw); }	// Wait for t_s
				catch (InterruptedException e) { System.out.println(stationName + ": interrupted"); }
				t_tot += t_cw;					// Add wait time to total time
				t_cw -= t_s;					// Decrement t_s from t_cw

				if (medium.inUse()) {													// If medium is in use
					while (medium.inUse()) {												// While medium is in use
						  try 						   { station.sleep(t_s); }				// Wait for t_s
						catch (InterruptedException e) { System.out.println(stationName + ": interrupted"); }
						t_tot += t_s;														// Add wait time to total time
					}

					  try 						   {station.sleep(t_difs); }	// Now that medium is free, wait for t_difs time
					catch (InterruptedException e) { System.out.println(stationName + ": interrupted"); }
					t_tot += t_difs;					// Add wait time to total time
					if (t_cw > 0) continue;				// If t_cw is greater than 0, re-enter wait loop
					else {								// Else t_cw is NOT greater than 0
						k = 2*k;	
						if (k > 16)   						
							k = 16;
						 t_cw = k*w;
						 continue;
					}
				}
				else {																		// Else if medium is NOT in use
					if (t_cw > 0) continue;														// If t_cw is greater than 0, re-enter wait loop
					else {																		// Else exit wait loop to acquire medium
						medium.setStatus(true);														// Set medium status to busy (true)
						break;
					}
				}
			}

			System.out.println(stationName + ": medium in use...");

			  try 						   { station.sleep(t_p); }
			catch (InterruptedException e) { System.out.println(stationName + ": interrupted"); }

			t_tot += t_p;

			  try 						   { station.sleep(t_ifs); }
			catch (InterruptedException e) { System.out.println(stationName + ": interrupted"); }

			t_tot += t_ifs;
			System.out.println(stationName + ": freeing medium...");
			medium.setStatus(false);
			
			  try 						   { station.sleep(t_difs); }
			catch (InterruptedException e) { System.out.println(stationName + ": interrupted"); }
			t_tot += t_difs;
		
			m--;	// Packet sent over medium successfully, decrement total number of packets left to send
		}
		System.out.println(stationName + " Total Time: " + t_tot);
    }

    public static void main(String[] args) {
    	
    	CSMA_CA_Station s;
    	String name = "";

    	for(int i = 0; i < n; i++) {   // Create n threads
    			 if (i == 0) name = "Station 1";
    		else if (i == 1) name = "Station 2";
    		else if (i == 2) name = "Station 3";
    		else if (i == 3) name = "Station 4";
    		else if (i == 4) name = "Station 5";
    		else if (i == 5) name = "Station 6";
    		else if (i == 6) name = "Station 7";
    		else if (i == 7) name = "Station 8";
    		s = new CSMA_CA_Station(name);
			s.start();
    	}		
	}
}