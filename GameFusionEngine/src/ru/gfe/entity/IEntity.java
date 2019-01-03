package ru.gfe.entity;

import java.awt.Rectangle;

import javax.swing.JComponent;

public interface IEntity 
{
	public void update();
	
	public int getId();
	
	public void setX(int posX);
	
	public int getX();
	
	public void setY(int posY);
	
	public int getY();
	
	public void processCollision(IEntity entity);
	
	public <C extends JComponent> C getVisual();
	
	public Rectangle getRect();
}
