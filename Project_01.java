import java.util.*;
import javax.swing.JOptionPane;
import java.io.*;

public class Project_01
{
	public static ArrayList<ArrayList<String>>  leagues;
	public static ArrayList<ArrayList<String>>  participants;
	public static ArrayList<ArrayList<Integer>> fixtures;	
	public static ArrayList<ArrayList<Integer>> results;
	public static int[][] leaderBoard;
	
	/**main() method calls the Login() method and if it doesn't return -1 it calls the Leagues() method.
	 */
	public static void main(String[] args) throws IOException
	{
		int idNumber;
		idNumber = Login();
		if(idNumber != -1)
			Leagues(idNumber);
	}
	
	/**Login() prompts user to input user name and password to login allowing three tries.
	 * @return Returns id number if logged in successfully otherwise returns -1.
	 */
	public static int Login() throws IOException
	{
		String fileElements[];
		String userName = "", userPassword = "";
		boolean found = false;
		int attempts = 1;
		int idNumber = -1;
		File aFile = new File("DataFiles/Admins.txt");

		ArrayList<ArrayList<String>> users = new ArrayList<ArrayList<String>>();
		users.add(new ArrayList<String>());
		users.add(new ArrayList<String>());
		users.add(new ArrayList<String>());
		
		if(!(aFile.exists()))
			JOptionPane.showMessageDialog(null,"Error: File \"DataFiles/Admins.txt\" not found.");
		else
		{
			Scanner in = new Scanner(aFile);
			while(in.hasNext())
			{
				fileElements = (in.nextLine()).split(",");
				for(int i = 0; i < fileElements.length; i++)
					(users.get(i)).add(fileElements[i]);
			}
			in.close();
			for(int i = 0; i < 3 && !found; i++)
			{
				userName = JOptionPane.showInputDialog(null, "Enter UserName:", "Attempt: " + attempts, 1);
				if(userName == null)
					break;
				userPassword = JOptionPane.showInputDialog(null,"Enter Password:", "Attempt: " + attempts, 1);
				attempts++;
				for(int Count = 0;Count < users.get(0).size() && !found; Count++)
				{
					if((users.get(1).get(Count)).equalsIgnoreCase(userName) && (users.get(2).get(Count)).equals(userPassword))
					{
						found = true;	
						idNumber = Integer.parseInt(users.get(0).get(Count));
					}
				}
					if(!found && attempts <= 3)
						JOptionPane.showMessageDialog(null,"Error: Wrong username or password");	
			}
			if(found)
				JOptionPane.showMessageDialog(null,"Welcome back!");
			else if(userName != null)
				JOptionPane.showMessageDialog(null,"No Entry! Too many incorrect attempts");
		}
		return idNumber;
	}
	
