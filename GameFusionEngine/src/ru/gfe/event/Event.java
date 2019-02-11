package ru.gfe.event;

public abstract class Event 
{
	protected Object[] data;
	
	public Event(Object... data)
	{
		this.data = data;
	}
	
	public Object[] getEventData()
	{
		return data;
	}
}
