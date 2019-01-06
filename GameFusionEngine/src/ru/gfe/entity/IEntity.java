package ru.gfe.entity;

import java.awt.Rectangle;

import javax.swing.JComponent;

import ru.gfe.engine.Level;

public interface IEntity 
{
	public void update();
	
	public int getId();
	
	public void setX(int posX);
	
	public int getX();
	
	public void setY(int posY);
	
	public int getY();
	
	public void setLevel(Level level, int idOnLevel);
	
	public Level getLevel();
	
	public <C extends JComponent> C getVisual();
	
	public Rectangle getRect();
	
	public void destroy();
}