	/**Leagues() method is used to manage admin's leagues.
	 * @param idNumber This is the id number of the currently logged in admin.
	 */
	public static void Leagues(int idNumber) throws IOException
	{
		String input = "", newName, selection;
		String [] fileItem, options1;
		boolean found = false;
		int lastLeaguePos, leagueNumber;
		
		leagues = new ArrayList<ArrayList<String>>();
		leagues.add(new ArrayList<String>());
		leagues.add(new ArrayList<String>());
		leagues.add(new ArrayList<String>());
		
		File aFile = new File("DataFiles/Leagues.txt");
		if(!aFile.exists())
			JOptionPane.showMessageDialog(null, "Error: File \"DataFiles/Leagues.txt\" not found.");
		else
		{	
			if(idNumber == -1)
				JOptionPane.showMessageDialog(null, "Error: Invalid admin id number.");
			else
			{
				Scanner filereader = new Scanner(aFile);
				while(filereader.hasNext())
				{			
					fileItem = (filereader.nextLine()).split(",");
					leagues.get(0).add(fileItem[0]);
					leagues.get(1).add(fileItem[1]);
					leagues.get(2).add(fileItem[2]);
				}
				filereader.close();
					
				ArrayList<String> yourLeagues = new ArrayList<String>();
				yourLeagues.add("Add League");
				for(int i = 0; i < leagues.get(2).size();i++)
				{	
					if(Integer.parseInt(leagues.get(2).get(i)) == idNumber )
						yourLeagues.add(leagues.get(0).get(i) + ". " + leagues.get(1).get(i));
				}
				options1 = yourLeagues.toArray(new String[yourLeagues.size()]);
				input = (String) JOptionPane.showInputDialog(null, "Choose a league you want to access",
																	"Leagues", 1, null, options1, options1[0]);	
				while(input != null)
				{	
					if(input.equals("Add League"))
					{
						lastLeaguePos = -1;
						for(int i = 1; i <= leagues.get(0).size() && !found;i++)
						{							
							if(!(leagues.get(0).contains(Integer.toString(i))))
							{
								lastLeaguePos = i - 1;
								found = true;
							}
						}
						if(lastLeaguePos == -1)
							lastLeaguePos = leagues.get(0).size();
						
						newName = JOptionPane.showInputDialog(null, "Enter the name of your league");
						if(newName != null)
						{
							int position = leagues.get(1).indexOf(newName);
							if(position != -1 && Integer.parseInt(leagues.get(2).get(position)) == idNumber)
								JOptionPane.showMessageDialog(null, "You already manage a league with this name.");
							else
							{
								if(newName.length() != 0)
								{
									leagues.get(0).add(lastLeaguePos, Integer.toString((lastLeaguePos + 1)));
									leagues.get(1).add(lastLeaguePos, newName);
									leagues.get(2).add(lastLeaguePos, Integer.toString(idNumber));
									updateLeagueFile();
									yourLeagues.add(lastLeaguePos, (lastLeaguePos + 1) + ". " + newName);
									options1 = yourLeagues.toArray(new String[yourLeagues.size()]);
								}
								else
									JOptionPane.showMessageDialog(null, "League name is invalid.");
							}
						}
					}
					else
					{
						fileItem = input.split(". ");
						leagueNumber = Integer.parseInt(fileItem[0]);
						String[] options2 = {"Add teams", "Generate fixtures", "Add/Edit results", "Show leader board", "Delete league"};
						selection = (String) JOptionPane.showInputDialog(null, "You are currently accessing league: " + fileItem[1] + "\nWhat would you like to do?",
																				"League management", 1, null, options2, options2[0]);
						while(selection != null)
						{	
							switch(selection)
							{
								case "Add teams":
									Participants(leagueNumber); 
									break;
								case "Generate fixtures": 
									generateFixtures(leagueNumber, true); 
									break;
								case "Add/Edit results": 
									if(readFilesIntoArrayLists(leagueNumber) == true)
										updateResults(leagueNumber); 
									break;
								case "Show leader board": 
									if(readFilesIntoArrayLists(leagueNumber) == true)
									{
										createEmptyLeaderBoard();
										processResults();
										orderLeaderBoard();
										displayLeaderboard(leagueNumber);
									}
									break;
								case "Delete league": 
									deleteLeague(input);
									yourLeagues.remove(input);
									options1 = yourLeagues.toArray(new String[yourLeagues.size()]);
									selection = null;
									break;
								default: break;
							}
							if(selection != null)
								selection = (String) JOptionPane.showInputDialog(null, "You are currently accessing league: " + fileItem[1] + "\nWhat would you like to do?",
																						"League management", 1, null, options2, options2[0]);
						}
					}
					input = (String) JOptionPane.showInputDialog(null, "Choose a league you want to access",
																		"Leagues", 1, null, options1, options1[0]);
				}	
			}
		}
	}

	/**updateLeagueFile() method when called updates the Leagues.txt file to have the up to date list of leagues.
	 */
	public static void updateLeagueFile()throws IOException
	{	
		File aFile = new File("DataFiles/Leagues.txt");
		PrintWriter out = new PrintWriter(aFile);
		
		for(int i = 0; i < leagues.get(0).size(); i++)
		{	
			out.print(leagues.get(0).get(i) + ",");
			out.print(leagues.get(1).get(i) + ",");
			out.println(leagues.get(2).get(i));
		}
		out.close();
	}
	
