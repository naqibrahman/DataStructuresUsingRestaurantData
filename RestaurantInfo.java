package proj1;


/* DO NOT MODIFY ANYTHING IN THIS FILE 
 * 
 * (You may specify a different package 
 * above.)
 * 
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This class provides a program that works with
 * NYC Restaurant Inspection Data.
 * 
 * The program reads an input file with commands
 * describing tasks to be performed.
 * 
 * For complete program specification,
 * 
 * @see http://cs.nyu.edu/~joannakl/cs102.03_s16/assignments/proj1_2.pdf
 * @see http://cs.nyu.edu/~joannakl/cs102.03_s16/assignments/proj2.pdf
 * 
 * @author Joanna Klukowska
 *
 */
public class RestaurantInfo {

	/**
	 * This is where the program starts. 
	 * The program can run in a regular mode or in a test mode (when the fourth
	 * argument is specified). 
	 * 
	 * In the regular mode the output from this method 
	 * is written to an output file specified by args[2], and to output 
	 * files specified by individual commands listed in file args[1]. 
	 * The errors are written to the standard error stream. 
	 * The program reads data from the file specified by args[0] and
	 * reads commands from the file specified by args[1].
	 * 
	 * In the test mode, the output is written to the console (standard output). 
	 * The program reads data from the file specified by args[0]. The
	 * arguments args[1] and args[2] are ignored. 
	 * This mode is triggered when args[3] contains string "sort". 
	 * 
	 * @param args an array of command line arguments,
	 * expected values are 
	 *   args[0] - name of the file containing
	 *   restaurant inspection data 
	 *   args[1] - name of the file containing commands 
	 *   to be executed 
	 *   args[2] - name of the output file 
	 *   args[3] - [optional] keyword "sort" indicating that the 
	 *             program should run in sort test mode.
	 */
	public static void main(String[] args) {
		if (args.length < 3) {
			System.err.println("Error: invalid usage\n"
					+ "RestaurantInfo dataFile commandFile outputFile [testModeFlag]\n");
			System.exit(0);
		}
		
		if (args.length == 4 ) { //run the test mode 
			if (args[3].equalsIgnoreCase("sort") ) {
				sortTest( args[0]);
				return; 
			}
			//if the fourth argument is anything other that a word "sort", 
			//the program continues in the regular mode 
		}

		/*****************************************************************
		 ** verify the input files and create appropriate input streams **
		 *****************************************************************/

		File fileName = new File(args[0]);

		if (!fileName.canRead()) {
			System.err.printf("Error: cannot read from file %s\n.",
					fileName.getAbsolutePath());
			System.exit(0);
		}

		Scanner dataInput = null;
		try {
			dataInput = new Scanner(fileName);
		}
		catch (FileNotFoundException e) {
			System.err.printf("Error: cannot open file %s for reading\n.",
					fileName.getAbsolutePath());
			System.exit(0);
		}

		fileName = new File(args[1]);

		if (!fileName.canRead()) {
			System.err.printf("Error: cannot read from file %s\n.",
					fileName.getAbsolutePath());
			System.exit(0);
		}

		Scanner commandInput = null;
		try {
			commandInput = new Scanner(fileName);
		}
		catch (FileNotFoundException e) {
			System.err.printf("Error: cannot open file %s for reading\n.",
					fileName.getAbsolutePath());
			System.exit(0);
		}

		/*****************************************************************
		 ** read the input data file and save the records               **
		 *****************************************************************/

		// collection of all records
		ListOfRecords allRecords = new ListOfRecords();

		// working set of records (results from the most recent find command
		MyArrayList mostRecentResults = null;
		String mostRecentCommand = "";

		// read and ignore the headings
		dataInput.nextLine();
		ArrayList<String> words;

		while (dataInput.hasNextLine()) {
			String textLine = dataInput.nextLine();
			words = splitDataLine(textLine);

			if (words.size() != EntriesOrder.SIZE.ordinal()) {
				continue; // skip lines that are not complete
			}

			allRecords.add(words);

		}

		/***************************************************************** 
		 ** open a file output stream for writing results out           ** 
		 *****************************************************************/

		PrintWriter output = null;
		File outputFile = new File(args[2]);

		try {
			output = new PrintWriter(outputFile);
		}
		catch (FileNotFoundException e) {
			System.err.printf("Error: cannot open file %s for writing.\n.",
					outputFile.getAbsolutePath());
		}

		/*****************************************************************
		 ** execute commands from the input command file                **
		 *****************************************************************/

		String commandLine = "";
		ArrayList<String> parsedCommand;

		PrintWriter sortOutputStream = null;
		int lineNumber = 0;

		while (commandInput.hasNextLine()) {
			commandLine = commandInput.nextLine();
			lineNumber++;
			parsedCommand = splitCommandLine(commandLine);

			// skip lines that are blank
			if (parsedCommand.size() == 0)
				continue;
			if (parsedCommand.get(0).equalsIgnoreCase("print")) {
			//////////////////////////////
			// process print command
			//////////////////////////////

				if (mostRecentResults == null)
					continue;
				output.print("Results of:\t" + mostRecentCommand);
				output.println("\n- - - - - - - - - - - - - - - - - - - -\n");
				output.println(mostRecentResults.toString());
				output.println("\n=======================================\n");

			}
			else if (parsedCommand.get(0).equalsIgnoreCase("sortAll")) {
			//////////////////////////////
			// process sortAll command
			//////////////////////////////

				if (parsedCommand.size() < 2) {
					// the follow-up sequence is incomplete or missing
					printError(lineNumber,
							"incomplete follow-up sequence for sortAll");
					continue;
				}
				String key = parsedCommand.get(1);

				if (key.equalsIgnoreCase("CAMIS") || key.equalsIgnoreCase("DBA")
						|| key.equalsIgnoreCase("CUISINE")) {
					allRecords.sort(key);
				}
				else {
					printError(lineNumber,
							"invalid KEY specification for sortAll");
					continue;
				}

				if (parsedCommand.size() > 2) { // write data to file

					File sortOutputFileName = new File(parsedCommand.get(2));
					try {
						sortOutputStream = new PrintWriter(sortOutputFileName);
					}
					catch (FileNotFoundException e) {
						System.err.printf(
								"Error: cannot open file %s for writing.\n.",
								sortOutputFileName.getAbsolutePath());
					}
					sortOutputStream.println(allRecords.toString());
					sortOutputStream.close();
				}

			}
			else if (parsedCommand.get(0).equalsIgnoreCase("sortResults")) {
			//////////////////////////////
			// process sortResults command
			//////////////////////////////

				if (parsedCommand.size() < 2) {
					// the follow-up sequence is incomplete or missing
					printError(lineNumber,
							"incomplete follow-up sequence for sortResults");
					continue;
				}
				String key = parsedCommand.get(1);

				if (key.equalsIgnoreCase("CAMIS") || key.equalsIgnoreCase("DBA")
						|| key.equalsIgnoreCase("CUSINE")
						|| key.equalsIgnoreCase("SCORE")) {
					mostRecentResults.sort(key);
					mostRecentCommand = commandLine;
				}
				else {
					printError(lineNumber,
							"invalid KEY specification for sortResults");
					continue;
				}

			}
			else if (parsedCommand.get(0).equalsIgnoreCase("findByName")) {
			//////////////////////////////
			// process findByName command
			//////////////////////////////
				if (parsedCommand.size() < 2) {
					// the follow-up sequence is incomplete or missing
					printError(lineNumber,
							"incomplete follow-up sequence for findByName");
					continue;
				}
				String key = parsedCommand.get(1);

				mostRecentResults = allRecords.findByName(key);
				mostRecentCommand = commandLine;

			}
			else if (parsedCommand.get(0)
					.equalsIgnoreCase("findByNameAddress")) {
			//////////////////////////////////////
			// process findByNameAddress command
			//////////////////////////////////////
				if (parsedCommand.size() < 4) {
					// the follow-up sequence is incomplete or missing
					printError(lineNumber,
							"incomplete follow-up sequence for findByNameAddress");
					continue;
				}
				String key1 = parsedCommand.get(1);
				String key2 = parsedCommand.get(2);
				String dateFlag = parsedCommand.get(3);
				if (dateFlag.equalsIgnoreCase("first")
						|| dateFlag.equalsIgnoreCase("last")
						|| dateFlag.equalsIgnoreCase("all")) {
					mostRecentResults = allRecords.findByNameAddress(key1, key2,
							dateFlag);
					mostRecentCommand = commandLine;
				}
				else {
					printError(lineNumber,
							"invalid option for findByNameAddress");
					continue;
				}

			}
			else if (parsedCommand.get(0).equalsIgnoreCase("findByDate")) {
			//////////////////////////////////////
			// process findByDate command
			//////////////////////////////////////
				if (parsedCommand.size() < 2) {
					// the follow-up sequence is incomplete or missing
					printError(lineNumber,
							"incomplete follow-up sequence for findByDate");
					continue;
				}
				int numOfDates;
				try {
					numOfDates = Integer.parseInt(parsedCommand.get(1));
				}
				catch (NumberFormatException e) {
					printError(lineNumber,
							"invalid value specified for NUM_OF_DATES in findByDate");
					continue;
				}
				mostRecentResults = allRecords.findByDate(numOfDates);
				mostRecentCommand = commandLine;

			}
			else if (parsedCommand.get(0).equalsIgnoreCase("findByScore")) {
			//////////////////////////////////////
			// process findByScore command
			//////////////////////////////////////
				if (parsedCommand.size() < 3) {
					// the follow-up sequence is incomplete or missing
					printError(lineNumber,
							"incomplete follow-up sequence for findByScore");
					continue;
				}
				int score;
				String zipcode;
				try {
					score = Integer.parseInt(parsedCommand.get(1));
					zipcode = parsedCommand.get(2);
				}
				catch (NumberFormatException e) {
					printError(lineNumber,
							"invalid value specified for SCORE in findByScore");
					continue;
				}
				mostRecentResults = allRecords.findByScore(score, zipcode);
				mostRecentCommand = commandLine;

			}
			else {
				printError(lineNumber, "invalid command");
			}

		}

		output.close();

	}

