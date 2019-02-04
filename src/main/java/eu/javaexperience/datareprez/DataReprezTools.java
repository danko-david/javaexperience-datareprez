package eu.javaexperience.datareprez;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

import eu.javaexperience.collection.map.BulkTransitMap;
import eu.javaexperience.collection.map.SmallMap;
import eu.javaexperience.datareprez.convertFrom.ArrayLike;
import eu.javaexperience.datareprez.convertFrom.ClassObjectLike;
import eu.javaexperience.datareprez.convertFrom.DataLike;
import eu.javaexperience.datareprez.convertFrom.DataWrapper;
import eu.javaexperience.datareprez.convertFrom.ModifiableObject;
import eu.javaexperience.exceptions.UnimplementedCaseException;
import eu.javaexperience.interfaces.ObjectWithProperty;
import eu.javaexperience.reflect.Mirror;
import eu.javaexperience.reflect.Mirror.FieldSelector;
import eu.javaexperience.reflect.NotatedCaster;

public class DataReprezTools
{
	
	public static Class<?> put(DataArray arr,Object o)
	{
		return put(arr, arr.size(), o);
	}
	
	public static final String[] emptyStringArray = new String[0];
	
	public static Class<?> put(DataArray arr, int i, Object o)
	{
		return put(null, arr, i, o);
	}
	
	/**
	 * vissztaért hogy milyen tipussal sikerült eltárolni az értéket.
	 * Void.class ha a megadott érték null volt.
	 * nullal tér vissza ha nem sikerült eltenni.
	 * DataObject.class - adatobjektum
	 * DataArray.class - adattömb
	 * 
	 * Összes:
	 * 	Void.class
	 * 	String.class
	 * 	Long.class
	 * 	Integer.class
	 * 	Double.class
	 * 	Boolean.class
	 * 	DataObject.class
	 * 	DataArray.class
	 * 	Float.class (double)
	 * 	Character.class (String)
	 * 	Short.class (int)
	 * 	Byte.class (int)
	 * */
	public static Class<?> put(DataWrapper dw, DataArray arr, int i, Object o)
	{
		if(o == null || arr.isNull(o))
		{
			arr.putNull(i);
			return Void.class;
		}
		else if(o instanceof String)
		{
			arr.putString(i,((String)o));
			return String.class;
		}
		else if(o instanceof Long)
		{
			arr.putLong(i,((Long)o).longValue());
			return Long.class;
		}
		else if(o instanceof Integer)
		{
			arr.putInt(i,((Integer)o).intValue());
			return Integer.class;
		}
		if(o instanceof Double)
		{
			arr.putDouble(i,((Double)o).doubleValue());
			return Double.class;
		}
		else if(o instanceof byte[])
		{
			arr.putBlob(i,(byte[]) o);
			return byte[].class;
		}
		else if(o instanceof Boolean)
		{
			arr.putBoolean(i,(Boolean) o);
			return Boolean.class;
		}
		else if(o instanceof DataObject)
		{
			if(arr.getCommonsClass().isAssignableFrom(((DataObject) o).getCommonsClass()))
			{
				arr.putObject(i,(DataObject)o);
			}
			else
			{
				DataObject dobj = ((DataObject) o).newObjectInstance();
				DataReprezTools.copyInto(dobj, (DataObject)o);
				arr.putObject(i, dobj);
			}
			return DataObject.class;
		}
		else if(o instanceof DataArray)
		{
			if(arr.getCommonsClass().isAssignableFrom(((DataArray) o).getCommonsClass()))
			{
				arr.putArray(i,(DataArray)o);
			}
			else
			{
				DataArray darr = ((DataArray) o).newArrayInstance();
				DataReprezTools.copyInto(darr, (DataArray)o);
				arr.putArray(i, darr);
			}	
			return DataArray.class;
		}
		
		
		else if(o instanceof Float)
		{
			arr.putDouble(i,((Float)o).doubleValue());
			return Float.class;
		}
		else if(o instanceof Character)
		{
			arr.putString(i,new String(new char[]{((Character)o)}));
			return Character.class;
		}
		else if(o instanceof Short)
		{
			arr.putInt(i,((Short)o).intValue());
			return Short.class;
		}
		else if(o instanceof Byte)
		{
			arr.putInt(i,((Byte)o).intValue());
			return Byte.class;
		}
		
		if(null != dw)
		{
			Object wrap = dw.wrap(dw, arr, o);
			if(null != wrap)
			{
				return put(dw, arr, i, wrap);
			}
		}
		
		return null;
	}

