package eu.javaexperience.datareprez.jsonImpl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.json.JSONArray;
import org.json.JSONObject;

import eu.javaexperience.datareprez.DataArray;
import eu.javaexperience.datareprez.DataCommonAbstractImpl;
import eu.javaexperience.datareprez.DataObject;
import eu.javaexperience.datareprez.abstractImpl.DataProtocol;
import eu.javaexperience.datareprez.convertFrom.DataReprezComponentTypes;
import eu.javaexperience.io.primitive.LineReader;
import eu.javaexperience.io.primitive.LineReader.LineMode;
import eu.javaexperience.reflect.CastTo;
import eu.javaexperience.reflect.Mirror;
import eu.javaexperience.text.Format;

public abstract class DataCommonJsonImpl extends DataCommonAbstractImpl
{
	public static final DataProtocol PROTOCOL = new DataProtocol()
	{
		@Override
		public void sendPacket(byte[] data, OutputStream os) throws IOException
		{
			os.write(data);
			os.write(10);
		}
		
		@Override
		public byte[] acquirePacket(InputStream is) throws IOException
		{
			return LineReader.readLine(is, LineMode.Unix).getBytes();
		}
		
		@Override
		public DataObject objectFromBlob(byte[] data)
		{
			return new DataObjectJsonImpl(new JSONObject(new String(data)));
		}
		
		@Override
		public DataObject newObjectInstance()
		{
			return new DataObjectJsonImpl();
		}
		
		@Override
		public DataArray newArrayInstance()
		{
			return new DataArrayJsonImpl();
		}
		
		@Override
		public Class getCommonsClass()
		{
			return JSONObject.class;
		}
		
		@Override
		public DataArray arrayFromBlob(byte[] data)
		{
			return new DataArrayJsonImpl(new JSONArray(new String(data)));
		}

		@Override
		public Object getNullObject()
		{
			return JSONObject.NULL;
		}
	};
	
	public static <T> T castToType(Object o, Class<T> cls)
	{
		if(null != o)
		{
			if(Object.class  == cls || null == cls)
			{
				if(o instanceof JSONObject)
				{
					cls = (Class<T>) DataObject.class;
				}
				else if(o instanceof JSONArray)
				{
					cls = (Class<T>) DataArray.class;
				}
				else
				{
					return (T) o;
				}
			}
			
			DataReprezComponentTypes type = DataReprezComponentTypes.recognise(cls);
			
			if(null != type)
			{
				switch(type)
				{
					case Boolean: 	return (T) CastTo.Boolean.cast(o);
					case DataArray:	return (T) new DataArrayJsonImpl((JSONArray)o);
					case DataObject:return (T) new DataObjectJsonImpl((JSONObject)o);
					case Double:	return (T) CastTo.Double.cast(o);
					case Integer:	return (T) CastTo.Int.cast(o);
					case Long:		return (T) CastTo.Long.cast(o);
					case NULL:		return null;
					case String: 	return (T) CastTo.String.cast(o);
					case Blob:
						if(o instanceof byte[])
						{
							return (T) o;
						}
						else if(o instanceof String)
						{
							return (T) Format.base64Decode((String) o);
						}
					break;
				}
			}
			throw new RuntimeException("Unrecognised class type: "+cls);
		}
		
		return null;
	}

	public static Object wrapObjectToStore(Object val, Class valueType)
	{
		if(DataObject.class == valueType)
		{
			val = ((DataObjectJsonImpl)val).obj;
		}
		else if(DataArray.class == valueType)
		{
			val = ((DataArrayJsonImpl)val).arr;
		}
		else if(null == val && void.class == valueType)
		{
			return JSONObject.NULL;
		}
		
		return val;
	}
	
}