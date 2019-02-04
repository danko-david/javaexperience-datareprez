package eu.javaexperience.datareprez.javaImpl;

import java.io.Serializable;
import java.util.Map;

import eu.javaexperience.collection.map.MapTools;
import eu.javaexperience.collection.map.SmallMap;
import eu.javaexperience.datareprez.DataArray;
import eu.javaexperience.datareprez.DataObject;
import eu.javaexperience.datareprez.convertFrom.DataReprezType;
import eu.javaexperience.io.SerializationTools;
import eu.javaexperience.reflect.Mirror;

public class DataObjectJavaImpl extends DataCommonJavaImpl implements DataObject//, Serializable
{
	Map<String,Object> obj = new SmallMap<>();
	
	public DataObjectJavaImpl(Map<String,Object> obj,MapProvider subImp)
	{
		this.obj = obj;
	}
	
	public DataObjectJavaImpl(Map<String, Object> data)
	{
		this.obj = data;
	}
	
	public DataObjectJavaImpl(){}

	
	@Override
	public void putString(String key, String val)
	{
		obj.put(key,val);
	}

	@Override
	public void putLong(String key, long val)
	{
		obj.put(key,val);		
	}

	@Override
	public void putDouble(String key, double val)
	{
		obj.put(key,val);
	}

	@Override
	public void putInt(String key, int val)
	{
		obj.put(key,val);	
	}

	@Override
	public void putBoolean(String key, boolean val)
	{
		obj.put(key,val);	
	}

	@Override
	public void putObject(String key, DataObject val)
	{
		if(val instanceof DataObjectJavaImpl)
			obj.put(key, ((DataObjectJavaImpl)val).getImpl());
		else
			obj.put(key,val);	
	}

	@Override
	public void putArray(String key, DataArray val)
	{
		if(val instanceof DataArrayJavaImpl)
			obj.put(key, ((DataArrayJavaImpl)val).getImpl());
		else
			obj.put(key,val);	
	}

	@Override
	public void putBlob(String key, byte[] blob)
	{
		obj.put(key,blob);	
	}

	@Override
	public void putNull(String key)
	{
		obj.put(key,null);		
	}

	@Override
	public String getString(String key)
	{
		return (String)obj.get(key);
	}

	@Override
	public long getLong(String key)
	{
		return (Long)obj.get(key);
	}

	@Override
	public double getDouble(String key)
	{
		return (Double)obj.get(key);
	}

	@Override
	public int getInt(String key)
	{
		return (Integer)obj.get(key);
	}

	@Override
	public boolean getBoolean(String key)
	{
		return (Boolean)obj.get(key);
	}

	@Override
	public DataObject getObject(String key)
	{
		return new DataObjectJavaImpl((Map<String,Object>)obj.get(key));
	}

	@Override
	public DataArray getArray(String key)
	{
		Object ret = obj.get(key);
		if(ret instanceof DataArray)
			return (DataArray) ret;
		return new DataArrayJavaImpl((Object[])ret);
	}

	@Override
	public byte[] getBlob(String key)
	{
		return (byte[]) obj.get(key);
	}

	@Override
	public Object get(String key)
	{
		Object ret = obj.get(key);
		if(ret instanceof Map)
			return new DataObjectJavaImpl((Map)ret);
		else if(ret != null && ret.getClass().isArray() && ret.getClass().getComponentType().isAssignableFrom(Object.class))
			return new DataArrayJavaImpl((Object[])ret);
		return ret;
	}

	@Override
	public String optString(String key)
	{
		Object o = obj.get(key);
		if(o instanceof String)
			return (String)o;
		return null;
	}

	@Override
	public long optLong(String key)
	{
		Object o = obj.get(key);
		if(o instanceof Long)
			return (Long)o;
		return 0;
	}

	@Override
	public double optDouble(String key)
	{
		Object o = obj.get(key);
		if(o instanceof Double)
			return (Double)o;
		return 0.0;
	}

	@Override
	public int optInt(String key)
	{
		Object o = obj.get(key);
		if(o instanceof Integer)
			return (Integer)o;
		return 0;
	}

	@Override
	public boolean optBoolean(String key)
	{
		Object o = obj.get(key);
		if(o instanceof Boolean)
			return (Boolean)o;
		return false;
	}