	public static Class<?> put(DataObject obj,String key,Object o)
	{
		return put(null, obj, key, o);
	}
	
	/**
	 * vissztaért hogy milyen tipussal sikerült eltárolni az értéket.
	 * Void.class ha a megadott érték null volt.
	 * nullal tér vissza ha nem sikerült eltenni.
	 * DataObject.class - adatobjektum
	 * DataArray.class - adattömb
	 * 
	 * Összes:
	 * 	Void.class
	 * 	String.class
	 * 	Long.class
	 * 	Integer.class
	 * 	Double.class
	 * 	Boolean.class
	 * 	DataObject.class
	 * 	DataArray.class
	 * 	Float.class (double)
	 * 	Character.class (String)
	 * 	Short.class (int)
	 * 	Byte.class (int)
	 * */
	public static Class<?> put(DataWrapper dw, DataObject obj, String key, Object o)
	{
		if(o == null || obj.isNull(o))
		{
			obj.putNull(key);
			return Void.class;
		}
		else if(o instanceof String)
		{
			obj.putString(key,((String)o));
			return String.class;
		}
		else if(o instanceof Long)
		{
			obj.putLong(key,((Long)o).longValue());
			return Long.class;
		}
		else if(o instanceof Integer)
		{
			obj.putInt(key,((Integer)o).intValue());
			return Integer.class;
		}
		if(o instanceof Double)
		{
			obj.putDouble(key,((Double)o).doubleValue());
			return Double.class;
		}
		else if(o instanceof byte[])
		{
			obj.putBlob(key,(byte[]) o);
			return byte[].class;
		}
		else if(o instanceof Boolean)
		{
			obj.putBoolean(key,(Boolean) o);
			return Boolean.class;
		}
		else if(o instanceof DataObject)
		{
			if(obj.getClass().isAssignableFrom(o.getClass()))
			{
				obj.putObject(key,(DataObject)o);
			}
			else
			{
				DataObject dobj = ((DataObject) o).newObjectInstance();
				DataReprezTools.copyInto(dobj, (DataObject)o);
				obj.putObject(key, dobj);
			}
			return DataObject.class;
		}
		else if(o instanceof DataArray)
		{
			if(obj.getClass().isAssignableFrom(o.getClass()))
			{
				obj.putArray(key,(DataArray)o);
			}
			else
			{
				DataArray darr = ((DataArray) o).newArrayInstance();
				DataReprezTools.copyInto(darr, (DataArray)o);
				obj.putArray(key, darr);
			}	
			return DataArray.class;
		}
		
		
		else if(o instanceof Float)
		{
			obj.putDouble(key,((Float)o).doubleValue());
			return Float.class;
		}
		else if(o instanceof Character)
		{
			obj.putString(key,new String(new char[]{((Character)o)}));
			return Character.class;
		}
		else if(o instanceof Short)
		{
			obj.putInt(key,((Short)o).intValue());
			return Short.class;
		}
		else if(o instanceof Byte)
		{
			obj.putInt(key,((Byte)o).intValue());
			return Byte.class;
		}
		
		else if(o instanceof Date)
		{
			obj.putLong(key,((Date)o).getTime());
			return Long.class;
		}
		else if(o.getClass().isArray())
		{
			DataArray arr = obj.newArrayInstance();
			for(int i=0;i<Array.getLength(o);++i)
			{
				DataReprezTools.put(dw, arr, i, Array.get(o, i));
			}
			obj.putArray(key, arr);
			return DataArray.class;
		}
		else if(o instanceof Collection)
		{
			Collection c = (Collection) o;
			DataArray arr = obj.newArrayInstance();
			int i = 0;
			for(Object add:c)
			{
				DataReprezTools.put(dw, arr, i++, add);
			}
			obj.putArray(key, arr);
			return DataArray.class;
		}
		
		if(null != dw)
		{
			Object wrap = dw.wrap(dw, obj, o);
			if(null != wrap)
			{
				return put(dw, obj, key, wrap);
			}
		}
		
		return null;
	}
	
