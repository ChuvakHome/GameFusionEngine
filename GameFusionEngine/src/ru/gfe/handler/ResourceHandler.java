package ru.gfe.handler;

import java.awt.Image;
import java.io.File;
import java.net.URL;

import javax.swing.ImageIcon;

public final class ResourceHandler
{
	private ResourceHandler() {}
  
	public static String getExtention(File file)
	{
		return file != null ? getExtention(file.getPath()) : null;
	}
  
	public static String getExtention(String filename)
	{
		if ((filename != null) && (filename.length() > 0))
		{
			int i = filename.length() - 1;
      
			while (filename.charAt(i) != '.')
			{
				if ((filename.charAt(i) != '/') && (filename != System.getProperty("file.separator")))
					i--;
				else
					return null;
			}
			
			return filename.substring(i + 1);
		}
    
		return null;
	}
  
	public static ImageIcon getImageIcon(URL url)
	{
		try
		{
			return new ImageIcon(url);
		} catch (Exception e) { e.printStackTrace();}
		
		return null;
	}
	
	public static ImageIcon getImageIcon(Image image)
	{
		return new ImageIcon(image);
	}
}