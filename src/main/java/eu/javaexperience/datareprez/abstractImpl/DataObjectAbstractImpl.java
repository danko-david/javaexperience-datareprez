package eu.javaexperience.datareprez.abstractImpl;

import java.util.HashMap;
import java.util.Map;

import eu.javaexperience.datareprez.DataArray;
import eu.javaexperience.datareprez.DataCommonAbstractImpl;
import eu.javaexperience.datareprez.DataObject;
import eu.javaexperience.datareprez.convertFrom.DataReprezType;
import eu.javaexperience.reflect.Mirror;

public abstract class DataObjectAbstractImpl extends DataProtocolAbstractImpl implements DataObject
{
	protected abstract void setSubjectValue(String key, Class<?> valueType, Object val);
	
	protected abstract <T> T getValueAs(String key, Class<T> retType, boolean mayNull);
	
	protected void deleteKey(String key)
	{
		setSubjectValue(key, void.class, key);
	}
	
	protected <T> T getValueOpt(String key, Class<T> retType, T defaultValue)
	{
		T ret = getValueAs(key, retType, true);
		if(null != ret)
		{
			return ret;
		}
		
		return defaultValue;
	}

	@Override
	public void putString(String key, String val)
	{
		setSubjectValue(key, String.class, val);
	}

	@Override
	public void putLong(String key, long val)
	{
		setSubjectValue(key, long.class, val);
	}

	@Override
	public void putDouble(String key, double val)
	{
		setSubjectValue(key, double.class, val);
	}

	@Override
	public void putInt(String key, int val)
	{
		setSubjectValue(key, int.class, val);
	}


	@Override
	public void putBoolean(String key, boolean val)
	{
		setSubjectValue(key, boolean.class, val);
	}

	@Override
	public void putObject(String key, DataObject val)
	{
		setSubjectValue(key, DataObject.class, val);
	}

	@Override
	public void putNull(String key)
	{
		setSubjectValue(key, void.class, null);
	}
	
	@Override
	public void putArray(String key, DataArray val)
	{
		setSubjectValue(key, DataArray.class,  val);
	}

	@Override
	public void putBlob(String key, byte[] blob)
	{
		setSubjectValue(key, byte[].class, blob);
	}
	
	@Override
	public String getString(String key)
	{
		return getValueAs(key, String.class, false);
	}

	@Override
	public long getLong(String key)
	{
		return getValueAs(key, long.class, false);
	}

	@Override
	public double getDouble(String key)
	{
		return getValueAs(key, double.class, false);
	}

	@Override
	public int getInt(String key)
	{
		return getValueAs(key, int.class, false);
	}

	@Override
	public boolean getBoolean(String key)
	{
		return getValueAs(key, boolean.class, false);
	}

	@Override
	public DataObject getObject(String key)
	{
		return getValueAs(key, DataObject.class, false);
	}

	@Override
	public DataArray getArray(String key)
	{
		return getValueAs(key, DataArray.class, false);
	}
	
	@Override
	public byte[] getBlob(String key)
	{
		return getValueAs(key, byte[].class, false);
	}

	@Override
	public String optString(String key)
	{
		return getValueOpt(key, String.class, "");
	}

	@Override
	public long optLong(String key)
	{
		return getValueOpt(key, long.class, 0l);
	}

	@Override
	public double optDouble(String key)
	{
		return getValueOpt(key, double.class, 0.0);
	}

	@Override
	public int optInt(String key)
	{
		return getValueOpt(key, int.class, 0);
	}

	@Override
	public boolean optBoolean(String key)
	{
		return getValueOpt(key, boolean.class, false);
	}

	@Override
	public DataObject optObject(String key)
	{
		return getValueAs(key, DataObject.class, true);
	}

	@Override
	public DataArray optArray(String key)
	{
		return getValueAs(key, DataArray.class, true);
	}

	@Override
	public byte[] optBlob(String key)
	{
		return getValueOpt(key, byte[].class, Mirror.emptyByteArray);
	}
	
	@Override
	public String optString(String key, String def)
	{
		return getValueOpt(key, String.class, def);
	}

	@Override
	public long optLong(String key, long def)
	{
		return getValueOpt(key, long.class, def);
	}

	@Override
	public double optDouble(String key, double def)
	{
		return getValueOpt(key, double.class, def);
	}

	@Override
	public int optInt(String key, int def)
	{
		return getValueOpt(key, int.class, def);
	}

	@Override
	public boolean optBoolean(String key, boolean def)
	{
		return getValueOpt(key, boolean.class, def);
	}

	@Override
	public DataObject optObject(String key, DataObject def)
	{
		return getValueAs(key, DataObject.class, false);
	}

	@Override
	public DataArray optArray(String key, DataArray def)
	{
		return getValueAs(key, null, true);
	}
	
	@Override
	public byte[] optBlob(String key, byte[] def)
	{
		try
		{
			return getValueOpt(key, byte[].class, def);
		}
		catch(Exception e)
		{
			return def;
		}
	}
	
	@Override
	public Object get(String key)
	{
		return getValueAs(key, Object.class, false);
	}

	@Override
	public Object opt(String key)
	{
		return getValueAs(key, Object.class, true);
	}

	@Override
	public Object opt(String key, Object o)
	{
		return getValueOpt(key, Object.class, o);
	}

	@Override
	public boolean has(String key)
	{
		return null != getValueAs(key, Object.class, true);
	}

	@Override
	public int size()
	{
		return keys().length;
	}

	@Override
	public boolean isString(String key)
	{
		return getValueAs(key, String.class, true) instanceof String;
	}

	@Override
	public boolean isLong(String key)
	{
		return getValueAs(key, Long.class, true) instanceof Long;
	}

	@Override
	public boolean isDouble(String key)
	{
		return getValueAs(key, Double.class, true) instanceof Double;
	}

	@Override
	public boolean isInt(String key)
	{
		return getValueAs(key, Integer.class, true) instanceof Integer;
	}

	@Override
	public boolean isBoolean(String key)
	{
		return getValueAs(key, Boolean.class, true) instanceof Boolean;
	}

	@Override
	public boolean isObject(String key)
	{
		return getValueAs(key, DataObject.class, true) instanceof DataObject;
	}

	@Override
	public boolean isArray(String key)
	{
		return getValueAs(key, DataArray.class, true) instanceof DataArray;
	}
	
	@Override
	public boolean isBlob(String key)
	{
		try
		{
			return getValueAs(key, byte[].class, true) instanceof byte[];
		}
		catch(Exception e)
		{
			return false;
		}
	}

	@Override
	public boolean isNull(String key)
	{
		return null == getValueAs(key, null, true);
	}

	@Override
	public Map<String, Object> asJavaMap()
	{
		Map<String,Object> ret = new HashMap<>();
		for(String k:keys())
		{
			Object curr = getValueAs(k, null, false);
			if(curr instanceof DataArray)
				ret.put(k, ((DataArray)curr).asJavaArray());
			else if(curr instanceof DataObject)
				ret.put(k, ((DataObject)curr).asJavaMap());
			else
				ret.put(k, curr);
		}
		
		return ret;
	}

	@Override
	public void remove(String key)
	{
		deleteKey(key);
	}
	
	@Override
	public boolean isNull(Object o)
	{
		return null == o;
	}
	
	@Override
	public DataReprezType getDataReprezType()
	{
		return DataReprezType.OBJECT;
	}
}
