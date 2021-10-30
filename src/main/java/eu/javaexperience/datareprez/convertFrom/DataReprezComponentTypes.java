package eu.javaexperience.datareprez.convertFrom;

import eu.javaexperience.datareprez.DataArray;
import eu.javaexperience.datareprez.DataObject;

public enum DataReprezComponentTypes
{
	NULL(void.class, Void.class),
	Boolean(boolean.class, Boolean.class),
	Integer(int.class, Integer.class),
	Long(long.class, Long.class),
	Double(double.class, Double.class),
	String(String.class),
	Blob(byte[].class),
	DataObject(DataObject.class),
	DataArray(DataArray.class),
	
	;
	
	protected final Class[] nativeClasses;
	
	private DataReprezComponentTypes(Class... nativeClasses)
	{
		this.nativeClasses = nativeClasses;
	}
	
	public boolean isAccepatble(Object o)
	{
		for(Class c:nativeClasses)
		{
			if(c.isInstance(o))
			{
				return true;
			}
		}
		return false;
	}

	public static DataReprezComponentTypes recognise(Class<?> valueType)
	{
		for(DataReprezComponentTypes v:values())
		{
			for(Class c:v.nativeClasses)
			{
				if(c == valueType)
				{
					return v;
				}
			}
		}
		return null;
	}
}
