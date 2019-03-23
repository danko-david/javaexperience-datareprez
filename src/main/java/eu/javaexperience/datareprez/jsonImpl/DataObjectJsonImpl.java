package eu.javaexperience.datareprez.jsonImpl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import eu.javaexperience.collection.iterator.IteratorTools;
import eu.javaexperience.datareprez.DataArray;
import eu.javaexperience.datareprez.DataCommon;
import eu.javaexperience.datareprez.DataObject;
import eu.javaexperience.datareprez.convertFrom.DataReprezType;
import eu.javaexperience.reflect.Mirror;
import eu.javaexperience.text.Format;

public class DataObjectJsonImpl extends DataOperatorJsonCommon implements DataObject 
{
	public static DataCommon instane = new DataObjectJsonImpl();
	
	final JSONObject obj;

	public DataObjectJsonImpl()
	{
		obj = new JSONObject();
	}
	
	public DataObjectJsonImpl(JSONObject obj)
	{
		this.obj = obj;
	}

	@Override
	public void putString(String key, String val)
	{
		obj.put(key, val);
	}

	@Override
	public void putLong(String key, long val)
	{
		obj.put(key, val);
	}

	@Override
	public void putDouble(String key, double val)
	{
		obj.put(key, val);
	}

	@Override
	public void putInt(String key, int val)
	{
		obj.put(key, val);
	}


	@Override
	public void putBoolean(String key, boolean val)
	{
		obj.put(key, val);
	}

	@Override
	public void putObject(String key, DataObject val)
	{
		obj.put(key, ((DataObjectJsonImpl)val).obj);
	}

	@Override
	public void putNull(String key)
	{
		obj.put(key, JSONObject.NULL);
	}
	
	@Override
	public void putArray(String key, DataArray val)
	{
		obj.put(key, ((DataArrayJsonImpl)val).arr);
	}

	@Override
	public String getString(String key)
	{
		return obj.getString(key);
	}

	@Override
	public long getLong(String key)
	{
		return obj.getLong(key);
	}

	@Override
	public double getDouble(String key)
	{
		return obj.getDouble(key);
	}

	@Override
	public int getInt(String key)
	{
		return obj.getInt(key);
	}

	@Override
	public boolean getBoolean(String key)
	{
		return obj.getBoolean(key);
	}

	@Override
	public DataObject getObject(String key)
	{
		return new DataObjectJsonImpl(obj.getJSONObject(key));
	}

	@Override
	public DataArray getArray(String key)
	{
		return new DataArrayJsonImpl(obj.getJSONArray(key));
	}

	@Override
	public String optString(String key)
	{
		if(!obj.has(key))
		{
			return null;
		}
		return obj.optString(key);
	}

	@Override
	public Long optLong(String key)
	{
		if(!obj.has(key))
		{
			return null;
		}
		return obj.optLong(key);
	}

	@Override
	public Double optDouble(String key)
	{
		if(!obj.has(key))
		{
			return null;
		}
		return obj.optDouble(key);
	}

	@Override
	public Integer optInt(String key)
	{
		if(!obj.has(key))
		{
			return null;
		}
		return obj.optInt(key);
	}

	@Override
	public Boolean optBoolean(String key)
	{
		if(!obj.has(key))
		{
			return null;
		}
		return obj.optBoolean(key);
	}

	@Override
	public DataObject optObject(String key)
	{
		JSONObject o = obj.optJSONObject(key);
		if(o == null)
			return null;
		return new DataObjectJsonImpl(o);
	}

	@Override
	public DataArray optArray(String key)
	{
		JSONArray o = obj.optJSONArray(key);
		if(o == null)
			return null;
		return new DataArrayJsonImpl(o);
	}

	@Override
	public String optString(String key, String def)
	{
		return obj.optString(key, def);
	}

	@Override
	public long optLong(String key, long def)
	{
		return obj.optLong(key, def);
	}

	@Override
	public double optDouble(String key, double val)
	{
		return obj.optDouble(key,val);
	}

	@Override
	public int optInt(String key, int def)
	{
		return obj.optInt(key, def);
	}

	@Override
	public boolean optBoolean(String key, boolean def)
	{
		return obj.optBoolean(key,def);
	}

