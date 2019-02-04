package eu.javaexperience.datareprez;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import eu.javaexperience.reflect.Mirror;

public class DataPacker
{
	protected ByteArrayOutputStream baos = new ByteArrayOutputStream();
	protected DataSender ds;
	
	public DataPacker(DataCommon prototype)
	{
		try
		{
			ds = prototype.newDataSender(baos);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public synchronized byte[] toSend(DataObject obj)
	{
		try
		{
			ds.send(obj);
		}
		catch (IOException e)
		{
			Mirror.throwSoftOrHardButAnyway(e);
		}
		
		byte[] ret = baos.toByteArray();
		baos.reset();
		return ret;
	}
}