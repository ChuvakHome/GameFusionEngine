package ru.gfe.engine;

import java.awt.Component;
import java.awt.Container;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.Stack;

import ru.gfe.event.Event;
import ru.gfe.physicobject.IPhysicObject;

public class Level 
{
	public static final int IENTITIES_ARRAY_SIZE = 127;
	public static final int IACTIONS_STACK_SIZE = 256;
	
	protected Container levelContainer = new Container();
	protected String levelName;
	
	private boolean[][] collisionMatrix = new boolean[IENTITIES_ARRAY_SIZE][IENTITIES_ARRAY_SIZE];
	
	protected IPhysicObject[] iPhysicObjects = new IPhysicObject[IENTITIES_ARRAY_SIZE];
	
	private Stack<IAction> actionStack = new Stack<IAction>();
	
	protected int freeId;
	
	private boolean update;
	
	protected int zOrder;
	
	public Container getLevelContainer()
	{
		return levelContainer;
	}
	
	public boolean addGameObject(IPhysicObject iGameObject)
	{
		return addGameObject(iGameObject, true);
	}
	
	public boolean addGameObject(IPhysicObject iGameObject, boolean setDefaultZOrder)
	{
		if (freeId >= 0 && freeId < IENTITIES_ARRAY_SIZE && iGameObject != null && iGameObject.getLevel() == null)
		{
			iPhysicObjects[freeId] = iGameObject;
					
			levelContainer.add(iPhysicObjects[freeId].getVisual());
				
			if (setDefaultZOrder)	
				levelContainer.setComponentZOrder(iPhysicObjects[freeId].getVisual(), zOrder);
				
			iGameObject.setLevel(this, freeId++);
				
			return true;
		}
		else
			return false;
	}
	
	public boolean checkStack()
	{
		return actionStack.size() < IACTIONS_STACK_SIZE;
	}
	
	public void addToActionStack(IAction iAction)
	{
		if (checkStack())
			actionStack.push(iAction);
	}
	
	public void removeGameObject(int id)
	{
		if (id >= 0)
		{	
			if (iPhysicObjects[id] != null && iPhysicObjects[id].getLevel() != null && iPhysicObjects[id].getLevel().equals(this))
			{
				addToActionStack(() ->
				{
					levelContainer.remove(iPhysicObjects[id].getVisual());
					iPhysicObjects[id].removeLevel(this, id);
					iPhysicObjects[id] = null;
					
					if (id + 1 == freeId)
						--freeId;
				});
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
			if (iPhysicObjects[i] != null)
			{
				temp = levelContainer.getComponent(i);
				
				iPhysicObjects[i].setX(temp.getX());
				iPhysicObjects[i].setY(temp.getY());
			}
		}
		
		temp = null;
		i = 0;
		
		update = true;
	}
	
	public void update()
	{	
		if (update)
		{	
			int i = 0;
			int j = 0;
			
			j = actionStack.size();
			
			for (i = 0; i < j; ++i)
				actionStack.pop().proceed();
			
			i = 0;
			j = 0;
			
			Rectangle rect1;
			Rectangle rect2;
			
			for (i = 0; i <= freeId; ++i)
			{
				if (iPhysicObjects[i] != null && iPhysicObjects[i].isActive())
				{
					iPhysicObjects[i].update();
						
					rect1 = iPhysicObjects[i].getRect();
						
					for (j = 0; j < freeId; ++j)
					{
						if (iPhysicObjects[j] != null && iPhysicObjects[j].isActive())
						{
							if (i >= j)
								collisionMatrix[i][i] = false;
							else
							{
								rect2 = iPhysicObjects[j].getRect();
										
								if (rect1 != null && rect2 != null)
								{
									if (!collisionMatrix[i][j] && rect1.intersects(rect2))
									{
										collisionMatrix[i][j] = true;
										collisionMatrix[j][i] = true;
											
										iPhysicObjects[i].processCollision(iPhysicObjects[j]);
										iPhysicObjects[j].processCollision(iPhysicObjects[i]);
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
	}
	
	public IPhysicObject[] getIGameObjectArray()
	{
		return iPhysicObjects;
	}
	
	public boolean collision(IPhysicObject ientity, Class<? extends IPhysicObject> clazz)
	{
		if (ientity != null && ientity.getLevel() != null && ientity.getLevel().equals(this) && clazz != null)
		{
			int i = ientity.getId();
			int j = 0;
		
			for (j = 0; j < IENTITIES_ARRAY_SIZE; ++j)
			{
				if (collisionMatrix[i][j] && clazz.isInstance(iPhysicObjects[j]))
					return true;
			}
		}
		
		return false;
	}
	
	public boolean collision(Class<? extends IPhysicObject> clazz1, Class<? extends IPhysicObject> clazz2)
	{
		if (clazz1 != null && clazz2 != null)
		{
			int i = 0;
			int j = 0;
		
			for (i = 0; i < IENTITIES_ARRAY_SIZE; ++i)
			{
				for (j = 0; j < IENTITIES_ARRAY_SIZE; ++j)
				{
					if (collisionMatrix[i][j] && (clazz1.isInstance(iPhysicObjects[i]) && clazz2.isInstance(iPhysicObjects[j]) || 
							clazz1.isInstance(iPhysicObjects[j]) && clazz2.isInstance(iPhysicObjects[i])))
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
	public boolean collision(IPhysicObject ientity)
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
	public boolean collision(Class<? extends IPhysicObject> clazz)
	{
		if (clazz != null)
		{
			int i = 0;
			int j = 0;
			
			for (i = 0; i < IENTITIES_ARRAY_SIZE; ++i)
			{
				for (j = 0; j < IENTITIES_ARRAY_SIZE; ++j)
				{
					if (collisionMatrix[i][j] && (clazz.isInstance(iPhysicObjects[i]) || clazz.isInstance(iPhysicObjects[j])))
						return true;
				}
			}
		}
		
		return false;
	}
	
	protected void destroy() 
	{
		update = false;
		
		levelContainer.removeAll();
		levelContainer.setLayout(null);
		levelContainer = null;
		levelName = null;
		
		int i = 0;
		int j = 0;
		
		j = actionStack.size();
		
		for (i = 0; i < j; ++i)
			actionStack.pop().proceed();
		
		for (i = 0; i < freeId; ++i)
		{
			if (iPhysicObjects[i] != null)	
			{
				iPhysicObjects[i].removeLevel(this, iPhysicObjects[i].getId());
				iPhysicObjects[i] = null;
			}
		}
		
		i = 0;
		j = 0;
	}
	
	public boolean canDestroy()
	{
		return update;
	}
	
	public boolean collision(IPhysicObject ientity1, IPhysicObject ientity2)
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
	
	public void setIGameObjectZOrder(int zOrder)
	{
		if (zOrder >= 0)
			this.zOrder = zOrder;
		else
			throw new NumberFormatException("ZOrder cannot be negative");
	}
	
	public int getIGameObjectZOrder()
	{
		return zOrder;
	}
	
	public final long getTimeOnLevel()
	{
		return GameFusionEngine.getTimeOnLevel();
	}
}
