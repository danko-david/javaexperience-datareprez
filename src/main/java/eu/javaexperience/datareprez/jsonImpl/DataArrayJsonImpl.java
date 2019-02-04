package eu.javaexperience.datareprez.jsonImpl;

import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

import eu.javaexperience.datareprez.DataArray;
import eu.javaexperience.datareprez.DataObject;
import eu.javaexperience.datareprez.convertFrom.DataReprezType;
import eu.javaexperience.text.Format;

public class DataArrayJsonImpl extends DataOperatorJsonCommon implements DataArray
{
	final JSONArray arr;
	
	public DataArrayJsonImpl(JSONArray arr)
	{
		this.arr = arr;
	}
	
	public DataArrayJsonImpl()
	{
		arr = new JSONArray();
	}

	@Override
	public void putString(int i, String val)
	{
		arr.put(i, val);
	}

	@Override
	public void putLong(int i, long val)
	{
		arr.put(i, val);
	}

	@Override
	public void putDouble(int i, double val)
	{
		arr.put(i, val);	
	}

	@Override
	public void putInt(int i, int val)
	{
		arr.put(i, val);
	}

	@Override
	public void putBoolean(int i, boolean val)
	{
		arr.put(i, val);
	}

	@Override
	public void putObject(int i, DataObject val)
	{
		arr.put(i, ((DataObjectJsonImpl)val).obj);	
	}

	@Override
	public void putArray(int i, DataArray val)
	{
		arr.put(i, ((DataArrayJsonImpl)val).arr);	
	}

	@Override
	public void putNull(int i)
	{
		arr.put(i, JSONObject.NULL);
	}

	@Override
	public String getString(int i)
	{
		return arr.getString(i);
	}

	@Override
	public long getLong(int i)
	{
		return arr.getLong(i);
	}

	@Override
	public double getDouble(int i)
	{
		return arr.getDouble(i);
	}

	@Override
	public int getInt(int i)
	{
		return arr.getInt(i);
	}

	@Override
	public boolean getBoolean(int i)
	{
		return arr.getBoolean(i);
	}

	@Override
	public DataObject getObject(int i)
	{
		JSONObject obj = arr.getJSONObject(i);
		if(obj == null)
			return null;
		
		return new DataObjectJsonImpl(obj);
	}

	@Override
	public DataArray getArray(int i)
	{
		JSONArray a = arr.getJSONArray(i);
		if(a == null)
			return null;
		
		return new DataArrayJsonImpl(a);
	}

	@Override
	public String optString(int i)
	{
		return arr.optString(i);
	}

	@Override
	public long optLong(int i)
	{
		return arr.optLong(i);
	}

	@Override
	public double optDouble(int i)
	{
		return arr.getDouble(i);
	}

	@Override
	public int optInt(int i)
	{
		return arr.getInt(i);
	}

	@Override
	public boolean optBoolean(int i)
	{
		return arr.getBoolean(i);
	}

	@Override
	public DataObject optObject(int i)
	{
		JSONObject o = arr.optJSONObject(i);
		if(o == null)
			return null;
		
		return new DataObjectJsonImpl(o);
	}

	@Override
	public DataArray optArray(int i)
	{
		JSONArray o = arr.optJSONArray(i);
		if(o == null)
			return null;
		
		return new DataArrayJsonImpl(o);
	}

	@Override
	public String optString(int i, String def)
	{
		return arr.optString(i, def);
	}

	@Override
	public long optLong(int i, long def)
	{
		return arr.optLong(i,def);
	}

	@Override
	public double optDouble(int i, double val)
	{
		return arr.optDouble(i,val);
	}

	@Override
	public int optInt(int i, int def)
	{
		return arr.optInt(i,def);
	}

	@Override
	public boolean optBoolean(int i, boolean def)
	{
		return arr.optBoolean(i, def);
	}

	@Override
	public DataObject optObject(int i, DataObject def)
	{
		JSONObject o = arr.optJSONObject(i);
		if(o == null)
			return def;
		
		return new DataObjectJsonImpl(o);
	}

	@Override
	public DataArray optArray(int i, DataArray def)
	{
		JSONArray o = arr.optJSONArray(i);
		if(o == null)
			return def;
		
		return new DataArrayJsonImpl(o);
	}

	@Override
	public void unset(int i)
	{
		arr.remove(i);
	}

	@Override
	public int size()
	{
		return arr.length();
	}

	@Override
	public boolean isString(int i)
	{
		return (arr.get(i) instanceof String);
	}

