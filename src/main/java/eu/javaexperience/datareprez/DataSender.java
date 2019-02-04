package eu.javaexperience.datareprez;

import java.io.Closeable;
import java.io.IOException;

public interface DataSender extends DataCommon, Closeable
{
	public void send(DataObject o) throws IOException;
	public void send(DataArray a) throws IOException;
}