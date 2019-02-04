package eu.javaexperience.datareprez.javaImpl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import eu.javaexperience.datareprez.DataArray;
import eu.javaexperience.datareprez.DataCommon;
import eu.javaexperience.datareprez.DataCommonAbstractImpl;
import eu.javaexperience.datareprez.DataObject;
import eu.javaexperience.datareprez.DataReceiver;
import eu.javaexperience.datareprez.DataSender;
import eu.javaexperience.io.SerializationTools;

public abstract class DataCommonJavaImpl extends DataCommonAbstractImpl
{
	public static DataCommon PROTOTYPE = new DataObjectJavaImpl();
	
	/*public static final MapProvider SMALL_MAP_PROVIDER = new MapProvider()
	{
		@Override
		public Map<String, Object> newMapForObject()
		{
			return new SmallMap<>();
		}
	};
	
	protected MapProvider subImp = SMALL_MAP_PROVIDER;
	*/
	
	@Override
	public DataObjectJavaImpl newObjectInstance()
	{
		return new DataObjectJavaImpl();
	}

	@Override
	public DataArrayJavaImpl newArrayInstance()
	{
		return new DataArrayJavaImpl();
	}

	@Override
	public void sendDataObject(DataObject dat, OutputStream os)	throws IOException
	{

	
	}

	@Override
	public DataObject receiveDataObject(InputStream is) throws IOException
	{
		return null;
	}

	@Override
	public void sendDataArray(DataArray dat, OutputStream os) throws IOException
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public DataArray receiveDataArray(InputStream is) throws IOException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DataSender newDataSender(OutputStream os) throws IOException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DataReceiver newDataReceiver(InputStream is) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public DataArray arrayFromBlob(byte[] data)
	{
		return new DataArrayJavaImpl((Object[]) SerializationTools.deserializeFromBlob(data));
	}

	@Override
	public DataObject objectFromBlob(byte[] data)
	{
		return new DataObjectJavaImpl((Map<String, Object>) SerializationTools.deserializeFromBlob(data));
	}
	
	@Override
	public boolean isNull(Object o)
	{
		return null == o;
	}
	
	@Override
	public Class getCommonsClass()
	{
		return Map.class;
	}
}