import java.io.IOException;
import java.util.ArrayList;

//Heap
public class Heap{
	// ===============================================================================================================================================================
	// This is a minheap for dijkstra's algorithm to solve optimum path between two specified nodes
	// ===========================================================================================================================================================
	// Node
	// ===========================================================================================================================================================
	// Private inner class Node stores the information about each city and pointers which effect the heap
	public class Node{
		// =======================================================================================================================================================



		// =======================================================================================================================================================
		private City city;
		private Node left;
		private Node right;
		private Node parent;
		// =======================================================================================================================================================



		// =======================================================================================================================================================
		// Constructor
		/**
		 * The constructor assigns only the name of the Node. Use init(l,r,p) to hook the Node into the Heap
		 * @param key
		 */
		private Node(City city){
			this.city=city;
		} // Node
		// =======================================================================================================================================================



		// =======================================================================================================================================================
		/**
		 * init takes an existing Node and hooks it into the tree
		 * 
		 * @param l The left child of this Node
		 * @param r The right child of this Node
		 * @param p The parent of this Node
		 */
		private void init(Node l, Node r, Node p){
			this.left=l;
			this.right=r;
			this.parent=p;
		} // init

		private int getName(){
			return this.city.getName();
		}

		private int getDist(){
			return this.city.getDist();
		}

		public String toString(){
			return "["+getName()+", "+getDist()+"| " +
					"parent="+this.parent.getName()+
					", left="+this.left.getName()+
					", right="+this.right.getName()+"]";
		}

		// =======================================================================================================================================================
	} // class Node
	// ===========================================================================================================================================================



	// ===========================================================================================================================================================
	private Node header;
	private Node last;
	private ArrayList<Node> nodeList=new ArrayList<Node>();
	private boolean debug;
	// ===========================================================================================================================================================



	// ===========================================================================================
	// Constructor
	/**
	 * Initialize the Heap to have only a header Node.
	 */
	public Heap(boolean debug){
		this.debug=debug;
		header=new Node(new City(-1,Integer.MAX_VALUE));
		header.init(header, header, header);
		this.last=header;
	} //Heap
	// ===========================================================================================================================================================



	//============================================================================================================================================================
	/**
	 * Add a new Node with name i to the Heap 
	 * @param i The name to add
	 * @param
	 * @param
	 * @return A pointer to the Node just added, which is the last Node in the Heap
	 */
	public Node addNode(City city){
		if (debug){
			System.out.println("\t\tAdding "+city);
		}
		Node n=new Node(city);
		n.init(header,header,findNext());
		if (debug){
			System.out.println("\t\t"+city+" is now a child of "+n.parent);
		}
		if (n.parent.left==header){
			n.parent.init(n, n.parent.right, n.parent.parent);
		}else{
			n.parent.init(n.parent.left, n, n.parent.parent);
		}
		this.last=n;
		nodeList.add(last);
		if (debug){
			System.out.println("\t\tThe (broken) heap looks like");
			this.print();
		}
		siftUp(n);
		if (debug){
			System.out.println("\t\tAfter siftin my heap is");
			this.print();
			try {System.in.read();} catch (IOException e) {e.printStackTrace();}
		}
		return n;
	} // addNode
	// ===========================================================================================================================================================



