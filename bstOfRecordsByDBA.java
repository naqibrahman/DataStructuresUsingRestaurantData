package proj1;
/**
 * This class creates a bst to 
 * organize the record class.
 * Each record is stored into a list within a node matching
 * its dba.
 *
 *@author Naqib Rahman
 */


public class bstOfRecordsByDBA {
	Node root;;
	
	/**Default constructor initializes root 
	 * @author Naqib Rahman
	 */
	public bstOfRecordsByDBA(){
		root = null; 
		}
	
	/** adds a record to the tree
	 *  @param r is a record
	 * @author Naqib Rahman
	 */
	public void add ( Record r ) {
		//is there already a node with the dba?
		if (contains(r.getDba())){
		
			this.get(r.getDba()).list.add(r);
		}
		else{
		DbaList list = new DbaList(r);
		
		root =recAdd(root, list);
		}
	}
	
	/**recursive method to add records
	 * @return Node where the record is added
	 * @author Naqib Rahman
	 */
	
	private Node recAdd(Node node, DbaList list){
		
		if (node == null){

			return new Node(list);
		}
		
		
		if ( node.list.compareTo(list) < 0 ){

			node.left = recAdd ( node.left, list );
			}
		else{

			node.right = recAdd (node.right, list );
					}
		
		
		
		return node;
	}
	
	/** Method to remove a node
	 * @return DbaList of node removed
	 * @param String dba sepciefies which node to remove
	 * @author Naqib Rahman
	 */
	public DbaList remove ( String dba ) {
		DbaList deleted = this.get(dba);
	    root = recRemove(root, deleted);
	    return deleted;

	}
	/**Recursive method to remove a node
	 * 
	 * @author Naqib Rahman
	 */
	
	private Node recRemove(Node node, DbaList deletedList)
	   {
	      //searches for node to remove
	      if (node.list.compareTo(deletedList) < 0)
	      node.left = recRemove (node.left, deletedList);
	      else if (node.list.compareTo(deletedList) > 0)
	      node.right = recRemove (node.right, deletedList);
	      else
	      {//if node is found
	    	  //one child check
	         if (node.left == null) {

	        	 
	        	 return node.right;
	        	 
	         }
	         else if (node.right == null)
	        	 
	         {	        

	        	 return node.left;
	         }
	         else
	         {
	        	// if two children gets predecessor  and swaps
	            node.list = getPredData(node.left);
	            node.left =  recRemove(node.left, node.list) ;
	         }
	      }
	      return node;
	   }
	/**This method  determines the predecessor
	 * @author Naqib Rahman
	 */
	   private DbaList getPredData(Node node)
	   {
	      while (node.right != null) 
	    	  node = node.right;
	      return node.list;
	   }
	
	 
	   /** Method to get a list of records based off dba
		 * @return DbaList of records
		 * @param String dba species the record being looked for.
		 * @author Naqib Rahman
		 */


	public DbaList get(String dba)
	{
		Node node = root;
		while(node!=null){
			if(node.list.getDba().equalsIgnoreCase(dba)){
				return node.list;
			}
			else if(node.list.getDba().compareTo(dba)<0){
				node = node.left;
			}
			else{
				node = node.right;
			}
		}
		return null;
	}
	/** Method to check if  a node for a specified dba 
	 * is  in the bst
	 * @return true if there is false if not
	 * @param String dba specifies which node to search for
	 * @author Naqib Rahman
	 */
	
	public boolean contains(String dba){
		Node node = root;
		while(node!=null){
			if(node.list.getDba().equalsIgnoreCase(dba)){
				return true;
			}
			else if(node.list.getDba().compareTo(dba)<0){
				node = node.left;
			}
			else{
				node = node.right;
			}
		}
		return false;
	}
	
	
	


	
	

}
/** 
 * Node class to use for BST
 * Contains a DbaList and a
 * reference for a left node and a 
 * right node. 
 * 
 * @author Naqib Rahman
 * 
 */

class Node implements Comparable<DbaList>{
	protected DbaList list;
	protected Node left, right;
	/** Constructor for all data fields
	 * @author Naqib 
	 */
	public Node(DbaList list, Node left, Node right )
	{
		this.list = list;
		this.left= left; this.right= right;
		
	}
	/** Constructor with only the list param
	 * @author Naqib
	 */
	public Node(DbaList list){
		this.list=list;
	}
	
	public int compareTo(DbaList o) {
		// TODO Auto-generated method stub
		return this.list.compareTo(list);
	}
	
}