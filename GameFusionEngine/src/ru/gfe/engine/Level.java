package ru.gfe.engine;

import java.awt.Container;

public class Level 
{
	protected Container levelContainer = new Container();
	protected String levelName;
	
	public Container getLevelContainer()
	{
		return levelContainer;
	}
	
	public String getLevelName()
	{
		return levelName;
	}
	
	public void init() {}
	
	public void postInit() {}
	
	public void update() {}
	
	protected void destroy() 
	{
		levelContainer.removeAll();
		
		levelContainer = null;
		levelName = null;
	}
	
	public final long getTimeOnLevel()
	{
		return GameFusionEngine.getTimeOnLevel();
	}
}
