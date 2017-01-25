package proj1;

import java.lang.reflect.Array;

/**
 * This class creates a personalized ArrayList Subclass
 * to handle the record class.
 * 
 * The methods included search and sort records 
 * stored in the list using different algorithms. 
 * 
 * 
 * @author Naqib Rahman
 *
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

@SuppressWarnings("serial")
public class MyArrayList extends ArrayList<Record> {
	/**
	 * sorts based of key 
	 * @param String key- decides which comparator to use
	 * @author Naqib
	 */
	
	public void sort(String key) {
		Comparator<Record> comp = null;
		//determines comparator
		if(key.equalsIgnoreCase("CAMIS"))
			comp =  new RecordComparatorByCamis();
		else if(key.equalsIgnoreCase("DBA"))
			comp =  new RecordComparatorByDBA();
		else if(key.equalsIgnoreCase("CUISINE"))
			comp =  new RecordComparatorByCuisine();
		else if(key.equalsIgnoreCase("SCORE"))
			comp =  new RecordComparatorByScore();
		else if(key.equalsIgnoreCase("DATE"))
			comp =  new RecordComparatorByDate();
		

		for (int i = 0; i < this.size() - 1; i++) {
	    	// Find the minimum in the list[i..list.length-1]
	    		Record currentMin = this.get(i); 
	    		int currentMinIndex = i;
	    		for (int j = i + 1; j < this.size(); j++) {
	    			if (comp.compare(this.get(j),currentMin)<1) {
	    				currentMin = this.get(j);
	    				currentMinIndex = j;
	    				}
	    			}
	    		if (currentMinIndex != i) {
	    			this.set(currentMinIndex,this.get(i));
	    			this.set(i,currentMin);
	    		}
		}
		
	}	
	/**
	 * Implements a merge sort based off a given comparator
	 * @param Comparator comp- chose comparator to sort data
	 * @author Naqib
	 */
	
	public void sort(Comparator<? super Record> comp){
		mergeSort(0,this.size()-1,comp);

	
	}
	/**
	 * Recursive merge sort method
	 * @param int left is the left index, int right is the right index,
	 * Comparator comp is the chosen comparator 
	 * @author Naqib
	 */
	
	
	private void mergeSort(int left, int right, Comparator<? super Record> comp) {
		if (left >= right)
			return;
				
		int middle = (left+right)/2;
		mergeSort(left,middle,comp);
		mergeSort(middle+1,right,comp);
		merge(left,middle,right,comp);
		
		
		
	}
	
	/**
	 * merge method for mergesort
	 * @param  @param int left is the left index, int right is the right index, int mid
	 * is the middle index. Comparator comp is the chosen comparator 
	 * @author Naqib
	 */

	private void merge(int left, int middle,int right, Comparator<? super Record> comp) {
		
	     Record[] temp = new Record[right-left+1];
	     
		 int i = left;
	     int j = middle + 1;
	     int k = 0;
	
		while(i<middle+1 && j<right+1){
			if(comp.compare(this.get(j),this.get(i))>0){
				temp[k++]=this.get(i++);
				}
			else{
				temp[k++]=this.get(j++);
			}
		}
		while(i<middle+1){
			temp[k++]= this.get(i++);
		}
		while(j<right+1){
			temp[k++]=this.get(j++);
		}
		for (int z= left; z<right+1;z++)
		{
			this.set(z, temp[z-left]);
		}
		
		
		
		}
	/**
	 * Sorts using a merge sort
	 * @param String key determines which comparator to use
	 * @author Naqib
	 */

	
	public void sortFaster(String key){
		Comparator<Record> comp = null;
		//determines comparator 
		if(key.equalsIgnoreCase("CAMIS"))
			comp =  new RecordComparatorByCamis();
		else if(key.equalsIgnoreCase("DBA"))
			comp =  new RecordComparatorByDBA();
		else if(key.equalsIgnoreCase("CUISINE"))
			comp =  new RecordComparatorByCuisine();
		else if(key.equalsIgnoreCase("SCORE"))
			comp =  new RecordComparatorByScore();
		this.sort(comp);
		
	}
	/**
	 * Checks to see if a list is sorted based of the given key
	 * @param  String key determines which comparator to use to check for a sort
	 * @return boolean true if sorted and false if not sorted 
	 * @author Naqib
	 */

	public boolean isSorted(String key){
		Comparator<Record> comp = null;
		if(key.equalsIgnoreCase("CAMIS"))
			comp =  new RecordComparatorByCamis();
		else if(key.equalsIgnoreCase("DBA"))
			comp =  new RecordComparatorByDBA();
		else if(key.equalsIgnoreCase("CUISINE"))
			comp =  new RecordComparatorByCuisine();
		else if(key.equalsIgnoreCase("SCORE"))
			comp =  new RecordComparatorByScore();
		//searches to see if condition is broke
		for (int i = 1; i < this.size(); i++) {
			if(comp.compare(this.get(i-1), this.get(i)) >0){
				System.out.println(key);
				System.out.println(this.get(i-1).toString());
				System.out.println(this.get(i).toString());
				return false;
			}
		}
		//if not broken than must be 
		return true;
	}
		
	public String toString ( ) {
		String toprint = "";
		for (int i = 0; i<this.size();i++){
			toprint+=(this.get(i).toString() +"\n");
			
		}
		
				
		return toprint;
	}
	
	
	
	
	
}
