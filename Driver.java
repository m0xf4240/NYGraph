// ===============================================================================================================================================================
// Driver
// A solver for shortest path using Dijkstra's Algorithm
// Terri Hultum and Michael Millian
// Fall 2013
// ===============================================================================================================================================================

import java.io.*;
import java.util.*;

// ================================================================================================================================================
public class Driver{
	// ===============================================================================================================================================================

	boolean debug=false;
	boolean buildDebug=false;
	boolean bwDebugVerbose=false;
	boolean bwDebugSparse=true;
	int problemCity=0;
	long enqueuedCount=2;

	// ===========================================================================================================================================================
	// main method switches to a non-static method
	public static void main(String args[]) throws IOException{

		try{
			new Driver().go();
		}
		catch(StackOverflowError e){
			System.out.println("You broke it.");
		}
	} //main
	// ===========================================================================================================================================================


	// ===========================================================================================================================================================
	/**
	 * Read the input file, generate a Heap, and solve.
	 * @throws IOException
	 */
	public void go() throws IOException {

		
		
		Scanner in = new Scanner(System.in);
		System.out.println("Do you want an obscene amount of debugging code? (0/1)");
		int d = in.nextInt();
		if (d==1){
			
			debug=true;
		}
		
		System.out.println("Enter a 1 for NY.gr or a 2 for us.gr");
		int gr = in.nextInt();
		File f;
		switch (gr){
		case 1:
			f=new File("NY.gr");
			break;
		case 2:
			f=new File("us.gr");
			break;
		case 3:
			System.out.println("SECRET CASE!");
			f=new File("test2.gr");
			break;
		default:
			f=new File("NY.gr");
			break;
		}
		System.out.println("Loading file and building map.");
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
		String line=null;
		HashMap<Integer,ArrayList<Tuple<Integer,Integer>>> graph = new HashMap<Integer,ArrayList<Tuple<Integer,Integer>>>(10);
		while ((line=br.readLine())!=null){
			String[] elements=line.split(" ");
			if (elements[0].equals("a")){
				int firstVertex=Integer.valueOf(elements[1]);
				int secondVertex=Integer.valueOf(elements[2]);
				int dist=Integer.valueOf(elements[3]);
				if (graph.containsKey(firstVertex)){
					graph.get(firstVertex).add(new Tuple<Integer,Integer>(secondVertex,dist));
				} else {
					ArrayList<Tuple<Integer,Integer>> a = new ArrayList<Tuple<Integer,Integer>>(10);
					a.add(new Tuple<Integer,Integer>(secondVertex,dist));
					graph.put(firstVertex,a);
				}

			}
			else if (elements[0].equals("c")){
				continue;
			}
			else if (elements[0].equals("p")){
				if(debug){					
					System.out.println("Hello. Running on a file of size about "+((f.length()-130)/13));
				}
				int size=Integer.valueOf(elements[2]);
				graph = new HashMap<Integer,ArrayList<Tuple<Integer,Integer>>>(size*2);//make sure its big enough to avoid too many conflicts
			}
		}
		br.close();

		if (buildDebug){
			print(graph);
		}

		Set<Integer>keySet=graph.keySet();
		System.out.println("Loaded "+keySet.size()+" cities.");
		ArrayList<City> cityList= new ArrayList<City>(keySet.size());
		for (Integer i:graph.keySet()){
			cityList.add(new City(i,Integer.MAX_VALUE));
			City a = cityList.get(cityList.size()-1);
			a.setDistFromEnd(Integer.MAX_VALUE);
			a.setStateFromEnd(-1);
		}

		Collections.sort(cityList, new Comparator<City>() {
			public int compare(City a, City b) {
				return Integer.signum(a.getName() - b.getName());
			}
		});

		if(buildDebug){
			for (City c:cityList){
				System.out.println(c);
			}
		}
		// ************************
		// All paths
		/*
		for (int i=1; i<cityList.size()+1;i++){
			for (int j=1; j<cityList.size()+1;j++) {
				//reset cityList
				for (City c:cityList){
					c.reset();
				}
				City k=dijkstra(i, j, graph, cityList);
				printPath(k);
				System.out.println("*************************");
			}
		}
		 */
		// ************************
		
		System.out.println("===============================================================");
		System.out.println("Enter a starting city or 0 to quit");
		int startCity = in.nextInt();
		while (startCity>cityList.size()){
			System.out.println("That city number is too large. Try again.");
			startCity = in.nextInt();
		}
		if (startCity<=0){
			return;
		}
		System.out.println("You will enter an ending city. Pressing 0 will not help you.");
		int endCity = in.nextInt();
		if(debug){
			//System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream("trace.txt"))));
		}
		while (endCity>cityList.size()){
			System.out.println("That city number is too large. Try again.");
			endCity = in.nextInt();
		}
		while (endCity<=0){
			System.out.println("I said pressing 0 will not help you. Enter an ending city.");
			endCity = in.nextInt();
		}

		while (startCity!=0){
			if(debug){			
				System.out.println(startCity+","+endCity);
			}
			City e=dijkstra(startCity, endCity, graph, cityList);
			if(debug){
				System.out.println(e+" is holding the path!!");
			}

			if(buildDebug){
				for (City c:cityList){
					System.out.println(c);
				}
			}
			System.out.println("===============================================================");
			//break this into printpath method
			if (e==null){
				System.out.println("I couldn't find a path. Either there isn't one or something broke.");
				System.out.println("Force quiting.");
				return;
			}

			int opPath=e.getDist()+e.getDistFromEnd();
			System.out.println("Here's the optimum path length: "+opPath);

			postPrintPath(e.getVia());		// print from start to e
			System.out.println("*"+e.toString()+" this was the middle node"); 
			prePrintPath(e.getViaFromEnd());		// from e to end
						
			System.out.println("Here's the optimum path length: "+opPath);
			System.out.println(enqueuedCount + " nodes enqueued");
			enqueuedCount =2;

			//reset cityList
			for (City c:cityList){
				c.reset();
			}
			
			
			System.out.println("Enter a starting city or 0 to quit");
			startCity = in.nextInt();
			while (startCity>cityList.size()){
				System.out.println("That city number is too large. Try again.");
				startCity = in.nextInt();
			}
			if (startCity<=0){
				break;
			}
			System.out.println("You will enter an ending city. Pressing 0 will not help you.");
			endCity = in.nextInt();
			if (endCity==0){
				//print startCity neighbors
				System.out.println(graph.get(cityList.get(startCity-1).getName()));
				
				System.out.println("Lookup another city's neighbors or press 0 to continue;");
				int nextCity=in.nextInt();
				while(nextCity!=0){
					System.out.println(graph.get(cityList.get(nextCity-1).getName()));
					System.out.println("Lookup another city's neighbors or press 0 to continue;");
					nextCity=in.nextInt();
				}
				System.out.println("Enter a ending city.");
				endCity = in.nextInt();
			}
		}


	} // go
	// ===========================================================================================================================================================

	

