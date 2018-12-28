package ru.gfe.sequence;

import java.awt.Image;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import ru.gfe.handler.ResourceHandler;

public class Sequence extends JLabel
{
	private static final long serialVersionUID = 8918754447101183453L;
	
	public static final int LOOP_CONTINUOUSLY = -1;
	
	private Image[] frames;
	private int index = 0;
	private int loop = 1;
	private boolean end;
	private long delay = 40;
	private Timer timer;
  
	public Sequence(String directory)
	{
		this(new File(directory));
	}
  
	public Sequence(URL... urls)
	{
		loadFrames(urls);
	}
  
	public Sequence(File... files)
	{
		loadFrames(files);
	}
  
	public Sequence(String... filenames)
	{
		loadFrames(filenames);
	}
  
	public Sequence(File directory)
	{
		this(getImages(directory));
	}
  
	public Sequence(Image[] imgs)
	{
		loadFrames(imgs);
	}
  
	public void loadFrames(String directory)
	{
		loadFrames(new File(directory));
	}
	
	public void loadFrames(URL... urls)
	{
		ArrayList<Image> imgs = new ArrayList();
	    
		ImageIcon imageIcon = null;
    
		for (URL url : urls)
		{
			imageIcon = ResourceHandler.getImageIcon(url);
      
			if (imageIcon != null)
				imgs.add(imageIcon.getImage());
		}
		
		if ((imgs != null) && (imgs.size() > 0))
			frames = imgs.toArray(new Image[0]);
		else
			frames = new Image[0];
		
		timer = new Timer();
	}
	
	public void loadFrames(File... files)
	{
		ArrayList<Image> imgs = new ArrayList();
    
		String s = null;
    
		for (File file : files)
		{
			s = ResourceHandler.getExtention(file);
      
			if ((s != null) && (s.equals("png")))
				imgs.add(new ImageIcon(file.getPath()).getImage());
		}
		
		if ((imgs != null) && (imgs.size() > 0))
			frames = imgs.toArray(new Image[0]);
		else
			frames = new Image[0];
      
		timer = new Timer();
	}
  
	public void loadFrames(String... filenames)
	{
		ArrayList<Image> imgs = new ArrayList();
    
		String s = null;
    
		for (String filename : filenames)
		{
			s = ResourceHandler.getExtention(filename);
      
			if ((s != null) && (s.equals("png")))
				imgs.add(new ImageIcon(filename).getImage());
		}
    
		if ((imgs != null) && (imgs.size() > 0))
			frames = imgs.toArray(new Image[0]);
		else
			frames = new Image[0];
		
		timer = new Timer();
	}
  
	public void loadFrames(File directory)
	{
		loadFrames(getImages(directory));
	}
  
	public void loadFrames(Image[] imgs)
	{
		if (imgs != null)
			frames = imgs;
		else
			frames = new Image[0];
	
		timer = new Timer();
	}
	
	public boolean framesSequenceEnd()
	{
		return end;
	}
  
	public void loop(int loopTimes)
	{
		loop = loopTimes;
	}
  
	public void setFPS(int fps)
	{
		delay = 1000 / fps;
	}
  
	public int getFPS()
	{
		return (int) (1000 / delay);
	}
  
	public void start()
	{
		if ((frames != null) && (frames.length > 0))
		{
			timer.scheduleAtFixedRate(new TimerTask() 
			{
				public void run() 
				{
					if (end)
						timer.cancel();
					else
					{
						setIcon(new ImageIcon(nextFrame()));
						repaint();
					}	
				}
			},  0, delay);
		}
    }
  
	public void stop()
	{
		end = true;
	}
  
	private Image nextFrame()
	{
		if (index >= frames.length)
		{
			loop -= 1;
      
			if (loop != 0)
				index = 0;
			else
			{
				end = true;
				return frames[frames.length - 1];
			}
		}
    
		return frames[index++];
	}
  
  	private static Image[] getImages(File directory)
  	{
  		if (directory.isDirectory())
  		{
  			ArrayList<Image> imgs = new ArrayList();
      
  			for (File f : directory.listFiles())
  			{
  				if (f.getName().contains(".png"))
  					imgs.add(new ImageIcon(f.getPath()).getImage());
  			}
  			
  			return (Image[]) imgs.toArray(new Image[0]);
  		}
    
  		return null;
  	}
}