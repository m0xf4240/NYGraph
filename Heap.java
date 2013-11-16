
public class Heap{
	
	
	private class Node{
		private int value;
		private Node left;
		private Node right;
		private Node parent;
		
		private Node(int key){this.value=key;}
		private boolean hasParent(){return parent!=null;}
		private void init(Node l, Node r, Node p){this.left=l;this.right=r;this.parent=p;}
	}
	
	private Node header;
	private Node min;
	private Node last;
	
	public Heap(){
		header=new Node(-1);
		header.init(header, header, header);
		this.last=header;
		this.min=header;
	}
	
	public Node addNode(int i){
		Node n=new Node(i);
		n.init(header,header,header);
		n.parent=findNext();
		if (n.parent.left==header){
			n.parent.left=n;
		}else{
			n.parent.right=n;
		}
		this.last=n;
		return last;
	}
	
	public Node removeMin(){
		Node toReturn=this.header.left;
		Node l=this.last;
		this.last=findPrev();	
		l.parent=this.header;
		this.header.left=l;
		(l.left=toReturn.left).parent=l;
		(l.right=toReturn.right).parent=l;
		this.header.left=l;
		siftDown(this.header.left);
		return toReturn;
	}
	
	
	//Always finds the place for the next thing after last
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
	}
	
	//Always finds the place of the last thing that was last
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
		
	}
	
	public void siftDown(Node n){
		if (n.left==header){
			return;
		} else {
			if (n.left.value<n.right.value || n.right==header){
				swap(n,n.left);
				
			}else{
				swap(n,n.right);
			}
			siftDown(n);
		}
	}
	
	
	public void siftUp(Node n){
		if (!n.hasParent() || n.value>n.parent.value){
			return;
		} else {
			swap(n,n.parent);
			siftUp(n);
		}
	}
	
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
	}
}