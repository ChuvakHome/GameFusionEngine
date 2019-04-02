package ru.gfe.engine;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.Timer;
import java.util.TimerTask;

import ru.gfe.display.Display;
import ru.gfe.event.Event;
import ru.gfe.handler.EngineHandler;
import ru.gfe.handler.KeyHandler;
import ru.gfe.handler.MouseHandler;
import ru.gfe.handler.SoundHandler;

public final class GameFusionEngine 
{
	private static Display display;
	
	private static Timer timer;
	
	private static EngineHandler engineHandler;
	private static KeyHandler keyHandler;
	private static MouseHandler mouseHandler;	
	
	private static boolean started;
	private static boolean updateLevel;
	
	private static long timeOnLevel;
	
	private static Level currentLevel;
	
	private GameFusionEngine() {}
	
	public static Level getLevel()
	{
		return currentLevel;
	}
	
	public static int getDisplayWidth()
	{
		return display.getWidth();
	}
	
	public static int getDisplayHeight()
	{
		return display.getHeight();
	}
	
	public static void setKeyHandler(KeyHandler handler)
	{
		keyHandler = handler;
	}
	
	public static void processEvent(Event e)
	{
		if (engineHandler != null)	
			engineHandler.processEvent(e);
		if (currentLevel != null)
			currentLevel.processEvent(e);
	}
	
	public static void setMouseHandler(MouseHandler handler)
	{
		mouseHandler = handler;
	}
	
	public static void processKeyEvent(KeyEvent e)
	{
		if (keyHandler != null)
			keyHandler.processKeyEvent(e);
		if (currentLevel != null)
			currentLevel.processKeyEvent(e);
	}
	
	public static void processMouseEvent(MouseEvent e)
	{
		if (mouseHandler != null)
			mouseHandler.processMouseEvent(e);
		if (currentLevel != null)
			currentLevel.processMouseEvent(e);
	}
	
	public static void processMouseMotionEvent(MouseEvent e)
	{
		if (mouseHandler != null)
			mouseHandler.processMouseMotionEvent(e);
		if (currentLevel != null)
			currentLevel.processMouseMotionEvent(e);
	}
	
	public static void processMouseWheelEvent(MouseWheelEvent e)
	{
		if (mouseHandler != null)
			mouseHandler.processMouseWheelEvent(e);
		if (currentLevel != null)
			currentLevel.processMouseWheelEvent(e);
	}
	
	public static void addLevel(String levelName, Class<? extends Level> levelClass, Object... initargs)
	{
		LevelCollection.add(levelName, levelClass, initargs);
	}
	
	public static void changeLevel(String levelName)
	{
		changeLevel(levelName, true);
	}
	
	public static void changeLevel(String levelName, boolean stopSounds)
	{
		EventQueue.invokeLater(() -> changeLevel0(levelName, stopSounds));
	}
	
	private static void changeLevel0(String levelName, boolean stopSounds)
	{
		Level level = LevelCollection.getLevelByName(levelName);
		
		if (level != null && level.levelContainer != null && (currentLevel != null || !level.equals(currentLevel)))
		{
			if (stopSounds)
				SoundHandler.stopAll();
			
			updateLevel = false;
			currentLevel.destroy();
			currentLevel = level;
			
			display.setVisible(false);
			
			currentLevel.init();	
			display.setContentPane(currentLevel.levelContainer);
			currentLevel.postInit();
			
			display.setVisible(true);	
				
			updateLevel = true;
			
			timeOnLevel = 0;
		}
	}
	
	public static void exit()
	{
		if (started)
		{
			if (engineHandler != null)
			{
				timer.cancel();
				engineHandler.exit();
			}
			else
				System.exit(0);
		}
	}
	
	public static boolean isStarted()
	{
		return started;
	}
	
	public static void setPrimaryLevel(String name)
	{
		if (currentLevel == null)
			currentLevel = LevelCollection.getLevelByName(name);
	}
	
	public static void setEngineHandler(EngineHandler handler)
	{
		engineHandler = handler;
	}
	
	public static void launch()
	{
		launch(false);
	}
	
	public static void launch(boolean undecorated)
	{
		launch(Display.getScreenDimension(), undecorated);
	}
	
	public static void launch(int width, int height)
	{
		launch(width, height, false);
	}
	
	public static void launch(Dimension dimension)
	{
		launch(dimension.width, dimension.height, false);
	}
	
	public static void launch(Dimension dimension, boolean undecorated)
	{
		launch(dimension.width, dimension.height, undecorated);
	}
	
	public static void launch(int width, int height, boolean undecorated)
	{	
		display = new Display(width, height, undecorated);
		display.setLocationRelativeTo(null);
		
		if (currentLevel != null)
		{
			currentLevel.init();
			display.setContentPane(currentLevel.getLevelContainer());
		}
		
		display.setVisible(true);
		
		if (currentLevel != null)
			currentLevel.postInit();
		
		started = true;
		updateLevel = true;
		
		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask()
		{
			public void run() 
			{
				if (!display.canClose())
				{
					if (updateLevel)
					{	
						++timeOnLevel;
						
						if (engineHandler != null)
							engineHandler.update();
						
						if (currentLevel != null) 
							currentLevel.update();
					}
				}
				else
					exit();
			}
		}, 0, 1);
	}
	
	public static OSType getOSType()
    {
        String s = System.getProperty("os.name").toLowerCase();
        return s.contains("win") ? OSType.WINDOWS : (s.contains("mac") ? OSType.OSX : (s.contains("solaris") ? OSType.SOLARIS : (s.contains("sunos") ? OSType.SOLARIS : (s.contains("linux") ? OSType.LINUX : (s.contains("unix") ? OSType.LINUX : OSType.UNKNOWN)))));
    }
	
	public static long getTimeOnLevel()
	{
		return timeOnLevel;
	}
	
	public static Display getDisplay()
	{
		return display;
	}
	
	public static String getLevelName()
	{
		return currentLevel.levelName;
	}
	
	public static enum OSType
	{
		OSX,
		LINUX,
		SOLARIS,
		WINDOWS,
		UNKNOWN;
	}
	
	public static int booleanToInt(boolean b)
	{
		return b ? 1 : 0;
	}
	
	public static boolean intToBoolean(int x)
	{
		switch (x)
		{
			case 1:
				return true;
			case 0:
				return false;
			default:
				throw new NumberFormatException("Argument cannot be negative");
		}
	}
}
