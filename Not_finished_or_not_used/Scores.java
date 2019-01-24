import java.util.*;
import javax.swing.JOptionPane;
import java.io.*;

public class Scores
{
	public static void main(String[] args)
	{
		
	}
	
	public static void Scoring(int type)
	{
		String input;
		String pattern = "[12]{1}";
		int win = 3;
		int draw = 1;
		int lose = 0;
		int temp = 0;
		boolean high = true;
		if(type == 0)
		{
			input = JOptionPane.showInputDialog(null, "Please specify which score wins:\n1 - Highest score wins" 
													+ "\n2 - Lowest scores wins\nIf input provided is invalid or you press cancel option 1 will be chosen.");
			if(input.matches(pattern))
			{
				temp = Integer.parseInt(input);
				if(temp == 2)
					high = false;
			}
		}
		
			
	}
}