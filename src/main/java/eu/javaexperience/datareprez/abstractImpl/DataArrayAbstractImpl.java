package eu.javaexperience.datareprez.abstractImpl;

import eu.javaexperience.datareprez.DataArray;
import eu.javaexperience.datareprez.DataObject;
import eu.javaexperience.datareprez.convertFrom.DataReprezType;

public abstract class DataArrayAbstractImpl extends DataProtocolAbstractImpl implements DataArray
{
	protected abstract <T> void setSubjectValue(int index, Class<T> cls, T value);
	protected abstract <T> T getValueAs(int index, Class<T> cls, boolean mayNull);
	
	protected <T> T getValueOpt(int index, Class<T> cls, T defaultValue)
	{
		T ret = getValueAs(index, cls, true);
		if(null != ret)
		{
			return ret;
		}
		
		return defaultValue;
	}
	
	@Override
	public void putString(int i, String val)
	{
		setSubjectValue(i, String.class, val);
	}

	@Override
	public void putLong(int i, long val)
	{
		setSubjectValue(i, long.class, val);
	}

	@Override
	public void putDouble(int i, double val)
	{
		setSubjectValue(i, double.class, val);
	}

	@Override
	public void putInt(int i, int val)
	{
		setSubjectValue(i, int.class, val);
	}

	@Override
	public void putBoolean(int i, boolean val)
	{
		setSubjectValue(i, boolean.class, val);
	}

	@Override
	public void putObject(int i, DataObject val)
	{
		setSubjectValue(i, DataObject.class, val);
	}

	@Override
	public void putArray(int i, DataArray val)
	{
		setSubjectValue(i, DataArray.class, val);
	}

	@Override
	public void putNull(int i)
	{
		setSubjectValue(i, void.class, null);
	}

	@Override
	public String getString(int i)
	{
		return getValueAs(i, String.class, false);
	}

	@Override
	public long getLong(int i)
	{
		return getValueAs(i, long.class, false);
	}

	@Override
	public double getDouble(int i)
	{
		return getValueAs(i, Double.class, false);
	}

	@Override
	public int getInt(int i)
	{
		return getValueAs(i, int.class, false);
	}

	@Override
	public boolean getBoolean(int i)
	{
		return getValueAs(i, boolean.class, false);
	}

	@Override
	public DataObject getObject(int i)
	{
		return getValueAs(i, DataObject.class, false);
	}

	@Override
	public DataArray getArray(int i)
	{
		return getValueAs(i, DataArray.class, false);
	}

	@Override
	public String optString(int i)
	{
		return getValueAs(i, String.class, true);
	}

	@Override
	public long optLong(int i)
	{
		return getValueAs(i, long.class, true);
	}

	@Override
	public double optDouble(int i)
	{
		return getValueAs(i, double.class, true);
	}

	@Override
	public int optInt(int i)
	{
		return getValueAs(i, int.class, true);
	}

	@Override
	public boolean optBoolean(int i)
	{
		return getValueAs(i, boolean.class, true);
	}

	@Override
	public DataObject optObject(int i)
	{
		return getValueAs(i, DataObject.class, true);
	}

	@Override
	public DataArray optArray(int i)
	{
		return getValueAs(i, DataArray.class, true);
	}

	@Override
	public String optString(int i, String def)
	{
		return getValueOpt(i, String.class, def);
	}

	@Override
	public long optLong(int i, long def)
	{
		return getValueOpt(i, long.class, def);
	}

	@Override
	public double optDouble(int i, double val)
	{
		return getValueOpt(i, double.class, val);
	}

	@Override
	public int optInt(int i, int def)
	{
		return getValueOpt(i, int.class, def);
	}

	@Override
	public boolean optBoolean(int i, boolean def)
	{
		return getValueOpt(i, boolean.class, def);
	}

	@Override
	public DataObject optObject(int i, DataObject def)
	{
		return getValueOpt(i, DataObject.class, def);
	}

