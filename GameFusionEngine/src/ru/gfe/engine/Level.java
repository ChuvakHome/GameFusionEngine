package ru.gfe.engine;

import java.awt.Component;
import java.awt.Container;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import ru.gfe.event.Event;
import ru.gfe.gameobject.IGameObject;

public class Level 
{
	public static final int IENTITIES_ARRAY_SIZE = 127;
	
	protected Container levelContainer = new Container();
	protected String levelName;
	
	private boolean[][] collisionMatrix = new boolean[IENTITIES_ARRAY_SIZE][IENTITIES_ARRAY_SIZE];
	
	protected IGameObject[] iGameObjects = new IGameObject[IENTITIES_ARRAY_SIZE];
	
	protected int freeId;
	
	private boolean canDestroy;
	
	public Container getLevelContainer()
	{
		return levelContainer;
	}
	
	public boolean addGameObject(IGameObject ientity)
	{
		if (freeId >= 0 && freeId < IENTITIES_ARRAY_SIZE && ientity != null && ientity.getLevel() == null)
		{
			iGameObjects[freeId] = ientity;
			
			levelContainer.add(iGameObjects[freeId].getVisual());
			
			ientity.setLevel(this, freeId++);
			
			return true;
		}
		else
			return false;
	}
	
	public void removeGameObject(int id)
	{
		if (id >= 0)
		{
			if (iGameObjects[id] != null && iGameObjects[id].getLevel() != null && iGameObjects[id].getLevel().equals(this))
			{
				iGameObjects[id].removeLevel(this, id);
				iGameObjects[id] = null;
				levelContainer.remove(id);
			}
		}
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
		
		for (i = 0; i <= freeId; ++i)
		{
			if (iGameObjects[i] != null)
			{
				temp = levelContainer.getComponent(i);
				
				iGameObjects[i].setX(temp.getX());
				iGameObjects[i].setY(temp.getY());
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
		
		for (i = 0; i <= freeId; ++i)
		{
			if (iGameObjects[i] != null)
			{
				iGameObjects[i].update();
			
				if (iGameObjects[i].isActive())
				{	
					rect1 = iGameObjects[i].getRect();
					
					for (j = 0; j <= freeId; ++j)
					{
						if (iGameObjects[j] != null)
						{
							if (i >= j)
								collisionMatrix[i][i] = false;
							else
							{
								rect2 = iGameObjects[j].getRect();
									
								if (rect1 != null && rect2 != null)
								{
									if (!collisionMatrix[i][j] && rect1.intersects(rect2))
									{
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
		}
		
		i = 0;
		j = 0;
		
		rect1 = null;
		rect2 = null;
	}
	
	public IGameObject[] getIGameObjectArray()
	{
		return iGameObjects;
	}
	
	public boolean collision(IGameObject ientity, Class<? extends IGameObject> clazz)
	{
		if (ientity != null && ientity.getLevel() != null && ientity.getLevel().equals(this) && clazz != null)
		{
			int i = ientity.getId();
			int j = 0;
		
			for (j = 0; j < IENTITIES_ARRAY_SIZE; ++j)
			{
				if (collisionMatrix[i][j] && clazz.isInstance(iGameObjects[j]))
					return true;
			}
		}
		
		return false;
	}
	
	public boolean collision(Class<? extends IGameObject> clazz1, Class<? extends IGameObject> clazz2)
	{
		if (clazz1 != null && clazz2 != null)
		{
			int i = 0;
			int j = 0;
		
			for (i = 0; i < IENTITIES_ARRAY_SIZE; ++i)
			{
				for (j = 0; j < IENTITIES_ARRAY_SIZE; ++j)
				{
					if (collisionMatrix[i][j] && (clazz1.isInstance(iGameObjects[i]) && clazz2.isInstance(iGameObjects[j]) || 
							clazz1.isInstance(iGameObjects[j]) && clazz2.isInstance(iGameObjects[i])))
							return true;
				}
			}
		}
		
		return false;
	}
	
	/**
	 * Returns true if {@code ientity} collided with any other ientity else return false
	 * @param ientity
	 * @return
	 */
	public boolean collision(IGameObject ientity)
	{
		if (ientity != null && ientity.getLevel() != null && ientity.getLevel().equals(this))
		{
			int i = ientity.getId();
			int j = 0;
			
			for (j = 0; j < IENTITIES_ARRAY_SIZE; ++j)
			{
				if (collisionMatrix[i][j])
					return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Returns true if {@code ientity} collided with any other ientity else return false
	 * @param ientity
	 * @return
	 */
	public boolean collision(Class<? extends IGameObject> clazz)
	{
		if (clazz != null)
		{
			int i = 0;
			int j = 0;
			
			for (i = 0; i < IENTITIES_ARRAY_SIZE; ++i)
			{
				for (j = 0; j < IENTITIES_ARRAY_SIZE; ++j)
				{
					if (collisionMatrix[i][j] && (clazz.isInstance(iGameObjects[i]) || clazz.isInstance(iGameObjects[j])))
						return true;
				}
			}
		}
		
		return false;
	}
	
	protected void destroy() 
	{
		canDestroy = true;
		
		levelContainer.removeAll();
		levelContainer.setLayout(null);
		levelContainer = null;
		levelName = null;
		
		int i = 0;
		
		for (i = 0; i < IENTITIES_ARRAY_SIZE; ++i)
		{
			if (iGameObjects[i] != null)	
				iGameObjects[i].destroy();
		}
		
		i = 0;
	}
	
	public boolean canDestroy()
	{
		return canDestroy;
	}
	
	public boolean collision(IGameObject ientity1, IGameObject ientity2)
	{
		if (ientity1 != null && ientity2 != null && !ientity1.equals(ientity2) && ientity1.getLevel() != null && ientity1.getLevel().equals(this) && ientity2.getLevel() != null 
			&& ientity2.getLevel().equals(this))
			return collisionMatrix[ientity1.getId()][ientity2.getId()];
		else
			return false;
	}
	
	public void processKeyEvent(KeyEvent e) {}
	
	public void processMouseEvent(MouseEvent e) {}

	public void processMouseMotionEvent(MouseEvent e) {}
	
	public void processEvent(Event e) {}
	
	public void processMouseWheelEvent(MouseWheelEvent e) {}
	
	public final long getTimeOnLevel()
	{
		return GameFusionEngine.getTimeOnLevel();
	}
}
