import java.io.*;
import javax.swing.*;
import java.util.*;
public class Participants{
	public static void main(String[] args) throws IOException{
		int teams=0;
		System.out.print(teams=Participants(1));
		
		
	}
	/**participants() number of participant as input
	 * outputs the teams/players to text file "x_participants.txt" 'x' being the league number
	 * @param league This is the number of the league to which user is adding the participants.
	 */
	public static void Participants(int league) throws IOException{
		// input as league or number of teams
		String result="";
		String pattern="[0-9]{1,2}";
		int numTeams=0;
		
		ArrayList<ArrayList<String>> teams=new ArrayList<ArrayList<String>>();
		teams.add(new ArrayList<String>());
		teams.add(new ArrayList<String>());
		
		if(league<=0){
			result="Invalid league number.";
		}else{
		
			String filePath="DataFiles/" + league + "_participants.txt";
			File fName=new File(filePath);
					
			if(fname.exists()){
				result=""+league+"_participants.txt file already exists";
			}else{
				PrintWriter out=new PrintWriter(fName);
			
				String num=JOptionPane.showInputDialog(null,"Enter number of participants");
			
				if(!(num.matches(pattern))){
					result="Number of participants must be an integer between 2 and 99 inclusive.";
				}else{
					int i=0;
					String name="";
					numTeams=Integer.parseInt(num);
					if(numTeams < 2)
						result="Number of participants must be an integer between 2 and 99 inclusive.";
					for(i=0;i<numTeams;){
						name=JOptionPane.showInputDialog(null,"Enter participant "+(i + 1));
					
						if(name != null){
							if(!(teams.get(1).contains(name))){
								teams.get(1).add(name);
								teams.get(0).add(""+i);
								out.println(i+","+name);
								i++;
							}else{
								JOptionPane.showMessageDialog(null,"Participant name already exists - please re-try");
							}
						}else
						{
							JOptionPane.showMessageDialog(null, "You entered " + i " out of " + num + " participants.")
							break;
						}
					}
				
					if(numTeams%2!=0){
						out.print(i+","+"BYES");
					}
					result="Participants added to file";
					out.close();
				}
			}
		}else{
			result="Invalid League Number";
		}
		
		JOptionPane.showMessageDialog(null,result);
	}
}