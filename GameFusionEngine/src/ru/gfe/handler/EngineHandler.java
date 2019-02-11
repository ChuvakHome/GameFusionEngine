package ru.gfe.handler;

import ru.gfe.event.Event;

public abstract class EngineHandler 
{
	public abstract void update();

	public abstract void exit();
	
	public abstract void processEvent(Event e);
}