	// ===========================================================================================================================================================
	/**
	 * Removes the Node at the top of the Heap, restructures the remaining Nodes into a valid Heap, 
	 * and returns that Node as the value of this function.
	 * @return The mimimum Node in the Heap
	 */
	public City deleteMin(){
		if (debug){
			System.out.println("\t\t\t\tStarting Delete Min");
		}
		Node min=this.header.left;
		if (debug){
			System.out.println("About to pop "+min+" off the heap");
		}
		Node l=this.last;
		Node secondToLast=findPrev();

		if (debug){
			System.out.println("Last is "+l);
			System.out.println("secondToLast is "+secondToLast);
		}

		// remove the min Node from Heap's list of all nodes
		nodeList.remove(min);
		if (debug){
			System.out.println("The list nodes in our heap is "+nodeList);
			try {System.in.read();} catch (IOException e) {e.printStackTrace();}
		}

		// restructure the Heap
		// first use findPrev to find the secondToLast Node in the heap.
		// Then move last to the root of the heap. The heap might be invalid, so
		// begin siftDown to fix it. After siftDown, secondToLast is probably in 
		// the last position, unless it swapped with the "last/min" node as it
		// was sifting down, in which case the old last is still the new last

		if (min==l){
			header.init(header, header, header);
		} else {
			if (debug){
				System.out.println("About to restructure heap");
			}
			
			header.init(l, header, header);
			//newRight and newLeft are the second row
			Node newRight=(min.right==l)?header:min.right;
			Node newLeft=(min.left==l)?header:min.left;			
			
			Node newLeftsLeft=(min.left.left==l)?header:min.left.left;
			Node newLeftsRight=(min.left.right==l)?header:min.left.right;
			Node newRightsLeft=(min.right.left==l)?header:min.right.left;
			Node newRightsRight=(min.right.right==l)?header:min.right.right;
			
			if (l.parent.left==l){
				l.parent.init(header, header, l.parent.parent);
			} else {
				l.parent.init(l.parent.left, header, l.parent.parent);
			}
			
			l.init(newLeft, newRight, header);
			if (min.left!=l && min.left!=header){		
				min.left.init(newLeftsLeft,newLeftsRight,l);
			}
			if (min.right!=l && min.right!=header){
				min.right.init(newRightsLeft,newRightsRight,l);
			}

			if (debug){
				System.out.println("Everything we just did is in");
				System.out.println(header);
			}
			if (debug){
				for (Node n:nodeList){
					if (n==last){System.out.print("*");}
					System.out.println(n);
				}
			}
			if (debug){
				System.out.println("About to siftDown");
			}

			if (debug){
				try {System.in.read();} catch (IOException e) {e.printStackTrace();}
			}
			siftDown(this.header.left);
			if (debug){
				System.out.println("      last is "+last);
			}
		}
		// 
		if (secondToLast.left==header){ //if secondToLast has no children
			this.last=secondToLast;
		}
		if (debug){
			System.out.println("      last is "+last);
			System.out.println("The heap is");
			this.print();
		}
		return min.city;
	} // removeMin
	// ===========================================================================================================================================================



	//============================================================================================================================================================
	/**
	 * Finds the location for where the next Node should be added.
	 * @return The parent Node for the location where the next Node should be added.
	 */
	public Node findNext(){
		if (last.parent==header){
			return last;
		} else if (last.parent.left==last){
			return last.parent;
		} else{
			Node p=last;
			while(p.parent.right==p){
				p=p.parent;
			}
			if (p.parent!=header){
				p=p.parent.right;
			}
			while(p.left!=header){
				p=p.left;
			}
			return p;
		}
	} // findNext
	// ===========================================================================================================================================================



	// ===========================================================================================================================================================
	/**
	 * Finds the second to last Node in the Heap.
	 * @return The second to last Node in the Heap.
	 */
	public Node findPrev(){
		if (debug){
			System.out.println("\t\t\t\t\tFinding thing to call last");
		}
		if (last.parent==header){
			return header;
		} else if (last.parent.right==last){
			return last.parent.left;
		} else{
			Node p=last;
			while(p.parent.left==p){
				if (debug){
					System.out.println("\t\t\t\t\twhile| p is "+p);
				}
				if (p.parent==header){
					break;
				}
				p=p.parent;
			}
			if (debug){
				System.out.println("\t\t\t\t\tfinally| p is "+p);
			}
			if (p.parent!=header){
				p=p.parent.left;
			}
			while(p.right!=header){
				p=p.right;
			}
			if (debug){
				System.out.println("\t\t\t\t\tmoving right");
				System.out.println("\t\t\t\t\tfinally| p is "+p);
			}

			return p;
		}
	} // findPrev
	// ===========================================================================================================================================================



