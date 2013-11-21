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
		
		
		
		// =======================================================================================================================================================
	} // class Node
	// ===========================================================================================================================================================
	
	
	
	// ===========================================================================================================================================================
	private Node header;
	private Node last;
	// ===========================================================================================================================================================

	
	
	// ===========================================================================================
	// Constructor
	/**
	 * Initialize the Heap to have only a header Node.
	 */
	public Heap(){
		header=new Node(new City(-1,-1));
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
		Node l=this.last;
		this.last=findPrev();	
		l.parent=this.header;
		this.header.left=l;
		(l.left=toReturn.left).parent=l;
		(l.right=toReturn.right).parent=l;
		this.header.left=l;
		siftDown(this.header.left);
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
			if (n.getName()<n.left.getName() && n.getName()<n.right.getName()){
				return;
			}
			else if (n.left.getName()<n.right.getName() || n.right==header){
				swap(n,n.left);

			}else{
				swap(n,n.right);
			}
			siftDown(n);
		}
	} // siftDown
	// ===========================================================================================================================================================



	// ===========================================================================================================================================================
	public void decreaseKey(Node n){
		siftUp(n);
	}
	
	/**
	 * Swaps a Node with its parent until the Heap is valid.
	 * @param n The Node to swap.
	 */
	public void siftUp(Node n){
		if ((n.parent==null) || n.getName()>n.parent.getName()){
			return;
		} else {
			swap(n,n.parent);
			siftUp(n);
		}
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
		if (np.left==n){
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
		}
	} //swap
	// ===========================================================================================================================================================
	
	
	// ===

	public boolean isEmpty(){
		return header.left==header;
	}
} // class Heap
// ===============================================================================================================================================================