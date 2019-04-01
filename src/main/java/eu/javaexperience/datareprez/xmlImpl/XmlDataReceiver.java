package eu.javaexperience.datareprez.xmlImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.xml.parsers.ParserConfigurationException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xml.sax.SAXException;

import eu.javaexperience.datareprez.DataArray;
import eu.javaexperience.datareprez.DataObject;
import eu.javaexperience.datareprez.DataReceiver;
import eu.javaexperience.datareprez.jsonImpl.DataArrayJsonImpl;
import eu.javaexperience.datareprez.jsonImpl.DataObjectJsonImpl;
import eu.javaexperience.datareprez.jsonImpl.DataOperatorJsonCommon;
import eu.javaexperience.document.DocumentTools;
import eu.javaexperience.reflect.Mirror;

public class XmlDataReceiver extends DataOperatorJsonCommon implements DataReceiver
{
	private final BufferedReader br;
	
	public XmlDataReceiver(InputStream is)
	{
		br = new BufferedReader(new InputStreamReader(is));
	}
	
	@Override
	public DataObject receiveDataObject() throws IOException
	{
		String dat = br.readLine();
		if(dat == null)
			return null;
		
		try
		{
			return new DataObjectXmlImpl(DocumentTools.parseDocument(dat));
		}
		catch(Exception e)
		{
			Mirror.propagateAnyway(e);
			return null;
		}
	}
	
	@Override
	public DataArray readDataArray() throws IOException
	{
		String dat = br.readLine();
		if(dat == null)
			return null;
		
		try
		{
			return new DataArrayXmlImpl(DocumentTools.parseDocument(dat));
		}
		catch(Exception e)
		{
			Mirror.propagateAnyway(e);
			return null;
		}
	}

	@Override
	public void close() throws IOException
	{
		br.close();
	}
}
