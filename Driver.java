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
			cityList.add(new City(i,-1));
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
			System.out.println(startCity+","+endCity);
			City e=dijkstra(startCity, endCity, graph, cityList);

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
			printPath(e);
			
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
	 * Solve the optimization problem using dijkstra's algorithm
	 * @throws IOException 
	 */
	public City dijkstra(int startCityName, int endCityName, HashMap<Integer,ArrayList<Tuple<Integer,Integer>>> graph, ArrayList<City> cityList) throws IOException{

		File output = new File("log.txt");
		if (!output.exists()) {
			output.createNewFile();
		}
		FileWriter fw = new FileWriter(output.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		
		
		Heap h= new Heap(debug);
		City startCity = cityList.get(startCityName-1);
		startCity.setState(0); //mark as enqueued
		startCity.setDist(0); //set distance to 0
		//default via is null
		h.addNode(startCity); //enheap

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
				bw.close();
				return null;
			}
			
			if (debug){
				System.out.println("Top of the while loop and our heap looks like ");
				h.print();
				System.out.println("About to deleteMin");
				try {System.in.read();} catch (IOException e) {e.printStackTrace();}
			}
			
			
			
			if (bwDebugVerbose){
				bw.write("---------------------------------------------------------------\n");
				ArrayList<City> heapList=h.getCityList();
				Collections.sort(cityList, new Comparator<City>() {
					public int compare(City a, City b) {
						return Integer.signum(a.getDist() - b.getDist());
					}
				});
				bw.write("The heap is"+heapList+"\n");
			}
			City d=h.getMin();
			if (d.getName()==problemCity){
				h.setDebug(true);
			}
			City v = h.deleteMin();
	
			if (bwDebugSparse){
				bw.newLine();
				bw.write("---------------------------------------------------------------\n");
				bw.write("Looking at "+v.toString()+"\n");
			}
			if (debug){
				System.out.println("Just popped "+v+" off the heap. The heap looks like");
				h.print();
			}
			ArrayList<Tuple<Integer, Integer>> neighbors=graph.get(v.getName());
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
				
			}
			if (bwDebugSparse){
				bw.write(neighbors.toString()+"\n");
			}
			if (debug){
				System.out.println("City "+v.getName()+" has "+neighbors.size()+" neighbors");
			}
			for (Tuple<Integer, Integer> n:neighbors){
				City w=cityList.get(n.getFirst()-1);
				if (debug){
					try {System.in.read();} catch (IOException e) {e.printStackTrace();}
					System.out.println("\tLooking at neighbor "+w);
				}
				if (w.getState() == -1){
					if(debug){
						System.out.println("\tAbout to add "+w+" to heap");
					}
					w.setState(0);
					w.setDist(v.getDist()+n.getSecond());
					w.setVia(v);
					if (bwDebugSparse){
						bw.write("\tAdding "+w.toString()+" ");
					}
					if (bwDebugVerbose){
						bw.write("\t---------------------------------------------------------------"+"\n");
						bw.write("\tAdding "+w.toString()+"\n");
					}
					if(debug){
						System.out.println("\tInitialized "+w);
						try {System.in.read();} catch (IOException e) {e.printStackTrace();}
					}
					h.addNode(w); //enheap
					if(debug){
						System.out.println("\tAdded "+w+" to heap. The heap looks like");
						h.print();
					}

				} else if (w.getState() == 0){
					int newDist=v.getDist()+n.getSecond();
					if (newDist<w.getDist()){
						w.setDist(newDist);
						w.setVia(v);
						h.decreaseKey(w);
					}
					if (bwDebugSparse){
						bw.write("\tUpdating "+w.toString()+" ");
					}
					if (bwDebugVerbose){
						bw.write("\t---------------------------------------------------------------"+"\n");
						bw.write("\tUpdating "+w.toString()+"\n");
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
			}
			v.setState(1);
			if (bwDebugSparse){
				bw.write("---------------------------------------------------------------"+"\n");
				bw.write("Done with "+v.toString()+"\n");
			}
			if (v.getName()==endCityName){
				bw.close();
				return v;
			}
			if(debug){
				System.out.println("Bottom of the while loop and our heap looks like ");
				h.print();
				try {System.in.read();} catch (IOException e) {e.printStackTrace();}
			}
			bw.flush();
		}
		
		bw.close();
		return null;
	} //dijkstra
	// ===========================================================================================================================================================



	// ===========================================================================================================================================================

	public void printPath(City e){
		if (e.getVia() == null){
			System.out.println(e);
			return;
		}
		printPath(e.getVia());
		System.out.println(e);
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
