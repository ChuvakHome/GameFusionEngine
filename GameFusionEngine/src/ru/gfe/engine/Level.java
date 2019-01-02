package ru.gfe.engine;

import java.awt.Container;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import ru.gfe.entity.IEntity;

public class Level 
{
	public static final int IENTITIES_ARRAY_SIZE = 127;
	
	protected Container levelContainer = new Container();
	protected String levelName;
	
	private IEntity[] ientities = new IEntity[127];
	
	private int index;
	
	public Container getLevelContainer()
	{
		return levelContainer;
	}
	
	public int addEntity(IEntity ientity)
	{
		ientities[index] = ientity;
		
		levelContainer.add(ientities[index++].getVisual());
		
		return index & ientity.getId();
	}
	
	public String getLevelName()
	{
		return levelName;
	}
	
	public void init() {}
	
	public void postInit() {}
	
	public void update() 
	{		
		int i = 0;
		
		for (i = 0; i <= index; ++i)
		{
			if (ientities[i] != null)
				ientities[i].update();
		}
		
		i = 0;
	}
	
	protected void destroy() 
	{
		levelContainer.removeAll();
		levelContainer.setLayout(null);
		levelContainer = null;
		levelName = null;
	}
	
	public void processKeyEvent(KeyEvent e) {}
	
	public void processMouseEvent(MouseEvent e) {}

	public void processMouseMotionEvent(MouseEvent e) {}
	
	public void processMouseWheelEvent(MouseWheelEvent e) {}
	
	public final long getTimeOnLevel()
	{
		return GameFusionEngine.getTimeOnLevel();
	}
}
