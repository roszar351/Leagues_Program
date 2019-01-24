import javax.swing.JOptionPane;
import java.util.*;
import java.io.*;

/**Generates and saves fixtures to a file.
 */
public class Fixtures
{
	public static void main(String[] args) throws IOException
	{
		generateFixtures(2, true);
	}
	/**generateFixtures() generates fixtures and saves fixtures to a file "x_fixtures.txt", 'x' being the league number.
	 * @param leagueNum League number for which the fixtures are generated.
	 * @param needToMirror If fixtures need to be mirrored.
	 */
	public static void generateFixtures(int leagueNum, boolean needToMirror) throws IOException
	{
		int numOfParticipants, numOfRounds, numOfMatchesPerRound, fixturesNum;
		int roundNum, matchNum, homeTeamNum, awayTeamNum, even, odd;
		boolean extraTeam = false;
		String fixtureAsTxt;
		String filePath = "DataFiles/" +  leagueNum + "_fixtures.txt";
		File aFile = new File(filePath);
		PrintWriter out = new PrintWriter(aFile);
		String[][] fixtures;
		String[][] finalFixtures;
		
		numOfParticipants = getNumOfParticipants(leagueNum);
		if(numOfParticipants != -1)
		{
			if(numOfParticipants % 2 == 1)
			{
				numOfParticipants++;
				extraTeam = true;
			}
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
			if(extraTeam)
				System.out.print("\nSince you had " + (numOfParticipants - 1)
							   + " teams at the outset(uneven number), fixtures "
							   + "against participant number "
							   + numOfParticipants + " are byes.");	
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
	 * @return Returns the number of participants in the given league.
	 */
	public static int getNumOfParticipants(int league)
	{
		String fileName = "DataFiles/" + league + "_participants.txt";
		int counter = 0;
		File aFile = new File(fileName);
		Scanner in = new Scanner(aFile);
		if(aFile.exists())
		{
			while(in.hasNext())
				counter++;
		}
		else
		{
			JOptionPane.showMessageDialog(null, fileName + " doesn't exist, fixtures cannot be generated.")
			counter = -1;
		}
		in.close();
		return counter;
	}
}