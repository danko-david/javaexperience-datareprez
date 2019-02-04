package eu.javaexperience.datareprez;

import java.io.Closeable;
import java.io.IOException;

public interface DataReceiver extends DataCommon, Closeable
{
	public DataObject receiveDataObject() throws IOException;
	public DataArray readDataArray() throws IOException;
}