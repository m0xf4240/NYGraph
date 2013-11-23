
// City
// ===========================================================================================================================================================
// TODO:write description
public class City{
	// =======================================================================================================================================================



	// =======================================================================================================================================================
	private int name;
	private int state;
	private int stateFromEnd;//only use in double Dijkstra
	private int dist;
	private int distFromEnd;
	private City via;
	private City viaFromEnd;

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
		this.stateFromEnd=-1;
		this.dist=dist;
		this.via=null;
		this.viaFromEnd=null;
	}

	public City(int name, int dist, City via, String dir){
		this.name=name;
		if (dir.equals("start")){
			this.state=-1;
			this.dist=dist;
			this.via=via;
		} else {
			this.stateFromEnd=-1;
			this.distFromEnd=dist;
			this.viaFromEnd=via;
		}
	} // City
	// =======================================================================================================================================================
	
	public void reset(){
		this.state=-1;
		this.stateFromEnd=-1;
		this.dist=Integer.MAX_VALUE;
		this.distFromEnd=Integer.MAX_VALUE;
		this.via=null;
		this.viaFromEnd=null;
	}
	
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
	
	public int getStateFromEnd() {
		return stateFromEnd;
	}

	public void setStateFromEnd(int stateFromEnd) {
		this.stateFromEnd = stateFromEnd;
	}

	public int getDist() {
		return dist;
	}

	public void setDist(int dist) {
		this.dist = dist;
	}

	public int getDistFromEnd() {
		return distFromEnd;
	}

	public void setDistFromEnd(int distFromEnd) {
		this.distFromEnd = distFromEnd;
	}

	public City getVia() {
		return via;
	}

	public void setVia(City via) {
		this.via = via;
	}
		
	public City getViaFromEnd() {
		return viaFromEnd;
	}

	public void setViaFromEnd(City viaFromEnd) {
		this.viaFromEnd = viaFromEnd;
	}
	
	public String whichWay(){
		if (state==1){
			return "end";
		} else if (stateFromEnd==1){
			return "start";
		}
		
		if (this.dist<this.distFromEnd){
			return "start";
		} else {
			return "end";
		}
	}
	// =======================================================================================================================================================
	
	
	
	// =======================================================================================================================================================
	/**
	 * toString
	 */
	public String toString(){
		String viaName=(this.via==null)?"NA":""+this.via.getName();
		String viaNameFromEnd=(this.viaFromEnd==null)?"NA":""+this.viaFromEnd.getName();
		return "["+this.name+": state="+this.state+", dist="+this.dist+", via="+viaName+": estate="+this.stateFromEnd+", edist="+this.distFromEnd+", evia="+viaNameFromEnd+"]";
	}
} // class Node