package eu.javaexperience.datareprez.xmlImpl;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import org.w3c.dom.Node;

import eu.javaexperience.datareprez.DataArray;
import eu.javaexperience.datareprez.DataObject;
import eu.javaexperience.datareprez.DataSender;
import eu.javaexperience.datareprez.jsonImpl.DataOperatorJsonCommon;

public class XmlDataSender extends DataOperatorJsonCommon implements DataSender
{
	private final PrintWriter pw; 
	
	public XmlDataSender(OutputStream os)
	{
		this.pw = new PrintWriter(os);
		/*this.pw = new LocklessBufferedOutputStreamUtf8PrintWriter(os, new byte[2048], null)
		{
			@Override
			public void flush()
			{
				flushBuffer();
			}
		};*/
	}
	
	@Override
	public void send(DataObject o)
	{
		pw.println(DataOperatorXmlCommon.xmlToString(((Node)(((DataObjectXmlImpl)o).getImpl()))));
		pw.flush();
	}

	@Override
	public void send(DataArray a)
	{
		pw.println(DataOperatorXmlCommon.xmlToString(((Node)(((DataArrayXmlImpl)a).getImpl()))));
		pw.flush();
	}

	@Override
	public void close() throws IOException
	{
		pw.close();
	}
}
