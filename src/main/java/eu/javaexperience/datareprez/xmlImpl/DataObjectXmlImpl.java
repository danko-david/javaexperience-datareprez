package eu.javaexperience.datareprez.xmlImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import eu.javaexperience.datareprez.DataAccessor;
import eu.javaexperience.datareprez.DataArray;
import eu.javaexperience.datareprez.DataCommon;
import eu.javaexperience.datareprez.DataObject;
import eu.javaexperience.datareprez.convertFrom.DataReprezType;
import eu.javaexperience.reflect.Mirror;
import eu.javaexperience.text.Format;
import eu.javaexperience.text.StringTools;
import eu.javaexperience.document.DocumentTools;
import  eu.javaexperience.datareprez.DataReprezDialectTools;

public class DataObjectXmlImpl extends DataOperatorXmlCommon implements DataObject
{
	public static DataCommon INSTANCE = new DataObjectXmlImpl();
	
	public static final DataAccessor DATA_ACCESSOR_DIALECT__DataXmlObject = DataReprezDialectTools.generateFieldDialectAccessor("DataXmlObject");
	
	protected Node getOrCreateSubjectValue(Object _key, boolean create)
	{
		String key = (String) _key;
		boolean attr = key.startsWith("-");
		
		if(attr)
		{
			NamedNodeMap nm = node.getAttributes();
			key = StringTools.getSubstringAfterFirstString(key, "-");
			Node ret = nm.getNamedItem(key);
			if(null == ret && create)
			{
				nm.setNamedItem(ret = DocumentTools.getOwnerDocument(node).createAttribute(key));
			}
			return ret;
		}
		else
		{
			NodeList nl = node.getChildNodes();
			for(int i=0; i < nl.getLength();++i)
			{
				Node n = nl.item(i);
				if(n.getNodeName().equals(key))
				{
					return n;
				}
			}
			
			Node ret = null;
			if(null == ret && create)
			{
				if(key.equals("#text"))
				{
					ret = DocumentTools.getOwnerDocument(node).createTextNode("");
				}
				else if(key.equals("#comment"))
				{
					ret = DocumentTools.getOwnerDocument(node).createComment("");
				}
				else
				{
					ret = DocumentTools.getOwnerDocument(node).createElement(key);
				}
				node.appendChild(ret);
			}
			return ret;
		}
	}
	
	protected void deleteKey(String key)
	{
		boolean attr = key.startsWith("-");
		
		if(attr)
		{
			NamedNodeMap nm = node.getAttributes();
			key = StringTools.getSubstringAfterFirstString(key, "-");
			nm.removeNamedItem(key);
		}
		else
		{
			NodeList nl = node.getChildNodes();
			for(int i=0; i < nl.getLength();++i)
			{
				Node n = nl.item(i);
				if(n.getNodeName().equals(key))
				{
					n.removeChild(n);
					return;
				}
			}
		}
	}
	
	/**
	 * <html>
	 * 	<head>
	 * 		<meta name="key1" content="value1"></meta>
	 * 		<meta name="key2" content="value2"></meta>
	 * 	</head>
	 * 	<body id="something"> </body>
	 * </html>
	 * 
	 * html => {head: ..., body: ...}
	 *
	 * html.head.meta[0].-name = "key1"
	 * html.head.meta[1].-name = "key2"
	 * 
	 * html.head => {meta:[{-name: "key1", -content:"value1"}, {-name:"key2", -content:"value2"}]}
	 * 
	 * html.body => {"-id": "something"}
	 * html.body.-id => "something"
	 * */
	public DataObjectXmlImpl()
	{
		super(DocumentTools.createEmptyDocument().createElement(NODE_NAME_MARK_NEED_ADOPT));
		node.getOwnerDocument().appendChild(node);
	}
	
	public DataObjectXmlImpl(Node node)
	{
		super(node);
	}

	@Override
	public void putString(String key, String val)
	{
		setSubjectValue(key, val);
	}

	@Override
	public void putLong(String key, long val)
	{
		setSubjectValue(key, val);
	}

	@Override
	public void putDouble(String key, double val)
	{
		setSubjectValue(key, val);
	}

	@Override
	public void putInt(String key, int val)
	{
		setSubjectValue(key, val);
	}


	@Override
	public void putBoolean(String key, boolean val)
	{
		setSubjectValue(key, val);
	}

	@Override
	public void putObject(String key, DataObject val)
	{
		setSubjectValue(key, val);
	}

	@Override
	public void putNull(String key)
	{
		setSubjectValue(key, null);
	}
	
	@Override
	public void putArray(String key, DataArray val)
	{
		setSubjectValue(key, val);
	}

	@Override
	public void putBlob(String key, byte[] blob)
	{
		setSubjectValue(key, Format.base64Encode(blob));
	}
	
	@Override
	public String getString(String key)
	{
		return getValueAs(key, String.class, false);
	}

	@Override
	public long getLong(String key)
	{
		return getValueAs(key, long.class, false);
	}

	@Override
	public double getDouble(String key)
	{
		return getValueAs(key, double.class, false);
	}

	@Override
	public int getInt(String key)
	{
		return getValueAs(key, int.class, false);
	}

	@Override
	public boolean getBoolean(String key)
	{
		return getValueAs(key, boolean.class, false);
	}

