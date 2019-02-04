package eu.javaexperience.datareprez;

import eu.javaexperience.datareprez.convertFrom.DataLike;

public interface ArrayLike extends DataLike
{
	public Object get(int i);
	public int size();
}
