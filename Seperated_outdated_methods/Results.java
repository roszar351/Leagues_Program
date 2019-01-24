import java.util.*;
import javax.swing.JOptionPane;
import java.io.*;

/**Used to edit the results,
 * and generates the leader board in the console and saving it to a file.
 */
public class Results
{
	public static ArrayList<ArrayList<String>>  participants;
	public static ArrayList<ArrayList<Integer>> fixtures;	
	public static ArrayList<ArrayList<Integer>> results;
	public static int[][] leaderBoard;
	
	/**main() calls all methods in this class to allow edition of results 
	 * saving them to a file and generating, displaying a leader board
	 * as well as saving it to a file.
	 */
	public static void main(String[] args) throws IOException
	{
		if(readFilesIntoArrayLists(2) == true)
		{
			updateResults(2);
			createEmptyLeaderBoard();
			processResults();
			orderLeaderBoard();
			displayLeaderboard(2);
		}
		else
			System.out.println("One or more files are missing.");
	}
	
	/**updateResults() is used to change or add results to fixtures
	 * by calling the getResult() method to validate input,
	 * and then saves the results to "x_results.txt" file, 'x' being the league number.
	 * @param leagueNum League number for which you are inputing results. 
	 */
	public static void updateResults(int leagueNum) throws IOException
	{
		String filePath = "DataFiles/" + leagueNum + "_results.txt";
		String homeTeamName, awayTeamName;
		int fixtureNum, position;
		
		File aFile = new File(filePath);
		PrintWriter out = new PrintWriter(aFile);
		
		ArrayList<String> temp = new ArrayList<>();
		temp.add("Finished");
		String[] resultElements;
		Object[] options;// = new Object[fixtures.get(0).size()];
		
		for(int i = 0; i < fixtures.get(0).size(); i++)
		{
			homeTeamName = participants.get(1).get(fixtures.get(1).get(i) - 1);
			awayTeamName = participants.get(1).get(fixtures.get(2).get(i) - 1);
			//options[i] =  ((i + 1) + "," + homeTeamName + "," + awayTeamName);
			if(!(homeTeamName.equals("BYES") || awayTeamName.equals("BYES")))
				temp.add((i + 1) + ", " + homeTeamName + ", " + awayTeamName);
		}
		
		options = temp.toArray(new String[temp.size()]);
		String choice = (String)JOptionPane.showInputDialog(null, "Choose fixture to edit(BYES are ignored):\nfixturesNum,homeTeamName,awayTeamName\n",
															"Results", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
		
		while(choice != null && !(choice.equals("Finished")))
		{
			resultElements = choice.split(", ");
			fixtureNum = Integer.parseInt(resultElements[0]);
			if(results.get(0).contains(fixtureNum))
			{
				position = results.get(0).indexOf(fixtureNum);
				results.get(1).set(position, getResult("home"));
				results.get(2).set(position, getResult("away"));
			}
			else
			{
				results.get(0).add(fixtureNum);
				results.get(1).add(getResult("home"));
				results.get(2).add(getResult("away"));
			}
			choice = (String)JOptionPane.showInputDialog(null, "Choose fixture to edit(byes are ignored):\nfixturesNum,homeTeamName,awayTeamName\n",
														 "Results", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
		}
		for(int i = 0; i < results.get(0).size(); i++)
			out.println(results.get(0).get(i) + "," + results.get(1).get(i) + "," + results.get(2).get(i));
		out.close();
	}
	
	/**readFilesIntoArrayLists() loads participants, fixtures and results files into memory
	 * as three different ArrayLists.
	 * @param leagueNum League number of the files to be loaded.
	 * @return Returns true if all files exist and are loaded.
	 * @return Returns false if participants or fixtures files don't exist.
	 */
	public static boolean readFilesIntoArrayLists(int leagueNum) throws IOException
	{
		String filename1 = "DataFiles/" + leagueNum + "_participants.txt";
		String filename2 = "DataFiles/" + leagueNum + "_fixtures.txt";
		String filename3 = "DataFiles/" + leagueNum + "_results.txt";
    
		String fileElements[];
		File inputFile1 = new File(filename1);
		File inputFile2 = new File(filename2);
		File inputFile3 = new File(filename3);
		
		participants = new ArrayList<ArrayList<String>>();
		participants.add(new ArrayList<String>());
		participants.add(new ArrayList<String>());
	  
		fixtures = new ArrayList<ArrayList<Integer>>();
		fixtures.add(new ArrayList<Integer>());
		fixtures.add(new ArrayList<Integer>());
		fixtures.add(new ArrayList<Integer>());
		
		results = new ArrayList<ArrayList<Integer>>();
		results.add(new ArrayList<Integer>());
		results.add(new ArrayList<Integer>());
		results.add(new ArrayList<Integer>());
		
		inputFile3.createNewFile(); // Creates new results file if it doesnt already exist
		
		if (inputFile1.exists() && inputFile2.exists() && inputFile3.exists())
		{
			Scanner in;
			in = new Scanner(inputFile1);
			while(in.hasNext())
			{
				fileElements = (in.nextLine()).split(",");
				participants.get(0).add(fileElements[0]);  
				participants.get(1).add(fileElements[1]);  
			} 
			in.close();
			in = new Scanner(inputFile2);
			while(in.hasNext())
			{
				fileElements = (in.nextLine()).split(",");
				fixtures.get(0).add(Integer.parseInt(fileElements[0]));  
				fixtures.get(1).add(Integer.parseInt(fileElements[1]));  
				fixtures.get(2).add(Integer.parseInt(fileElements[2]));  
			} 
			in.close();
			in = new Scanner(inputFile3);
			while(in.hasNext())
			{
				fileElements = (in.nextLine()).split(",");
				results.get(0).add(Integer.parseInt(fileElements[0]));  
				results.get(1).add(Integer.parseInt(fileElements[1]));  
				results.get(2).add(Integer.parseInt(fileElements[2]));  
			} 
			in.close();
			return true;
		}
		else
			return false;
	}
	
	/**getResult() gets and validates the input for the score from the user.
	 * @param team Used to specify if it is home team or away team.
	 * @return Returns the score input as an integer or returns 0 if user clicks cancel.
	 */
	public static int getResult(String team)
	{
		String pattern = "[0-9]{1,}";
		String input = JOptionPane.showInputDialog(null, "Enter result for " + team + ".\n(Pressing cancel will default result to 0)");
		int result = 0;
		
		while(input != null && !(input.matches(pattern)))
			input = JOptionPane.showInputDialog(null, "Enter result for " + team + ".\n(Pressing cancel will default result to 0)");
		if(input.matches(pattern))
			result = Integer.parseInt(input);
		return result;
	}
	
	/**createEmptyLeaderBoard initializes a 2D integer array to store the leader board
	 */
	public static void createEmptyLeaderBoard()
	{
		// find out the number of teams/players which will determine 
		// the number of rows  
		int rows = participants.get(0).size();
		int columns = 14;  
		leaderBoard = new int[rows][columns];
		// place team numbers in column 0 of leader board
		for(int i = 0; i < leaderBoard.length; i++)
			leaderBoard[i][0] = Integer.parseInt(participants.get(0).get(i));
	}

	/**processResults() scans the results to see which participant won the fixture,
	 * and calls the recordFixtureResultForAwayTeam() and recordFixtureResultForHomeTeam() 
	 * to update the leader board values.
	 */
	public static void processResults() throws IOException
	{
		File aFile = new File("DataFiles/Scoring.txt");
		if(aFile.exists())
		{
			Scanner in = new Scanner(aFile);
			String lineFromFile;
			int fixtureNumber, homeTeamScore, awayTeamScore, homeTeamNumber, awayTeamNumber;
			int position, scoreForWin, scoreForDraw, scoreForLosing;
			if(in.hasNext())
			{
				String[] scoringElements;
				lineFromFile = in.nextLine();
				scoringElements = lineFromFile.split(",");
				scoreForWin    = Integer.parseInt(scoringElements[0]);
				scoreForDraw   = Integer.parseInt(scoringElements[1]);
				scoreForLosing = Integer.parseInt(scoringElements[2]);
				for(int i = 0; i < results.get(0).size(); i++)  
				{
					fixtureNumber  = results.get(0).get(i);
					homeTeamScore  = results.get(1).get(i);
					awayTeamScore  = results.get(2).get(i);
					position       = fixtures.get(0).indexOf(fixtureNumber);
					homeTeamNumber = fixtures.get(1).get(position);
					awayTeamNumber = fixtures.get(2).get(position);
					if(homeTeamScore == awayTeamScore)
					{
						recordFixtureResultForHomeTeam(homeTeamNumber, 0, 1, 0, homeTeamScore, awayTeamScore, scoreForDraw);
						recordFixtureResultForAwayTeam(awayTeamNumber, 0, 1, 0, homeTeamScore, awayTeamScore, scoreForDraw);
					}  
					else if(homeTeamScore > awayTeamScore)
					{
						recordFixtureResultForHomeTeam(homeTeamNumber, 1, 0, 0, homeTeamScore, awayTeamScore, scoreForWin);
						recordFixtureResultForAwayTeam(awayTeamNumber, 0, 0, 1, homeTeamScore, awayTeamScore, scoreForLosing);  
					}  
					else
					{
						recordFixtureResultForHomeTeam(homeTeamNumber, 0, 0, 1, homeTeamScore, awayTeamScore, scoreForLosing);
						recordFixtureResultForAwayTeam(awayTeamNumber, 1, 0, 0, homeTeamScore, awayTeamScore, scoreForWin);  
					}    
				}
			}
			else
				System.out.println("Scoring.txt is empty.");
			in.close();
		}
		else
			System.out.println("Scoring.txt doesn't exist.");
	}	

	/**recordFixtureResultForHomeTeam() adds results to leader board for the home team.
	 * @param hTN Home team number.
	 * @param w Adds value to the win column.
	 * @param d Adds value to the draw column.
	 * @param l Adds value to the lose column.
	 * @param hTS Adds value to the home team score column.
	 * @param aTS Adds value to the away team score column.
	 * @param p Adds value to the score column.
	 */
	public static void recordFixtureResultForHomeTeam(int hTN, int w, int d, int l, 
                                                       int hTS, int aTS, int p)
	{
		leaderBoard[hTN-1][1]++;        			// gamesPlayed
		leaderBoard[hTN-1][2]+= w;      			// homeWin
		leaderBoard[hTN-1][3]+= d;      			// homeDraw
		leaderBoard[hTN-1][4]+= l;      			// homeLoss
		leaderBoard[hTN-1][5]+= hTS;    			// homeTeamScore
		leaderBoard[hTN-1][6]+= aTS;    			// awayTeamScore
		leaderBoard[hTN-1][12] += (hTS - aTS);    	// goalDifference
		leaderBoard[hTN-1][13] += p;    			// points
	}
 
	/**recordFixtureResultForAwayTeam() adds results to leader board for the away team.
	 * @param aTN Away team number.
	 * @param w Adds value to the win column.
	 * @param d Adds value to the draw column.
	 * @param l Adds value to the lose column.
	 * @param hTS Adds value to the home team score column.
	 * @param aTS Adds value to the away team score column.
	 * @param p Adds value to the score column.
	 */
	public static void recordFixtureResultForAwayTeam(int aTN, int w, int d, int l, 
													   int hTS, int aTS, int p)
	{
		leaderBoard[aTN-1][1]++;        			// gamesPlayed
		leaderBoard[aTN-1][7]+= w;      			// awayWin
		leaderBoard[aTN-1][8]+= d;      			// awayDraw
		leaderBoard[aTN-1][9]+= l;      			// awayLoss
		leaderBoard[aTN-1][10]+= aTS;    			// awayTeamScore
		leaderBoard[aTN-1][11]+= hTS;    			// homeTeamScore
		leaderBoard[aTN-1][12] += (aTS - hTS);    	// goalDifference
		leaderBoard[aTN-1][13] += p;    			// points  
	}

	/**orderLeaderBoard() sorts the leader board in descending order of points.
	 */
	public static void orderLeaderBoard()
	{
		int [][] temp = new int[leaderBoard.length][leaderBoard[0].length];
		boolean finished = false;
		while(!finished) 
		{
			finished = true;
			for(int i = 0; i < leaderBoard.length - 1; i++) 
			{
				if(leaderBoard[i][13] < leaderBoard[i + 1][13])
				{
					for(int j = 0; j < leaderBoard[i].length; j++) 
					{
						temp[i][j]            = leaderBoard[i][j];
						leaderBoard[i][j]     = leaderBoard[i + 1][j];
						leaderBoard[i + 1][j] = temp[i][j];
					}
				finished = false;
				}
			}
		}
	}	

	/**displayLeaderboard() prints the leader board to default output
	 * and saves it to a "x_leaderboard.txt" file, 'x' being the league number.
	 * @param leagueNum League number for which to print and save the leader board.
	 */
	public static void displayLeaderboard(int leagueNum) throws IOException
	{
		String filePath = "DataFiles/" + leagueNum + "_leaderboard.txt";
		File aFile = new File(filePath);
		PrintWriter out = new PrintWriter(aFile);
		int aTeamNumber;
		String aTeamName, formatStringTeamName;
		String longestTeamName       = participants.get(1).get(0);
		int    longestTeamNameLength = longestTeamName.length();
		
		for(int i = 1; i < participants.get(1).size(); i++)
		{
			longestTeamName = participants.get(1).get(i);  
			if(longestTeamNameLength < longestTeamName.length())
				longestTeamNameLength = longestTeamName.length();
		}
		formatStringTeamName = "%-" + (longestTeamNameLength + 4) + "s";
		System.out.printf(formatStringTeamName,"Team Name");
		out.printf(formatStringTeamName,"Team Name");
		System.out.println("  GP  HW  HD  HL  GF  GA  AW  AD  AL  GF  GA   GD   TP");
		out.println("  GP  HW  HD  HL  GF  GA  AW  AD  AL  GF  GA   GD   TP");
	   
		for(int i = 0; i < leaderBoard.length; i++)
		{
			aTeamNumber       = leaderBoard[i][0];
			aTeamName         = participants.get(1).get(aTeamNumber - 1);
			// Display in default output.
			System.out.printf(formatStringTeamName, aTeamName);
			System.out.printf("%4d", leaderBoard[i][1]);
			System.out.printf("%4d", leaderBoard[i][2]);
			System.out.printf("%4d", leaderBoard[i][3]);
			System.out.printf("%4d", leaderBoard[i][4]);
			System.out.printf("%4d", leaderBoard[i][5]);
			System.out.printf("%4d", leaderBoard[i][6]);
			System.out.printf("%4d", leaderBoard[i][7]);
			System.out.printf("%4d", leaderBoard[i][8]);
			System.out.printf("%4d", leaderBoard[i][9]);
			System.out.printf("%4d", leaderBoard[i][10]);
			System.out.printf("%4d", leaderBoard[i][11]);
			System.out.printf("%5d", leaderBoard[i][12]);
			System.out.printf("%5d", leaderBoard[i][13]);
			System.out.println();
			// Save to file.
			out.printf(formatStringTeamName, aTeamName);
			out.printf("%4d", leaderBoard[i][1]);
			out.printf("%4d", leaderBoard[i][2]);
			out.printf("%4d", leaderBoard[i][3]);
			out.printf("%4d", leaderBoard[i][4]);
			out.printf("%4d", leaderBoard[i][5]);
			out.printf("%4d", leaderBoard[i][6]);
			out.printf("%4d", leaderBoard[i][7]);
			out.printf("%4d", leaderBoard[i][8]);
			out.printf("%4d", leaderBoard[i][9]);
			out.printf("%4d", leaderBoard[i][10]);
			out.printf("%4d", leaderBoard[i][11]);
			out.printf("%5d", leaderBoard[i][12]);
			out.printf("%5d", leaderBoard[i][13]);
			out.println();
		}
		out.close();
	}  	
}