	@Override
	public DataObject getObject(String key)
	{
		return new DataObjectXmlImpl(getValueAs(key, Node.class, false));
	}

	@Override
	public DataArray getArray(String key)
	{
		return new DataArrayXmlImpl(key, node);
	}
	
	@Override
	public byte[] getBlob(String key)
	{
		return getValueAs(key, byte[].class, false);
	}

	@Override
	public String optString(String key)
	{
		return getValueAs(key, String.class, true);
	}

	@Override
	public long optLong(String key)
	{
		return getValueAs(key, long.class, true);
	}

	@Override
	public double optDouble(String key)
	{
		return getValueAs(key, double.class, true);
	}

	@Override
	public int optInt(String key)
	{
		return getValueAs(key, int.class, true);
	}

	@Override
	public boolean optBoolean(String key)
	{
		return getValueAs(key, boolean.class, true);
	}

	@Override
	public DataObject optObject(String key)
	{
		Node o = getValueAs(key, Node.class, false);
		if(o == null)
		{
			return null;
		}
		return new DataObjectXmlImpl(o);
	}

	@Override
	public DataArray optArray(String key)
	{
		if(null == getOrCreateSubjectValue(key, false))
		{
			return null;
		}
		return new DataArrayXmlImpl(key, node);
	}

	@Override
	public byte[] optBlob(String key)
	{
		try
		{
			return getValueAs(key, byte[].class, false);
		}
		catch(Exception e)
		{
			Mirror.propagateAnyway(e);
			return null;
		}
	}
	
	@Override
	public String optString(String key, String def)
	{
		return getValueOpt(key, String.class, def);
	}

	@Override
	public long optLong(String key, long def)
	{
		return getValueOpt(key, long.class, def);
	}

	@Override
	public double optDouble(String key, double def)
	{
		return getValueOpt(key, double.class, def);
	}

	@Override
	public int optInt(String key, int def)
	{
		return getValueOpt(key, int.class, def);
	}

	@Override
	public boolean optBoolean(String key, boolean def)
	{
		return getValueOpt(key, boolean.class, def);
	}

	@Override
	public DataObject optObject(String key, DataObject def)
	{
		Node o = getValueAs(key, Node.class, false);
		if(o == null)
		{
			return def;
		}
		return new DataObjectXmlImpl(o);
	}

	@Override
	public DataArray optArray(String key, DataArray def)
	{
		Object ret = getValueAs(key, null, true);
		if(null == ret)
			return null;
		return new DataArrayXmlImpl(key, node);
	}
	
	@Override
	public byte[] optBlob(String key, byte[] def)
	{
		try
		{
			return getValueOpt(key, byte[].class, def);
		}
		catch(Exception e)
		{
			return def;
		}
	}
	
	@Override
	public boolean has(String key)
	{
		return null != getOrCreateSubjectValue(key, false);
	}

	@Override
	public String[] keys()
	{
		ArrayList<String> ret = new ArrayList<>();
		NamedNodeMap nm = node.getAttributes();
		if(null != nm)
		{
			for(int i=0;i <nm.getLength();++i)
			{
				ret.add("-"+nm.item(i).getNodeName());
			}
		}
		NodeList nl = node.getChildNodes();
		for(int i=0; i < nl.getLength();++i)
		{
			ret.add(nl.item(i).getNodeName());
		}
		
		return ret.toArray(Mirror.emptyStringArray);
	}

	@Override
	public int size()
	{
		return keys().length;
	}

	@Override
	public boolean isString(String key)
	{
		return getValueAs(key, String.class, true) instanceof String;
	}

	@Override
	public boolean isLong(String key)
	{
		return getValueAs(key, Long.class, true) instanceof Long;
	}

	@Override
	public boolean isDouble(String key)
	{
		return getValueAs(key, Double.class, true) instanceof Double;
	}

	@Override
	public boolean isInt(String key)
	{
		return getValueAs(key, Integer.class, true) instanceof Integer;
	}

	@Override
	public boolean isBoolean(String key)
	{
		return getValueAs(key, Boolean.class, true) instanceof Boolean;
	}

	@Override
	public boolean isObject(String key)
	{
		return getValueAs(key, Node.class, true) instanceof Node;
	}

	@Override
	public boolean isArray(String key)
	{
		return getValueAs(key, Node[].class, true) instanceof Node[];
	}
	
	@Override
	public boolean isBlob(String key)
	{
		try
		{
			return getValueAs(key, byte[].class, true) instanceof byte[];
		}
		catch(Exception e)
		{
			return false;
		}
	}

	@Override
	public boolean isNull(String key)
	{
		return null == getValueAs(key, null, true);
	}

	@Override
	public Map<String, Object> asJavaMap()
	{
		Map<String,Object> ret = new HashMap<>();
		for(String k:keys())
		{
			Object curr = getValueAs(k, null, false);
			if(curr instanceof DataArrayXmlImpl)
				ret.put(k, ((DataArray)curr).asJavaArray());
			else if(curr instanceof DataObjectXmlImpl)
				ret.put(k, ((DataObject)curr).asJavaMap());
			else
				ret.put(k, curr);
		}
		
		return ret;
	}

	@Override
	public Object getImpl()
	{
		return node;
	}

	@Override
	public void remove(String key)
	{
		deleteKey(key);
	}

	@Override
	public DataReprezType getDataReprezType()
	{
		return DataReprezType.OBJECT;
	}
}