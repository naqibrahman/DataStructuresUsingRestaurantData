package proj1;

/**
 * This class provides different data structures to
 * organize the record class.
 * 
 * It uses the MyArrayList class to organize it as well
 * as the bstOfRecordsByDBA class.
 * 
 * 
 * 
 * @author Naqib Rahman
 *
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Collections;
public class ListOfRecords {

	private MyArrayList list;
	private bstOfRecordsByDBA bst;
	//for scores
	
	private  Hashtable<String, PriorityQueue<Record>> heapZip;
	//uses MyArrayList as an aux class
	private Hashtable<String, LinkedList> addressTable;
	//uses a linkedlist as an auxclass
	private Hashtable<Date, LinkedList> DateTable;
	
	/**Default constructor initializes data fields
	 * @author Naqib Rahman
	 */
	
	public ListOfRecords() {
		list = new MyArrayList();
		bst = new bstOfRecordsByDBA();
		heapZip =  new Hashtable<String, PriorityQueue<Record>>();
		addressTable = new Hashtable<String, LinkedList>();
		DateTable = new Hashtable<Date, LinkedList> ();
		}
	/**
	 * adds to the list the bst's and hashtables
	 * @param ArrayList<String> words is an
	 * ArrayList of each line in the csv file.
	 * @author Naqib Rahman
	 */
	public void add(ArrayList<String> words) {
		
		Record r = new Record(words);
		//Uppercase string for nameAddress
		String name =  r.getDba().toUpperCase() + r.getAddress().toUpperCase();
		list.add(r);
		bst.add(r);
		//initializes and adds to hash of zipcode heaps
		if(heapZip.containsKey(r.getZipcode()) )
			heapZip.get(r.getZipcode()).add(r);	
		else
			heapZip.put(r.getZipcode(),new PriorityQueue<Record>(11,new RecordComparatorByScore()));
		//initializes and adds to has of nameAddresses
		if (addressTable.containsKey(name))
			addressTable.get(name).add(r);
		else
			{addressTable.put(name, new LinkedList());
			 addressTable.get(name).add(r);
			}
		if (DateTable.containsKey(r.getInspectionDate()))
			DateTable.get(r.getInspectionDate()).add(r);
		else{
			DateTable.put(r.getInspectionDate(), new LinkedList());
			DateTable.get(r.getInspectionDate()).add(r);
			}
			
			

	}

	public String toString() {
		String toprint = "";
		for (int i = 0; i<list.size();i++){
			toprint+=(list.get(i).rawData() +"\n");
			
		}
					
		return toprint;
	}

	/**
	 * sorts using given key
	 * @param key determines what to sort by
	 * @author Naqib Rahman
	 */
	public void sort(String key) {
		list.sort(key);
	}
	/**
	 * does a merge sort using given key
	 * @param key determines what to sort by
	 * @author Naqib Rahman
	 */

	
	public void sortFaster(String key){
		list.sortFaster(key);
	}
	
	
	
	
	/**
	 * Uses a bst to return an arrayList of
	 * records with the same dba
	 * @param key determines what dba to use
	 * @author Naqib Rahman
	 */

	public MyArrayList findByName(String key){
		MyArrayList results = new MyArrayList();
		
		DbaList list= bst.get(key);

		
		
		for (Record r : list.getList()){
			results.add(r);
			
		}
		

		
		return results;
		
	}
	
	
	
	/**
	 * finds by Nameaddress  key1 = dba key2 = address 
	 * 
	 * My change in the code is beneficial because it greatly reduces the time needed to search for
	 * a restaurant.  While using an ArrayList I had to linearly search through the entire list to find
	 * results, this is an efficiency of O(N). However, with a hashtable I can directly get restaurant data
	 * and now have an efficiency of O(1).
	 * 
	 * @returns list of matches
	 * @author Naqib Rahman
	 */

	public MyArrayList findByNameAddress(String key1, String key2, String dateFlag) {
		String name = key1.toUpperCase() +key2.toUpperCase();
		
		
		//for all matches
		// Gets results from address  hashtable
		MyArrayList results = new MyArrayList();
		results.addAll(addressTable.get(name));
		//for only first/last matches
		MyArrayList results2= new MyArrayList();
		//sorts by date
		results.sort(new RecordComparatorByDate());
		
		//for all
		if (dateFlag.equalsIgnoreCase("ALL"))
				return results;
		//for first
		else if (dateFlag.equalsIgnoreCase("First"))
		{
			results2.add(results.get(0));
			return results2;
			
		}
		//for last
		else if (dateFlag.equalsIgnoreCase("Last"))
		{
			results2.add(results.get(results.size()-1));
			return results2;
		}
			
		
		return results;
	}
	/**
	 * finds by last date an inspection was recorded 
	 * 
	 * My change in the code is beneficial because the  total number of records that need to be searched through is
	 * greatly reduced as as all records with the same date are in a linked list. Furthermore due to there being no repeats,
	 * finding the nth inspection date is much simplified. A hashtable allows us to retrieve all the records in about O(1) time
	 * obtaining the dates. 
	 * 
	 * 
	 * @param numOfDates determines which last inspection
	 * date to use
	 * @returns list of matches
	 * @author Naqib Rahman
	 */

	public MyArrayList findByDate(int numOfDates) {	
	
		MyArrayList results = new MyArrayList();
		//creates list of date keys and sorts them
		List<Date> dates = Collections.list(DateTable.keys());
		Collections.sort(dates);
		//adds the dates we need
		for (int i =0; i<numOfDates; i++){
			results.addAll(DateTable.get(dates.get(i)));
		}
		//sorts
		results.sort(new RecordComparatorByDate());
		return results;
	}
	/**
	 * finds by score in a give zipcode
	 * @param score is the score; String zipcode is the zipcode
	 * 
	 * My change in the code is beneficial because by organizing all the data into different zipcodes with a
	 * hashtable we instantly have access to the records we need to search through rather than with an arraylist where
	 * we have to go through every single record regardless of zipcode.Furthermore,by having the records sorted in a minheap
	 * we can easily extract all the records below the score parameter as the top of the que will be the minimum score.
	 * 
	 * 
	 * @returns list of matches
	 * @author Naqib Rahman
	 */
	public MyArrayList findByScore(int score, String zipcode) {
		
		MyArrayList results = new MyArrayList();
		///iterate through heap
		for (Record e: heapZip.get(zipcode)){
			//check if -1 (no score) and if less than score
			if(e.getScore()!=-1 && e.getScore()<=score)
				results.add(e);
		}
		//sort reuslts
		results.sortFaster("SCORE");
		
		return results;
	}
	/**
	 * checks if list is sorted based off a given key
	 * @param key determines what to sort by
	 * @author Naqib
	 */

	public boolean isSorted(String key){
	
		return list.isSorted(key);
	
}
}
