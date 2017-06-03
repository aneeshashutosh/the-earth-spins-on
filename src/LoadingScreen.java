import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/*
 * @author Aneesh Ashutosh
 * @date 11/20/14
 * 
 * The loading screen. Checks to ensure that there is an internet connection
 * and that all APIs are reachable.
 */

public class LoadingScreen extends JFrame
{
	private static final long serialVersionUID = 3512993479481687969L;
	private JLabel loadingTextLabel;
	private Timer loadingTimer;
	private TimerTask changeEllipses;

	public LoadingScreen()
	{
		super();

		//Formats the loading screen
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setPreferredSize(new Dimension(300, 400));
		this.setTitle(Globals.APPNAME);
		this.setUndecorated(true);
		this.setResizable(false);
		this.getContentPane().setBackground(new Color(0x1AB0C7));

		this.getContentPane().removeAll();

		//Creates the loading GIF
		URL loadingGifUrl = LoadingScreen.class.getResource("/res/loading.gif");
		Icon loadingGifIcon = new ImageIcon(loadingGifUrl);
		JLabel loadingGifLabel = new JLabel(loadingGifIcon);

		this.add(loadingGifLabel, BorderLayout.NORTH);

		//Formats the loading text label
		this.loadingTextLabel = new JLabel("", SwingConstants.CENTER);
		this.loadingTextLabel.setForeground(Color.WHITE);
		this.loadingTextLabel.setFont(Globals.getFont(2, 24.0f));

		this.add(loadingTextLabel);

		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);

		try
		{
			this.checkConnection();
		}
		catch (UnknownHostException e)
		{
			Globals.infoBox("An unexpected error occurred.", "Error");
			System.exit(0);
		}
		ChooseLocationView chooseLocationView = new ChooseLocationView();
		this.setVisible(false);
		this.dispose();
	}

	//Checks all external APIs and internet connection
	public void checkConnection() throws UnknownHostException
	{
		this.changeMessage(Globals.LOADING_MESSAGES[0]);
		this.doEllipses(this.loadingTextLabel);
		
		try
		{
			Thread.sleep(3000);
		}
		catch (InterruptedException e1)
		{
			Globals.infoBox("An unexpected error occurred.", "Error");
			System.exit(0);
		}

		if ("127.0.0.1".equals(InetAddress.getLocalHost().getHostAddress().toString()))
		{
			Globals.infoBox(Globals.ERROR_INTERNET_OFF, "Error");
			System.exit(0);
		}

		this.changeMessage(Globals.LOADING_MESSAGES[1]);
		this.doEllipses(this.loadingTextLabel);

		try
		{
			Thread.sleep(2000);
		}
		catch (InterruptedException e1)
		{
			Globals.infoBox("An unexpected error occurred.", "Error");
			System.exit(0);
		}
		
		try
		{
			URL url=new URL("http://foursquare.com");
			URLConnection con=url.openConnection();
			con.getInputStream();
		}
		catch (Exception e)
		{
			Globals.infoBox(Globals.ERROR_FOURSQUARE_OFF, "Error");
			System.exit(0);
		}

		this.changeMessage(Globals.LOADING_MESSAGES[2]);
		this.doEllipses(this.loadingTextLabel);
		
		try
		{
			Thread.sleep(2000);
		}
		catch (InterruptedException e1)
		{
			Globals.infoBox("An unexpected error occurred.", "Error");
			System.exit(0);
		}
		
		try
		{
			URL url=new URL("http://instagram.com");
			URLConnection con=url.openConnection();
			con.getInputStream();
		}
		catch (Exception e)
		{
			Globals.infoBox(Globals.ERROR_INSTAGRAM_OFF, "Error");
			System.exit(0);
		}
	}

	//Changes the message of the loading text label
	public void changeMessage(String s)
	{
		this.loadingTextLabel.setText(s);
	}

	//Animates the ellipses at the end of each loading text label. Runs in a separate thread.
	public void doEllipses(JLabel loadingTextLabel)
	{
		if (changeEllipses != null)
		{
			changeEllipses.cancel();
		}
		changeEllipses = new TimerTask()
		{
			int numEllipses = 1;
			String originalMessage = loadingTextLabel.getText();

			public void run()
			{
				String s = "";
				for (int i = 0; i < numEllipses; i++)
				{
					s += ".";
				}
				loadingTextLabel.setText(originalMessage + s);
				loadingTextLabel.invalidate();
				if (numEllipses < 3)
				{
					numEllipses++;
				}
				else
				{
					numEllipses = 1;
				}
			}  
		};
		this.loadingTimer = new Timer();
		this.loadingTimer.schedule(changeEllipses, 0, 250);
	}
}
