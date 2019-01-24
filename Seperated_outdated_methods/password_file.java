import javax.swing.JOptionPane;
import java.io.*;
import java.util.*;
public class password_file
{
	public static void main(String[] args) throws IOException
	{
		System.out.println(Login());
	}

	/**Login() prompts user to input user name and password to login allowing three tries.
	 * @return Returns id number if logged in successfully otherwise returns -1.
	 */
	public static int Login() throws IOException
	{
		int idNumber = -1;
		File aFileReader = new File("DataFiles/Admins.txt");
		Scanner in = new Scanner(aFileReader);
		ArrayList<ArrayList<String>> users = new ArrayList<ArrayList<String>>();
		users.add(new ArrayList<String>());
		users.add(new ArrayList<String>());
		users.add(new ArrayList<String>());
		String userName = "",userPassword = "";
		boolean found = false;
		int attempts = 1;
		String fileElements[];
		if (!(aFileReader.exists()))
		{
			JOptionPane.showMessageDialog(null,"Error: File does not exist");
		}
		else
		{
			 while (in.hasNext())
			{
				fileElements = (in.nextLine()).split(",");
				for (int i = 0; i < fileElements.length; i++)
					(users.get(i)).add(fileElements[i]);
			}
			in.close();
			for(int i = 0;i < 3 && !found;i++)
			{
				userName = JOptionPane.showInputDialog(null,"Enter UserName:","Attempt: " + attempts,1);
				if(userName == null)
					{
						break;
					}
				userPassword = JOptionPane.showInputDialog(null,"Enter Password:","Attempt: " + attempts,1);
				attempts++;
				 for (int Count = 0;Count < users.get(0).size() && !found; Count++)
				 {
					 if ((users.get(1).get(Count)).equalsIgnoreCase(userName) && (users.get(2).get(Count)).equals(userPassword))
					{
						found = true;	
						idNumber = Integer.parseInt(users.get(0).get(Count));
					}
				 }
					if(!found && attempts <= 3)
					{
						JOptionPane.showMessageDialog(null,"Error: Wrong username or password");	
					}
			}
			if(found)
			{
				JOptionPane.showMessageDialog(null,"Welcome back!");
			}
			else
			{			
				JOptionPane.showMessageDialog(null,"No Entry! Too many incorrect attempts");
			}
		}
		return idNumber;
	}
}