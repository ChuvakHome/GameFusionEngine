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
	
	protected IPhysicObject[] iPhysicObjects = new IPhysicObject[IENTITIES_ARRAY_SIZE];
	
	private Stack<IAction> actionStack = new Stack<IAction>();
	
	protected int freeId;
	
	boolean canDestroyLevel;
	
	protected int zOrder;
	
	protected long timeOnLevel;
	
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
	
	public void restart()
	{
		GameFusionEngine.changeLevel(levelName);
	}
	
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
	}
	
	public void update()
	{	
		int i = 0;
		int j = actionStack.size();
			
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
						rect2 = iPhysicObjects[j].getRect();
										
							if (rect1 != null && rect2 != null)
							{
								if (rect1.intersects(rect2))
								{
									iPhysicObjects[i].processCollision(iPhysicObjects[j]);
									iPhysicObjects[j].processCollision(iPhysicObjects[i]);
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
		
		timeOnLevel = GameFusionEngine.getTimeOnLevel();
	}
	
	public IPhysicObject[] getIGameObjectArray()
	{
		return iPhysicObjects;
	}
	
	protected void destroy() 
	{	
		if (canDestroyLevel)
		{
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
	}
	
	public boolean canDestroy()
	{
		return canDestroyLevel;
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
		return timeOnLevel;
	}
}