	/**deleteLeague() method when called will delete a league and all the files associated with that league.
	 * @param input This is the league chosen to be deleted.
	 */
	public static void deleteLeague(String input)throws IOException
	{	
		String [] datafiles = {"_fixtures.txt", "_results.txt", "_leaderboard.txt", "_participants.txt"};
		String [] temp = input.split(". ");
		String fileName, message = "";
		File aFile;
		
		int leagueNumber = leagues.get(0).indexOf(temp[0]);
		leagues.get(0).remove(leagueNumber);
		leagues.get(1).remove(leagueNumber);
		leagues.get(2).remove(leagueNumber);
		
		updateLeagueFile();
		
		for(int i = 0; i < datafiles.length; i++)
		{	
			fileName = "DataFiles/" + temp[0] + datafiles[i];
			aFile = new File(fileName);
			if(!(aFile.exists()))
				message += "\"" + fileName + "\" does not exist.\n";
			else if(aFile.delete())
				message += "\"" + fileName + "\" was deleted.\n";
			else
				message += "Operation to delete \"" + fileName + "\" file failed.\nDelete the file manualy to prevent errors.\n";
		}
		JOptionPane.showMessageDialog(null, message);
	}
	
	/**participants() number of participant as input
	 * outputs the teams/players to text file "x_participants.txt" 'x' being the league number
	 * @param league This is the number of the league to which user is adding the participants.
	 */
	public static void Participants(int league) throws IOException
	{
		String result = "";
		boolean finished = false;
		int i = 0;
		
		ArrayList<ArrayList<String>> teams=new ArrayList<ArrayList<String>>();
		ArrayList<String> checkDuplicates = new ArrayList<String>();
		teams.add(new ArrayList<String>());
		teams.add(new ArrayList<String>());
		
		if(league <= 0)
			JOptionPane.showMessageDialog(null,"Error: Invalid league number.");
		else
		{
			String filePath="DataFiles/" + league + "_participants.txt";
			File fName = new File(filePath);
					
			if(fName.exists() && fName.length() != 0)
				JOptionPane.showMessageDialog(null, league + "Error: File \"" + filePath + "\" already exists, if you want to change participants delete this file.");
			else
			{	
				JOptionPane.showMessageDialog(null, "Enter the names for participants, you can have at least 2 and at most 99 participants.");
				String name = "";
				while(!finished)
				{
					name = JOptionPane.showInputDialog(null, "Enter name of participant " + (i + 1) + " or press cancel to finish.");
					
					if(name != null && name.length() != 0)
					{
						if(!(checkDuplicates.contains(name.toLowerCase())))
						{
							checkDuplicates.add(name.toLowerCase());
							teams.get(1).add(name);
							teams.get(0).add(""+(i+1));
							i++;
						}
						else
							JOptionPane.showMessageDialog(null, "Participant name already exists - please re-try");
					}
					else
					{
						JOptionPane.showMessageDialog(null, "You entered " + i + " participants.");
						finished = true;
					}
					if(i >= 99)
						finished = true;
				}
			result = "Participants added to file";
			if(i < 2)
				result = "Insufficient amount of participants, file not created.";							
			else
			{
				PrintWriter out=new PrintWriter(fName);
				for(int j = 0; j < i; j++)
					out.println(teams.get(0).get(j) + "," + teams.get(1).get(j));
				if(i%2!=0)
					out.print((i+1) + "," + "BYES");
				out.close();
			}
			JOptionPane.showMessageDialog(null,result);	
			}					
		}
	}
	
