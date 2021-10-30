package eu.javaexperience.datareprez.jsonImpl;

import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

import eu.javaexperience.datareprez.abstractImpl.DataArrayAbstractImpl;
import eu.javaexperience.datareprez.abstractImpl.DataProtocol;
import eu.javaexperience.datareprez.convertFrom.DataReprezType;
import eu.javaexperience.text.Format;

public class DataArrayJsonImpl extends DataArrayAbstractImpl
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
	
	@Override
	public String toString()
	{
		return "DataArrayJsonImpl: "+arr;
	}
	
	public Class getCommonsClass()
	{
		return JSONObject.class;
	}

	@Override
	public int size()
	{
		return arr.length();
	}
	
	@Override
	protected <T> void setSubjectValue(int index, Class<T> cls, T value)
	{
		if(null == value && cls == null)
		{
			arr.remove(index);
		}
		else
		{
			Object in = DataCommonJsonImpl.wrapObjectToStore(value, cls);
			if(null != in && byte[].class == cls)
			{
				in = Format.base64Encode((byte[]) in);
			}
			
			arr.put(index, null == in?JSONObject.NULL:in);
		}
	}

	@Override
	protected <T> T getValueAs(int index, Class<T> cls)
	{
		Object o = null;
		if(index < arr.length())
		{
			o = arr.get(index);
		}
		return DataCommonJsonImpl.castToType(o, cls);
	}

	@Override
	protected DataProtocol getProtocolHandler()
	{
		return DataCommonJsonImpl.PROTOCOL;
	}
}