package eu.javaexperience.datareprez.jsonImpl;

import org.json.JSONObject;

import eu.javaexperience.datareprez.DataCommon;
import eu.javaexperience.datareprez.abstractImpl.DataObjectAbstractImpl;
import eu.javaexperience.datareprez.abstractImpl.DataProtocol;
import eu.javaexperience.reflect.Mirror;
import eu.javaexperience.text.Format;

public class DataObjectJsonImpl extends DataObjectAbstractImpl
{
	public static DataCommon PROTOTYPE = new DataObjectJsonImpl();
	
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
	public byte[] toBlob()
	{
		return obj.toString().getBytes();
	}
	
	@Override
	public String toString()
	{
		return "DataObjectJsonImpl: "+obj;
	}

	@Override
	public Object getImpl()
	{
		return obj;
	}

	@Override
	public Class getCommonsClass()
	{
		return JSONObject.class;
	}

	@Override
	public boolean has(String key)
	{
		return obj.has(key);
	}

	@Override
	public String[] keys()
	{
		return obj.keySet().toArray(Mirror.emptyStringArray);
	}

	@Override
	protected void setSubjectValue(String key, Class<?> valueType, Object val)
	{
		if(null == val && valueType != void.class)
		{
			obj.remove(key);
		}
		else
		{
			Object in = DataCommonJsonImpl.wrapObjectToStore(val, valueType);
			if(null != in && byte[].class == valueType)
			{
				in = Format.base64Encode((byte[]) in);
			}
			
			obj.put(key, null == in?JSONObject.NULL:in);
		}
	}

	@Override
	protected <T> T getValueAs(String key, Class<T> cls)
	{
		Object o = null;
		if(obj.has(key))
		{
			o = obj.get(key);
		}
		
		if(JSONObject.NULL == o)
		{
			return null;
		}
		
		if(null != o)
		{
			return DataCommonJsonImpl.castToType(o, cls);
		}
		return null;
	}

	@Override
	protected DataProtocol getProtocolHandler()
	{
		return DataCommonJsonImpl.PROTOCOL;
	}
}