import java.io.*;
import java.util.*;

public class Driver{
	public static void main(String args[]) throws IOException{
		new Driver().go();
	}

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
				System.out.println("Hello");
				int size=Integer.valueOf(elements[2]);
				graph = new HashMap<Integer,ArrayList<Tuple<Integer,Integer>>>(size*2);//make sure its big enough to avoid too many conflicts
			}
		}
		br.close();
		print(graph);
	}
	
	
	
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
	}
}