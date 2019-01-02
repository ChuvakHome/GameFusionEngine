package ru.gfe.entity;

import javax.swing.JComponent;

public interface IEntity 
{
	public void update();
	
	public int getId();
	
	public <C extends JComponent> C getVisual();
}
