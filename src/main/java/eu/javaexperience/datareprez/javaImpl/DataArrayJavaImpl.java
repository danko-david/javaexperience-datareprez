package eu.javaexperience.datareprez.javaImpl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import eu.javaexperience.collection.CollectionTools;
import eu.javaexperience.datareprez.abstractImpl.DataArrayAbstractImpl;
import eu.javaexperience.datareprez.abstractImpl.DataProtocol;
import eu.javaexperience.io.SerializationTools;

public class DataArrayJavaImpl extends DataArrayAbstractImpl
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected List<Object> arr;
	
	public DataArrayJavaImpl()
	{
		this.arr = new ArrayList<>();
	}
	
	public DataArrayJavaImpl(List<Object> objects)
	{
		this.arr = objects;
	}

	@Override
	public String toString()
	{
		return "DataArrayJavaImpl: "+CollectionTools.toString(arr);
	}
	
	@Override
	public Class getCommonsClass()
	{
		return DataCommonJavaImpl.PROTOTYPE.getCommonsClass();
	}

	@Override
	protected <T> void setSubjectValue(int index, Class<T> cls, T value)
	{
		if(null == value && null == cls)
		{
			arr.remove(index);
		}
		else
		{
			arr.add(index, DataCommonJavaImpl.wrapObjectToStore(value, cls));
		}
	}

	@Override
	protected <T> T getValueAs(int index, Class<T> cls)
	{
		return DataCommonJavaImpl.castToType(arr.get(index), cls);
	}

	@Override
	protected DataProtocol getProtocolHandler()
	{
		return DataCommonJavaImpl.PROTOCOL;
	}

	@Override
	public Object getImpl()
	{
		return arr;
	}

	@Override
	public byte[] toBlob()
	{
		return SerializationTools.serializeIntoBlob((Serializable) arr);
	}

	@Override
	public Iterator<Object> iterator()
	{
		return arr.iterator();
	}

	@Override
	public int size()
	{
		return arr.size();
	}
}