	// ===========================================================================================================================================================
	/**
	 * Solve the optimization problem using dijkstra's algorithm in one direction from start to end
	 * @param startCityName
	 * @param endCityName
	 * @param graph
	 * @param cityList
	 * @return endCity
	 * @throws IOException
	 */
	public City dijkstra(int startCityName, int endCityName, HashMap<Integer,ArrayList<Tuple<Integer,Integer>>> graph, ArrayList<City> cityList) throws IOException{
		
		Heap h= new Heap(debug);
		if(debug){
			System.out.println("Adding startCity");
		}
		City startCity = cityList.get(startCityName-1);
		startCity.setState(0); //mark as enqueued from start
		startCity.setDist(0); //set distance to 0
		//default via is null
		h.addNode(startCity); //enheap
		if(debug){
			System.out.println("Adding endCity");
		}
		City endCity = cityList.get(endCityName-1);
		endCity.setStateFromEnd(0); //mark as enqueued from end
		endCity.setDistFromEnd(0); //set distance to 0
		//default via is null
		h.addNode(endCity); //enheap

		//state: -1,unvisited | 0,enqueued | 1 done
		while(!h.isEmpty()){
			//h.print();
			if(!h.validate()){
				System.out.println("The broken heap is");
				h.safePrint();
				System.out.println("The city list is");
				for (City c:h.getCityList()){
					System.out.println(c);
				}
				System.out.println("Leaving.");
				return null;
			}
			
			if (debug){
				System.out.println("Top of the while loop and our heap looks like ");
				h.print();
				System.out.println("About to deleteMin");
				try {System.in.read();} catch (IOException e) {e.printStackTrace();}
			}
			
			City d=h.getMin();		
			if (d.getName()==problemCity){
				h.setDebug(true);
			}
			City v = h.deleteMin();		// min in terms of sorting - min distance from any end
			String dir=v.whichWay();	// which end?
			if (debug){
				System.out.println("Just popped "+v+" off the heap. The heap looks like");
				h.print();
			}
			ArrayList<Tuple<Integer, Integer>> neighbors=graph.get(v.getName()); // get neighbor list
			if (v.getName()==problemCity){
				debug=true;
				h.setDebug(true);
			}
			if(debug){
				h.safePrint();
				System.out.println("Neighbors are");
				for (Tuple<Integer,Integer> t:neighbors){
					System.out.println(cityList.get(t.getFirst()-1));
				}				
				System.out.println("City "+v.getName()+" has "+neighbors.size()+" neighbors");
			}

			for (Tuple<Integer, Integer> n:neighbors){			//for each neighbor
				City w=cityList.get(n.getFirst()-1);
				if (debug){
					try {System.in.read();} catch (IOException e) {e.printStackTrace();}
					System.out.println("\tLooking at neighbor "+w+" in direction "+dir);
					System.out.println("\tv is "+v);
				}
				if (dir.equals("start")){						// if we popped from the start
					if (w.getState()==-1){							// and the neighbor is unvisited from the start
						w.setState(0);									// enheap from the start
						enqueuedCount++;
						w.setDist(v.getDist()+n.getSecond());			// with a distance of popped.getDist(front) + dist(popped,neighbor)
						w.setVia(v);									// front via = popped
						if (!h.contains(w)){							// if is not visited from end either
							h.addNode(w); //enheap							// add to heap
						} else {										// otherwise
							h.decreaseKey(w);								// tell heap that dist(front) has changed
						}
					} else if (w.getState() == 0){					// if we popped from the start and the neighbor is enqueued from start already
						int newDist=v.getDist()+n.getSecond();			// make new distance(front) of popped.getDist(front) + dist(popped,neighbor)
						if (newDist<w.getDist()){						// if dist(front) has decreased
							w.setDist(newDist);								//update the via and distance and tell heap that dist(front) has changed
							w.setVia(v);
							h.decreaseKey(w);
						}
					}
				} else {
					if (w.getStateFromEnd()==-1 && v.getStateFromEnd()==0){
						w.setStateFromEnd(0);
						enqueuedCount++;
						w.setDistFromEnd(v.getDistFromEnd()+n.getSecond());
						w.setViaFromEnd(v);
						if (!h.contains(w)){
							h.addNode(w); //enheap
						} else {
							h.decreaseKey(w);
						}
					} else if (w.getStateFromEnd() == 0){
						int newDistFromEnd=v.getDistFromEnd()+n.getSecond();
						if (newDistFromEnd<w.getDistFromEnd()){
							w.setDistFromEnd(newDistFromEnd);
							w.setViaFromEnd(v);
							h.decreaseKey(w);
						}
					}
				}
				
				if(debug){
					System.out.println("Done with the for loop");
					System.out.println("neighbors is "+neighbors);
					try {System.in.read();} catch (IOException e) {e.printStackTrace();}
				}
			}
			if(debug){
				System.out.println("Setting v="+v.getName()+" to done");
				try {System.in.read();} catch (IOException e) {e.printStackTrace();}
			}									// for v
			if (dir.equals("start")){				// if we popped from start
				v.setState(1);						// set state(front) to done
				if (v.getStateFromEnd()==0){		// if v still needs to be enheaped from end
					h.addNode(v);						// add it back in
				}
				if (v.getStateFromEnd()==1){		// if v is done from both ends
					return search(h,v);					// do search for optimal path between v, and the nodes in the heap that are complete from one end and enheaped from the other
				}
			} else {
				v.setStateFromEnd(1);
				if (v.getState()==0){
					h.addNode(v);
				}
				if (v.getState()==1){
					return search(h,v);
				}
			}
			
			if(debug){
				System.out.println("Bottom of the while loop and our heap looks like ");
				h.print();
				try {System.in.read();} catch (IOException e) {e.printStackTrace();}
			}
	
		}
		return null;
	}
	
