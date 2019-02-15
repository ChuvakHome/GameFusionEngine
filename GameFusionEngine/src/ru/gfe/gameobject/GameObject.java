package ru.gfe.gameobject;

import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.Icon;
import javax.swing.JLabel;

import ru.gfe.engine.Level;
import ru.gfe.exception.IndexAlreadyInUseException;
import ru.gfe.sequence.Sequence;

public class GameObject implements IGameObject 
{
	public static final int SEQUENCE_ARRAY_SIZE = 127;
	
	private JLabel body;
	
	private Icon icon;	
	private Icon temp;
	
	private Sequence[] sequences;
	private Sequence primarySequence;
	
	protected int posX; 
	protected int posY;
	private int index = -1;
	protected int id = -1;
	
	protected Level level;
	
	public GameObject(JLabel body, Sequence primarySequence, int posX, int posY)
	{
		this.body = body;
		this.primarySequence = primarySequence;
		this.posX = posY;
		this.posY = posY;
		
		sequences = new Sequence[SEQUENCE_ARRAY_SIZE];
	}
	
	public GameObject(JLabel body, int posX, int posY)
	{
		this.body = body;
		this.posX = posY;
		this.posY = posY;
		
		sequences = new Sequence[SEQUENCE_ARRAY_SIZE];
	}
	
	public JLabel getVisual()
	{
		return body;
	}
	
	public Rectangle getRect()
	{	
		if (body != null && body.getIcon() != null)
			return new Rectangle(posX, posY, body.getIcon().getIconWidth(), body.getIcon().getIconHeight());
		else
			return null;
	}
	
	public GameObject(JLabel body, Sequence primarySequence)
	{
		this(body, primarySequence, body.getX(), body.getY());
	}
	
	public GameObject(JLabel body)
	{
		this(body, body.getX(), body.getY());
	}
	
	public void setBody(JLabel body)
	{
		if (body != null)
		{	
			this.body = body;
			posX = body.getX();
			posY = body.getY();
		}
	}
	
	public void setLevel(Level level, int id)
	{
		if (level != null && id >= 0)
		{
			this.level = level;
			this.id = id;
		}
	}
	
	public void removeLevel(Level level, int id)
	{
		if (this.level != null && level != null && this.level.equals(level) && this.id >= 0 && id >= 0 && this.id == id)
		{
			this.level = null;
			this.id = -1;
		}
	}
	
	public boolean collision(IGameObject ientity)
	{
		return level != null ? level.collision(this, ientity) : false;
	}
	
	public boolean collision()
	{
		return level != null ? level.collision(this) : false;
	}
	
	public boolean collision(Class<? extends IGameObject> clazz)
	{
		return level != null ? level.collision(this, clazz) : false;
	}

	public Level getLevel()
	{
		return level;
	}
	
	public void changeBodyStatic(JLabel body)
	{
		if (body != null)
		{
			this.body = body;
		}
	}
	
	public boolean addSequence(Sequence sequence, int index, boolean replace) throws IndexAlreadyInUseException
	{
		if (index >= 0 && index < SEQUENCE_ARRAY_SIZE && sequence != null)
		{
			if (sequences[index] != null && !sequences[index].equals(sequence))
			{
				if (replace)
					sequences[index] = sequence;
				else
					throw new IndexAlreadyInUseException(this, index);
			}
			
			return true;
		}
		else
			return false;
	}

	public boolean addSequence(Sequence sequence, int index)
	{
		if (index >= 0 && index < SEQUENCE_ARRAY_SIZE && sequence != null)
		{
			sequences[index] = sequence;
			
			return true;
		}
		else
			return false;
	}
	
	public boolean currentSequenceEnded()
	{
		if (index > 0)
			return sequences[index].framesSequenceEnd();
		else
			return primarySequence.framesSequenceEnd();
	}
	
	public boolean currentSequenceStarted()
	{
		if (index > 0)
			return sequences[index].isStarted();
		else
			return primarySequence.isStarted();
	}
	
	public void destroy() 
	{
		if (canDestroy())
			destroyGameObject();
	}
	
	protected void destroyGameObject()
	{
		icon = null;
		temp = null;
		
		body = null;
		
		level = null;
		
		destroyAllSequence();
		
		sequences = null;
		
		if (primarySequence != null)
		{
			primarySequence.destroy();
			primarySequence = null;
		}
		
		posX = 0;
		posY = 0;
		
		index = 0;
		id = 0;
	}
	
	protected boolean canDestroy()
	{
		return (level != null && level.canDestroy()) || level == null;
	}
	
	protected void destroyAllSequence()
	{
		int i = 0;
		
		for (i = 0; i < SEQUENCE_ARRAY_SIZE; ++i)
		{
			if (sequences[i] != null)
				sequences[i].destroy();
		}
	}
	
	public void setPrimarySequence(Sequence primarySequence)
	{
		if (primarySequence != null && (this.primarySequence == null || !this.primarySequence.equals(primarySequence)))
			this.primarySequence = primarySequence;
	}
	
