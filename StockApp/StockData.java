
import javax.swing.*;

/**
 * The StockData class uses the URLReader to get the data from yahoo
 * and then processes the data checking if the stock symbol is valid
 * and then analysing the data and getting the information needed and presenting the data
 * onto the outputScreen.
 * @author Jayenkumar Jaentilal
 */

public class StockData{
	/**
	 * Static method getData uses the URLReader to get the data and storing the information
	 * provided by yahoo in a array, then processes the data also using the static method outputData
	 * @param name
	 * @param outputScreen
	 * @param window
	 */
	public static void getData(String name,JTextPane outputScreen,JFrame window)
	{
		String stockName=name;
		stockName=stockName.replaceAll(" ","+");
		//count the number of '+' occurrences to find the number of company user has asked for
		int numberOfCompanies=1;
		if(stockName.contains("+"))
		{//check for + up to the second last char, last char being a + will not make a difference
			for(int i=0;i<stockName.length()-1;i++)
			{
				if(stockName.charAt(i)=='+'){numberOfCompanies++;}
			}
		}
		
		String[] data;
		
		/*Try getting the data from yahoo however if there is a connection problem
		 * a NullPointerException is thrown as there will be no data in the array.
		 * The user is notified with a message dialog that there is a problem with the connection.
		 */
		try
		{
			String url="http://finance.yahoo.com/d/quotes.csv?s="+stockName+"&f=snl1pdj1xe1";
			String yahooData=URLReader.readURL(url);
			/*
			 * replace the \n with a , so split can be applied and 
			 * stock name wont be left with the error indication part of the array
			*/
			yahooData=yahooData.replaceAll("\n",",");
			
			int lastQuoteIndex=0;//used to store the index of the last " found
			int nextQuoteIndex=0;//used to store the index of the next " found after lastQuoteIndex
			/*
			 *(alt 0167) § used to replace , in the strings so that the split can be used
			 *without any problems on stocks such as CAJ which has a , in its name.
			 */
			String specialChar="§";
			
			for(int i=0;i<yahooData.length();i++)
			{
				if(yahooData.charAt(i)=='"')//find the quote index
				{
					lastQuoteIndex=i;
					
					for(int j=lastQuoteIndex+1;j<yahooData.length();j++)//find the next code index after lastQuoteIndex
					{
						if(yahooData.charAt(j)=='"')
						{
							nextQuoteIndex=j;
							//set i as j+1 as once the next quotation mark is found there is no reason to go through 
							//same part of the string again to find the quotation mark start searching after j+1 instead
							i=j+1;
							
							//create a string which stores the text between the last and the next quote
							String oneDataString=yahooData.substring(lastQuoteIndex,nextQuoteIndex+1);
							
							//if oneDataString contains a ","(comma) then replace with special char
							String replacedDataString=oneDataString.replaceAll(",",specialChar);
							
							//check if the string has been changed i.e if , has been replaced meaning they are not equal
							if(!(oneDataString.equals(replacedDataString)))
							{
								//replace the original string between the quote with the replacedDataString
								yahooData=yahooData.replaceAll(oneDataString, replacedDataString);
							}
							lastQuoteIndex=j;//set the last quote index found as the nextQuoteIndex
							break;//break the loop after finding and checking for one string between the quotes
						}
					}
				}
			}
			/*
			 * Stored in the array data at index 0=Stock Symbol, 1=Company Name, 2=last trading price, 3=previous price
			 * 4=dividend, 5=Market capitalisation 6=Stock market name 7=error indication
			 */
			data=yahooData.split(",");
			
			/* If the arraySize is more than 8 meaning there is more than 1 stock.
			 * This loop will go through the array yahooData which holds the data received from yahoo
			 * and the loop will split the yahooData into arrays for individual companies
			 * and process them by using the method outputData and outputs the data into the outputScreen
			 */
			int sizeOfCompanyData=8;//size needed for one company 
			int arrayStartIndex=0;
			if(data.length>8)
			{
				for(int i=1;i<=numberOfCompanies;i++)
				{
					String[] companyData= new String[8];
					System.arraycopy(data,arrayStartIndex,companyData,0,sizeOfCompanyData);
					arrayStartIndex=arrayStartIndex+sizeOfCompanyData;//change the arrayStartIndex to the next company data
					outputData(companyData,outputScreen,window);
				}
			}
			//only one 1 stock data requested so directly call outputData to process the yahooData
			else
			{
				outputData(data,outputScreen,window);
			}
		}
		catch(NullPointerException e)
		{
			JOptionPane.showMessageDialog(window,"cannot connect to Yahoo please check internet connection");
		}
	}//end of getData
	
