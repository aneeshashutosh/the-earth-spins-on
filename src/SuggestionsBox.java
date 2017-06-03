import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

/*
 * @author Aneesh Ashutosh
 * @date 11/20/14
 * 
 * A view that displays all options after the user's search.
 */


public class SuggestionsBox extends JFrame
{
	private static final long serialVersionUID = 5682996065010658288L;
	private TreeMap<String, String> map;

	public SuggestionsBox(TreeMap<String, String> map)
	{
		super();
		this.map = map;
		
		//Decorating the JFrame
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setPreferredSize(new Dimension(680,60));
		this.setTitle(Globals.APPNAME);
		this.setResizable(false);
		this.setUndecorated(true);
		this.setShape(new RoundRectangle2D.Double(0, 0, 680, 60, 20, 20));
		this.setBackground(new Color(0xF1F1F1));

		this.getContentPane().removeAll();

		//The container that will be used in the JScrollPane
		JPanel listContainer = new JPanel();
		listContainer.setBorder(null);
		BoxLayout boxLayout1 = new BoxLayout(listContainer, BoxLayout.Y_AXIS);
		listContainer.setLayout(boxLayout1);
		listContainer.add(Box.createVerticalGlue());
		
		Set<String> set = this.map.keySet();
		
		//Add each element to the container
		for (String s : set)
		{
			JButton listView = new JButton(s);
			listView.setBorder(null);
			listView.setPreferredSize(new Dimension(640, 60));
			listView.setBackground(new Color(0xF1F1F1));
			listView.setForeground(new Color(0x6A6A6A));
			listView.setFont(Globals.getFont(2, 40));
			listView.setBorder(BorderFactory.createCompoundBorder(listView.getBorder(), BorderFactory.createEmptyBorder(10, 10, 0, 0)));
			
			listView.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					InstagramView iView = new InstagramView(SuggestionsBox.this.map.get(s));
					SuggestionsBox.this.setVisible(false);
					SuggestionsBox.this.dispose();
				}
			});
			
			listContainer.add(listView);
		}

		//Create and format the JScrollPane
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setPreferredSize(new Dimension(680, 360));
		scrollPane.setViewportView(listContainer);
		scrollPane.setBorder(null);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.getVerticalScrollBar().setUnitIncrement(10);

		this.add(scrollPane);
		
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
}
