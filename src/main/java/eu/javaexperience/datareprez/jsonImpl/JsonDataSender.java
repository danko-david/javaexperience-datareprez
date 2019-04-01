package eu.javaexperience.datareprez.jsonImpl;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import org.json.JSONArray;
import org.json.JSONObject;

import eu.javaexperience.datareprez.DataArray;
import eu.javaexperience.datareprez.DataObject;
import eu.javaexperience.datareprez.DataSender;

public class JsonDataSender extends DataOperatorJsonCommon implements DataSender
{
	private final PrintWriter pw; 
	
	public JsonDataSender(OutputStream os)
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
		JSONObject obj = ((DataObjectJsonImpl)o).obj;
		obj.write(pw);
		pw.append('\n');
		pw.flush();
	}

	@Override
	public void send(DataArray a)
	{
		JSONArray obj = ((DataArrayJsonImpl)a).arr;
		obj.write(pw);
		pw.append('\n');
		pw.flush();
	}

	@Override
	public void close() throws IOException
	{
		pw.close();
	}
}
