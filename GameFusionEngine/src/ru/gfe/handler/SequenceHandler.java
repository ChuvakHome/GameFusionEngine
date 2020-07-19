package ru.gfe.handler;

import ru.gfe.physicobject.PhysicObject;
import ru.gfe.sequence.Sequence;

public interface SequenceHandler 
{
	public void onSequenceEnd(PhysicObject source, Sequence sequence, int sequenceNumber);
}
