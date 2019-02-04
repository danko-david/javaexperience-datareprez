package eu.javaexperience.datareprez.abstractImpl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import eu.javaexperience.datareprez.DataArray;
import eu.javaexperience.datareprez.DataObject;

public interface DataProtocol
{
	public byte[] acquirePacket(InputStream is) throws IOException;
	public void sendPacket(byte[] data, OutputStream os) throws IOException;
	
	public DataObject newObjectInstance();
	public DataArray newArrayInstance();
	
	public DataObject objectFromBlob(byte[] data);
	public DataArray arrayFromBlob(byte[] data);
	
	public Class getCommonsClass();
}