	@Override
	public DataArray optArray(int i, DataArray def)
	{
		return getValueOpt(i, DataArray.class, def);
	}

	@Override
	public void unset(int i)
	{
		setSubjectValue(i, void.class, null);
	}

	@Override
	public boolean isString(int i)
	{
		return getValueAs(i, String.class, true) instanceof String;
	}

	@Override
	public boolean isLong(int i)
	{
		return getValueAs(i, Long.class, true) instanceof Long;
	}

	@Override
	public boolean isDouble(int i)
	{
		return getValueAs(i, Double.class, true) instanceof Double;
	}

	@Override
	public boolean isInt(int i)
	{
		return getValueAs(i, Integer.class, true) instanceof Integer;
	}

	@Override
	public boolean isBoolean(int i)
	{
		return getValueAs(i, Boolean.class, true) instanceof Boolean;
	}

	@Override
	public boolean isObject(int i)
	{
		return getValueAs(i, DataObject.class, true) instanceof DataObject;
	}

	@Override
	public boolean isArray(int i)
	{
		return getValueAs(i, DataObject.class, true) instanceof DataObject;
	}

	@Override
	public boolean isNull(int i)
	{
		return null == getValueAs(i, null, true);
	}

	@Override
	public void putString(String val)
	{
		setSubjectValue(size(), String.class, val);
	}

	@Override
	public void putLong(long val)
	{
		setSubjectValue(size(), long.class, val);
	}

	@Override
	public void putDouble(double val)
	{
		setSubjectValue(size(), double.class, val);
	}

	@Override
	public void putInt(int val)
	{
		setSubjectValue(size(), int.class, val);
	}

	@Override
	public void putBoolean(boolean val)
	{
		setSubjectValue(size(), boolean.class, val);
	}

	@Override
	public void putObject(DataObject val)
	{
		setSubjectValue(size(), DataObject.class, val);
	}

	@Override
	public void putArray(DataArray val)
	{
		setSubjectValue(size(), DataArray.class, val);
	}

	@Override
	public void putNull()
	{
		setSubjectValue(size(), void.class, null);
	}
	
	@Override
	public Object get(int key)
	{
		return getValueAs(key, null, false);
	}

	@Override
	public Object opt(int key)
	{
		return getValueAs(key, null, true);
	}

	@Override
	public Object opt(int key, Object obj)
	{
		Object o = opt(key);
		if(o == null)
			return obj;
		return o;
	}

	@Override
	public Object[] asJavaArray()
	{
		Object[] ret = new Object[size()];
		for(int i=0;i<ret.length;i++)
		{
			Object curr = get(i);
			if(curr instanceof DataArray)
				ret[i] = ((DataArray)curr).asJavaArray();
			else if(curr instanceof DataObject)
				ret[i] = ((DataObject)curr).asJavaMap();
			else
				ret[i] = curr;
		}
		
		return ret;
	}

	@Override
	public void putBlob(byte[] blob)
	{
		setSubjectValue(size(), byte[].class, blob);
	}

	@Override
	public void putBlob(int i, byte[] blob)
	{
		setSubjectValue(i, byte[].class, blob);
	}

	@Override
	public byte[] getBlob(int i)
	{
		return getValueAs(i, byte[].class, false);
	}

	@Override
	public byte[] optBlob(int i)
	{
		return getValueAs(i, byte[].class, true);
	}

	@Override
	public byte[] optBlob(int i, byte[] blob)
	{
		try
		{
			byte[] ret = getValueAs(i, byte[].class, true);
			if(null == ret)
			{
				return blob;
			}
			
			return ret;
		}
		catch(Exception e)
		{
			return blob;
		}
	}

	@Override
	public boolean isBlob(int i)
	{
		return getValueAs(i, byte[].class, true) instanceof byte[];
	}

	@Override
	public DataReprezType getDataReprezType()
	{
		return DataReprezType.ARRAY;
	}	
}
