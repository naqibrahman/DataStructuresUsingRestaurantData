package proj1;

import java.util.LinkedList;
import java.util.List;

/**
 * This class organizes the record class with a linked list 
 * based off its DBA
 * 
 * @author Joanna Kluckowaska 
 *
 */


public class DbaList implements Comparable<DbaList>{
	String dba;
	List<Record> list;
	/**
	 * Constructs a DbaList based off an initial Record 
	 * @author Joanna Kluckowaska
	 */
	
	public DbaList( Record r ) throws IllegalArgumentException {
		if ( r == null ) throw new IllegalArgumentException ("Error: cannot create "
				+ "DbaList with a null Record object.");
		dba = r.getDba();
		list = new LinkedList<Record>();
		list.add(0, r);
	}
	/**
	 * Constructs a DbaList based off an initial dba 
	 * @author Joanna Kluckowaska 
	 */
	public DbaList(String dba) {
		this.dba = dba;
		list = new LinkedList<Record>();
	}
	/**
	 * Returns Dba
	 * @author Joanna Kluckowaska 
	 */
	public String getDba () {
		return dba;
	}
	/**Returns linked list of items
	 * @author Joanna Kluckowaska 
	 */
	public List<Record> getList () {
		return list;
	}
	/**Adds a record to the list
	 * @author Joanna Kluckowaska 
	 *
	 */
	public boolean add ( Record r ) {
		if (r.getDba().equalsIgnoreCase( dba )) {
			list.add(0,r);
			return true;
		}
		else
			return false;
	}
	
	public int compareTo( DbaList other ) {
		return this.dba.compareTo(other.dba);

	}
}