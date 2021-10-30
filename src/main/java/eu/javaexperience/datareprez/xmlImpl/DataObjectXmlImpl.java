package eu.javaexperience.datareprez.xmlImpl;

import java.util.ArrayList;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import eu.javaexperience.datareprez.DataCommon;
import eu.javaexperience.reflect.Mirror;
import eu.javaexperience.document.DocumentTools;
import eu.javaexperience.datareprez.abstractImpl.DataObjectAbstractImpl;
import eu.javaexperience.datareprez.abstractImpl.DataProtocol;

public class DataObjectXmlImpl extends DataObjectAbstractImpl
{
	protected Node node;
	
	public static DataCommon PROTOTYPE = new DataObjectXmlImpl();
	
	//public static final DataAccessor DATA_ACCESSOR_DIALECT__DataXmlObject = DataReprezDialectTools.generateFieldDialectAccessor("DataXmlObject");
	
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
		this(DataCommonXmlImpl.NODE_NAME_MARK_NEED_ADOPT);
	}
	
	public DataObjectXmlImpl(String name)
	{
		node = DocumentTools.createEmptyDocument().createElement(name);
		node.getOwnerDocument().appendChild(node);
	}
	
	public DataObjectXmlImpl(Node node)
	{
		this.node = node;
	}
	
	@Override
	public Object getImpl()
	{
		return node;
	}

	@Override
	public Class getCommonsClass()
	{
		return Node.class;
	}

	@Override
	public byte[] toBlob()
	{
		return DataCommonXmlImpl.xmlToString(node).getBytes();
	}

	@Override
	public boolean has(String key)
	{
		return null != DataCommonXmlImpl.getOrCreateSubjectValue(node, key, false, false);
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
			String name = nl.item(i).getNodeName();
			if(!ret.contains(name))
			{
				ret.add(name);
			}
		}
		
		return ret.toArray(Mirror.emptyStringArray);
	}

	@Override
	protected void setSubjectValue(String key, Class<?> valueType, Object val)
	{
		DataCommonXmlImpl.setSubjectValue(node, null, key, valueType, val);
	}

	@Override
	protected <T> T getValueAs(String key, Class<T> retType)
	{
		return DataCommonXmlImpl.getValueAs(node, null, key, retType);
	}

	@Override
	protected DataProtocol getProtocolHandler()
	{
		return DataCommonXmlImpl.PROTOCOL;
	}
	
	@Override
	public String toString()
	{
		return "DataObjectXmlImpl: "+DataCommonXmlImpl.xmlToString(node);
	}
	
	@Override
	public boolean isNull(String key)
	{
		Object ret = getValueAs(key, String.class, true);
		return null == ret || "".equals(ret);
	}
	
	protected void deleteKey(String key)
	{
		DataCommonXmlImpl.deleteKey(node, key);
	}
}