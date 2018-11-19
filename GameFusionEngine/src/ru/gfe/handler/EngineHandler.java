package ru.gfe.handler;

import ru.gfe.engine.Event;

public abstract class EngineHandler 
{
	public abstract void update();

	public abstract void exit();
	
	public abstract<E extends Event> void processEvent(E e);
}
