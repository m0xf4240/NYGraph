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
	// ===========================================================================================================================================================

	
	
	// ===========================================================================================
	// Constructor
	/**
	 * Initialize the Heap to have only a header Node.
	 */
	public Heap(){
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
		Node n=new Node(city);
		n.init(header,header,header);
		n.parent=findNext();
		if (n.parent.left==header){
			n.parent.left=n;
		}else{
			n.parent.right=n;
		}
		this.last=n;
		nodeList.add(last);
		siftUp(last);
		return last;
	} // addNode
	// ===========================================================================================================================================================



	// ===========================================================================================================================================================
	/**
	 * Removes the Node at the top of the Heap, restructures the remaining Nodes into a valid Heap, 
	 * and returns that Node as the value of this function.
	 * @return The mimimum Node in the Heap
	 */
	public City deleteMin(){
		Node toReturn=this.header.left;
		// remove the min Node from Heap's list of all nodes
		nodeList.remove(toReturn);
		// restructure the Heap
		Node l=this.last;
		Node secondToLast=findPrev();
		System.out.println(nodeList);
		if (nodeList.size()==1){
			System.out.println("Catching size==2");
			secondToLast=last;
		}
		System.out.println("Min is "+header.left);
		System.out.println("Last is "+last);
		System.out.println("new last will be "+secondToLast);
		if (toReturn==l){
			header.init(header, header, header);
		} else {
			header.init(l, header, header);
			Node newRight=(toReturn.right==l)?header:toReturn.right;
			Node newLeft=(toReturn.left==l)?header:toReturn.left;
			l.init(newLeft, newRight, header);
			if (toReturn.left!=l && toReturn.left!=header){		
				toReturn.left.init(toReturn.left.left, toReturn.left.right, l);
			}
			if (toReturn.right!=l && toReturn.right!=header){
				toReturn.right.init(toReturn.right.left, toReturn.right.right, l);
			}
			
			System.out.println("About to siftDown");
			try {System.in.read();} catch (IOException e) {e.printStackTrace();}
			siftDown(this.header.left);
		}
		this.last=secondToLast;
		return toReturn.city;
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
		if (last.parent==header){
			return header;
		} else if (last.parent.right==last){
			return last.parent.left;
		} else{
			Node p=last;
			while(p.parent.left==p){
				p=p.parent;
			}
			if (p.parent!=header){
				p=p.parent.left;
			}
			while(p.right!=header){
				p=p.right;
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
		if ((n.parent==header) || n.getDist()>n.parent.getDist()){
			return;
		} else {
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
		//TODO: use init()
		if (n==last){
			last=n.parent;
		}
		
		Node nleft=n.left;
		Node nright=n.right;
		Node nparent=n.parent;
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
		int i=7;
		
		/*if (np.left==n){
			(np.left=n.left).parent=np;

			n.parent=np.parent;
			if (n.parent.left==np){
				n.parent.left=n;
			} else{
				n.parent.right=n;
			}
			np.parent=n;
			n.left=np;

			Node s=n.right;		
			(n.right=np.right).parent=n;
			(np.right=s).parent=np;
		} else{
			(np.right=n.right).parent=np;

			n.parent=np.parent;
			if (n.parent.left==np){
				n.parent.left=n;
			} else{
				n.parent.right=n;
			}
			np.parent=n;
			n.right=np;

			Node s=n.left;		
			(n.left=np.left).parent=n;
			(np.left=s).parent=np;
		}*/
	} //swap
	// ===========================================================================================================================================================
	
	
	// ===

	public boolean isEmpty(){
		return header.left==header;
	}
	
	public void print(){
		if (this.isEmpty()){
			System.out.println("Empty");
		} else{
			print(header.left);
		}
	}
	
	public void print(Node n){
		if (n==header){
			return;
		}
		System.out.println(n);
		print(n.left);
		print(n.right);
	}
} // class Heap
// ===============================================================================================================================================================