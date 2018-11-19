package ru.gfe.entity;

import java.awt.Component;

public abstract class Mesh<C extends Component> implements IEntity 
{
	private C body;
	private int posX, posY;
	
	private int id = -1;
	
	public Mesh(C body, int posX, int posY, int id)
	{
		this.body = body;
		this.posX = posY;
		this.posY = posY;
		this.id = id > 0 ? id : -1;
	}
	
	public Mesh(C body, int id)
	{
		this(body, body.getX(), body.getY(), id);
	}
	
	public void setBody(C body)
	{
		this.body = body;
		posX = body.getX();
		posY = body.getY();
	}
	
	public void changeBodyStatic(C body)
	{
		this.body = body;
	}
	
	public void setX(int posX)
	{
		this.posX = posX;
	}
	
	public int getX()
	{
		return posX;
	}
	
	public void setY(int posY)
	{
		this.posY = posY;
	}
	
	public int getY()
	{
		return posY;
	}
	
	public C getBody()
	{
		return body;
	}
	
	public int getId()
	{
		return id;
	}
}
