package ru.gfe.physicobject;

import java.awt.Rectangle;

import javax.swing.JComponent;

import ru.gfe.engine.Level;

public interface IPhysicObject 
{
	public void update();
	
	public int getId();
	
	public void setX(int posX);
	
	public int getX();
	
	public void setY(int posY);
	
	public int getY();
	
	public void setLevel(Level level, int idOnLevel);
	
	public void removeLevel(Level level, int id);
	
	public Level getLevel();
	
	public <C extends JComponent> C getVisual();
	
	public Rectangle getRect();
	
	public boolean isActive();
	
	public void processCollision(IPhysicObject iGameObject);
	
	public void setName(String name);
	
	public String getName();
}