	@Override
	public DataObject optObject(String key)
	{
		Object o = obj.get(key);
		if(o instanceof Map)
			return new DataObjectJavaImpl((Map)o);
		return null;
	}

	@Override
	public DataArray optArray(String key)
	{
		Object o = obj.get(key);
		if(o != null)
			if(o.getClass().isArray())
				return new DataArrayJavaImpl((Object[])o);
		return null;
	}

	@Override
	public byte[] optBlob(String key)
	{
		Object o = obj.get(key);
		if(o instanceof byte[])
			return (byte[])o;
		return null;
	}

	@Override
	public Object opt(String key)
	{
		Object o = obj.get(key);
		if(o == null)
			return null;
		if(o instanceof Map)
			return new DataObjectJavaImpl((Map)o);
		else if(o.getClass().isArray())
			return new DataArrayJavaImpl((Object[])o);
		return o;
	}

	@Override
	public String optString(String key, String def)
	{
		Object o = obj.get(key);
		if(o instanceof String)
			return (String)o;
		return def;
	}

	@Override
	public long optLong(String key, long def)
	{
		Object o = obj.get(key);
		if(o instanceof Long)
			return (Long)o;
		return def;
	}

	@Override
	public double optDouble(String key, double val)
	{
		Object o = obj.get(key);
		if(o instanceof Double)
			return (Double)o;
		return val;
	}

	@Override
	public int optInt(String key, int def)
	{
		Object o = obj.get(key);
		if(o instanceof Integer)
			return (Integer)o;
		return def;
	}

	@Override
	public boolean optBoolean(String key, boolean def)
	{
		Object o = obj.get(key);
		if(o instanceof Boolean)
			return (Boolean)o;
		return def;
	}

	@Override
	public DataObject optObject(String key, DataObject def)
	{
		Object o = obj.get(key);
		if(o instanceof Map)
			return new DataObjectJavaImpl((Map)o);;
		return def;
	}

	@Override
	public DataArray optArray(String key, DataArray def)
	{
		Object o = obj.get(key);
		if(o != null)
			if(o.getClass().isArray())
				return new DataArrayJavaImpl((Object[])o);
		return def;
	}

	@Override
	public byte[] optBlob(String key, byte[] blob)
	{
		Object o = obj.get(key);
		if(o instanceof byte[])
			return (byte[])o;
		return blob;
	}

	@Override
	public Object opt(String key, Object def)
	{
		Object o = obj.get(key);
		if(o instanceof String)
			return o;
		return def;
	}

	@Override
	public boolean has(String key)
	{
		return obj.containsKey(key);
	}

	@Override
	public String[] keys()
	{
		return obj.keySet().toArray(Mirror.emptyStringArray);
	}

	@Override
	public int size()
	{
		return obj.size();
	}

	@Override
	public boolean isString(String key)
	{
		return obj.get(key) instanceof String;
	}

	@Override
	public boolean isLong(String key)
	{
		return obj.get(key) instanceof Long;
	}

	@Override
	public boolean isDouble(String key)
	{
		return obj.get(key) instanceof Double;
	}

	@Override
	public boolean isInt(String key)
	{
		return obj.get(key) instanceof Integer;
	}

	@Override
	public boolean isBoolean(String key)
	{
		return obj.get(key) instanceof Boolean;
	}

	@Override
	public boolean isObject(String key)
	{
		return obj.get(key) instanceof Map;
	}

	@Override
	public boolean isArray(String key)
	{
		Object o = obj.get(key);
		if(o == null)
			return false;
		return o.getClass().isArray();
	}

	@Override
	public boolean isBlob(String key)
	{
		return obj.get(key) instanceof byte[];
	}

	@Override
	public boolean isNull(String key)
	{
		return obj.get(key) == null;
	}

	@Override
	public Map<String, Object> asJavaMap()
	{
		Map<String,Object> ret = new SmallMap<>();
		ret.putAll(obj);
		return ret;
	}

	@Override
	public Map<String,Object> getImpl()
	{
		return obj;
	}

	@Override
	public void remove(String key)
	{
		obj.remove(key);
	}

	@Override
	public DataReprezType getDataReprezType()
	{
		return DataReprezType.OBJECT;
	}

	@Override
	public byte[] toBlob()
	{
		return SerializationTools.serializeIntoBlob((Serializable) obj);
	}
	
	@Override
	public String toString()
	{
		return MapTools.toStringMultiline(obj);
	}
}