	public static Class<?> isStorable(Object o)
	{
		if(o == null)
			return Void.class;
		else if(o instanceof String)
			return String.class;
		else if(o instanceof Long)
			return Long.class;
		else if(o instanceof Integer)
			return Integer.class;
		if(o instanceof Double)
			return Double.class;
		else if(o instanceof byte[])
			return byte[].class;
		else if(o instanceof Boolean)
			return Boolean.class;
		else if(o instanceof DataObject)
			return DataObject.class;
		else if(o instanceof DataArray)
			return DataArray.class;
		
		else if(o instanceof Float)
			return Float.class;
		else if(o instanceof Character)
			return Character.class;
		else if(o instanceof Short)
			return Short.class;
		else if(o instanceof Byte)
			return Byte.class;
		else if(o instanceof Date)
			return Long.class;
		//else if(o.getClass().isArray())
		//	return DataArray.class;
		
		return null;
	}
	
	public static Object extractToJavaPrimitiveTypes(Object any)
	{
		if(any instanceof ArrayLike)
		{
			DataArray arr = (DataArray) any;
			Object[] ret = new Object[arr.size()];
			for(int i=0;i<ret.length;++i)
			{
				ret[i] = extractToJavaPrimitiveTypes(arr.get(i));
			}
			
			return ret;
		}
		else if(any instanceof ObjectWithProperty)
		{
			ObjectWithProperty obj = (ObjectWithProperty) any;
			Map<String, Object> ret = new SmallMap<>();
			for(String k:obj.keys())
			{
				ret.put(k, extractToJavaPrimitiveTypes(obj.get(k)));
			}
			
			return ret;
		}
		
		return any;
	}
	
	public static Object wrapRecursively
	(
		DataWrapper wrapper,
		DataCommon carrierPrototype,
		Object toWrap
	)
	{
		return wrapper.wrap(wrapper, carrierPrototype, toWrap);
	}
	
	public static DataWrapper combineWrappers(final DataWrapper... wrappers)
	{
		return new DataWrapper()
		{
			@Override
			public DataCommon wrap(DataWrapper topWrapper, DataCommon prototype, Object o)
			{
				DataCommon ret = null;
				for(DataWrapper w:wrappers)
				{
					ret = w.wrap(this, prototype, o);
					if(null != ret)
					{
						return ret;
					}
				}
				
				return null;
			}
		};
	}
	
	public static DataWrapper createClassInstanceWrapper(final FieldSelector select)
	{
		return createClassInstanceWrapper(select, null);
	}
	
