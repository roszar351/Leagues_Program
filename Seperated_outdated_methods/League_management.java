import java.io.*;
import javax.swing.JOptionPane;
import java.util.*;

public class League_management{
	
	public static ArrayList<ArrayList<String>> leagues = new ArrayList<ArrayList<String>>();
	
	public static void main(String [] args) throws IOException{
	
		Leagues(1);
	}
	
	/**Leagues() method is used to manage admin's leagues.
	 * @param idNumber This is the id number of the currently logged in admin.
	 */
	public static void Leagues(int idNumber) throws IOException{
		
		String input = "", newName, selectedLeague, selection, pattern = "[0-9]{1}";
		int inputInt = 0, lastLeaguePos = -1, leagueNumber;
		leagues.add(new ArrayList<String>());
		leagues.add(new ArrayList<String>());
		leagues.add(new ArrayList<String>());
		String [] fileItem, options1, options2;
		boolean found = false;
		
		File aFile = new File ("DataFiles/Leagues.txt");
		if(!aFile.exists())
			JOptionPane.showMessageDialog(null, "File called \"leagues\" does not exist");
		else{
			
			if(idNumber == -1)
				JOptionPane.showMessageDialog(null, "You have no access to the league");
			else{
				
				Scanner filereader = new Scanner(aFile);
				while(filereader.hasNext()){
							
					fileItem = (filereader.nextLine()).split(",");
					leagues.get(0).add(fileItem[0]);
					leagues.get(1).add(fileItem[1]);
					leagues.get(2).add(fileItem[2]);
				}
				filereader.close();
					
				ArrayList<String> yourLeagues = new ArrayList<String>;
				yourLeagues.add("Add League")
				for( int i = 0; i < leagues.get(2).size();i++){
					
					if(Integer.parseInt(leagues.get(2).get(i)) == idNumber )
						yourLeagues.add(leagues.get(0).get(i) + ". " + leagues.get(1).get(i));
				}
				options1 = yourLeagues.toArray(new String[yourLeagues.size()]);
				input = (String) JOptionPane.showInputDialog(null, "Choose a league you want to access",
																	"Input",1,null,options1,options1[0]);	
				while(input != null){
					
					if( input.equals("Add League"){
					
						for( int i = 1; i < leagues.get(0).size() && !found;i++){
							
							if(!(leagues.get(0).contains(i))){
								lastLeaguePos = i - 1;
								found = true;
							}
						}
						if( lastLeaguePos == -1)
							lastLeaguePos = leagues.get(0).size();										//check this
						
						
						newName = JOptionPane.showInputDialog(null, "Enter the name of your league");
						if(newName != null && newName.length() != 0){
							leagues.get(0).add(Integer.toString(lastLeaguePos + 1),lastLeaguePos);
							leagues.get(1).add(newName,lastLeaguePos);
							leagues.get(2).add(Integer.toString(idNumber),lastLeaguePos);
							updateLeagueFile();
						}else
							JOptionPane.showMessageDialog(null, "Invalid league name.");
						
					}else{
						options2 = {"Add teams", "Generate fixtures", "Add/Edit results", "Show leader board", "Delete league"};
						selection = (String) JOptionPane.showInputDialog(null, "You are currently accessing league: " + input + "\nWhat would you like to do?",
																			"Input",1,null,options2,options2[0]);
						while(selection != null){
							
							switch (selection){
								case "Add teams": JOptionPane.showMessageDialog(null, "we are adding teams now, please hold Kappa"); break;
								case "Generate fixtures": JOptionPane.showMessageDialog(null, "we are generating fixtures LUL"); break;
								case "Add/Edit results": JOptionPane.showMessageDialog(null, "you can add or edit results now, PogChamp"); break;
								case "Show leader board": JOptionPane.showMessageDialog(null, "we are showing leader board KappaPride"); break;
								case "Delete league": deleteLeague();
							}
						}
					}
					input = (String) JOptionPane.showInputDialog(null, "Choose a league you want to access",
																		"Input",1,null,options1,options1[0]);
				}	
			}
		}
	}

	/**updateLeagueFile() method when called updates the Leagues.txt file to have the up to date list of leagues.
	 */
	public static void updateLeagueFile()throws IOException{
		
		File aFile = new File ("DataFiles/Leagues.txt");
		PrintWriter out = new PrintWriter(aFile);
		
		for(int i = 0; i < leagues.get(0).size(); i++){
			
			out.print(leagues.get(0).get(i) + ",");
			out.print(leagues.get(1).get(i) + ",");
			out.println(leagues.get(2).get(i));
		}
		
	}
	
	/**deleteLeague() method when called will delete a league and all the files associated with that league.
	 * @param input This is the league chosen to be deleted.
	 */
	public static void deleteLeague(String input)throws IOException{
		
		String [] temp = input.split(". ");
		
		leagueNumber = leagues.get(0).indexOf(temp[0]);
		leagues.get(0).remove(leagueNumber);
		leagues.get(1).remove(leagueNumber);
		leagues.get(2).remove(leagueNumber);
		updateLeagueFile();
			
		String fileName;
		File aFile;
		String [] datafiles = {"_fixtures.txt", "_results.txt", "_leaderboard.txt", "_participants.txt"}
		for(int i = 0; i < datafiles.length; i++){
			
			fileName = "DataFiles/" + temp[0] + datafiles[i];
			aFile = new File(fileName);
			if(!(aFile.exists()))
				JOptionPane.showMessageDialog(null, aFile.getName() + " does not exist.");
			else if(aFile.delete())
				JOptionPane.showMessageDialog(null, temp[0] + datafiles[i] + " was deleted.");
			else
				JOptionPane.showMessageDialog(null,"Operation to delete file failed.");
		}
	}
}