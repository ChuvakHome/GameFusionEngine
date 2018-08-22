package ru.gfe.handler;

import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public abstract class MouseHandler 
{
	public abstract void processMouseEvent(MouseEvent e);

	public abstract void processMouseMotionEvent(MouseEvent e);
	
	public abstract void processMouseWheelEvent(MouseWheelEvent e);
}
