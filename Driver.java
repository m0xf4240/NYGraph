import java.io.*;
import java.util.*;

public class Driver{
	public static void main(String args[]) throws IOException{
		new Driver().go();
	}

	public void go() throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File("test.gr"))));
		String line=null;
		//graph should be a hashmap
		ArrayList<ArrayList<Tuple<Integer,Integer>>> graph= new ArrayList<ArrayList<Tuple<Integer,Integer>>>();
		while ((line=br.readLine())!=null){
			String[] elements=line.split(" ");
			if (elements[0].equals("a")){
				int firstVertex=Integer.valueOf(elements[1]);
				System.out.println(graph.size());
				System.out.println(firstVertex);
				int secondVertex=Integer.valueOf(elements[2]);
				int dist=Integer.valueOf(elements[3]);
				if (firstVertex>graph.size()){
					
				}
					graph.get(firstVertex).add(new Tuple<Integer,Integer>(secondVertex,dist));
					
			}
			else if (elements[0].equals("c")){
				continue;
			}
			else if (elements[0].equals("p")){
				System.out.println("Hello");
				int size=Integer.valueOf(elements[2]);
				System.out.println("size:"+size);
				graph=new ArrayList<ArrayList<Tuple<Integer,Integer>>>(size);
				System.out.println(graph.size());
				graph.ensureCapacity(size);
			}
		}
		print(graph);
	}
	
	public void print(ArrayList<ArrayList<Tuple<Integer,Integer>>> g){
		for (ArrayList<Tuple<Integer,Integer>> vertex:g){
			for (Tuple<Integer,Integer>neighbor:vertex){
				System.out.print(neighbor.getFirst()+"\t");
			}
			System.out.println();
		}
	}
}