	/**generateFixtures() generates fixtures and saves fixtures to a file "x_fixtures.txt", 'x' being the league number.
	 * @param leagueNum League number for which the fixtures are generated.
	 * @param needToMirror If fixtures need to be mirrored.
	 */
	public static void generateFixtures(int leagueNum, boolean needToMirror) throws IOException
	{
		int numOfParticipants, numOfRounds, numOfMatchesPerRound, fixturesNum;
		int roundNum, matchNum, homeTeamNum, awayTeamNum, even, odd;
		String fixtureAsTxt;
		String filePath = "DataFiles/" +  leagueNum + "_fixtures.txt";
		File aFile = new File(filePath);
		String[][] fixtures;
		String[][] finalFixtures;
		
		numOfParticipants = getNumOfParticipants(leagueNum); // Gets number of participants, which also validates if the required participants.txt file exists.
		if(numOfParticipants > 1)
		{ 
			PrintWriter out = new PrintWriter(aFile);
			numOfRounds			 = numOfParticipants - 1;
			numOfMatchesPerRound = numOfParticipants / 2;
			fixtures = new String[numOfRounds][numOfMatchesPerRound];
			fixturesNum = 1;
			
			for(roundNum = 0; roundNum < numOfRounds; roundNum++)
			{
				for(matchNum = 0; matchNum < numOfMatchesPerRound; matchNum++)
				{
					homeTeamNum = (roundNum + matchNum) % (numOfParticipants - 1);
					awayTeamNum = (numOfParticipants - 1 - matchNum + roundNum) % (numOfParticipants - 1);
					if(matchNum == 0)
						awayTeamNum = numOfParticipants - 1;
					fixtures[roundNum][matchNum] = (homeTeamNum + 1) + "," + (awayTeamNum + 1);
				}
			}
			finalFixtures = new String[numOfRounds][numOfMatchesPerRound];
			even = 0;
			odd = numOfParticipants / 2;
			for(int i = 0; i < fixtures.length; i++)
			{
				if(i % 2 == 0)
					finalFixtures[i] = fixtures[even++];
				else
					finalFixtures[i] = fixtures[odd++];
			}
			fixtures = finalFixtures;
			
			for(roundNum = 0; roundNum < fixtures.length; roundNum++)
			{
				if(roundNum % 2 == 1)
				{
					fixtureAsTxt = fixtures[roundNum][0];
					fixtures[roundNum][0] = mirrorFixture(fixtureAsTxt);
				}
			}
			for(roundNum = 0; roundNum < numOfRounds; roundNum++)
			{
				for(matchNum = 0; matchNum < numOfMatchesPerRound; matchNum++)
				{
					out.println(fixturesNum + "," + fixtures[roundNum][matchNum]);
					fixturesNum++;
				}
			}
			if(needToMirror)
			{
				while(roundNum < (numOfRounds * 2))
				{
					for(matchNum = 0; matchNum < numOfMatchesPerRound; matchNum++)
					{
						out.println(fixturesNum + "," + mirrorFixture(fixtures[roundNum - numOfRounds][matchNum]));
						fixturesNum++;
					}
					roundNum++;
				}
			}
			JOptionPane.showMessageDialog(null, "Fixtures generated successfully.");
			out.close();
		}			
	}
	
	/**mirrorFixture() reverses a string.
	 * @param s A string to be reversed.
	 * @return Returns the reverse of s.
	 */
	public static String mirrorFixture(String s)
	{
		String[] temp;
		String reversed = "";
		temp = s.split(",");
		reversed += temp[1] + "," + temp[0];
		return reversed;
	}
	
	/**getNumOfParticipants() is used to read the x_participants.txt file to get the number of participants.
	 * @param league League number for which to get number of participants.
	 * @return Returns the number of participants in the given league.
	 */
	public static int getNumOfParticipants(int league) throws IOException
	{
		String fileName = "DataFiles/" + league + "_participants.txt";
		int counter = 0;
		File aFile = new File(fileName);
		if(aFile.exists())
		{
			Scanner in = new Scanner(aFile);
			while(in.hasNext())
			{
				in.nextLine();
				counter++;
			}
			in.close();
			if(counter < 2)
				JOptionPane.showMessageDialog(null, "Insufficient number of participants.");
		}
		else
		{
			JOptionPane.showMessageDialog(null, "Error: File \"" + fileName + "\" not found, fixtures cannot be generated.");
			counter = -1;
		}
		return counter;
	}
	
