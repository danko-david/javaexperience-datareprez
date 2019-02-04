package eu.javaexperience.datareprez.convertFrom;

import eu.javaexperience.interfaces.ObjectWithProperty;

public interface ObjectLike extends DataPrimitiveSourceObject, ObjectWithProperty
{
	public boolean has(String key);
	public String[] keys();
	public int size();
}
