package eu.javaexperience.datareprez.abstractImpl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import eu.javaexperience.datareprez.DataArray;
import eu.javaexperience.datareprez.DataCommonAbstractImpl;
import eu.javaexperience.datareprez.DataObject;
import eu.javaexperience.datareprez.DataReceiver;
import eu.javaexperience.datareprez.DataSender;

public abstract class DataProtocolAbstractImpl extends DataCommonAbstractImpl
{
	protected abstract DataProtocol getProtocolHandler();
	
	@Override
	public DataObject newObjectInstance()
	{
		return getProtocolHandler().newObjectInstance();
	}

	@Override
	public DataArray newArrayInstance()
	{
		return getProtocolHandler().newArrayInstance();
	}

	@Override
	public void sendDataObject(DataObject dat, OutputStream os) throws IOException
	{
		getProtocolHandler().sendPacket(dat.toBlob(), os);
	}

	@Override
	public DataObject receiveDataObject(InputStream is) throws IOException
	{
		DataProtocol proto = getProtocolHandler();
		byte[] data = proto.acquirePacket(is);
		return proto.objectFromBlob(data);
	}

	@Override
	public void sendDataArray(DataArray dat, OutputStream os) throws IOException
	{
		getProtocolHandler().sendPacket(dat.toBlob(), os);
	}

	@Override
	public DataArray receiveDataArray(InputStream is) throws IOException
	{
		DataProtocol proto = getProtocolHandler();
		byte[] data = proto.acquirePacket(is);
		return proto.arrayFromBlob(data);
	}

	@Override
	public DataSender newDataSender(OutputStream os) throws IOException
	{
		return new GenericDataSender(getProtocolHandler(), os);
	}

	@Override
	public DataReceiver newDataReceiver(InputStream is) throws IOException
	{
		return new GenericDataReceiver(getProtocolHandler(), is);
	}

	@Override
	public boolean isNull(Object o)
	{
		return null == o;
	}

	@Override
	public DataArray arrayFromBlob(byte[] data)
	{
		return getProtocolHandler().arrayFromBlob(data);
	}

	@Override
	public DataObject objectFromBlob(byte[] data)
	{
		return getProtocolHandler().objectFromBlob(data);
	}
}
