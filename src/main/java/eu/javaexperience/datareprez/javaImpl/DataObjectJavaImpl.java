package eu.javaexperience.datareprez.javaImpl;

import java.io.Serializable;
import java.util.Map;

import eu.javaexperience.collection.map.MapTools;
import eu.javaexperience.collection.map.SmallMap;
import eu.javaexperience.datareprez.DataCommon;
import eu.javaexperience.datareprez.abstractImpl.DataObjectAbstractImpl;
import eu.javaexperience.datareprez.abstractImpl.DataProtocol;
import eu.javaexperience.io.SerializationTools;
import eu.javaexperience.reflect.Mirror;

public class DataObjectJavaImpl extends DataObjectAbstractImpl
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static DataCommon PROTOTYPE = new DataObjectJavaImpl();
	
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
	public String toString()
	{
		return "DataObjectJavaImpl: "+MapTools.toStringMultiline(obj);
	}

	@Override
	public Object getImpl()
	{
		return obj;
	}

	@Override
	public Class getCommonsClass()
	{
		return DataCommonJavaImpl.PROTOCOL.getCommonsClass();
	}

	@Override
	public byte[] toBlob()
	{
		return SerializationTools.serializeIntoBlob((Serializable) obj);
	}

	@Override
	public String[] keys()
	{
		return obj.keySet().toArray(Mirror.emptyStringArray);
	}
	
	@Override
	public boolean has(String key)
	{
		return obj.containsKey(key);
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
			obj.put(key, DataCommonJavaImpl.wrapObjectToStore(val, valueType));
		}
	}

	@Override
	protected <T> T getValueAs(String key, Class<T> cls)
	{
		Object o = obj.get(key);
		if(null != o)
		{
			return DataCommonJavaImpl.castToType(o, cls);
		}
		return null;
	}

	@Override
	protected DataProtocol getProtocolHandler()
	{
		return DataCommonJavaImpl.PROTOCOL;
	}
}