package eu.javaexperience.datareprez;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import eu.javaexperience.collection.CollectionTools;
import eu.javaexperience.collection.map.BulkTransitMap;
import eu.javaexperience.collection.map.MapTools;
import eu.javaexperience.collection.map.NullMap;
import eu.javaexperience.datareprez.convertFrom.DataWrapper;
import eu.javaexperience.interfaces.simple.SimpleGetFactory;
import eu.javaexperience.reflect.CastTo;
import eu.javaexperience.reflect.Caster;
import eu.javaexperience.reflect.FieldNameDialect;
import eu.javaexperience.reflect.FieldNameDialects;
import eu.javaexperience.reflect.Mirror;

public class DataReprezDialectTools
{
	protected static final ConcurrentMap<Class, Map<String,Map<String, Field>>> DIALECT = new ConcurrentHashMap<>();
	
	protected static Map<String, Map<String, Field>> getDialect(Class cls)
	{
		Map<String, Map<String, Field>> ret = DIALECT.get(cls);
		
		if(null != ret && 0 == ret.size())
		{
			return ret;
		}
		
		Map<String, Map<String, Field>> dialects = new BulkTransitMap<>();
		
		for(Field f:Mirror.collectClassFields(cls, false))
		{
			ArrayList<FieldNameDialect> fnds = new ArrayList<>();
			
			CollectionTools.copyInto(f.getAnnotationsByType(FieldNameDialect.class), fnds);
			
			for(FieldNameDialects d:f.getAnnotationsByType(FieldNameDialects.class))
			{
				CollectionTools.copyInto(d.value(), fnds);
			}
			
			for(FieldNameDialect fnd:fnds)
			{
				String dial = fnd.dialect();
				String name = fnd.name();
				MapTools.ensureMapInMap(dialects, dial, SimpleGetFactory.getHashMapFactory()).put(name, f);
			}
		}
		
		if(0 == dialects.size())
		{
			dialects = NullMap.instance;
		}
		else
		{
			HashMap<String, Map<String, Field>> add = new HashMap<>();
			for(Entry<String, Map<String, Field>> kv:dialects.entrySet())
			{
				Map<String, Field> in = kv.getValue();
				if(in.size() > 0)
				{
					in = Collections.unmodifiableMap(in);
				}
				
				add.put(kv.getKey(), in);
			}
			dialects = Collections.unmodifiableMap(add);
		}
		
		DIALECT.put(cls, dialects);
		return dialects;
	}
	
	/**
	 * Dialect name => { fieldname => java field}
	 * */
	public static Map<String, Map<String, Field>> getDialectFieldsOfClass(Class<?> cls)
	{
		return getDialect(cls);
	}
	
	public static DataAccessor generateFieldDialectAccessor(final String dialect)
	{
		return new DataAccessor()
		{
			@Override
			public boolean canHandle(Object subject)
			{
				Class c = subject.getClass();
				if(c.isArray())
				{
					return false;
				}
				
				Map<String, Field> b = getDialect(c).get(dialect);
				
				return null!= b &&  0 != b.size();
			}
			
			@Override
			public String[] keys(Object subject)
			{
				return getDialect(subject.getClass()).get(dialect).keySet().toArray(Mirror.emptyStringArray);
			}
			
			@Override
			public Object get(Object subject, String key)
			{
				Field f = getDialect(subject.getClass()).get(dialect).get(key);
				if(null != f)
				{
					try
					{
						return f.get(subject);
					}
					catch (Exception e)
					{
						Mirror.propagateAnyway(e);
					}
				}
				
				return null;
			}
		};
	}
	
	public static boolean fillPoJoFromDialectNaive(Object target, String dialect, DataObject data)
	{
		Map<String, Field> fs = getDialectFieldsOfClass(target.getClass()).get(dialect);
		
		if(null == fs)
		{
			return false;
		}
		
		for(Entry<String, Field> kv:fs.entrySet())
		{
			Object o = data.opt(kv.getKey());
			if(null != o)
			{
				Field f = kv.getValue();
				Caster c = CastTo.getCasterRestrictlyForTargetClass(f.getType());
				if(null != c)
				{
					o = c.cast(o);
					if(null != o)
					{
						try
						{
							f.set(target, o);
						}
						catch (Exception e){}
					}
				}
			}
		}
		
		return true;
	}
	
	public static boolean fillPoJoFromDialectNaive(Object target, String dialect, Map<String, Object> data)
	{
		Map<String, Field> fs = getDialectFieldsOfClass(target.getClass()).get(dialect);
		
		if(null == fs)
		{
			return false;
		}
		
		for(Entry<String, Field> kv:fs.entrySet())
		{
			Object o = data.get(kv.getKey());
			if(null != o)
			{
				Field f = kv.getValue();
				Caster c = CastTo.getCasterRestrictlyForTargetClass(f.getType());
				if(null != c)
				{
					o = c.cast(o);
					if(null != o)
					{
						try
						{
							f.set(target, o);
						}
						catch (Exception e){}
					}
				}
			}
		}
		
		return true;
	}
	
	public static DataWrapper generateFieldDialectWrapper(final String dialect)
	{
		return new DataWrapper()
		{
			@Override
			public DataCommon wrap(DataWrapper topWrapper, DataCommon prototype, Object o)
			{
				Map<String, Map<String, Field>> d = getDialect(o.getClass());
				if(null == d || 0 == d.size())
				{
					return null;
				}
				
				Map<String, Field> di = d.get(dialect);
				if(null == di || 0 == di.size())
				{
					return null;
				}
				
				DataObject ret = prototype.newObjectInstance();
				
				for(Entry<String, Field> ent:di.entrySet())
				{
					String key = ent.getKey();

					try
					{
						Object w = ent.getValue().get(o);
						if(null != w)
						{
							if(null == DataReprezTools.put(topWrapper, ret, key, w))
							{
								DataReprezTools.put(topWrapper, ret, key, topWrapper.wrap(topWrapper, prototype, w));
							}
						}

					}
					catch (Exception e)
					{
						Mirror.propagateAnyway(e);
					}
				}
				
				return ret;
			}
		};
	}
}