	@Override
	public boolean isLong(int i)
	{
		return (arr.get(i) instanceof Long);
	}

	@Override
	public boolean isDouble(int i)
	{
		return (arr.get(i) instanceof Double);
	}

	@Override
	public boolean isInt(int i)
	{
		return (arr.get(i) instanceof Integer);
	}

	@Override
	public boolean isBoolean(int i)
	{
		return (arr.get(i) instanceof Boolean);
	}

	@Override
	public boolean isObject(int i)
	{
		return (arr.get(i) instanceof JSONObject);
	}

	@Override
	public boolean isArray(int i)
	{
		return (arr.get(i) instanceof JSONArray);
	}

	@Override
	public boolean isNull(int i)
	{
		return arr.isNull(i);
	}

	@Override
	public void putString(String val)
	{
		arr.put(val);
	}

	@Override
	public void putLong(long val)
	{
		arr.put(val);		
	}

	@Override
	public void putDouble(double val)
	{
		arr.put(val);		
	}

	@Override
	public void putInt(int val)
	{
		arr.put(val);		
	}

	@Override
	public void putBoolean(boolean val)
	{
		arr.put(val);		
	}

	@Override
	public void putObject(DataObject val)
	{
		arr.put(((DataObjectJsonImpl)val).obj);		
	}

	@Override
	public void putArray(DataArray val)
	{
		arr.put(((DataArrayJsonImpl)val).arr);
	}

	@Override
	public void putNull()
	{
		arr.put(JSONObject.NULL);
	}
	
	@Override
	public Object get(int key)
	{
		Object o = arr.get(key);
		if(o instanceof JSONObject)
			return new DataObjectJsonImpl((JSONObject) o);
		if(o instanceof JSONArray)
			return new DataArrayJsonImpl((JSONArray) o);
		return o;
	}

	@Override
	public Object opt(int key)
	{
		Object o = arr.opt(key);
		if(o instanceof JSONObject)
			return new DataObjectJsonImpl((JSONObject) o);
		if(o instanceof JSONArray)
			return new DataArrayJsonImpl((JSONArray) o);
		return o;
	}

	@Override
	public Object opt(int key, Object obj)
	{
		Object o = arr.opt(key);
		if(o == null)
			return obj;
		if(o instanceof JSONObject)
			return new DataObjectJsonImpl((JSONObject) o);
		if(o instanceof JSONArray)
			return new DataArrayJsonImpl((JSONArray) o);
		return o;
	}

	@Override
	public Object[] asJavaArray()
	{
		Object[] ret = new Object[arr.length()];
		for(int i=0;i<ret.length;i++)
		{
			Object curr = arr.get(i);
			if(isNull(curr))
			{
				ret[i] = null;
			}
			else if(curr instanceof JSONArray)
			{
				ret[i] = new DataArrayJsonImpl((JSONArray)curr).asJavaArray();
			}
			else if(curr instanceof JSONObject)
			{
				ret[i] = new DataObjectJsonImpl((JSONObject)curr).asJavaMap();
			}
			else if(curr instanceof DataArray)
			{
				ret[i] = ((DataArray)curr).asJavaArray();
			}
			else if(curr instanceof DataObject)
			{
				ret[i] = ((DataObject)curr).asJavaMap();
			}
			else
			{
				ret[i] = curr;
			}
		}
		
		return ret;
	}

	@Override
	public void putBlob(byte[] blob)
	{
		arr.put(Format.base64Encode(blob));
	}

	@Override
	public void putBlob(int i, byte[] blob)
	{
		arr.put(i, Format.base64Encode(blob));
	}

	@Override
	public byte[] getBlob(int i)
	{
		return Format.base64Decode(arr.getString(i));
	}

	@Override
	public byte[] optBlob(int i)
	{
		try
		{
			return Format.base64Decode(arr.getString(i));
		}
		catch(Exception e)
		{
			return null;
		}
	}

	@Override
	public byte[] optBlob(int i, byte[] blob)
	{
		try
		{
			return Format.base64Decode(arr.getString(i));
		}
		catch(Exception e)
		{
			return blob;
		}
	}

	@Override
	public boolean isBlob(int i)
	{
		try
		{
			Format.base64Decode(arr.getString(i));
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}

	@Override
	public Object getImpl()
	{
		return arr;
	}

	@Override
	public Iterator<Object> iterator()
	{
		return arr.iterator();
	}

	@Override
	public DataReprezType getDataReprezType()
	{
		return DataReprezType.ARRAY;
	}

	@Override
	public byte[] toBlob()
	{
		return arr.toString().getBytes();
	}
}