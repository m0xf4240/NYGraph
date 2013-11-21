// ========================================================
// Tuple 
// Tuple data structure for paired data
// Terri Hultum and Michael Millian
// Fall 2013
// ========================================================


public class Tuple<F,S> { 
// ========================================================
	
	// ========================================================
	public F first; 
	public S second; 
	// ===========================================================================================================================================================
	// Constructor
	/**
	 * The constructor takes two objects of any type and stores them as a Tuple
	 * @param first
	 * @param second
	 */
	public Tuple(F first, S second) { 
		this.first=first; 
		this.second=second; 
	} //Constructor
	// ===========================================================================================================================================================
	
	
	
	// ===========================================================================================================================================================
	/**
	 * Returns the element in the first position.
	 * @return
	 */
	public F getFirst() {
		return first;
	} // getFirst
	// ===========================================================================================================================================================
	
	
	
	// ===========================================================================================================================================================
	/**
	 * Replaces the element in the first position with the specified element.
	 * @param first
	 */
	public void setFirst(F first) {
		this.first = first;
	} // setFirst
	// ===========================================================================================================================================================
	
	
	
	// ===========================================================================================================================================================
	/**
	 * Returns the element in the second position.
	 * @return
	 */
	public S getSecond() {
		return second;
	} // getSecond
	// ===========================================================================================================================================================
	
	
	
	// ===========================================================================================================================================================
	/**
	 * Replaces the element in the second position with the specified element.
	 * @param second
	 */
	public void setSecond(S second) {
		this.second = second;
	} // setSecond
	// ===========================================================================================================================================================
	
	
	
	// ===========================================================================================================================================================
	/**
	 * Returns a string representation of this Tuple. The string representation consists of the Tuple's elements enclosed in square brackets ("[]") and separated
	 * by the characters ", " (comma and space). Elements are converted to strings as by String.valueOf(Object).
	 * @override toString in class Object
	 * @return a string representation of this Tuple
	 */
	public String toString(){
		return "["+this.first.toString()+", "+this.second.toString()+"]";
	} // toString
// ========================================================
} //tuple
// ========================================================