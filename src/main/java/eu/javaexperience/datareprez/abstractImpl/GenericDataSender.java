package eu.javaexperience.datareprez.abstractImpl;

import java.io.IOException;
import java.io.OutputStream;

import eu.javaexperience.datareprez.DataArray;
import eu.javaexperience.datareprez.DataObject;
import eu.javaexperience.datareprez.DataSender;

public class GenericDataSender extends DataProtocolAbstractImpl implements DataSender
{
	protected DataProtocol protocol;
	protected OutputStream os;
	
	public GenericDataSender(DataProtocol protocol, OutputStream os)
	{
		this.protocol = protocol;
		this.os = os;
	}
	
	@Override
	protected DataProtocol getProtocolHandler()
	{
		return protocol;
	}

	@Override
	public void close() throws IOException
	{
		os.close();
	}

	@Override
	public void send(DataObject o) throws IOException
	{
		protocol.sendPacket(o.toBlob(), os);
	}

	@Override
	public void send(DataArray a) throws IOException
	{
		protocol.sendPacket(a.toBlob(), os);
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
}
