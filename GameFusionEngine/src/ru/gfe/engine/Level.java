package ru.gfe.engine;

import java.awt.Component;
import java.awt.Container;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import ru.gfe.entity.IEntity;

public class Level 
{
	public static final int IENTITIES_ARRAY_SIZE = 127;
	
	protected Container levelContainer = new Container();
	protected String levelName;
	
	private boolean[][] collisionMatrix = new boolean[IENTITIES_ARRAY_SIZE][IENTITIES_ARRAY_SIZE];
	
	private IEntity[] ientities = new IEntity[IENTITIES_ARRAY_SIZE];
	
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
	
	public void postInit() 
	{
		int i = 0;

		Component temp;
		
		for (i = 0; i <= index; ++i)
		{
			if (ientities[i] != null)
			{
				temp = levelContainer.getComponent(i);
				
				ientities[i].setX(temp.getX());
				ientities[i].setY(temp.getY());
			}
		}
		
		temp = null;
		i = 0;
	}
	
	public void update()
	{
		int i = 0;
		int j = 0;
		
		Rectangle rect1;
		Rectangle rect2;
		
		for (i = 0; i <= index; ++i)
		{
			if (ientities[i] != null)
			{
				ientities[i].update();
			
				rect1 = ientities[i].getRect();
				
				for (j = 0; j <= index; ++j)
				{
					if (ientities[j] != null)
					{
						if (i == j)
							collisionMatrix[i][i] = true;
						else
						{
							rect2 = ientities[j].getRect();
								
							if (rect1 != null && rect2 != null )
							{
								if (!collisionMatrix[i][j] && rect1.intersects(rect2))
								{
									ientities[i].processCollision(ientities[j]);
									ientities[j].processCollision(ientities[i]);
									
									collisionMatrix[i][j] = true;
									collisionMatrix[j][i] = true;
								}
								else if (collisionMatrix[i][j] && !rect1.intersects(rect2))
								{
									collisionMatrix[i][j] = false;
									collisionMatrix[j][i] = false;
								}
							}
						}
					}
				}
			}
		}
		
		i = 0;
		j = 0;
		
		rect1 = null;
		rect2 = null;
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
