import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import java.awt.FlowLayout;

import javax.swing.JPanel;

/*
 * @author Aneesh Ashutosh
 * @date 11/20/14
 * 
 * The view that contains the actual Instagram photos. Hit left arrow
 * to go to the previous image and right arrow to go to the next.
 */

public class InstagramView extends JFrame {
    private static final long serialVersionUID = 3321834939847270328L;

    public ArrayList<BufferedImage> images;
    private long minTime;
    private long maxTime;
    private String foursquareId;
    public JLabel imageViewer;
    public boolean hasImages;
    private int count;
    public JLabel loadingStaticLabel;
    public boolean isLoading;

    public InstagramView(String foursquareId) {
        super();

        //Formats the JFrame
        this.images = new ArrayList<BufferedImage>();
        this.foursquareId = foursquareId;
        this.hasImages = false;
        this.count = 0;
        this.isLoading = false;

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setPreferredSize(new Dimension(640, 740));
        this.setTitle(Globals.APPNAME);
        this.setUndecorated(true);
        this.setResizable(false);
        this.getContentPane().setBackground(new Color(0xF5F5F5));

        this.getContentPane().removeAll();

        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        JPanel topPanel = new JPanel();
        ((BorderLayout) this.getLayout()).setVgap(0);
        ((BorderLayout) this.getLayout()).setHgap(0);
        ((FlowLayout) topPanel.getLayout()).setVgap(0);
        ((FlowLayout) topPanel.getLayout()).setHgap(0);

        topPanel.setPreferredSize(new Dimension(640, 100));
        topPanel.setBackground(new Color(0x1AB0C7));
        topPanel.setOpaque(true);
        topPanel.setBorder(null);

        //Formats the back button
        URL backUrl = LoadingScreen.class.getResource("/res/back.png");
        Icon backIcon = new ImageIcon(backUrl);
        JButton backButton = new JButton(backIcon);
        backButton.setBorder(null);
        topPanel.add(backButton);
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ChooseLocationView chooseLocationView = new ChooseLocationView();
                InstagramView.this.setVisible(false);
                InstagramView.this.dispose();
            }
        });

        //Formats the filler space (uninteractable JButtons)
        URL fillerUrl = LoadingScreen.class.getResource("/res/filler_space.png");
        Icon fillerIcon = new ImageIcon(fillerUrl);
        JButton fillerButton = new JButton(fillerIcon);
        fillerButton.setBorder(null);
        topPanel.add(fillerButton);

        JButton fillerButton2 = new JButton(fillerIcon);
        fillerButton2.setBorder(null);
        topPanel.add(fillerButton2);

        //Formats the loading icon
        URL loadingStaticUrl = LoadingScreen.class.getResource("/res/loading.png");
        Icon loadingStaticIcon = new ImageIcon(loadingStaticUrl);
        this.loadingStaticLabel = new JLabel(loadingStaticIcon);
        this.loadingStaticLabel.setBorder(null);
        topPanel.add(this.loadingStaticLabel);

        //Formats the minimize button
        URL minUrl = LoadingScreen.class.getResource("/res/min.png");
        Icon minIcon = new ImageIcon(minUrl);
        JButton minButton = new JButton(minIcon);
        minButton.setBorder(null);
        topPanel.add(minButton);
        minButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                InstagramView.this.setState(Frame.ICONIFIED);
            }
        });

        //Formats the exit button
        URL exitUrl = LoadingScreen.class.getResource("/res/exit.png");
        Icon exitIcon = new ImageIcon(exitUrl);
        JButton exitButton = new JButton(exitIcon);
        exitButton.setBorder(null);
        topPanel.add(exitButton);
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        this.add(topPanel, BorderLayout.NORTH);

        //Sets the image to the large loading gif.
        URL loadingGifUrl = LoadingScreen.class.getResource("/res/loading_big.gif");
        Icon loadingGifIcon = new ImageIcon(loadingGifUrl);

        imageViewer = new JLabel(loadingGifIcon);
        this.add(imageViewer, BorderLayout.SOUTH);

        this.pack();

        //Downloads all images taken in the past month
        this.maxTime = System.currentTimeMillis() / 1000L;
        this.minTime = this.maxTime - 12 * 2592000;
        new DownloadImageThread(getImageUrls(getInstaLocationId(this.foursquareId)), this).start();

        //Key listener stuff so that the program knows when you press left and right arrows
        this.setFocusable(true);
        this.addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent e) {
            }

            public void keyPressed(KeyEvent e) {
            }

            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    InstagramView.this.moveLeft();
                }
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    InstagramView.this.moveRight();
                }
            }
        });
    }

    //Gets the Instagram ID from the Foursquare ID using the Instagram API
    public String getInstaLocationId(String foursquareId) {
        String url = "https://api.instagram.com/v1/locations/search?foursquare_v2_id=" + foursquareId + "&client_id=" + Globals.INSTAGRAM_CLIENT_ID;
        APIHandler api = new APIHandler();
        String html = api.get(url);
        html = html.substring(html.indexOf("\"id\":\"") + 6);
        return html.substring(0, html.indexOf("\","));
    }

    //Gets the URLs for all images taken in the area
    public ArrayList<String> getImageUrls(String instaId) {
        String url = "https://api.instagram.com/v1/locations/" + instaId + "/media/recent/?client_id=" + Globals.INSTAGRAM_CLIENT_ID + "&min_timestamp=" + this.minTime + "&max_timestamp=" + this.maxTime;
        APIHandler api = new APIHandler();
        String html = api.get(url);

        ArrayList<String> urls = new ArrayList<String>();

        while (html.contains("\"standard_resolution\":{\"url\":\"")) {
            html = html.substring(html.indexOf("\"standard_resolution\":{\"url\":\"") + 30);
            String imgUrl = html.substring(0, html.indexOf("\",")).replaceAll("\\\\", "");
            if (!imgUrl.contains(".mp4")) {
                urls.add(imgUrl);
            }
        }
        this.maxTime = this.minTime;
        this.minTime -= 2592000;

        return urls;
    }

    //Handles pressing the left arrow key
    public void moveLeft() {
        if (this.count - 1 >= 0 && this.images.get(this.count - 1) != null) {
            this.count--;
            this.imageViewer.setIcon(new ImageIcon(this.images.get(count)));
        }
    }

    //Handles pressing the right arrow key
    public void moveRight() {
        if (this.count + 1 < this.images.size() && this.images.get(this.count + 1) != null) {
            this.count++;
            this.imageViewer.setIcon(new ImageIcon(this.images.get(this.count)));
        }

        //If there are fewer than five images remaining in the list, download more (lazy loading)
        if (this.count >= this.images.size() - 5) {
            new DownloadImageThread(getImageUrls(getInstaLocationId(this.foursquareId)), this).start();
        }
    }
}
