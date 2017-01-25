package proj1;

import java.text.DateFormat;

/* TODO: Implement and properly document
 * the Record class class. 
 * You may change the types of the data fields, if you wish. 
 * 
 * The Comparator classes are provided for your convenience. 
 * 
 */


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

public class Record {
		
	private String camis;
	private String dba;
	private String boro;
	private String building;
	private String street;
	private String zipcode;
	private String phone;
	private String cuisineDescription;
	private Date inspectionDate;
	private String action;
	private String violationCode;
	private String violationDescription;
	private String criticalFlag;
	private int score;
	private String grade;
	private Date gradeDate;
	private Date recordDate;
	private String inspectionType;
	String OriginalString="";
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");
	
	private static SimpleDateFormat myFormat = new SimpleDateFormat ("MM/dd/yyyy");
	public Record ( ArrayList<String> entries ) {
		
		for(int i = 0; i<18;i++){
		OriginalString+= ( "\""+ entries.get(i) + "\",");
		}
		
		//initializes based off array
		camis = entries.get(0);
		dba = entries.get(1);
		boro= entries.get(2);
		building = entries.get(3);
		street = entries.get(4);
		zipcode= entries.get(5);
		phone = entries.get(6);
		cuisineDescription= entries.get(7);
		//handles parseexception
		try {
			inspectionDate = dateFormat.parse(entries.get(8));
		} catch (ParseException e) {
			inspectionDate = null;
		}
	
			
		action = entries.get(9);
		violationCode = entries.get(10);
		violationDescription= entries.get(11);
		criticalFlag= entries.get(12);
		//gives blank entries a score of -1
		try{ 
		score = Integer.parseInt(entries.get(13));
		}
		catch (NumberFormatException e){
			score = -1;
		}
		grade = entries.get(14);
		
		try {
			gradeDate = dateFormat.parse(entries.get(15));
		} catch (ParseException e) {
			gradeDate = null;
		}
		try {
			recordDate = dateFormat.parse(entries.get(16));
		} catch (ParseException e) {
			recordDate = null;
		}
		inspectionType = entries.get(17);

		
		
		
		
		
		
		 
		
		
		
		
		
				
		
	}

	public String rawData(){
		return OriginalString.toString();
		
	}
	
	public String toString ( ) {
		//checks for negative scores and nulll dates 
		String inspectionString = "";
		String gradeString = "";
		String recordString = "";
		String scoreString = "";
		if(inspectionDate!= null)
			inspectionString = myFormat.format(inspectionDate);
		if(gradeDate != null)
			gradeString = myFormat.format(gradeDate);
		if(recordDate !=null)
			myFormat.format(recordDate);
		if(score != -1)
			scoreString= Integer.toString(score);
		
	return String.format( "%10.10s\t%20.20s\t%20.20s\t%20.20s\t%10.10s\t%3.3s",
			camis, dba, building + " " + street,cuisineDescription,inspectionString,scoreString);
	}

	/**
	 *@returns dba
	 * 
	 * @author Naqib
	 */
	public String getDba() {
		
		return dba;
	}
	

	/**
	 *@returns inspectionDate
	 * 
	 * @author Naqib
	 */
	
	
	public Date getInspectionDate() {
		
		return inspectionDate;
	}
	/**
	 *@returns camis
	 * 
	 * @author Naqib
	 */

	public String getCamis() {
		
		return camis;
	}

	/**
	 *@returns cuisineDescription
	 * 
	 * @author Naqib
	 */
	public String getCuisineDescription() {
		
		return cuisineDescription;
	}

	/**
	 *@returns score
	 * 
	 * @author Naqib
	 */
	public int getScore() {
		
		return score;
	}
	/**
	 *@returns zipcode
	 * 
	 * @author Naqib
	 */
	public String getZipcode(){
		return zipcode;
	}
	/**
	 *@returns address(building + street)
	 * 
	 * @author Naqib
	 */
	public String getAddress(){
		return building+" " + street;
	}
	
}


/**
 * Defines Comparator object for the objects of type
 * record. The objects are compared by their unique
 * camis number. 
 * 
 * @author Joanna Klukowska
 *
 */
class RecordComparatorByCamis implements Comparator<Record>{
	public int compare(Record arg0, Record arg1) { 
		return arg0.getCamis().compareTo( arg1.getCamis() ) ;
	}	
}



/**
 * Defines Comparator object for the objects of type
 * record. The objects are compared by the name of the business;
 * ties are resolved based on the unique camis number. 
 * 
 * @author Joanna Klukowska
 *
 */
class RecordComparatorByDBA implements Comparator<Record>{
	
	public int compare(Record arg0, Record arg1) { 
		int compareResult = arg0.getDba().compareToIgnoreCase(arg1.getDba() ) ; 
		if ( compareResult == 0 ) {
			return  arg0.getCamis().compareTo( arg1.getCamis() );
		}
		else 
			return compareResult;
	}	
}


/**
 * Defines Comparator object for the objects of type
 * record. The objects are compared by the type of cuisine;
 * ties are resolved based on the unique camis number. 
 * 
 * @author Joanna Klukowska
 *
 */
class RecordComparatorByCuisine implements Comparator<Record>{
	
	public int compare(Record arg0, Record arg1) { 
		int compareResult = arg0.getCuisineDescription().compareToIgnoreCase(
				arg1.getCuisineDescription() ) ; 
		if ( compareResult == 0 ) {
			return  arg0.getCamis().compareTo( arg1.getCamis() );
		}
		else 
			return compareResult;
	}	
}


/**
 * Defines Comparator object for the objects of type
 * record. The objects are compared by inspection scores;
 * ties are resolved based on the unique camis number. 
 * 
 * @author Joanna Klukowska
 *
 */
class RecordComparatorByScore implements Comparator<Record>{
	
	public int compare(Record arg0, Record arg1) { 
		int compareResult = arg0.getScore() - arg1.getScore() ; 
		if ( compareResult == 0 ) {
			return  arg0.getCamis().compareTo( arg1.getCamis() );
		}
		else 
			return compareResult;
	}	
}


/**
 * Defines Comparator object for the objects of type
 * record. The objects are compared by inspection date;
 * ties are resolved based on the unique camis number. 
 * 
 * @author Joanna Klukowska
 *
 */
class RecordComparatorByDate implements Comparator<Record>{
	
	public int compare(Record arg0, Record arg1) { 
		int compareResult = arg0.getInspectionDate().compareTo( arg1.getInspectionDate() ); 
		if ( compareResult == 0 ) {
			return  arg0.getCamis().compareTo( arg1.getCamis() );
		}
		else 
			return compareResult;
	}	
}











