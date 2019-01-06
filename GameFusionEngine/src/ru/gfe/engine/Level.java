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
	
	private boolean canDestroy;
	
	public Container getLevelContainer()
	{
		return levelContainer;
	}
	
	public boolean addEntity(IEntity ientity)
	{
		if (index >= 0 && index < IENTITIES_ARRAY_SIZE && ientity != null && ientity.getLevel() == null)
		{
			ientities[index] = ientity;
			
			levelContainer.add(ientities[index].getVisual());
			
			ientity.setLevel(this, index++);
			
			return true;
		}
		else
			return false;
	}
	
	public void removeEntity(int id)
	{
		if (id >= 0)
		{
			if (ientities[id] != null && ientities[id].getLevel() != null && ientities[id].getLevel().equals(this))
			{
				ientities[id].removeLevel(this, id);
				ientities[id] = null;
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
						if (i >= j)
							collisionMatrix[i][i] = false;
						else
						{
							rect2 = ientities[j].getRect();
								
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
		
		i = 0;
		j = 0;
		
		rect1 = null;
		rect2 = null;
	}
	
	public IEntity[] getIEntityArray()
	{
		return ientities;
	}
	
	public boolean collision(IEntity ientity, Class<? extends IEntity> clazz)
	{
		if (ientity != null && ientity.getLevel() != null && ientity.getLevel().equals(this) && clazz != null)
		{
			int i = ientity.getId();
			int j = 0;
		
			for (j = 0; j < IENTITIES_ARRAY_SIZE; ++j)
			{
				if (collisionMatrix[i][j] && clazz.isInstance(ientities[j]))
					return true;
			}
		}
		
		return false;
	}
	
	public boolean collision(Class<? extends IEntity> clazz1, Class<? extends IEntity> clazz2)
	{
		if (clazz1 != null && clazz2 != null)
		{
			int i = 0;
			int j = 0;
		
			for (i = 0; i < IENTITIES_ARRAY_SIZE; ++i)
			{
				for (j = 0; j < IENTITIES_ARRAY_SIZE; ++j)
				{
					if (collisionMatrix[i][j] && (clazz1.isInstance(ientities[i]) && clazz2.isInstance(ientities[j]) || 
							clazz1.isInstance(ientities[j]) && clazz2.isInstance(ientities[i])))
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
	public boolean collision(IEntity ientity)
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
	public boolean collision(Class<? extends IEntity> clazz)
	{
		if (clazz != null)
		{
			int i = 0;
			int j = 0;
			
			for (i = 0; i < IENTITIES_ARRAY_SIZE; ++i)
			{
				for (j = 0; j < IENTITIES_ARRAY_SIZE; ++j)
				{
					if (collisionMatrix[i][j] && (clazz.isInstance(ientities[i]) || clazz.isInstance(ientities[j])))
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
			if (ientities[i] != null)	
				ientities[i].destroy();
		}
		
		i = 0;
	}
	
	public boolean canDestroy()
	{
		return canDestroy;
	}
	
	public boolean collision(IEntity ientity1, IEntity ientity2)
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
	
	public void processMouseWheelEvent(MouseWheelEvent e) {}
	
	public final long getTimeOnLevel()
	{
		return GameFusionEngine.getTimeOnLevel();
	}
}
