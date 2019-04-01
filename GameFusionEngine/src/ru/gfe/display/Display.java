package ru.gfe.display;

import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import ru.gfe.engine.GameFusionEngine;

public class Display extends JFrame
{
	private static final long serialVersionUID = -8296410480257618092L;
	private boolean fullScreen;
	private boolean close;
	
	public Display(int width, int height, boolean undecorated)
	{
		setSize(width, height);
		setUndecorated(undecorated);
	}
	
	public void setFullScreen(boolean fullScreen)
	{
		if (this.fullScreen != fullScreen)
		{
			this.fullScreen = fullScreen;
	      
			GraphicsConfiguration graphicsConfiguration = getGraphicsConfiguration();
			GraphicsDevice graphicsDevice = null;
	      
			if (graphicsConfiguration != null)
			{
				graphicsDevice = graphicsConfiguration.getDevice();
				
				if (graphicsDevice != null)
				{	
					if (fullScreen)
						graphicsDevice.setFullScreenWindow(this);
					else
						graphicsDevice.setFullScreenWindow(null);
				}
			}
		}
	}
	
	public static int getResolution()
	{
		return Toolkit.getDefaultToolkit().getScreenResolution();
	}
	
	public static int getScreenWidth()
	{
		return getScreenDimension().width;
	}
	
	public static int getScreenHeight()
	{
		return getScreenDimension().height;
	}
	
	public static Dimension getScreenDimension()
	{
		return Toolkit.getDefaultToolkit().getScreenSize();
	}
	
	public void switchFullScreen()
	{
		setFullScreen(!fullScreen);
	}
	  
	public boolean isFullScreen() 
	{
		return fullScreen;
	}
	  
	protected void processKeyEvent(KeyEvent e)
	{
		GameFusionEngine.processKeyEvent(e);
	}
	  
	protected void processMouseEvent(MouseEvent e)
	{
		GameFusionEngine.processMouseEvent(e);
	}
	  
	protected void processMouseMotionEvent(MouseEvent e)
	{
		GameFusionEngine.processMouseMotionEvent(e);
	}
	  
	protected void processMouseWheelEvent(MouseWheelEvent e)
	{
		GameFusionEngine.processMouseWheelEvent(e);
	}
	  
	protected void processWindowEvent(WindowEvent e)
	{
		if (e.getID() == WindowEvent.WINDOW_CLOSING)
			close = true;
	}
	  
	public boolean canClose() 
	{
		return close;
	}
}