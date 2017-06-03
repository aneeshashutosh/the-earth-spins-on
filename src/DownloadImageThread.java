import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/*
 * @author Aneesh Ashutosh
 * @date 11/20/14
 * 
 * This class actually downloads the images from Instagram using
 * the provided URLs found in InstagramView. It is asynchronous
 * and supports lazy loading so that the experience with the app
 * is as smooth as possible.
 */

public class DownloadImageThread extends Thread
{
	private InstagramView v;
	private ArrayList<String> urls;

	public DownloadImageThread(ArrayList<String> urls, InstagramView v)
	{
		this.urls = urls;
		this.v = v;
		
		//Replaces the loading icon with a moving gif
		if (!v.isLoading)
		{
			URL loadingStaticUrl = LoadingScreen.class.getResource("/res/loading_small.gif");
			Icon loadingStaticIcon = new ImageIcon(loadingStaticUrl);
			this.v.loadingStaticLabel.setIcon(loadingStaticIcon);
			v.isLoading = true;
		}
	}

	public void run()
	{
		//No images found, display an image
		if (urls.size() == 0 && v.images.size() == 0)
		{
			BufferedImage image = null;
			try
			{
			    image = ImageIO.read(new File("/res/no_photos.png"));
			    if (!v.hasImages)
				{
				    v.images.add(image);
					v.hasImages = true;
					v.imageViewer.setIcon(new ImageIcon(v.images.get(0)));
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			//Iterate through all the URLs
			for (int i = 0; i < urls.size(); i++)
			{
				BufferedImage image = null;
				try
				{
					//Download the image from the internet
					image = ImageIO.read(new URL(urls.get(i)));
					
					//Resize the image if it is not at Instagram's new 640x640 standard
					if (image.getHeight() != Globals.IMAGE_SIZE || image.getWidth() != Globals.IMAGE_SIZE)
					{
						image = resize(image);
					}
					
					v.images.add(image);
					
					//Load the first image if there is no image being displayed
					if (!v.hasImages)
					{
						v.hasImages = true;
						v.imageViewer.setIcon(new ImageIcon(v.images.get(0)));
					}
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
				
				//Change the loading icon image to a transparent 100x100 square when the app is not loading
				if (v.isLoading)
				{
					URL loadingStaticUrl = LoadingScreen.class.getResource("/res/loading.png");
					Icon loadingStaticIcon = new ImageIcon(loadingStaticUrl);
					this.v.loadingStaticLabel.setIcon(loadingStaticIcon);
					v.isLoading = false;
				}
			}
		}
	}

	//Resizes an image
	public BufferedImage resize(BufferedImage img)
	{ 
		Image tmp = img.getScaledInstance(Globals.IMAGE_SIZE, Globals.IMAGE_SIZE, Image.SCALE_SMOOTH);
		BufferedImage dimg = new BufferedImage(Globals.IMAGE_SIZE, Globals.IMAGE_SIZE, BufferedImage.TYPE_INT_ARGB);

		Graphics2D g2d = dimg.createGraphics();
		g2d.drawImage(tmp, 0, 0, null);
		g2d.dispose();

		return dimg;
	}  
}
