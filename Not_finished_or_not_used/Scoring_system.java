import java.util.*;
import javax.swing.JOptionPane;
import java.io.*;

public class Scoring_system
{
	public static void main(String[] args)
	{
		int i;
		i = scoringType();
		JOptionPane.showMessageDialog(null, i);
	}
/**
*	method() Method chooses scoring type
*	@returns returns an int for scoring type
*/
	public static int scoringType()
	{
		String message = "Please input what kind of scoring you want for your league:"
					   + "\n1 - Custom."
					   + "\n2 - High default(highest score wins, 3 points for win, 1 for draw, 0 for lose)." 
					   + "\n3 - Low default(lowest score wins, 3 points for win, 1 for draw, 0 for lose)."
					   + "\nIf you press cancel or exit the program the scoring will default to option 2";
		String pattern = "[1-3]{1}";
		String input;
		boolean stop = false;
		int type = 2;
		while(!stop)
		{
			input = JOptionPane.showInputDialog(null, message);
			if(input == null)
				stop = true;
			else if(input.matches(pattern))
			{
				type = Integer.parseInt(input);
				stop = true;
			}
		}
		return type;
	}
}