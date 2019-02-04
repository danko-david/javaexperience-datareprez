package eu.javaexperience.datareprez.javaImpl;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

import eu.javaexperience.datareprez.DataArray;
import eu.javaexperience.datareprez.DataObject;
import eu.javaexperience.datareprez.convertFrom.DataReprezType;
import eu.javaexperience.io.SerializationTools;
import eu.javaexperience.reflect.Mirror;

public class DataArrayJavaImpl extends DataCommonJavaImpl implements DataArray, Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	Object[] arr = new Object[10];
	int ep = 0;
	
	public DataArrayJavaImpl(Object[] objects)
	{
		this.arr = objects;
		ep = arr.length;
	}

	public DataArrayJavaImpl()
	{
		this.arr = Mirror.emptyObjectArray;
		ep = 0;
	}
	
	private void i()
	{
		if(ep == arr.length)
			arr = Arrays.copyOf(arr, ep*2); 
	}
	
	private void i(int i)
	{
		if(i < arr.length)
			return;
		int ci = arr.length;
		while(i >= ci)
		{
			if(0 == ci)
			{
				ci = 1;
			}
			else
			{
				ci*= 2;
			}
		}
		arr = Arrays.copyOf(arr, ci);
	}
	
	@Override
	public void putString(String val)
	{
		i();
		arr[ep++] = val;
	}

	@Override
	public void putLong(long val)
	{
		i();
		arr[ep++] = val;		
	}

	@Override
	public void putDouble(double val)
	{
		i();
		arr[ep++] = val;	
	}

	@Override
	public void putInt(int val)
	{
		i();
		arr[ep++] = val;	
	}

	@Override
	public void putBoolean(boolean val)
	{
		i();
		arr[ep++] = val;		
	}

	@Override
	public void putObject(DataObject val)
	{
		i();
		if(val instanceof DataObjectJavaImpl)
			arr[ep++] = ((DataObjectJavaImpl)val).getImpl();
		else
			arr[ep++] = val;	
	}

	@Override
	public void putArray(DataArray val)
	{
		i();
		if(val instanceof DataArrayJavaImpl)
			arr[ep++] = ((DataArrayJavaImpl)val).getImpl();
		else
			arr[ep++] = val;	
	}

	@Override
	public void putBlob(byte[] blob)
	{
		i();
		arr[ep++] = blob;	
	}

	@Override
	public void putNull()
	{
		i();
		arr[ep++] = null;		
	}

	@Override
	public void putString(int i, String val)
	{
		i(i);
		arr[i] = val;
		ep = i+1;
	}

	@Override
	public void putLong(int i, long val)
	{
		i(i);
		arr[i] = val;
		ep = i+1;		
	}

	@Override
	public void putDouble(int i, double val)
	{
		i(i);
		arr[i] = val;
		ep = i+1;	
	}

	@Override
	public void putInt(int i, int val)
	{
		i(i);
		arr[i] = val;
		ep = i+1;		
	}

	@Override
	public void putBoolean(int i, boolean val)
	{
		i(i);
		arr[i] = val;
		ep = i+1;
	}

	@Override
	public void putObject(int i, DataObject val)
	{
		i(i);
		arr[i] = ((DataObjectJavaImpl)val).obj;
		ep = i+1;	
	}

	@Override
	public void putArray(int i, DataArray val)
	{
		i(i);
		arr[i] = ((DataArrayJavaImpl)val).arr;
		ep = i+1;		
	}

	@Override
	public void putBlob(int i, byte[] blob)
	{
		i(i);
		arr[i] = blob;
		ep = i+1;
	}

	@Override
	public void putNull(int i)
	{
		i(i);
		arr[i] = null;
		ep = i+1;	
	}

	@Override
	public String getString(int i)
	{
		return (String)arr[i];
	}

	@Override
	public long getLong(int i)
	{
		return (Long)arr[i];
	}

	@Override
	public double getDouble(int i)
	{
		return (Double)arr[i];
	}

	@Override
	public int getInt(int i)
	{
		return (Integer)arr[i];
	}

	@Override
	public boolean getBoolean(int i)
	{
		return (Boolean)arr[i];
	}

	@Override
	public DataObject getObject(int i)
	{
		return new DataObjectJavaImpl((Map<String,Object>)arr[i]);
	}

	@Override
	public DataArray getArray(int i)
	{
		return new DataArrayJavaImpl((Object[])arr[i]);
	}

	@Override
	public byte[] getBlob(int i)
	{
		return (byte[])arr[i];
	}

	@Override
	public Object get(int i)
	{
		Object o = arr[i];
		if(o == null)
			return null;
		if(o instanceof Map)
			return new DataObjectJavaImpl((Map<String,Object>) o);
		if(o.getClass().isArray())
			return new DataArrayJavaImpl((Object[])o);
		return o;
	}

	@Override
	public String optString(int i)
	{
		Object o = arr[i];
		if(o instanceof String)
			return (String)o;
		return null;
	}

	@Override
	public long optLong(int i)
	{
		Object o = arr[i];
		if(o instanceof Long)
			return (Long)o;
		return 0;
	}

	@Override
	public double optDouble(int i)
	{
		Object o = arr[i];
		if(o instanceof Double)
			return (Double)o;
		return 0.0;
	}

	@Override
	public int optInt(int i)
	{
		Object o = arr[i];
		if(o instanceof Integer)
			return (Integer)o;
		return 0;
	}

	@Override
	public boolean optBoolean(int i)
	{
		Object o = arr[i];
		if(o instanceof Boolean)
			return (Boolean)o;
		return false;
	}

	@Override
	public DataObject optObject(int i)
	{
		Object o = arr[i];
		if(o instanceof Map)
			return new DataObjectJavaImpl((Map<String,Object>)o);
		return null;
	}

	@Override
	public DataArray optArray(int i)
	{
		Object o = arr[i];
		if(o == null)
			return null;
		if(o.getClass().isArray())
			return new DataArrayJavaImpl((Object[])o);
		return null;
	}

	@Override
	public byte[] optBlob(int i)
	{
		Object o = arr[i];
		if(o instanceof byte[])
			return (byte[])o;
		return null;
	}

	@Override
	public Object opt(int i)
	{
		Object o = arr[i];
		if(o == null)
			return null;
		if(o instanceof Map)
			return new DataObjectJavaImpl((Map<String,Object>)o);
		if(o.getClass().isArray())
			return new DataArrayJavaImpl((Object[])o);
		return o;
	}

	@Override
	public String optString(int i, String def) {
		Object o = arr[i];
		if(o instanceof String)
			return (String)o;
		return def;
	}

	@Override
	public long optLong(int i, long def)
	{
		Object o = arr[i];
		if(o instanceof Long)
			return (Long)o;
		return def;
	}

	@Override
	public double optDouble(int i, double val)
	{
		Object o = arr[i];
		if(o instanceof Double)
			return (Double)o;
		return val;
	}

	@Override
	public int optInt(int i, int def)
	{
		Object o = arr[i];
		if(o instanceof Integer)
			return (Integer)o;
		return def;

	}

	@Override
	public boolean optBoolean(int i, boolean def)
	{
		Object o = arr[i];
		if(o instanceof Boolean)
			return (Boolean)o;
		return def;
	}

	@Override
	public DataObject optObject(int i, DataObject def)
	{
		Object o = arr[i];
		if(o instanceof Map)
			return new DataObjectJavaImpl((Map<String,Object>)o);
		return def;
	}

	@Override
	public DataArray optArray(int i, DataArray def)
	{
		Object o = arr[i];
		if(o == null)
			return def;
		if(o.getClass().isArray())
			return new DataArrayJavaImpl((Object[])o);
		return def;
	}

	@Override
	public byte[] optBlob(int i, byte[] blob)
	{
		Object o = arr[i];
		if(o instanceof byte[])
			return (byte[])o;
		return blob;
	}

	@Override
	public Object opt(int i, Object def)
	{
		Object o = arr[i];
		if(o== null)
			return def;
		if(o instanceof Map)
			return new DataObjectJavaImpl((Map<String,Object>) o);
		if(o.getClass().isArray())
			return new DataArrayJavaImpl((Object[])o);
		return o;
	}

	@Override
	public void unset(int i)
	{
		if(i>ep)
			return;
		if( i == ep-1)
			ep--;
		arr[i] = null;
	}

	@Override
	public int size()
	{
		return ep;
	}

	@Override
	public boolean isString(int i)
	{
		return arr[i] instanceof String;
	}

	@Override
	public boolean isLong(int i)
	{
		return arr[i] instanceof Long;
	}

	@Override
	public boolean isDouble(int i)
	{
		return arr[i] instanceof Double;
	}

	@Override
	public boolean isInt(int i)
	{
		return arr[i] instanceof Integer;
	}

	@Override
	public boolean isBoolean(int i)
	{
		return arr[i] instanceof Boolean;
	}

	@Override
	public boolean isObject(int i)
	{
		return arr[i] instanceof Map;
	}

	@Override
	public boolean isArray(int i)
	{
		Object o = arr[i];
		if(o == null)
			return false;
		return o.getClass().isArray();
	}

	@Override
	public boolean isNull(int i)
	{
		return arr[i] == null;
	}

	@Override
	public boolean isBlob(int i)
	{
		return arr[i] instanceof byte[];
	}

	@Override
	public Object[] asJavaArray()
	{
		return Arrays.copyOf(arr, ep);
	}

	@Override
	public Object getImpl()
	{
		return arr;
	}

	@Override
	public Iterator<Object> iterator()
	{
		return new Iterator<Object>()
		{
			protected int cep = 0;
			@Override
			public boolean hasNext()
			{
				return cep < ep;
			}

			@Override
			public Object next()
			{
				return arr[cep++];
			}

			@Override
			public void remove()
			{
				throw new UnsupportedOperationException("removing is not supported");
			}
		};
	}

	@Override
	public DataReprezType getDataReprezType()
	{
		return DataReprezType.ARRAY;
	}
	
	@Override
	public byte[] toBlob()
	{
		return SerializationTools.serializeIntoBlob((Serializable) arr);
	}
}