	/**
	 * outputData method process the data for each company calculating the price change, %change
	 * applying the colours according to the change it also checks if the data received is for a 
	 * valid company if the stock symbol is wrong then an error will be shown 
	 * @param companyData
	 * @param outputScreen
	 * @param window
	 */
	public static void outputData(String companyData[],JTextPane outputScreen,JFrame window)
	{
		int arraySize=companyData.length;
		for(int i=0; i<arraySize; i++)//remove "" and also §.
		{
			companyData[i]=companyData[i].replaceAll("\"","");
			companyData[i]=companyData[i].replaceAll("§", ",");
		}
		//check the error indication part if it does not have N/A then it means there is an error
		//so then the user is notified
		if((companyData[arraySize-1].contains("N/A"))==false)
		{
			JOptionPane.showMessageDialog(window,"the symbol "+companyData[1]+" is not found.");
			return;//end the method as soon as the error of symbol not found occurs
		}
		
		String outputScreenText=outputScreen.getText();
		
		String currency="";
		if((companyData[6].equals("NasdaqNM"))||(companyData[6].equals("NYSE")))
		{
			currency="$";
		}
		else if((companyData[6].equals("Brussels")) ||(companyData[6].equals("Paris")))
		{
			currency="EUR";
		}
		else if(companyData[6].equals("HKSE"))
		{
			currency="HK$";
		}
		else if(companyData[6].equals("London"))
		{
			currency="pence";
		}
		else if(companyData[6].equals("SES"))
		{
			currency="S$";
		}
		/*
		 * try calculating the price, price change however some stocks may have the price not 
		 * available which would lead to a NumberFormatException when parsing.
		 */
		try
		{
			double currentPrice=(double)Math.round((Double.parseDouble(companyData[2]))*100000)/100000;
			double previousPrice=Double.parseDouble(companyData[3]);
			double priceChange=(double)Math.round((currentPrice-previousPrice)*10000)/10000;
			double percentChange=(double)Math.round((((currentPrice-previousPrice)/previousPrice)*100)*1000)/1000;
			String change=Double.toString(priceChange)+" "+"("+Double.toString(percentChange)+"%"+")";
		
			if(priceChange>0)
			{
				change="<span style=color:green><b>"+change+"</b></font></span>";
			}
			else if(priceChange<0)
			{
				change="<span style=color:red><b>"+change+"</b></font></span>";
			}
			
			companyData[0]="<b>"+companyData[1]+"</b>";
			companyData[1]="Price: "+Double.toString(currentPrice)+" "+currency;
			companyData[2]="Change: "+change;
			companyData[3]="Dividend: "+companyData[4];
			companyData[4]="Market Cap: "+companyData[5];
			companyData[5]="Stock Exchange: "+companyData[6];
			companyData[6]="<b>::::::::::::::::::::::::::::::::::</b>";
		}
		catch(NumberFormatException e)
		{
			String currentPrice="N/A";
			String change="N/A";
			
			companyData[0]="<b>"+companyData[1]+"</b>";
			companyData[1]="Price: "+currentPrice+" "+currency;
			companyData[2]="Change: "+change;
			companyData[3]="Divident: "+companyData[4];
			companyData[4]="Market Cap: "+companyData[5];
			companyData[5]="Stock Exchange: "+companyData[6];
			companyData[6]="<b>::::::::::::::::::::::::::::::::::</b>";
		}
	
		for(int i=0;i<7;i++)
		{
			outputScreenText=outputScreenText+companyData[i]+"<br></br>";
			//open up the body and the html tags so that more text can be added before closing the tags again.
			outputScreenText=outputScreenText.replaceAll("</body>","");
			outputScreenText=outputScreenText.replaceAll("</html>","");
			outputScreen.setContentType("text/html");
			outputScreen.setText(outputScreenText);
		}
	}//end of outputData method
}//end of StockData class

