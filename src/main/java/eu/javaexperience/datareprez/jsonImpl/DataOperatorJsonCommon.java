package eu.javaexperience.datareprez.jsonImpl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONObject;

import eu.javaexperience.datareprez.DataArray;
import eu.javaexperience.datareprez.DataCommon;
import eu.javaexperience.datareprez.DataCommonAbstractImpl;
import eu.javaexperience.datareprez.DataObject;
import eu.javaexperience.datareprez.DataReceiver;
import eu.javaexperience.datareprez.DataSender;

public abstract class DataOperatorJsonCommon extends DataCommonAbstractImpl
{
	static DataObjectJsonImpl objPrototype = new DataObjectJsonImpl();
	static DataArrayJsonImpl arrPrototype = new DataArrayJsonImpl();
	
	@Override
	public DataObject newObjectInstance()
	{
		return new DataObjectJsonImpl();
	}

	@Override
	public DataArray newArrayInstance()
	{
		return new DataArrayJsonImpl();
	}

	@Override
	public void sendDataObject(DataObject dat, OutputStream os) throws IOException
	{
		DataOperatorJsonCommon.sendObject(dat,os);
	}

	public static DataObject receiveObject(InputStream is) throws IOException
	{
		byte b[] = new byte[1000];
		int ep =0;
		int i=0;
		if((i=is.read())==-1)
			throw new IOException("A socket bezárult");
		b[ep]=(byte)i;
		ep++;
		while((i=is.read())!=-1)
		{
			if(i==10)
				break;
			b[ep]=(byte)i;
			ep++;
			if(ep==b.length) b = Arrays.copyOf(b, ep*2);
		}
		String ki = new String(Arrays.copyOf(b, ep));
		return ki.length()==0?null:new DataObjectJsonImpl(new JSONObject(ki));
	}
	
	public static void sendObject(DataObject dat,OutputStream os) throws IOException
	{
		os.write(((DataObjectJsonImpl)dat).obj.toString().getBytes());
		os.write('\n');
		os.flush();
	}

	public static void sendArray(DataArray dat,OutputStream os) throws IOException
	{
		os.write(((DataArrayJsonImpl)dat).arr.toString().getBytes());
		os.write('\n');
		os.flush();
	}
	
	public static DataArray receiveArray(InputStream is) throws IOException
	{
		byte b[] = new byte[1000];
		int ep =0;
		int i=0;
		if((i=is.read())==-1)
			throw new IOException("A socket bezárult");
		b[ep]=(byte)i;
		ep++;
		while((i=is.read())!=-1)
		{
			if(i==10)
				break;
			b[ep]=(byte)i;
			ep++;
			if(ep==b.length) b = Arrays.copyOf(b, ep*2);
		}
		String ki = new String(Arrays.copyOf(b, ep));
		return ki.length()==0?null:new DataArrayJsonImpl(new JSONArray(ki));
	}
	
	@Override
	public DataObject receiveDataObject(InputStream is) throws IOException
	{
		return receiveObject(is);
	}

	@Override
	public void sendDataArray(DataArray dat, OutputStream os) throws IOException
	{
		sendArray(dat, os);
	}

	@Override
	public DataArray receiveDataArray(InputStream is) throws IOException
	{
		return receiveArray(is);
	}

	@Override
	public DataSender newDataSender(final OutputStream os)
	{
		return new JsonDataSender(os);
	}

	@Override
	public DataReceiver newDataReceiver(final InputStream is)
	{
		return new JsonDataReceiver(is);
	}
	/*
	@Override
	public DataArray fromJavaArray(Object[] in)
	{
		JSONArray arr = new JSONArray();
		for(Object o:in)
			if(o == null)
				arr.put(JSONObject.NULL);
			else if(o instanceof Map)
				arr.put(DataOperatorJsonCommon.objPrototype.fromJavaMap((Map<String,Object>) o));
			else if(o.getClass().isArray())
				arr.put(fromJavaArray((Object[]) o));
			else if(o instanceof DataArrayJsonImpl)
				arr.put(((DataArrayJsonImpl)o).arr);
			else if(o instanceof DataObjectJsonImpl)
				arr.put(((DataObjectJsonImpl)o).obj);
			else if(o instanceof DataArray)
				arr.put(fromJavaArray(((DataArray)o).asJavaArray()));
			else if(o instanceof DataObject)
				arr.put(DataOperatorJsonCommon.objPrototype.fromJavaMap(((DataObject)o).asJavaMap()));
			else
				arr.put(o);
		
			return new DataArrayJsonImpl(arr);
	}
	
	@Override
	public DataObject fromJavaMap(Map<String, Object> map)
	{
		JSONObject obj = new JSONObject();
		
		for(Entry<String, Object> kv:map.entrySet())
		{
			Object o = kv.getValue();
			if(o == null)
				obj.put(kv.getKey(),JSONObject.NULL);
			else if(o instanceof Map)
				obj.put(kv.getKey(),fromJavaMap((Map<String,Object>) o));
			else if(o.getClass().isArray())
				obj.put(kv.getKey(),DataOperatorJsonCommon.arrPrototype.fromJavaArray((Object[]) o));
			else if(o instanceof DataArrayJsonImpl)
				obj.put(kv.getKey(),((DataArrayJsonImpl)o).arr);
			else if(o instanceof DataObjectJsonImpl)
				obj.put(kv.getKey(),((DataObjectJsonImpl)o).obj);
			else if(o instanceof DataArray)
				obj.put(kv.getKey(),DataOperatorJsonCommon.arrPrototype.fromJavaArray(((DataArray)o).asJavaArray()));
			else if(o instanceof DataObject)
				obj.put(kv.getKey(),objPrototype.fromJavaMap(((DataObject)o).asJavaMap()));
			else
				obj.put(kv.getKey(),o);
		}

		return new DataObjectJsonImpl(obj);
	}
	*/
	@Override
	public boolean isNull(Object o)
	{
		return null == o || o == JSONObject.NULL;
	}
	
	@Override
	public DataArray arrayFromBlob(byte[] data)
	{
		return new DataArrayJsonImpl(new JSONArray(new String(data)));
	}

	@Override
	public DataObject objectFromBlob(byte[] data)
	{
		return new DataObjectJsonImpl(new JSONObject(new String(data)));
	}
	
	@Override
	public Class getCommonsClass()
	{
		return JSONObject.class;
	}
	
	@Override
	public Object getImpl()
	{
		return null;
	}
	
	@Override
	public byte[] toBlob()
	{
		return null;
	}
}