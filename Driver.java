// ===============================================================================================================================================================
// Driver
// A solver for shortest path using Dijkstra's Algorithm
// Terri Hultum and Michael Millian
// Fall 2013
// ===============================================================================================================================================================

import java.io.*;
import java.util.*;

import Heap.Node;
// ================================================================================================================================================
public class Driver{
// ===============================================================================================================================================================
	
	
	
	// ===========================================================================================================================================================
	// main method switches to a non-static method
	public static void main(String args[]) throws IOException{
		new Driver().go();
	} //main
	// ===========================================================================================================================================================
	
	
	// ===========================================================================================================================================================
	/**
	 * Read the input file, generate a Heap, and solve.
	 * @throws IOException
	 */
	public void go() throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File("test2.gr"))));
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
				System.out.println("Hello. Running on a file of size about "+(((new File("test2.gr")).length()-130)/13));
				int size=Integer.valueOf(elements[2]);
				graph = new HashMap<Integer,ArrayList<Tuple<Integer,Integer>>>(size*2);//make sure its big enough to avoid too many conflicts
			}
		}
		br.close();
		//print(graph);
		
		Set<Integer>keySet=graph.keySet();
		System.out.println("Size "+keySet.size());
		ArrayList<City> cityList= new ArrayList<City>(keySet.size());
		for (Integer i:graph.keySet()){
			cityList.add(new City(i,-1));
		}
		
		Collections.sort(cityList, new Comparator<City>() {
		    public int compare(City a, City b) {
		        return Integer.signum(a.getName() - b.getName());
		    }
		});
		
		for (City c:cityList){
			System.out.println(c);
		}
		
		Integer startCity = 60; 
		Integer endCity = 71;
		
		dijkstra(startCity, endCity, graph, cityList);
	} // go
	// ===========================================================================================================================================================
	
	
	
	// ===========================================================================================================================================================
	/**
	 * Solve the optimization problem using dijkstra's algorithm
	 */
	public void dijkstra(int startCityName, int endCityName, HashMap<Integer,ArrayList<Tuple<Integer,Integer>>> graph, ArrayList<City> cityList){
		
		Heap h= new Heap();
		City startCity = cityList.get(startCityName-1);
		startCity.setState(0); //mark as enqueued
		startCity.setDist(0); //set distance to 0
		//default via is null
		Node n=h.addNode(startCity); //enheap
		
		while(!h.isEmpty()){
			City v = h.deleteMin();
			ArrayList<Tuple<Integer, Integer>> neighbors=graph.get(v.getName());
			for (Tuple<Integer, Integer> n:neighbors){
				City w=cityList.get(n.getFirst()-1);
				if (w.getState() == -1){
					w.setState(0);
					w.setDist(v.getDist()+n.getSecond());
					w.setVia(v);
					h.addNode(w);
				} else if (w.getState() == 0){
					int newDist=v.getDist()+n.getSecond();
					if (newDist<w.getDist()){
						w.setDist(newDist);
						w.setVia(v);
/*TODO:fix this*/			h.decreaseKey(w);
					}
				}
			}
		}
		
		//state: -1,unvisited | 0,enqueued | 1 done
		H = new Heap();
		for all (v){
			v.state=-1;
		}
		v0.state=0;
		v0.dist=0;
		v0.via=null;
		H.enqueue(v0);
		
		while(!H.isEmpty()){
			v=H.deleteMin();
			for (Node w: v.getNeighbors()){
				if (w.getState() == -1){
					w.setState(0);
					w.setDist(v.getDist()+dist(v,w));
					w.setVia(v);
					H.enqueue(w);
				} else if (w.getState() == 0){
					int newDist = v.getDist()+dist(v,w);
					if (newDist < w.getDist()){
						w.setDist(newDist);
						w.setVia(v);
						H.decreaseKey(w);
					}
				}
			}
			v.setState(1);
		}
	} //dijkstra
	// ===========================================================================================================================================================
	
	
	
	// ===========================================================================================================================================================
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