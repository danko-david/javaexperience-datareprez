package eu.javaexperience.datareprez.jsonImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.JSONArray;
import org.json.JSONObject;

import eu.javaexperience.datareprez.DataArray;
import eu.javaexperience.datareprez.DataObject;
import eu.javaexperience.datareprez.DataReceiver;

public class JsonDataReceiver extends DataOperatorJsonCommon implements DataReceiver
{
	private final BufferedReader br;
	
	public JsonDataReceiver(InputStream is)
	{
		br = new BufferedReader(new InputStreamReader(is));
	}
	
	@Override
	public DataObject receiveDataObject() throws IOException
	{
		String dat = br.readLine();
		if(dat == null)
		{
			throw new IOException("RPC connection closed");
			//return null;
		}
		return new DataObjectJsonImpl(new JSONObject(dat));
	}
	
	@Override
	public DataArray readDataArray() throws IOException
	{
		return new DataArrayJsonImpl(new JSONArray(br.readLine()));
	}

	@Override
	public void close() throws IOException
	{
		br.close();
	}	
}