	public City search(Heap h, City v){								// optimal path between v, and the nodes in the heap that are complete from one end and enheaped from the other
		ArrayList<City> cityList=h.getCityList();					// we want to remove all nodes from list that aren't complete from one end and enheaped from the other
		ArrayList<City> cList=h.getCityList();
		if (debug){
			System.out.println("Inside Search, v found is "+v+" cityList is ");
			System.out.println(cityList);
			try {System.in.read();} catch (IOException e) {e.printStackTrace();}
		}
		for (City c:cList){
			if (c.getState()==-1 || c.getStateFromEnd()==-1){
				cityList.remove(c);
			} else if (c.getState()==0 && c.getStateFromEnd()==0){
				cityList.remove(c);
			}
		}
		for (City c:cityList){										// for each node that is complete from one end and enheaped from the other
			if ((c.getDist()+c.getDistFromEnd())<(v.getDist()+v.getDistFromEnd())){
				v=c;												// if it's the least distance so far, keep it
			}
		}
		return v;
	}
	
	
	public void postPrintPath(City e){		// print path from start up to e
		if (e.getVia() == null){
			System.out.println(e);
			return;
		}
		postPrintPath(e.getVia());
		
			System.out.println(e);
		
	}


	public void prePrintPath(City e){		// print path from e to end
		if (e.getViaFromEnd() == null){
			System.out.println(e);
			return;
		}
		System.out.println(e);
		prePrintPath(e.getViaFromEnd());
	}
	

	/**
	 * Print each Node in a graph and all of its neighbors.
	 * @param g The graph to print
	 */
	public void print(HashMap<Integer,ArrayList<Tuple<Integer,Integer>>> g){
		Set<Integer> keys = g.keySet();
		System.out.println("keys:"+keys.toString());
		for(Integer i:keys){
			ArrayList<Tuple<Integer,Integer>> vertex = g.get(i);
			System.out.print(i+":\t");
			for (Tuple<Integer,Integer>neighbor:vertex){
				System.out.print("<"+neighbor.getFirst()+","+neighbor.getSecond()+">"+"\t");
			}
			System.out.println();
		}
	} // print
	// ===========================================================================================================================================================
} // class Driver
// ===============================================================================================================================================================