	/**
	 * Splits a given line according to commas and double quotes 
	 * (double quotes surround multi-word entries that may or may not 
	 * contain commas).
	 * Commas within entries are left in the entries. Commas outside of the
	 * entries are removed. Multiple white-space characters are
	 * replaced by single space. 
	 * 
	 * @param textLine line of text to be parsed
	 * @return an ArrayList object containing all individual entries/tokens
	 *         found on the line.
	 */
	public static ArrayList<String> splitDataLine(String textLine) {
		ArrayList<String> entries = new ArrayList<String>();
		int lineLength = textLine.length();
		StringBuffer nextWord = new StringBuffer();
		char nextChar;
		boolean insideQuotes = false;
		boolean firstSpace = true;

		for (int i = 0; i < lineLength; i++) {
			nextChar = textLine.charAt(i);
			// add character to the current entry
			if (nextChar != ',' && nextChar != '"'
					&& !Character.isWhitespace(nextChar)) {
				nextWord.append(nextChar);
				firstSpace = true;
			}
			else if (Character.isWhitespace(nextChar)) {
				if (firstSpace) {
					// switch the flag to indicate that first white space had
					// been read
					firstSpace = false;
					if (nextWord.length() != 0) { // append a single space to
													 // the entry
						nextWord.append(" ");
					}
				}
				else if (!firstSpace) {
					// skip all additional spaces
					continue;
				}
			}
			// double quote found, decide if it is opening or closing one
			else if (nextChar == '"') {
				if (insideQuotes) {
					insideQuotes = false;
				}
				else {
					insideQuotes = true;
				}
			}
			// found comma inside double quotes, just add it to the string
			else if (nextChar == ',' && insideQuotes) {
				nextWord.append(nextChar);
			}
			// end of the current entry reached, add it to the list of entries
			// and reset the nextWord to empty string
			else if (nextChar == ',' && !insideQuotes) {
				// trim the white space before adding to the list
				entries.add(nextWord.toString().trim());

				nextWord = new StringBuffer();
			}

			else {
				System.err.println("This should never be printed.\n");
			}
		}
		// add the last word
		// trim the white space before adding to the list
		entries.add(nextWord.toString().trim());

		return entries;
	}

