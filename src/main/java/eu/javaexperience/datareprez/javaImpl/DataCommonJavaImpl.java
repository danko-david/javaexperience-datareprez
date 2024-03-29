package eu.javaexperience.datareprez.javaImpl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import eu.javaexperience.binary.FramedPacketCutter;
import eu.javaexperience.binary.PacketFramingTools;
import eu.javaexperience.datareprez.DataArray;
import eu.javaexperience.datareprez.DataCommon;
import eu.javaexperience.datareprez.DataCommonAbstractImpl;
import eu.javaexperience.datareprez.DataObject;
import eu.javaexperience.datareprez.abstractImpl.DataProtocol;
import eu.javaexperience.datareprez.convertFrom.DataReprezComponentTypes;
import eu.javaexperience.io.SerializationTools;
import eu.javaexperience.reflect.CastTo;
import eu.javaexperience.text.Format;

public abstract class DataCommonJavaImpl extends DataCommonAbstractImpl
{
	public static DataCommon PROTOTYPE = new DataObjectJavaImpl();
	
	public static final DataProtocol PROTOCOL = new DataProtocol()
	{
		@Override
		public void sendPacket(byte[] data, OutputStream os) throws IOException
		{
			os.write(PacketFramingTools.frameBytes(data, (byte) 0xff));
		}
		
		@Override
		public DataObject objectFromBlob(byte[] data)
		{
			return new DataObjectJavaImpl((Map) SerializationTools.deserializeFromBlob(data));
		}
		
		@Override
		public DataObject newObjectInstance()
		{
			return new DataObjectJavaImpl();
		}
		
		@Override
		public DataArray newArrayInstance()
		{
			return new DataArrayJavaImpl();
		}
		
		@Override
		public Class getCommonsClass()
		{
			return Map.class;
		}
		
		@Override
		public DataArray arrayFromBlob(byte[] data)
		{
			return new DataArrayJavaImpl((List) SerializationTools.deserializeFromBlob(data));
		}
		
		@Override
		public byte[] acquirePacket(InputStream is) throws IOException
		{
			return receiveData(is);
		}

		@Override
		public Object getNullObject()
		{
			return null;
		}
	};
	
	protected static byte[] receiveData(InputStream is) throws IOException
	{
		byte[][] rec = new byte[1][0];
		rec[0] = null;
		FramedPacketCutter cut = new FramedPacketCutter((byte) 0xff, p->{rec[0] = p;});
		//this is really inefficient, but gives a sample how the implementation works
		
		byte[] read = new byte[1];
		while(null == rec[0])
		{
			if(is.read(read, 0, 1) < 1)
			{
				return null;
			}
			cut.feedBytes(read, 1);
		}
		
		return rec[0];
	}

	public static <T> T castToType(Object o, Class<T> cls)
	{
		if(null != o)
		{
			if(Object.class  == cls || null == cls)
			{
				if(o instanceof Map)
				{
					cls = (Class<T>) DataObject.class;
				}
				else if(o instanceof List)
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
					case DataArray:
						if(o instanceof DataArray)
						{
							return (T)o;
						}
						return (T) new DataArrayJavaImpl((List)o);
					case DataObject:
						if(o instanceof DataObject)
						{
							return (T)o;
						}
						return (T) new DataObjectJavaImpl((Map)o);
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
			val = ((DataObjectJavaImpl)val).obj;
		}
		else if(DataArray.class == valueType)
		{
			val = ((DataArrayJavaImpl)val).arr;
		}
		
		return val;
	}
}