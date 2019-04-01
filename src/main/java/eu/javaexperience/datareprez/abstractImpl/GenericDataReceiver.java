package eu.javaexperience.datareprez.abstractImpl;

import java.io.IOException;
import java.io.InputStream;

import eu.javaexperience.datareprez.DataArray;
import eu.javaexperience.datareprez.DataObject;
import eu.javaexperience.datareprez.DataReceiver;

public class GenericDataReceiver extends DataProtocolAbstractImpl implements DataReceiver
{
	protected DataProtocol protocol;
	protected InputStream is;
	
	public GenericDataReceiver(DataProtocol protocol, InputStream is)
	{
		this.protocol = protocol;
		this.is = is;
	}
	
	@Override
	public void close() throws IOException
	{
		is.close();
	}

	@Override
	public DataObject receiveDataObject() throws IOException
	{
		byte[] data = protocol.acquirePacket(is);
		return protocol.objectFromBlob(data);
	}

	@Override
	public DataArray readDataArray() throws IOException
	{
		byte[] data = protocol.acquirePacket(is);
		return protocol.arrayFromBlob(data);
	}

	@Override
	protected DataProtocol getProtocolHandler()
	{
		return protocol;
	}
	
	@Override
	public Object getImpl()
	{
		return null;
	}
	
	@Override
	public Class getCommonsClass()
	{
		return protocol.getCommonsClass();
	}

	@Override
	public byte[] toBlob()
	{
		return null;
	}
}
