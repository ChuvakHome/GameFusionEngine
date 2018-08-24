package ru.gfe.engine;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

public final class LevelCollection 
{
	private LevelCollection() {}
	
	private static class LevelBuilder
	{
		private Class<? extends Level> levelClass;
		private Object[] contructorArgs;
		
		private LevelBuilder(Class<? extends Level> levelClass, Object... args)
		{
			this.levelClass = levelClass;
			contructorArgs = args;
		}
	}
	
	private static Map<String, LevelBuilder> levels = new HashMap();
	
	static <L extends Level> boolean add(String levelName, Class<L> levelClass, Object... constructorArgs)
	{
		if (levels.containsKey(levelName))
			return false;
		else
		{
			levels.put(levelName, new LevelBuilder(levelClass, constructorArgs));
			
			return true;
		}
	}
	
	static Level getLevelByName(String levelName)
	{
		if (levels.containsKey(levelName))
		{	
			LevelBuilder lb = levels.get(levelName);
			Class<? extends Level> levelClass = lb.levelClass;
			Object[] args = lb.contructorArgs;
			lb = null;
			Object obj = null;
			
			for (Constructor<?> contructor: levelClass.getConstructors())
			{
				try 
				{
					obj = contructor.newInstance(args);
					
					if (obj != null && obj instanceof Level)
					{
						Level level = levelClass.cast(obj);
						obj = null;
						level.levelName = levelName;
						
						return level;
					}
				} catch (Exception e) {}
			}
		}
		
		return null;
	}
}
