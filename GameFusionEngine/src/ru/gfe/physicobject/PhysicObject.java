package ru.gfe.physicobject;

import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.Icon;
import javax.swing.JLabel;

import ru.gfe.engine.Level;
import ru.gfe.sequence.Sequence;

public class PhysicObject implements IPhysicObject 
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
	
	private boolean active;
	
	public PhysicObject(JLabel body, Sequence primarySequence, int posX, int posY)
	{
		this.body = body;
		this.primarySequence = primarySequence;
		this.posX = posY;
		this.posY = posY;
		
		active = true;
		
		sequences = new Sequence[SEQUENCE_ARRAY_SIZE];
	}
	
	public PhysicObject(JLabel body, int posX, int posY)
	{
		this.body = body;
		this.posX = posY;
		this.posY = posY;
		
		active = true;
		
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
	
	public PhysicObject(JLabel body, Sequence primarySequence)
	{
		this(body, primarySequence, body.getX(), body.getY());
	}
	
	public PhysicObject(JLabel body)
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
			
			destroyGameObject();
		}
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
	
	public void addSequence(Sequence sequence, int index, boolean replace)
	{
		checkIndex(index);
		
		if (sequence != null)
		{
			if ((sequences[index] != null && !sequences[index].equals(sequence) && replace) || sequences[index] == null)
				sequences[index] = sequence;
		}
	}

	public void addSequence(Sequence sequence, int index)
	{
		addSequence(sequence, index, true);
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
		
		active = false;
		
		posX = 0;
		posY = 0;
		
		index = 0;
		id = 0;
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
		checkIndex(index);
		
		if (this.index != index)
		{
			if (index >= 0 && index < SEQUENCE_ARRAY_SIZE)
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
			else if (index == -1)
				resetToPrimarySequence();
		}
	}
	
	private static void checkIndex(int index)
	{
		if (index < -1)
			throw new IndexOutOfBoundsException("Index cannot be negative");
		else if (index > SEQUENCE_ARRAY_SIZE)
			throw new IndexOutOfBoundsException("Index cannot be greater than SequenceArraySize");
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
			body.setSize(icon.getIconWidth(), icon.getIconHeight());
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
		if (body != null)
		{
			this.posX = posX;
			this.posY = posY;
			
			body.setLocation(this.posX, this.posY);
		}
	}
	
	public void setLocation(Point p)
	{
		if (body != null)
		{
			this.posX = p.x;
			this.posY = p.y;
			
			body.setLocation(p);
		}
	}
	
	public Point getLocation()
	{
		return body != null ? body.getLocation() : null;
	}
	
	public void setX(int posX)
	{
		if (body != null)
		{
			this.posX = posX;
			
			body.setLocation(this.posX, posY);
		}
	}
	
	public int getX()
	{
		return posX;
	}
	
	public void setY(int posY)
	{
		if (body != null)
		{
			this.posY = posY;
			
			body.setLocation(posX, this.posY);
		}
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
		return active;
	}
	
	public void processCollision(IPhysicObject iGameObject) {}
}
