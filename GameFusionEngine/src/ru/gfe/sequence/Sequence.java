package ru.gfe.sequence;

import java.awt.Image;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import ru.gfe.handler.ResourceHandler;

public class Sequence
{	
	public static final int LOOP_CONTINUOUSLY = -1;
	
	private Image[] frames;
	private int index = 0;
	private int loop = 1;
	private boolean start;
	private boolean pause;
	private boolean end;
	private long delay = 40;
	private long updateTime;
	
	private String name; 
	
	private Icon currentFrame;
  
	public Sequence(String directory)
	{
		this(new File(directory));
	}
  
	public void setSequenceName(String sequenceName)
	{
		name = sequenceName;
	}
	
	public String getSequenceName()
	{
		return name;
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
  
	public Sequence(Image... imgs)
	{
		loadFrames(imgs);
	}
  
	public void loadFrames(String directory)
	{
		loadFrames(new File(directory));
	}
	
	public void loadFrames(URL... urls)
	{
		if (urls != null)
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
		}
	}
	
	public void loadFrames(File... files)
	{
		if (files != null)
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
		}
	}
  
	public void loadFrames(String... filenames)
	{
		if (filenames != null)
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
		}
	}
  
	public void loadFrames(File directory)
	{
		loadFrames(getImages(directory));
	}
  
	public void loadFrames(Image... imgs)
	{
		if (imgs != null)
			frames = imgs;
		else
			frames = new Image[0];
	}
	
	public boolean framesSequenceEnd()
	{
		return end;
	}
  
	public void loop(int loopTimes)
	{
		if (loopTimes >= 0 || loopTimes == LOOP_CONTINUOUSLY)	
			loop = loopTimes;
		else
			throw new NumberFormatException("Loop cannot be negative");
	}
  
	public void setFPS(int fps)
	{
		if (fps > 0)
			delay = 1000 / fps;
		else if (fps == 0)
			throw new NumberFormatException("FPS cannot be zero");
		else
			throw new NumberFormatException("FPS cannot be negative");
	}
  
	public int getFPS()
	{
		return (int) (1000 / delay);
	}
  
	public boolean isStarted()
	{
		return start;
	}
	
	public void start()
	{
		if (frames.length > 0)
		{
			start = true;
		
			currentFrame = ResourceHandler.getImageIcon(frames[0]);
			index++;
		}
    }
  
	public void pause()
	{
		pause = true;
	}
	
	public boolean isPaused()
	{
		return pause;
	}
	
	public void resume()
	{
		pause = false;
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
				
				index = frames.length - 1;
				stop();
			}
		}
    
		return frames[index++];
	}
  
	public void reset()
	{
		index = 0;
	}
	
	public void update()
	{
		if (start)
		{	
			++updateTime;
			
			if (updateTime >= delay)
			{
				updateTime = 0;
				
				if (!pause)
					currentFrame = ResourceHandler.getImageIcon(nextFrame());
			}
		}
	}
	
	public Icon getCurrentFrame()
	{
		return currentFrame;
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
  	
  	public Sequence clone()
  	{
  		Sequence clone = new Sequence("");
  		
  		clone.loop = loop;
  		clone.delay = delay;
  		clone.frames = frames.clone();
  		
  		return clone;
  	}
  	
  	public boolean equalsFrames(Sequence sequence)
  	{
  		return sequence != null && sequence.frames == frames;
  	}
  	
  	public boolean equals(Object o)
  	{
  		if (o != null && o instanceof Sequence)
  		{
  			Sequence seq = (Sequence) o;
  			
  			return frames.equals(seq.frames) && delay == seq.delay;
  		}
  			
  		return false;
  	}
  	
  	public void removeFrames()
  	{
  		if (frames != null)
  		{
  			int i = 0;
  			
  			for (i = 0; i < frames.length; ++i)
  				frames[i] = null;
  		}
  	}
  	
  	public void destroy()
  	{
  		removeFrames();
  		
  		frames = null;
  		currentFrame = null;
  		
  		name = null;
  		
  		index = 0;
  		loop = 0;
  		
  		delay = 0;
  		updateTime = 0;
  		
  		start = false;
  		pause = false;
  		end = false;
  	}
}