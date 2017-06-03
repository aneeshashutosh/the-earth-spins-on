import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JTextField;

/*
 * @author Aneesh Ashutosh
 * @date 11/20/14
 * 
 * A searchbar that lets the user search for their location of choice.
 */

public class ChooseLocationView extends JFrame
{
	private static final long serialVersionUID = -6484725512111760162L;

	private JTextField area;
	private JTextField location;
	
	private TreeMap<String, String> sug;

	public ChooseLocationView()
	{
		super();
		
		//Formats the JFrame
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setPreferredSize(new Dimension(680, 60));
		this.setTitle(Globals.APPNAME);
		this.setResizable(false);
		this.setUndecorated(true);
		this.setShape(new RoundRectangle2D.Double(0, 0, 680, 60, 20, 20));
		this.setBackground(new Color(0xF1F1F1));

		this.getContentPane().removeAll();

		//Formats the searchbar
		this.location = new JTextField ("Search...");
		this.location.setBorder(null);
		this.location.setPreferredSize(new Dimension(440, 60));
		this.location.setBackground(new Color(0xF1F1F1));
		this.location.setForeground(new Color(0x6A6A6A));
		this.location.setFont(Globals.getFont(2, 40));
		this.location.setBorder(BorderFactory.createCompoundBorder(this.location.getBorder(), BorderFactory.createEmptyBorder(10, 10, 0, 0)));
		this.add(location, BorderLayout.WEST);

		//Formats the area search bar
		this.area = new JTextField("Andover, MA");
		this.area.setBorder(null);
		this.area.setPreferredSize(new Dimension(240, 60));
		this.area.setBackground(new Color(0xF1F1F1));
		this.area.setForeground(new Color(0x6A6A6A));
		this.area.setFont(Globals.getFont(2, 40));
		this.area.setBorder(BorderFactory.createCompoundBorder(this.location.getBorder(), BorderFactory.createEmptyBorder(0, 0, 0, 0)));
		this.add(area, BorderLayout.EAST);
		
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);

		location.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				ChooseLocationView.this.sug = getVenues();
				if (sug.isEmpty())
				{
					Globals.infoBox("That location is not available. Sorry :(", "Error");
				}
				else
				{
					//If there is more than one element, opens up a suggestion box so the user can choose what location they want
					if (sug.size() == 1)
					{
						InstagramView iView = new InstagramView(sug.get(sug.firstKey()));
					}
					else
					{
						new SuggestionsBox(sug);
					}
					ChooseLocationView.this.setVisible(false);
					ChooseLocationView.this.dispose();
				}
			}
		});
		
		area.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				ChooseLocationView.this.sug = getVenues();
				if (sug.isEmpty())
				{
					Globals.infoBox("That location is not available. Sorry :(", "Error");
				}
				else
				{
					//If there is more than one element, opens up a suggestion box so the user can choose what location they want
					if (sug.size() == 1)
					{
						InstagramView iView = new InstagramView(sug.get(sug.firstKey()));
					}
					else
					{
						new SuggestionsBox(sug);
					}
					ChooseLocationView.this.setVisible(false);
					ChooseLocationView.this.dispose();
				}
			}
		});
	}

	//Parses the response from the Foursquare API to get the names and IDs of all locations matching the user query
	public TreeMap<String, String> getVenues()
	{
		String url = ("https://api.foursquare.com/v2/venues/suggestCompletion?near=" + area.getText() + "&query="+ location.getText() + "&oauth_token=" + Globals.FOURSQUARE_AUTH_TOKEN + Globals.FOURSQUARE_VERSION).replaceAll(" ", "%20");
		APIHandler getSuggestions = new APIHandler();

		String suggestions = getSuggestions.get(url);
		TreeMap<String, String> namesAndIds = new TreeMap<String, String>(); 

		while (suggestions.contains("id"))
		{
			suggestions = suggestions.substring(suggestions.indexOf("\"id\":\"") + 6);
			String id = suggestions.substring(0, suggestions.indexOf("\","));
			suggestions = suggestions.substring(suggestions.indexOf("\",\"name\":\"") + 10);
			String name = suggestions.substring(0, suggestions.indexOf("\","));
			namesAndIds.put(name, id);
		}

		//TreeMap!
		TreeMap<String, String> tempMap = new TreeMap<String, String>(); 
		for (Map.Entry<String,String> entry : namesAndIds.entrySet())
		{
			String key = entry.getKey();
			String value = entry.getValue();
			String[] params = location.getText().split(" ");
			for (String s : params)
			{
				if (key.toLowerCase().contains(s.toLowerCase()) && !tempMap.containsKey(key))
				{
					tempMap.put(key, value);
				}
			}
		}
		return tempMap;
	}
	
	//Utility method to print a tree map. Used in testing.
	public void printTreeMap(TreeMap<String, String> map)
	{
		for(Map.Entry<String,String> entry : map.entrySet())
		{
			String key = entry.getKey();
			String value = entry.getValue();
			System.out.println(key + " => " + value);
		}
	}
}