	// ===========================================================================================================================================================
	/**
	 * Swaps a Node with the least of its children until the Heap is valid.
	 * @param n The Node to swap.
	 */
	public void siftDown(Node n){
		if (debug){
			System.out.println("The (broken) heap looks like ");
			this.print();
		}
		if (n.left==header){
			return;
		} else {
			if (n.getDist()<n.left.getDist() && n.getDist()<n.right.getDist()){
				return;
			}
			else if (n.left.getDist()<n.right.getDist() || n.right==header){
				swap(n.left,n);

			}else{
				swap(n.right,n);
			}
			siftDown(n);
			if (debug){
				System.out.println("      last is "+last);
			}
		}
	} // siftDown
	// ===========================================================================================================================================================



	// ===========================================================================================================================================================
	public void decreaseKey(City c){
		//TODO: Make this code nice

		Node t=header.left;
		for (Node n: nodeList){
			if (c.getName()==n.getName()){
				t=n;
				break;
			}
		}
		siftUp(t);
	}

	/**
	 * Swaps a Node with its parent until the Heap is valid.
	 * @param n The Node to swap.
	 */
	public void siftUp(Node n){
		if (debug){
			System.out.println("\t\t\tSifting up on "+n+"^^"+n.parent);
			try {System.in.read();} catch (IOException e) {e.printStackTrace();}
		}
		if ((n.parent==header) || n.getDist()>n.parent.getDist()){
			return;
		} else {
			if (debug){
				System.out.println("\t\t\tAbout to swap");
			}
			swap(n,n.parent);			
			siftUp(n);
		}
		header.init(header.left, header, header);
	} // siftUp
	// ===========================================================================================================================================================



	// ===========================================================================================================================================================
	/**
	 * Swaps a Node with its parent.
	 * @param n Child Node
	 * @param np Parent Node
	 * TODO: throw an Exception if n.parent!=np. This can't happen because of how siftUp and siftDown
	 * are written, but it would be rigorous.
	 */
	public void swap(Node n, Node np){
		if (debug){
			System.out.println("   about to swap "+n.getName()+" and "+np.getName());
		}
		//n will never be last when sifting down
		if (n==last){
			last=n.parent;
		}

		Node nleft=n.left;
		Node nright=n.right;
		Node npleft=np.left;
		Node npright=np.right;
		Node npparent=np.parent;

		if (np.left==n){
			n.init(np, npright, npparent);
			np.init(nleft, nright, n);
			if (n.right!=header){
				n.right.init(n.right.left, n.right.right, n);				
			}
		} else {
			n.init(npleft, np, npparent);
			np.init(nleft, nright, n);
			if (n.left!=header){				
				n.left.init(n.left.left, n.left.right, n);
			}
		}
		if (npparent.left==np){
			npparent.init(n, npparent.right, npparent.parent);
		} else{
			npparent.init(npparent.left, n, npparent.parent);
		}
		if (np.left!=header){
			np.left.init(np.left.left, np.left.right, np);
		}
		if (np.right!=header){
			np.right.init(np.right.left, np.right.right, np);
		}
	} //swap
	// ===========================================================================================================================================================


	// ===
	
	public ArrayList<City> getCityList(){
		ArrayList<City> cityList = new ArrayList<City>();
		for (Node n: nodeList){
			cityList.add(n.city);
		}
		return cityList;
	}

	public boolean isEmpty(){
		return header.left==header;
	}

	public void print(){
		System.out.println(header);
		if (this.isEmpty()){
			return;
		} else{
			print(header.left);
		}
		System.out.println("*"+last+" is marked as last");
	}

	public void print(Node n){
		if (n==header){
			return;
		}
		System.out.println(n);
		print(n.left);
		print(n.right);
	}
	
	public boolean validate(){
		//This will have awful running time
		
		
		
		return false;
	}
} // class Heap
// ===============================================================================================================================================================