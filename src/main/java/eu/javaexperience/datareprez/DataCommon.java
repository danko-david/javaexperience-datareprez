package eu.javaexperience.datareprez;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import eu.javaexperience.interfaces.ObjectWithProperty;

public interface DataCommon
{
	public DataObject newObjectInstance();
	public DataArray newArrayInstance();
	
	public void sendDataObject(DataObject dat, OutputStream os) throws IOException;
	public DataObject receiveDataObject(InputStream is) throws IOException;
	
	public void sendDataArray(DataArray dat, OutputStream os) throws IOException;
	public DataArray receiveDataArray(InputStream is) throws IOException;
	
	public DataSender newDataSender(OutputStream os) throws IOException;
	public DataReceiver newDataReceiver(InputStream is) throws IOException;
	
	public DataObject fromObjectLike(ObjectWithProperty map);
	public DataArray fromArrayLike(ArrayLike arr);
	
	public Object getImpl();
	
	public DataArray arrayFromBlob(byte[] data);
	public DataObject objectFromBlob(byte[] data);
	
	
	public Class getCommonsClass();
	
	public byte[] toBlob();
}