	public void startSequence(int index)
	{
		if (index >= 0 && index < SEQUENCE_ARRAY_SIZE && this.index != index)
		{	
			if (this.index >= 0)
			{
				sequences[this.index].pause();
				sequences[this.index].reset();
			}
			
			this.index = index;
			
			if (sequences[this.index].isPaused())
				sequences[this.index].resume();
			
			sequences[this.index].reset();
			sequences[this.index].start();
		}
	}
	
	public void resetToPrimarySequence()
	{
		if (index >= 0 && index < SEQUENCE_ARRAY_SIZE && index != -1)
		{
			sequences[index].pause();
			sequences[index].reset();
		}
			
		index = -1;
		
		if (primarySequence.isPaused())
			primarySequence.resume();
		
		primarySequence.reset();
		
		primarySequence.start();
	}
	
	public void startOrResumeCurrentSequence()
	{
		if (index >= 0 && index < SEQUENCE_ARRAY_SIZE)
		{
			if (sequences[index].isStarted())
			{
				if (sequences[index].isPaused())
					sequences[index].resume();
			}
			else
				sequences[index].start();
		}
		else if (index == -1)
		{
			if (primarySequence.isStarted())
			{
				if (primarySequence.isPaused())
					primarySequence.resume();
			}
			else
				primarySequence.start();
		}
	}
	
	public void update()
	{
		if (index >= 0 && index < SEQUENCE_ARRAY_SIZE)
		{
			sequences[index].update();
			
			temp = sequences[index].getCurrentFrame();
		}
		else if (index == -1)
		{
			if (primarySequence != null)
			{	
				primarySequence.update();
				
				temp = primarySequence.getCurrentFrame();
			}
		}
		
		if (temp != null && (icon == null || !icon.equals(temp)))
		{
			icon = temp;
			body.setIcon(icon);
			body.repaint();
		}
	}
	
	public String getCurrentSequenceName()
	{
		if (index >= 0 && index < SEQUENCE_ARRAY_SIZE)
			return sequences[index].getSequenceName();
		else if (index == -1)
			return primarySequence.getSequenceName();
		else
			return null;
	}
	
	public void pauseAllSequences()
	{
		int i = 0;
		
		for (i = 0; i < SEQUENCE_ARRAY_SIZE; ++i)
		{			
			if (sequences[i] != null)
				sequences[i].pause();
		}
		
		i = 0;
	}
	
	public void resetAllSequences()
	{
		int i = 0;
		
		for (i = 0; i < SEQUENCE_ARRAY_SIZE; ++i)
		{			
			if (sequences[i] != null)
				sequences[i].reset();
		}
		
		i = 0;
	}
	
	public void resumeAllSequences()
	{
		int i = 0;
		
		for (i = 0; i < SEQUENCE_ARRAY_SIZE; ++i)
		{			
			if (sequences[i] != null)
				sequences[i].resume();
		}
		
		i = 0;
	}
	
	public void removeSequence(Sequence sequence)
	{
		if (sequence != null)
		{
			int i = 0;
			int j = -1;
			int k = 0;
			
			for (i = 0; i < SEQUENCE_ARRAY_SIZE && j < 0; ++i)
			{
				if (sequences[i].equals(sequence))
					j = i;
			}
			
			if (j >= 0)
			{
				sequences[j].stop();
				
				sequences[j] = null; 
				
				Sequence[] temp = new Sequence[SEQUENCE_ARRAY_SIZE];
				
				for (i = 0; i < SEQUENCE_ARRAY_SIZE; ++i)
				{
					if (i != j)
						temp[k++] = sequences[i];
				}
				
				sequences = temp;
				
				temp = null;
			}
			
			i = 0;
			j = 0;
			k = 0;
		}
	}
	
	public void clearAllSequences()
	{
		primarySequence = null;
		
		int i = 0;
		
		for (i = 0; i < SEQUENCE_ARRAY_SIZE; ++i)
		{
			sequences[i].stop();
			sequences[i] = null;
		}
		
		i = 0;
		
		sequences = null;
		
		sequences = new Sequence[SEQUENCE_ARRAY_SIZE];
	}
	
	public void setLocation(int posX, int posY)
	{
		this.posX = posX;
		this.posY = posY;
		
		body.setLocation(this.posX, this.posY);
	}
	
	public void setLocation(Point p)
	{
		this.posX = p.x;
		this.posY = p.y;
		
		body.setLocation(p);
	}
	
	public Point getLocation()
	{
		return body.getLocation();
	}
	
	public void setX(int posX)
	{
		this.posX = posX;
		
		body.setLocation(this.posX, posY);
	}
	
	public int getX()
	{
		return posX;
	}
	
	public void setY(int posY)
	{
		this.posY = posY;
		
		body.setLocation(posX, this.posY);
	}
	
	public int getY()
	{
		return posY;
	}
	
	public int getId()
	{
		return id;
	}

	public boolean isActive() 
	{
		return true;
	}
	
	public void processCollision(IGameObject iGameObject) {}
}
