package ru.gfe.gameobject;

import java.awt.Rectangle;

import javax.swing.JComponent;

import ru.gfe.engine.Level;

public interface IGameObject 
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
	
	public void destroy();
	
	public boolean isActive();
}