	public static DataWrapper createClassInstanceWrapper(final FieldSelector select, final String classFieldName)
	{
		return new DataWrapper()
		{
			@Override
			public DataCommon wrap
			(
				DataWrapper topWrapper,
				DataCommon prototype,
				Object o
			)
			{
				try
				{
					BulkTransitMap<String, Object> values = new BulkTransitMap<String, Object>();
					Mirror.extractFieldsToMap(o, values, select);
					DataObject ret = prototype.newObjectInstance();
					for(Entry<String, Object> kv:values.entrySet())
					{
						String key = kv.getKey();
						Object val = kv.getValue();
						if(null == put(topWrapper, ret, key, val))
						{
							put(topWrapper, ret, key, topWrapper.wrap(topWrapper, prototype, val));
						}
					}
					
					if(null != classFieldName)
					{
						ret.putString(classFieldName, o.getClass().getSimpleName());
					}
					
					return ret;
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				return null;
			}
		};
	}
	
	public static final DataWrapper WRAP_ARRAY_COLLECTION_MAP =  new DataWrapper()
	{
		@Override
		public DataCommon wrap
		(
			DataWrapper topWrapper,
			DataCommon transferDatatype,
			Object in
		)
		{
			//for arrays
			if(in.getClass().isArray())
			{
				int len = Array.getLength(in);
				//return Array.get(o, 0);
				DataArray arr = transferDatatype.newArrayInstance();
				for(int i=0;i<len;++i)
				{
					Object add = Array.get(in, i);
					if(null == DataReprezTools.put(topWrapper, arr, i, add))
					{
						DataReprezTools.put(topWrapper, arr, i, topWrapper.wrap(topWrapper, transferDatatype, add));
					}
				}
				return arr;
			}
			else if(in instanceof Collection)
			{
				Collection src = (Collection) in;
				DataArray arr = transferDatatype.newArrayInstance();
				int i = 0;
				for(Object o: src)
				{
					if(null == DataReprezTools.put(topWrapper, arr, i++, o))
					{
						DataReprezTools.put(topWrapper, arr, i++, topWrapper.wrap(topWrapper, transferDatatype, o));
					}
				}
				return arr;
			}
			else if(in instanceof Map)
			{
				Map<Object, Object> m = (Map) in;
				DataObject ret = transferDatatype.newObjectInstance();
				for(Entry<Object, Object> kv:m.entrySet())
				{
					if(null == DataReprezTools.put(topWrapper, ret, String.valueOf(kv.getKey()), kv.getValue()))
					{
						Object value = topWrapper.wrap(topWrapper, transferDatatype, kv.getValue());
						DataReprezTools.put(topWrapper, ret, String.valueOf(kv.getKey()), value);
					}
				}
				
				return ret;
			}
			
			
			return null;
		}
	};
	
	public static final DataWrapper WRAP_DATA_LIKE = new DataWrapper()
	{
		@Override
		public DataCommon wrap(DataWrapper dw, DataCommon prototype, Object o)
		{
			if(o instanceof DataLike)
			{
				DataLike in = (DataLike) o; 
				switch (in.getDataReprezType())
				{
					case ARRAY:
					{
						ArrayLike arr = (ArrayLike) in;
						DataArray ret = prototype.newArrayInstance();
						for(int i=0;i<arr.size();++i)
						{
							Object add = arr.get(i);
							if(null == put(ret, i, add))
							{
								put(ret, i, dw.wrap(dw, prototype, add));
							}
						}
						return ret;
					}	
					case OBJECT:
					case CLASS_OBJECT:
					case RESOURCE:
					{
						ObjectWithProperty obj = (ObjectWithProperty) in;
						DataObject ret = prototype.newObjectInstance();
						for(String key:obj.keys())
						{
							Object w = obj.get(key);
							//if(null != w)
							{
								if(null == put(ret, key, w))
								{
									put(ret, key, dw.wrap(dw, prototype, w));
								}
							}
						}
						
						if(obj instanceof ClassObjectLike)
						{
							ret.putString("class", ((ClassObjectLike) obj).getClassIdentifier());
						}
						
						return ret;
					}
						
					case NULL:
					case PRIMITIVE:
						return null;
						
					default:
						throw new UnimplementedCaseException(in.getDataReprezType());
				}
			}
			return null;
		}
	};

	public static final DataWrapper WRAP_ENUM = new DataWrapper()
	{
		@Override
		public DataCommon wrap(DataWrapper topWrapper, DataCommon prototype, Object o)
		{
			if(null != o && o.getClass().isEnum())
			{
				DataObject ret = prototype.newObjectInstance();
				ret.putString("name", ((Enum)o).name());
				ret.putInt("ordinal", ((Enum)o).ordinal());
				
				ret.putString("class", o.getClass().getName());
				
				return ret;
			}
			return null;
		}
	};
	
	public static final DataWrapper WRAP_CLASS__OBJECT_WITH_PROPERTY = new DataWrapper()
	{
		@Override
		public DataCommon wrap(DataWrapper topWrapper, DataCommon prototype, Object o)
		{
			if(o instanceof ObjectWithProperty)
			{
				ObjectWithProperty owp = (ObjectWithProperty) o;
				DataObject ret = prototype.newObjectInstance();
				for(String s:owp.keys())
				{
					DataReprezTools.put(topWrapper, ret, s, owp.get(s));
				}
				
				return ret;
			}
			return null;
		}
	};
	
	public static void copyInto(DataObject dst, ObjectWithProperty src)
	{
		for(String s:src.keys())
		{
			put(dst, s, src.get(s));
		}
	}
	
	public static void copyInto(ModifiableObject dst, ObjectWithProperty src)
	{
		for(String s:src.keys())
		{
			dst.set(s, src.get(s));
		}
	}
	
	public static void copyInto(DataArray dst, ArrayLike src)
	{
		int s = src.size();
		for(int i=0;i<s;++i)
		{
			put(dst, i, src.get(i));
		}
	}
	
	public static <T> T getAsOrThrow(DataObject obj, String key, NotatedCaster c)
	{
		Object ret = obj.get(key);
		if(null == ret)
		{
			throw new RuntimeException("`"+key+"` key not found");
		}
		ret = c.cast(ret);
		if(null == ret)
		{
			throw new RuntimeException("Can't cast `"+ret+"` to "+c.getTypeFullQualifiedName());
		}
		
		return (T) ret;
	}
	
	public static <T> T getAsOrThrow(DataArray arr, int index, NotatedCaster c)
	{
		Object ret = arr.get(index);
		if(null == ret)
		{
			throw new RuntimeException(index+" index not fount");
		}
		ret = c.cast(ret);
		if(null == ret)
		{
			throw new RuntimeException("Can't cast `"+ret+"` to "+c.getTypeFullQualifiedName());
		}
		
		return (T) ret;
	}
	
}