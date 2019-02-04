package eu.javaexperience.datareprez.convertFrom;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import eu.javaexperience.collection.map.ConcurrentMapTools;
import eu.javaexperience.interfaces.simple.getBy.GetBy1;
import eu.javaexperience.reflect.Mirror;
import eu.javaexperience.reflect.Mirror.ClassData;

public class PublicFieldObjectLike implements ObjectLike
{
	protected static final ConcurrentMap<Class<?>, Map<String, Field>> ACCESSOR = new ConcurrentHashMap<Class<?>, Map<String, Field>>();
	
	protected static final GetBy1<Map<String, Field>, Class<?>> COLLECTOR = new GetBy1<Map<String,Field>, Class<?>>()
	{
		@Override
		public Map<String, Field> getBy(Class<?> a)
		{
			HashMap<String, Field> ret = new HashMap<>();
			ClassData cd = Mirror.getClassData(a);
			for(Field f:cd.getAllFields())
			{
				int mod = f.getModifiers();
				if(!Mirror.isStatic(f) && ((mod & Modifier.PUBLIC) == Modifier.PUBLIC))
				{
					//ret.put(Strings.getSubstringAfterLastString(f.getName(), ".", null), f);
					ret.put(f.getName(), f);
				}
			}
			return ret;
		}
	};
	
	protected static Map<String, Field> getAccessor(Class<?> cls)
	{
		return ConcurrentMapTools.getOrCreate(ACCESSOR, cls, COLLECTOR);
	}
	
	@Override
	public Object get(String key)
	{
		Field f = getAccessor(getClass()).get(key);
		if(null == f)
		{
			return null;
		}
		
		try
		{
			return f.get(this);
		}
		catch (Exception e)
		{
			Mirror.propagateAnyway(e);
			return null;
		}
	}

	@Override
	public DataReprezType getDataReprezType()
	{
		return DataReprezType.OBJECT;
	}

	@Override
	public boolean has(String key)
	{
		return getAccessor(getClass()).containsKey(key);
	}

	@Override
	public String[] keys()
	{
		return getAccessor(getClass()).keySet().toArray(Mirror.emptyStringArray);
	}

	@Override
	public int size()
	{
		return getAccessor(getClass()).size();
	}
}
