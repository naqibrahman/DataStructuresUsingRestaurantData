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
 * 
 * @author Joanna Klukowska
 *
 */
public class RestaurantInfoTest {

	/**
	 * This is where the program starts. The output from this method 
	 * is written to an output file specified by args[2], and to output 
	 * files specified by individual commands listed in file args[1]. 
	 * The errors are written to the standard error stream. 
	 * 
	 * @param args an array of command line arguments,
	 * expected values are 
	 *   args[0] - name of the file containing
	 *   restaurant inspection data 
	 *   args[1] - name of the file containing commands 
	 *   to be executed 
	 *   args[2] - name of the output file 
	 */
	public static void main(String[] args) {
		if (args.length < 3) {
			System.err.println("Error: invalid usage\n"
					+ "RestaurantInfo dataFile commandFile outputFile\n");
			System.exit(0);
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

				if (mostRecentResults     == null)
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
						  || key.equalsIgnoreCase("CUISINE") || key.equalsIgnoreCase("SCORE")) {
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
						|| key.equalsIgnoreCase("CUISNE")
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
`	 * @param message specific error message 
	 */
	public static void printError(int line, String message) {
		System.err.printf("Error on line %d: \n\t%s\n", line, message);
	}

}
