package eu.javaexperience.datareprez.convertFrom;

import java.lang.reflect.Field;
import java.util.Map;

import eu.javaexperience.collection.map.SmallMap;
import eu.javaexperience.reflect.Mirror;
import eu.javaexperience.reflect.Mirror.ClassData;
import eu.javaexperience.reflect.Mirror.FieldSelector;
import eu.javaexperience.text.StringTools;

public class ReflectObjectLike implements ObjectLike
{
	protected Map<String, Field> fields = new SmallMap();
	
	public ReflectObjectLike(FieldSelector select)
	{
		ClassData dat = Mirror.getClassData(this.getClass());
		Field[] fs = dat.selectFields(select);
		for(Field f:fs)
		{
			fields.put(StringTools.getSubstringAfterLastString(f.getName(), "."), f);
		}
	}
	
	
	@Override
	public Object get(String key)
	{
		Field f = fields.get(key);
		if(null != f)
		{
			try
			{
				return f.get(this);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public DataReprezType getDataReprezType()
	{
		return DataReprezType.OBJECT;
	}

	@Override
	public boolean has(String key)
	{
		return fields.containsKey(key);
	}

	@Override
	public String[] keys()
	{
		return fields.keySet().toArray(Mirror.emptyStringArray);
	}

	@Override
	public int size()
	{
		return fields.size();
	}
}
