package ru.gfe.exception;

import ru.gfe.physicobject.PhysicObject;

public class IndexAlreadyInUseException extends Exception 
{
	private static final long serialVersionUID = -9075275476632286015L;

	public IndexAlreadyInUseException(PhysicObject entity, int index)
	{
		super(String.format("Entity %s is already using index %d", entity.getClass(), index));
	}
}
