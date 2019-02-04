package eu.javaexperience.datareprez;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import eu.javaexperience.io.SwappableInputStream;
import eu.javaexperience.reflect.Mirror;

public class DataUnpacker
{
	protected SwappableInputStream in = new SwappableInputStream();
	protected DataReceiver rec;
	
	public DataUnpacker(DataCommon comm)
	{
		try
		{
			rec = comm.newDataReceiver(in);
		}
		catch(Exception e)
		{
			Mirror.throwSoftOrHardButAnyway(e);
		}
	}
	
	public synchronized DataObject toReceiveObject(byte[] data) throws IOException
	{
		in.setInputStream(new ByteArrayInputStream(data));
		return rec.receiveDataObject();
	}

	public synchronized DataArray toReceiveArray(byte[] data) throws IOException
	{
		in.setInputStream(new ByteArrayInputStream(data));
		return rec.readDataArray();
	}

}
