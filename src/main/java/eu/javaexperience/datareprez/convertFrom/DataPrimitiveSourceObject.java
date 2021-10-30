package eu.javaexperience.datareprez.convertFrom;

import eu.javaexperience.datareprez.DataLike;

public interface DataPrimitiveSourceObject extends DataLike
{
	public Object get(String key);
}
