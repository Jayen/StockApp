
import java.awt.event.*;
import javax.swing.*;

/**
 * The Button class creates the JButton and the ActionListener
 * for each of the buttons unless its a invisible button.
 * Button class contains a nested class action which implements the
 * ActionListener
 * @author Jayenkumar Jaentilal
 */

public class Button {
	
	private JButton button;
	
	/**
	 * Button Constructor
	 * creates the JButton with button name and assign it to the panel
	 * adds the ActionListener for each button created which is not invisible
	 * @param name
	 * @param panel
	 * @param window
	 * @param text
	 * @param output
	 */
	
	public Button(String name,JPanel panel,JFrame window,JTextField text,JTextPane output)
	{
		if(name.equals(""))//if "" button text then set the button invisible and do not add any action listener
		{
			button=new JButton();
			button.setVisible(false);
			panel.add(button);
		}
		else
		{
			button=new JButton(name);
			button.addActionListener(new Action(window,text,output));
			panel.add(button);
		}
	}
	
	/**nested action class which implements the ActionListener*/
	private class Action implements ActionListener {
		
		private JFrame window;
		private JTextField text;
		private JTextPane output;
		/**
		 * Action constructor
		 * With parameters window, text as JTextField and the JTextPane output as some actions require to show 
		 * a message the window parameter is required, and almost all the button change the text
		 * in the input field hence the parameter of type JTextField is passed to which
		 * the action is applied to.
		 * @param window
		 * @param text
		 * @param ouput
		 */
		public Action(JFrame window,JTextField text,JTextPane output)
		{
			this.window=window;
			this.text=text;
			this.output=output;
		}
		public void actionPerformed(ActionEvent e)
		{
			if(((JButton)e.getSource()).getText().equals("Del"))
			{
				String currentText=text.getText();
			
				if(currentText.length()>0)
				{
					text.setText(currentText.substring(0,currentText.length()-1));
				}
				else
				{
					JOptionPane.showMessageDialog(window,"There is nothing to delete.");
				}
			}
			else if(((JButton)e.getSource()).getText().equals("Ret"))
			{
				String currentText=text.getText();
				//check first if there is any stock entered
				String onlySpaceCheck=text.getText().replaceAll(" ", "");
				if(onlySpaceCheck.length()==0)
				{
					JOptionPane.showMessageDialog(window,"No stock symbol entered");
					text.setText(currentText.substring(0,0));
				}
				else
				{
					StockData.getData(currentText,output,window);
					text.setText(currentText.substring(0,0));	
				}
			}
			else if(((JButton)e.getSource()).getText().equals("Space"))
			{
				//check the length of text as if empty then calculating lastChar will give exception
				if(text.getText().length()>0)
				{
					//find the last character entered
					char lastChar=text.getText().charAt((text.getText().length()-1));
					//limit the number of spaces between stocks to 1 as more than one are not needed
					if(lastChar==' ')
					{
					JOptionPane.showMessageDialog(window,"Only 1 space needed between stock symbols");
					}
					else
					{
						text.setText(text.getText()+" ");
					}
				}
				else
				{
					text.setText(text.getText()+" ");
				}
			}
			else
			{
				String currentText=text.getText();
				
				//check if the stock symbol entered is at most 8 letters if its more then give a message
				if(currentText.length()<8)
				{
					text.setText(text.getText()+button.getText());
				}
				else if((currentText.contains(" ")))
				{
					//nextStockSymbol is used to check if the second stock symbol is not more than 8 letters
					int spacePosition=0;
					for(int i=0;i<currentText.length();i++)
					{
						if(currentText.charAt(i)==' ') {spacePosition=i;}
					}
					
					String nextStockSymbol=text.getText().substring(spacePosition,text.getText().length());
					if(nextStockSymbol.length()<=8)
					{
						text.setText(text.getText()+button.getText());
					}
					else
					{
						JOptionPane.showMessageDialog(window,"One stock symbol is usually less than 8 letters");
					}
				}
				else
				{
					JOptionPane.showMessageDialog(window,"One stock symbol is usually less than 8 letters");
				}
			}
		}//end actionPerformed method
	}//end action class
}//end Button class

