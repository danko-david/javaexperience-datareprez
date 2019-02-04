package eu.javaexperience.datareprez.xmlImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import eu.javaexperience.collection.NullCollection;
import eu.javaexperience.datareprez.DataArray;
import eu.javaexperience.datareprez.DataObject;
import eu.javaexperience.datareprez.convertFrom.ArrayLike;
import eu.javaexperience.datareprez.convertFrom.DataReprezType;
import eu.javaexperience.datareprez.jsonImpl.DataObjectJsonImpl;
import eu.javaexperience.document.DocumentTools;

public class DataArrayXmlImpl extends DataOperatorXmlCommon implements DataArray
{
	protected String name;
	
	public DataArrayXmlImpl(String name, Node owner)
	{
		super(owner);
		this.name = name;
	}
	
	public DataArrayXmlImpl(Node owner)
	{
		super(owner);
		this.name = NODE_NAME_MARK_NEED_ADOPT;
	}
	
	
	public DataArrayXmlImpl()
	{
		super(DocumentTools.createEmptyDocument().createElement(NODE_NAME_MARK_NEED_ADOPT));
		name = NODE_NAME_MARK_NEED_ADOPT;
		node.getOwnerDocument().appendChild(node);
	}
	
	protected int fillNodes(Collection<Node> dst)
	{
		NodeList nl = node.getChildNodes();
		int max = nl.getLength();
		int found = 0;
		for(int i=0;i<max;++i)
		{
			Node n = nl.item(i);
			if(name.equals(n.getNodeName()))
			{
				++found;
				dst.add(n);
			}
		}
		
		return found;
	}
	
	protected Node getOrCreateSubjectValue(Object _index, boolean create)
	{
		int index = (int) _index;
		NodeList nl = node.getChildNodes();
		//response fast
		int max = nl.getLength();
		if(!create && max < index)
		{
			return null;
		}
		
		int found = 0;
		
		for(int i=0;i<max;++i)
		{
			Node n = nl.item(i);
			if(name.equals(n.getNodeName()))
			{
				if(found == index)
				{
					return n;
				}
				++found;
			}
		}
		
		if(create)
		{
			for(int i = found;i<=index;++i)
			{
				Node n = DocumentTools.getOwnerDocument(node).createElement(name);
				node.appendChild(n);
				if(i  == index)
				{
					return n;
				}
			}
		}
		
		return null;
	}
	
	@Override
	public void putString(int i, String val)
	{
		setSubjectValue(i, val);
	}

	@Override
	public void putLong(int i, long val)
	{
		setSubjectValue(i, val);
	}

	@Override
	public void putDouble(int i, double val)
	{
		setSubjectValue(i, val);
	}

	@Override
	public void putInt(int i, int val)
	{
		setSubjectValue(i, val);
	}

	@Override
	public void putBoolean(int i, boolean val)
	{
		setSubjectValue(i, val);
	}

	@Override
	public void putObject(int i, DataObject val)
	{
		setSubjectValue(i, val);
	}

	@Override
	public void putArray(int i, DataArray val)
	{
		setSubjectValue(i, val);
	}

	@Override
	public void putNull(int i)
	{
		setSubjectValue(i, null);
	}

	@Override
	public String getString(int i)
	{
		return getValueAs(i, String.class, false);
	}

	@Override
	public long getLong(int i)
	{
		return getValueAs(i, long.class, false);
	}

	@Override
	public double getDouble(int i)
	{
		return getValueAs(i, Double.class, false);
	}

	@Override
	public int getInt(int i)
	{
		return getValueAs(i, int.class, false);
	}

	@Override
	public boolean getBoolean(int i)
	{
		return getValueAs(i, boolean.class, false);
	}

	@Override
	public DataObject getObject(int i)
	{
		return new DataObjectXmlImpl(getValueAs(i, Node.class, false));
	}

	@Override
	public DataArray getArray(int i)
	{
		return new DataArrayXmlImpl(name, getValueAs(i, Node.class, false));
	}

	@Override
	public String optString(int i)
	{
		return getValueAs(i, String.class, true);
	}

	@Override
	public long optLong(int i)
	{
		return getValueAs(i, long.class, true);
	}

	@Override
	public double optDouble(int i)
	{
		return getValueAs(i, double.class, true);
	}

	@Override
	public int optInt(int i)
	{
		return getValueAs(i, int.class, true);
	}

	@Override
	public boolean optBoolean(int i)
	{
		return getValueAs(i, boolean.class, true);
	}

	@Override
	public DataObject optObject(int i)
	{
		return new DataObjectXmlImpl(getValueAs(i, Node.class, true));
	}

	@Override
	public DataArray optArray(int i)
	{
		//TODO
		return new DataArrayXmlImpl(name, getValueAs(i, Node.class, true));
	}

	@Override
	public String optString(int i, String def)
	{
		return getValueOpt(i, String.class, def);
	}

	@Override
	public long optLong(int i, long def)
	{
		return getValueOpt(i, long.class, def);
	}

	@Override
	public double optDouble(int i, double val)
	{
		return getValueOpt(i, double.class, val);
	}