	/**
	 * Splits the given line according to spaces a double quotes
	 * (double quotes surround all multi-word entries). 
	 * Multiple white-space characters are replaced by single space. 
	 * 
	 * @param textLine  line of text to be parsed
	 * @return an ArrayList object containing all individual entries/tokens
	 *         found on the line.
	 */
	public static ArrayList<String> splitCommandLine(String textLine) {
		ArrayList<String> entries = new ArrayList<String>();
		int lineLength = textLine.length();
		StringBuffer nextWord = new StringBuffer();
		char nextChar;
		boolean insideQuotes = false;
		boolean firstSpace = true;
		//iterate over all characters in the textLine
		for (int i = 0; i < lineLength; i++) {
			nextChar = textLine.charAt(i);
			
			if (nextChar == '"') { 
				//change insideQuotes flag when nextChar is a quotes
				if (insideQuotes) {
					insideQuotes = false;
				}
				else {
					insideQuotes = true;
				}
			}
			else if (Character.isWhitespace(nextChar)) {
				if (firstSpace && !insideQuotes) {
					// add completed entry to the list of entries
					firstSpace = false;
					if (!nextWord.toString().equals("")) {
						entries.add(nextWord.toString());
						nextWord = new StringBuffer();
					}
				}
				else if (firstSpace && insideQuotes) {
					// add a space to the current entry
					firstSpace = false;
					nextWord.append(" ");
				}
				else if (!firstSpace) {
					// skip all additional spaces
					continue;
				}
			}
			else {
				//add all other characters to the nextWord 
				nextWord.append(nextChar);
				firstSpace = true;
			}

		}
		// add the last word (assuming not empty)
		// trim the white space before adding to the list
		if (!nextWord.toString().equals("")) {
			entries.add(nextWord.toString().trim());
		}

		return entries;
	}

