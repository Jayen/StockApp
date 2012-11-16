
import javax.swing.*;
import java.awt.*;

/**
 * Class to create the Stock market app GUI
 * The Class creates the output screen ,input screen
 * and the keyboard
 * @author Jayenkumar Jaentilal
 */

public class AppGUI {
	
	private JFrame window;
	private JTextPane outputScreen;
	private JPanel mainBottomPanel;
	private JPanel inputPanel;
	private JTextField inputScreen;
	/**
	 * AppGUI constructor create the main window and the panels needed for the GUI
	 * and it also creates the input and the output fields.
	 */	
	public AppGUI()
	{
		window= new JFrame("Stock Market App");
		window.setLayout(new BorderLayout());
		window.setPreferredSize(new Dimension(975,400));
		window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		//output label and screen are directly added to the window
		JLabel output= new JLabel(" Output:");
		outputScreen= new JTextPane();
		outputScreen.setEditable(false);
		JScrollPane jspScroll= new JScrollPane(outputScreen);
		window.add(jspScroll,BorderLayout.CENTER);
		window.add(output,BorderLayout.WEST);
		
		/* ========================================KEYBOARD============================================================
		 * mainBottomPanel will hold the input text field and the keyboard
		 * mainBottomPanel will hold 5 panels to get 1 input row and 4 rows of the keyboard
		 */
		mainBottomPanel= new JPanel(new GridLayout(5,0,0,6));
		window.add(mainBottomPanel,BorderLayout.SOUTH);
		
		//Adds the input label and input Text field to the inputPanel which is added to the main bottom panel
		inputPanel= new JPanel();
		inputPanel.setLayout(new BoxLayout(inputPanel,BoxLayout.X_AXIS));
		inputScreen= new JTextField();
		inputScreen.setEditable(false);
		JLabel input= new JLabel(" Input:   ");
		inputPanel.add(input);
		inputPanel.add(inputScreen);
		mainBottomPanel.add(inputPanel);
		
		//empty invisible button is used to get a nice keyboard layout
		//allowing the button for 0 to start just after button Q in the first row		
		//Array stores the button labels "" refers to the empty invisible button
		
		String[] buttonLabels={"","0","1","2","3","4","5","6","7","8","9",
							"Q","W","E","R","T","Y","U","I","O","P","Del","","A","S","D","F","G",
							"H","J","K","L","Ret","","","Z","X","C","V","B","N","M",".","Space"};
		
		JPanel bottomPanel1= new JPanel(new GridLayout(0,12,4,0));//bottomPanel1 used to store the numbers 0-9
		JPanel bottomPanel2= new JPanel(new GridLayout(0,11,4,0));//bottomPanel2 holds the buttons from Q to Delete
		JPanel bottomPanel3= new JPanel(new GridLayout(0,12,4,0));//bottomPanel3 holds the buttons from A to Return
		JPanel bottomPanel4= new JPanel(new GridLayout(0,13,4,0));//bottomPanel4 holds the buttons from Z to Space
		
		//for loop creates the buttons and adds the buttons to the appropriate panels
		for(int i=0;i<buttonLabels.length;i++)
		{
			String buttonLabel=buttonLabels[i];
			if(i>=0 && i<=10)//buttons from 0 to 9 are added to the bottomPanel1
			{
				new Button(buttonLabel,bottomPanel1,window,inputScreen,outputScreen);
			}
			else if(i>=11 && i<=21)//buttons from Q to Del are added to the bottomPanel2
			{
				new Button(buttonLabel,bottomPanel2,window,inputScreen,outputScreen);
			}
			else if(i>=22 && i<=32)//buttons from A to Rey are added to the bottomPanel3
			{
				new Button(buttonLabel,bottomPanel3,window,inputScreen,outputScreen);
			}
			else if(i>=33 && i<buttonLabels.length)//buttons from Z to Space are added to the bottomPanel3
			{
				new Button(buttonLabel,bottomPanel4,window,inputScreen,outputScreen);
			}
		}
		mainBottomPanel.add(bottomPanel1);
		mainBottomPanel.add(bottomPanel2);
		mainBottomPanel.add(bottomPanel3);
		mainBottomPanel.add(bottomPanel4);
		//====================================END OF KEYBOARD==========================================================
		window.setVisible(true);
		window.pack();
	}//end of the AppGUI constructor
}//end of the AppGUI class