	@Override
	public int optInt(int i, int def)
	{
		return getValueOpt(i, int.class, def);
	}

	@Override
	public boolean optBoolean(int i, boolean def)
	{
		return getValueOpt(i, boolean.class, def);
	}

	@Override
	public DataObject optObject(int i, DataObject def)
	{
		//TODO
		return null;
	}

	@Override
	public DataArray optArray(int i, DataArray def)
	{
		//TODO
				return null;
	}

	@Override
	public void unset(int i)
	{
		Node n = getOrCreateSubjectValue(i, false);
		n.setTextContent(null);
	}

	@Override
	public int size()
	{
		return fillNodes(NullCollection.INSTANCE);
	}

	@Override
	public boolean isString(int i)
	{
		return getValueAs(i, String.class, true) instanceof String;
	}

	@Override
	public boolean isLong(int i)
	{
		return getValueAs(i, Long.class, true) instanceof Long;
	}

	@Override
	public boolean isDouble(int i)
	{
		return getValueAs(i, Double.class, true) instanceof Double;
	}

	@Override
	public boolean isInt(int i)
	{
		return getValueAs(i, Integer.class, true) instanceof Integer;
	}

	@Override
	public boolean isBoolean(int i)
	{
		return getValueAs(i, Boolean.class, true) instanceof Boolean;
	}

	@Override
	public boolean isObject(int i)
	{
		return getValueAs(i, Node.class, true) instanceof Node;
	}

	@Override
	public boolean isArray(int i)
	{
		return getValueAs(i, Node.class, true) instanceof Node;
	}

	@Override
	public boolean isNull(int i)
	{
		return null == getValueAs(i, null, true);
	}

	@Override
	public void putString(String val)
	{
		setSubjectValue(size(), val);
	}

	@Override
	public void putLong(long val)
	{
		setSubjectValue(size(), val);
	}

	@Override
	public void putDouble(double val)
	{
		setSubjectValue(size(), val);
	}

	@Override
	public void putInt(int val)
	{
		setSubjectValue(size(), val);
	}

	@Override
	public void putBoolean(boolean val)
	{
		setSubjectValue(size(), val);
	}

	@Override
	public void putObject(DataObject val)
	{
		setSubjectValue(size(), ((DataObjectXmlImpl)val).node);
	}

	@Override
	public void putArray(DataArray val)
	{
		//TODO
		
	}

	@Override
	public void putNull()
	{
		setSubjectValue(size(), null);
	}
	
	@Override
	public Object get(int key)
	{
		Object o = getValueAs(key, null, false);
		if(o instanceof Node)
			return new DataObjectXmlImpl((Node) o);
		/*if(o instanceof JSONArray)
			return new DataArrayXmlImpl((JSONArray) o);*/
		return o;
	}

	@Override
	public Object opt(int key)
	{
		Object o = getValueAs(key, null, true);
		/*if(o instanceof Node[])
			return new DataObjectXmlImpl((Node[])o);*/
		if(o instanceof Node)
			return new DataArrayXmlImpl((Node) o);
		return o;
	}

	@Override
	public Object opt(int key, Object obj)
	{
		Object o = opt(key);
		if(o == null)
			return obj;
		return o;
	}

	@Override
	public Object[] asJavaArray()
	{
		Object[] ret = new Object[size()];
		/*for(int i=0;i<ret.length;i++)
		{
			Object curr = arr.get(i);
			if(curr instanceof DataArrayXmlImpl)
				ret[i] = ((DataArray)curr).asJavaArray();
			else if(curr instanceof DataObjectJsonImpl)
				ret[i] = ((DataObject)curr).asJavaMap();
			else
				ret[i] = curr;
		}*/
		
		return ret;
	}

	@Override
	public void putBlob(byte[] blob)
	{
		setSubjectValue(size(), blob);
	}

	@Override
	public void putBlob(int i, byte[] blob)
	{
		setSubjectValue(i, blob);
	}

	@Override
	public byte[] getBlob(int i)
	{
		return getValueAs(i, byte[].class, false);
	}

	@Override
	public byte[] optBlob(int i)
	{
		try
		{
			return getValueAs(i, byte[].class, true);
		}
		catch(Exception e)
		{
			return null;
		}
	}

	@Override
	public byte[] optBlob(int i, byte[] blob)
	{
		try
		{
			byte[] ret = getValueAs(i, byte[].class, true);
			if(null == ret)
			{
				return blob;
			}
			
			return ret;
		}
		catch(Exception e)
		{
			return blob;
		}
	}

	@Override
	public boolean isBlob(int i)
	{
		try
		{
			return null != getValueAs(i, byte[].class, true);
		}
		catch(Exception e)
		{
			return false;
		}
	}

	@Override
	public Object getImpl()
	{
		return node;
	}

	@Override
	public Iterator<Object> iterator()
	{
		Collection ret = new ArrayList<>();
		fillNodes(ret);
		return ret.iterator();
	}

	@Override
	public DataReprezType getDataReprezType()
	{
		return DataReprezType.ARRAY;
	}
}