	/**
	 * Print an error message to the standard error stream. 
	 * @param line line number to be cited in the error message
	 * @param message specific error message 
	 */
	public static void printError(int line, String message) {
		System.err.printf("Error on line %d: \n\t%s\n", line, message);
	}
	
	
	
	
	
	
	
	
	/**
	 * This method runs tests that compare time performance
	 * of an implementation of a quadratic sort and an 
	 * implementation of a O(NlogN) sort on data sets of 
	 * increasing sizes. 
	 * 
	 * @param inputFileName name of the input file containing the data
	 */
	public static void sortTest(String inputFileName) { 
		//change these constants to modify sizes of data sets
		//and number of data sets in the "experiment" 		
		final int NUM_OF_TEST_DATA_SETS = 10;
		final int NUM_OF_RECORDS_PER_SET = 10000;
		final int NUM_OF_RECORDS_TOTAL = NUM_OF_TEST_DATA_SETS * NUM_OF_RECORDS_PER_SET;
		final int NUM_OF_WARMUP_RECORDS_PER_SET = 100;
		final int NUM_OF_WARMUP_RECORDS_TOTAL = NUM_OF_TEST_DATA_SETS * NUM_OF_WARMUP_RECORDS_PER_SET;
		
		//change this constant to modify the sort order
		final String SORT_KEY = "CAMIS";
		
		
		/*****************************************************************
		 ** verify the input file and create appropriate input stream   **
		 *****************************************************************/

		File fileName = new File(inputFileName);

		if (!fileName.canRead()) {
			System.err.printf("Error: cannot read from file %s\n.",
					fileName.getAbsolutePath());
			System.exit(0);
		}

		Scanner dataInput = null;
		try {
			dataInput = new Scanner(fileName);
		}
		catch (FileNotFoundException e) {
			System.err.printf("Error: cannot open file %s for reading\n.",
					fileName.getAbsolutePath());
			System.exit(0);
		}


		/*****************************************************************
		 ** read the input data file and save the records               **
		 *****************************************************************/
		System.out.println("Reading data ");
		
		// collections of records with different sizes
		ListOfRecords [] dataSets1 = new ListOfRecords [NUM_OF_TEST_DATA_SETS];
		ListOfRecords [] warmUpSet1 = new ListOfRecords [NUM_OF_TEST_DATA_SETS];
		ListOfRecords [] dataSets2 = new ListOfRecords [NUM_OF_TEST_DATA_SETS];
		ListOfRecords [] warmUpSet2 = new ListOfRecords [NUM_OF_TEST_DATA_SETS];
		
		for (int i = 0; i < NUM_OF_TEST_DATA_SETS; i++ ) {
			dataSets1[i] = new ListOfRecords();
			warmUpSet1[i] = new ListOfRecords();
			dataSets2[i] = new ListOfRecords();
			warmUpSet2[i] = new ListOfRecords();
		}

		// read and ignore the headings
		dataInput.nextLine();
		ArrayList<String> words;
		
		for (int i = 0,  k = 0; i < NUM_OF_RECORDS_TOTAL; i++, k++) {
			if (i % NUM_OF_RECORDS_PER_SET == 0) {
				System.out.print(".");
			}
			//if file is too small, terminate the program 
			if (! dataInput.hasNextLine() ) {
				System.err.printf("Error reading file %s: number of records too small. \n"
						+ "File needs to contain at least %d records. \n\n", 
						fileName.getAbsolutePath(), NUM_OF_TEST_DATA_SETS * 10000 );
				System.exit(-1); 
			}
			//read and parse the line
			String textLine = dataInput.nextLine();
			words = splitDataLine(textLine);
			//verify the number of entries
			if (words.size() != EntriesOrder.SIZE.ordinal()) {
				continue; // skip lines that are not complete
			}
			
			//add to data set(s)
			for (int j = 0; j < i /  NUM_OF_RECORDS_PER_SET ; j++ ) {
				dataSets1[NUM_OF_TEST_DATA_SETS-1 - j].add(words); 
				dataSets2[NUM_OF_TEST_DATA_SETS-1 - j].add(words); 
			}
			
			//add to warmup data set(s)
			if (k < NUM_OF_WARMUP_RECORDS_TOTAL) {
				for (int j = 0; j < k /  NUM_OF_WARMUP_RECORDS_PER_SET ; j++ ) {
					warmUpSet1[NUM_OF_TEST_DATA_SETS-1 - j].add(words); 
					warmUpSet2[NUM_OF_TEST_DATA_SETS-1 - j].add(words); 
				}
			}
		}
		
		/*****************************************************
		 ** run different sort implementations and measure  **
		 ** their time performance                          **
		 *****************************************************/

		long start, end; 
		
		
		System.out.println("\n\nWarmup JIT:");
		//warmup the JIT
		for (int i = 0; i < NUM_OF_TEST_DATA_SETS; i++) {
			
			
			System.out.print((i+1) * NUM_OF_WARMUP_RECORDS_PER_SET  );

			System.gc();  //run Java garbage collector 
			//run the slow sort 
			start = System.nanoTime();
			warmUpSet1[i].sort(SORT_KEY); 
			end = System.nanoTime();
			System.out.printf("\t%12d\t", end - start);
			if (! warmUpSet1[i].isSorted(SORT_KEY) ) {
				System.err.printf("\nMost recent call to sort() did not sort the elements. \n\n");
			}

			System.gc();  //run Java garbage collector 
			//run the faster sort 
			start = System.nanoTime();
			warmUpSet2[i].sortFaster(SORT_KEY); 
			end = System.nanoTime();
			System.out.printf("\t%12d\n", end - start);
			if (! warmUpSet2[i].isSorted(SORT_KEY) ) {
				System.err.printf("\nMost recent call to sortFaster() did not sort the elements. \n\n");
			}
			
		}

		System.out.println("\n\nActual runs:");
		
		for (int i = 0; i < NUM_OF_TEST_DATA_SETS; i++) {
			
			
			System.out.print((i+1) *NUM_OF_RECORDS_PER_SET  );

			System.gc();  //run Java garbage collector 
			//run the slow sort 
			start = System.nanoTime();
			dataSets1[i].sort(SORT_KEY); 
			end = System.nanoTime();
			System.out.printf("\t%12d\t", end - start);
			if (! dataSets1[i].isSorted(SORT_KEY) ) {
				System.err.printf("\nMost recent call to sort() did not sort the elements. \n\n");
			}

			System.gc();  //run Java garbage collector 
			//run the faster sort 
			start = System.nanoTime();
			dataSets2[i].sortFaster(SORT_KEY); 
			end = System.nanoTime();
			System.out.printf("\t%12d\n", end - start);
			if (! dataSets2[i].isSorted(SORT_KEY) ) {
				System.err.printf("\nMost recent call to sortFaster() did not sort the elements. \n\n");
			}
			
		}
		
		

	}
	
	
	
	
	
	
	
	

}