	/**updateResults() is used to change or add results to fixtures
	 * by calling the getResult() method to validate input,
	 * and then saves the results to "x_results.txt" file, 'x' being the league number.
	 * @param leagueNum League number for which you are inputing results. 
	 */
	public static void updateResults(int leagueNum) throws IOException
	{
		String filePath = "DataFiles/" + leagueNum + "_fixtures.txt";
		File aFile = new File(filePath);
		if(!aFile.exists() || aFile.length() == 0)
			JOptionPane.showMessageDialog(null, "Fixtures not yet generated, generate fixtures to edit results.");
		else
		{
			filePath = "DataFiles/" + leagueNum + "_results.txt";
			String homeTeamName, awayTeamName;
			int fixtureNum, position;
			
			aFile = new File(filePath);
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
			String choice = (String)JOptionPane.showInputDialog(null, "Choose fixture to edit(BYES are ignored):\nfixtureNumber, homeTeamName, awayTeamName\n",
																		"Results", 1, null, options, options[0]);
			
			while(choice != null && !(choice.equals("Finished")))
			{
				resultElements = choice.split(", ");
				fixtureNum = Integer.parseInt(resultElements[0]);
				if(results.get(0).contains(fixtureNum))
				{
					position = results.get(0).indexOf(fixtureNum);
					results.get(1).set(position, getResult("home: " + resultElements[1]));
					results.get(2).set(position, getResult("away: " + resultElements[2]));
				}
				else
				{
					results.get(0).add(fixtureNum);
					results.get(1).add(getResult("home: " + resultElements[1]));
					results.get(2).add(getResult("away: " + resultElements[2]));
				}
				choice = (String)JOptionPane.showInputDialog(null, "Choose fixture to edit(BYES are ignored):\nfixturesNumber, homeTeamName, awayTeamName\n",
																	"Results", 1, null, options, options[0]);
			}
			for(int i = 0; i < results.get(0).size(); i++)
				out.println(results.get(0).get(i) + "," + results.get(1).get(i) + "," + results.get(2).get(i));
			out.close();
		}
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
		{
			JOptionPane.showMessageDialog(null, "The results or fixtures files are missing.");
			return false;
		}
	}
	
	/**getResult() gets and validates the input for the score from the user.
	 * @param team Used to specify if it is home team or away team.
	 * @return Returns the score input as an integer or returns 0 if user clicks cancel.
	 */
	public static int getResult(String team)
	{
		String pattern = "[0-9]{1,4}";
		boolean valid = false;
		String input = " ";
		int result = 0;
		
		while(input != null && !valid)
		{
			input = JOptionPane.showInputDialog(null, "Enter score for " + team + ".\n(Pressing cancel will default the score to 0)");
			if(input != null)
			{
				if(input.matches(pattern))
				{
					result = Integer.parseInt(input);
					valid = true;
				}
				else
					JOptionPane.showMessageDialog(null, "Incorrect input, score has to be an integer with maximum 4 digits.");
			}
		}
		return result;
	}
	
	/**createEmptyLeaderBoard() initializes a 2D integer array to store the leader board
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
				System.out.println("\"Scoring.txt\" is empty.");
			in.close();
		}
		else
			System.out.println("Error: File \"DataFiles/Scoring.txt\" not found.");
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
		leaderBoard[hTN-1][2]  += w;      			// homeWin
		leaderBoard[hTN-1][3]  += d;      			// homeDraw
		leaderBoard[hTN-1][4]  += l;      			// homeLoss
		leaderBoard[hTN-1][5]  += hTS;    			// homeTeamScore
		leaderBoard[hTN-1][6]  += aTS;    			// awayTeamScore
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
		leaderBoard[aTN-1][7]  += w;      			// awayWin
		leaderBoard[aTN-1][8]  += d;      			// awayDraw
		leaderBoard[aTN-1][9]  += l;      			// awayLoss
		leaderBoard[aTN-1][10] += aTS;    			// awayTeamScore
		leaderBoard[aTN-1][11] += hTS;    			// homeTeamScore
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
		JOptionPane.showMessageDialog(null, "Leader board generated successfully.");
		out.close();
	}  	
}