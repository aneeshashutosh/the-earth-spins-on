import java.awt.Font;
import java.io.InputStream;

import javax.swing.JOptionPane;

/*
 * @author Aneesh Ashutosh
 * @date 11/20/14
 * 
 * A container class that houses a number of constants and utility methods.
 */
public class Globals
{
	public static final String APPNAME = "The Earth Spins On";
	public static final String TAGLINE = "Showing you a bit more of your world.";

	//API Keys
	public static final String FOURSQUARE_CLIENT_ID = "5DKL5XT01QYXOYKNMBUTKBMLPLBCGZWENST1XXG01IS2TWKR";
	public static final String FOURSQUARE_CLIENT_SECRET = "SO04TJ32C4GH2BZ0AIN0V2NVHRKFV11NJFOM3ZOEUQCESO1L";
	public static String FOURSQUARE_AUTH_TOKEN = "5YPUX323D3SSINNPMBCGYIJBPRHNPKXG2RQR0OGQKKJ1VIO3";
	public static final String FOURSQUARE_VERSION = "&v=20141119";

	public static final String INSTAGRAM_CLIENT_ID = "f3fc36d4ffef43b5890638905eff3f2e";
	public static final String INSTAGRAM_CLIENT_SECRET = "86db555b15554908a399fbd50962d4e0";

	//Important strings for parsing
	public static final String RESPONSE = "response";
	public static final String MINIVENUES = "minivenues";
	public static final String ID = "id";
	public static final String NAME = "name";

	//Error messages
	public static final String ERROR_INTERNET_OFF = "Your internet connection is turned off. Please turn\nit back on and connect to a network to continue\nusing " + Globals.APPNAME + ".";
	public static final String ERROR_FOURSQUARE_OFF = "Unable to connect to Foursquare.";
	public static final String ERROR_INSTAGRAM_OFF = "Unable to connect to Instagram.";

	public static final String[] LOADING_MESSAGES = new String[]
			{
		"Checking Internet connection",
		"Checking external APIs (1/2)",
		"Checking external APIs (2/2)"
			};

	public static final int IMAGE_SIZE = 640;

	//Loads up a packaged font (Bebas Neue) at the given weight and font size.
	public static Font getFont(int weight, float fontSize)
	{
		Font font = null;
		String fName = "/res/";
		switch (weight)
		{
			case 0:
				fName += "BebasNeue Light.ttf";
				break;
			case 1:
				fName += "BebasNeue Regular.ttf";
				break;
			case 2:
				fName += "BebasNeue Bold.ttf";
				break;
		}
		try
		{
			InputStream is = Globals.class.getResourceAsStream(fName);
			font = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(fontSize);
		}
		catch (Exception ex)
		{
			font = new Font("serif", Font.PLAIN, 40);
		}
		return font;
	}

	//Displays an error message	
	public static void infoBox(String infoMessage, String titleBar)
	{
		JOptionPane.showMessageDialog(null, infoMessage, titleBar, JOptionPane.WARNING_MESSAGE);
	}
}