	@Override
	public DataObject optObject(String key, DataObject def)
	{
		JSONObject o = obj.optJSONObject(key);
		if(o == null)
			return def;
		return new DataObjectJsonImpl(o);
	}

	@Override
	public DataArray optArray(String key, DataArray def)
	{
		JSONArray a = obj.optJSONArray(key);
		if(a == null)
			return def;
		return new DataArrayJsonImpl(a);
	}

	@Override
	public boolean has(String key)
	{
		return obj.has(key);
	}

	@Override
	public String[] keys()
	{
		return (String[]) obj.keySet().toArray(Mirror.emptyStringArray);
	}

	@Override
	public int size()
	{
		return obj.keySet().size();
	}

	@Override
	public boolean isString(String key)
	{
		return obj.optString(key) != null;
	}

	@Override
	public boolean isLong(String key)
	{
		return Long.class.equals(obj.get(key).getClass());
	}

	@Override
	public boolean isDouble(String key)
	{
		return Double.class.equals(obj.get(key).getClass());
	}

	@Override
	public boolean isInt(String key)
	{
		return Integer.class.equals(obj.get(key).getClass());
	}

	@Override
	public boolean isBoolean(String key)
	{
		return Boolean.class.equals(obj.get(key).getClass());
	}

	@Override
	public boolean isObject(String key)
	{
		return (obj.get(key) instanceof JSONObject);
	}

	@Override
	public boolean isArray(String key)
	{
		return (obj.get(key) instanceof JSONArray);
	}

	@Override
	public boolean isNull(String key)
	{
		return obj.isNull(key);
	}

	@Override
	public Object get(String key)
	{
		Object o = obj.get(key);
		if(o instanceof JSONObject)
			return new DataObjectJsonImpl((JSONObject) o);
		if(o instanceof JSONArray)
			return new DataArrayJsonImpl((JSONArray) o);
		return o;
	}

	@Override
	public Object opt(String key)
	{
		Object o = obj.opt(key);
		if(o instanceof JSONObject)
			return new DataObjectJsonImpl((JSONObject) o);
		if(o instanceof JSONArray)
			return new DataArrayJsonImpl((JSONArray) o);
		return o;
	}

	@Override
	public Object opt(String key, Object obj)
	{
		Object o = this.obj.opt(key);
		if(o == null)
			return obj;
		if(o instanceof JSONObject)
			return new DataObjectJsonImpl((JSONObject) o);
		if(o instanceof JSONArray)
			return new DataArrayJsonImpl((JSONArray) o);
		return o;
	}

	@Override
	public Map<String, Object> asJavaMap()
	{
		Map<String,Object> ret = new HashMap<>();
		
		for(String k:IteratorTools.wrapIterator(obj.keys()))
		{
			Object curr = obj.get(k);
			if(isNull(curr))
			{
				ret.put(k, null);
			}
			else if(curr instanceof JSONArray)
			{
				ret.put(k, new DataArrayJsonImpl((JSONArray)curr).asJavaArray());
			}
			else if(curr instanceof JSONObject)
			{
				ret.put(k, new DataObjectJsonImpl((JSONObject)curr).asJavaMap());
			}
			else if(curr instanceof DataArray)
			{
				ret.put(k, ((DataArray)curr).asJavaArray());
			}
			else if(curr instanceof DataObject)
			{
				ret.put(k, ((DataObject)curr).asJavaMap());
			}
			else
			{
				ret.put(k, curr);
			}
		}
		
		return ret;
	}

	@Override
	public void putBlob(String key, byte[] blob)
	{
		obj.put(key, Format.base64Encode(blob));
	}

	@Override
	public byte[] getBlob(String key)
	{
		return Format.base64Decode(obj.getString(key));
	}

	@Override
	public byte[] optBlob(String key)
	{
		try
		{
			return Format.base64Decode(obj.getString(key));
		}
		catch(Exception e)
		{
			return null;
		}
	}

	@Override
	public byte[] optBlob(String key, byte[] blob)
	{
		try
		{
			return Format.base64Decode(obj.getString(key));	
		}
		catch(Exception e)
		{
			return blob;
		}
	}

	@Override
	public boolean isBlob(String key)
	{
		try
		{
			Format.base64Decode(obj.getString(key));	
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
		return obj.toString().getBytes();
	}
}