DOCUMENTATION

---------------------------
--Compilation & Execution--
---------------------------
To compile: javac CSMA_CA_Station.java
To execute: java  CSMA_CA_Station

---------------------------
-------File Structure------
---------------------------
The overall structure of the file begins in the main method, as usual, where each "station" is given a unique
name via a for loop, and passed into the CSMA_CA_Station constructor in order to be initialized. The
CSMA_CA_Station method takes a station name, sets that as the name of the station it was passed into, and
generates a random p value specific to that thread. After each thread is created, they are then started.
From the start method, each station is initialized as a unique thread, which is then started in the next
instruction. This happens immediately after the creation of the thread within the for loop, so the threads 
start one at a time rather than all at once. This prevents complications with medium access later in the 
program. 

Once a thread is initialized, it then calls the run method, where the bulk of the program's logic takes place.
The outermost while loop of the program only exits once each packet has been read in, represented by the
global integer m. The program faithfully follows the multithreaded approach flowchart to a tee, with very
few deviations. At the end of the while loop i.e. when a packet has successfully been sent over the medium,
the total number of packets is decremented and the loop begins again. If the number of packets left to send
is 0, the loop terminates and the total time is printed. Otherwise, work for sending the next packet is done.

I created a separate object called Medium as a semaphore to ensure that only one station can access the medium
at any given time.

---------------------------
------Data & Analysis------
---------------------------

Assumptions

  - T_tot values per station don't exactly mean that this was the exact order in which
      they were printed. The first station to finish is Station 1, with the last being
      Station 8, when, in the context of how I named the stations in the program, maybe
      Station 4 finished first. In the context of this graph, Station 4 (program) == Station 1 (graph).
  - All graphs assume t_s = 10 milliseconds


Graphs

1. T_tot for Different Values of M

  | Station | Station | Station | Station | Station | Station | Station | Station |
  |    1    |    2    |    3    |    4    |    5    |    6    |    7    |    8    |
M  _______________________________________________________________________________|
--|         |         |         |         |         |         |         |         |
1 |  11330  |  11630  |  11930  |  12530  |  21090  |  26940  |  40440  |  40740  |
-----------------------------------------------------------------------------------
2 |  33410  |  48040  |  61550  |  78160  |  81920  |  93440  | 112620  | 114880  |
-----------------------------------------------------------------------------------
3 |  32800  |  68520  |  96220  | 101530  | 109960  | 112220  | 115280  | 117540  |
-----------------------------------------------------------------------------------

Documentation (Point: 25%)
The documentation should include brief manuals (how to compile and run the file, file structure,
result analysis and source codes with brief explanation on every function)
Analysis part should provide at least two graphs. For example, a graph that shows the values of
t_tot for different P and M