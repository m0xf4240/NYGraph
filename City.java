
// City
// ===========================================================================================================================================================
// TODO:write
public class City{
	// =======================================================================================================================================================



	// =======================================================================================================================================================
	private int name;
	private int state;
	private int dist;
	private City via;

	// =======================================================================================================================================================



	// =======================================================================================================================================================
	// Constructor
	/**
	 * The constructor assigns only the name of the Node. Use init(l,r,p) to hook the Node into the Heap
	 * @param key
	 */
	public City(int name, int dist){
		this.name=name;
		this.state=-1;
		this.dist=dist;
		this.via=null;
	}

	public City(int name, int dist, City via){
		this.name=name;
		this.state=-1;
		this.dist=dist;
		this.via=via;
	} // City
	// =======================================================================================================================================================
	
	
	
	// =======================================================================================================================================================
	public int getName() {
		return name;
	}

	public void setName(int name) {
		this.name = name;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getDist() {
		return dist;
	}

	public void setDist(int dist) {
		this.dist = dist;
	}

	public City getVia() {
		return via;
	}

	public void setVia(City via) {
		this.via = via;
	}
	// =======================================================================================================================================================
	
	
	
	// =======================================================================================================================================================
	/**
	 * toString
	 */
	public String toString(){
		String viaName=(this.via==null)?"NA":""+this.via.getName();
		return "["+this.name+": state="+this.state+", dist="+this.dist+", via="+viaName+"]";
	}
} // class Node