package eu.javaexperience.datareprez.xmlImpl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import eu.javaexperience.datareprez.DataArray;
import eu.javaexperience.datareprez.DataCommonAbstractImpl;
import eu.javaexperience.datareprez.DataObject;
import eu.javaexperience.datareprez.DataReprezTools;
import eu.javaexperience.datareprez.abstractImpl.DataProtocol;
import eu.javaexperience.reflect.CastTo;
import eu.javaexperience.reflect.Mirror;
import eu.javaexperience.text.Format;
import eu.javaexperience.text.StringTools;
import eu.javaexperience.document.DocumentTools;
import eu.javaexperience.io.primitive.LineReader;
import eu.javaexperience.io.primitive.LineReader.LineMode;

public abstract class DataCommonXmlImpl
{
	static DataObjectXmlImpl objPrototype = new DataObjectXmlImpl();
	static DataArrayXmlImpl arrPrototype = new DataArrayXmlImpl();
	
	public static final String NODE_NAME_MARK_NEED_ADOPT = "DataReprezRoot";
	
	public static final String ATTRIBUTE_NAME_PREFIX = "-";
	
	public static final DataProtocol PROTOCOL = new DataProtocol()
	{
		@Override
		public void sendPacket(byte[] data, OutputStream os) throws IOException
		{
			os.write(data);
			os.write('\n');
		}
		
		@Override
		public DataObject objectFromBlob(byte[] data)
		{
			return DataCommonXmlImpl.xmlObjectFromBlob(data);
		}
		
		@Override
		public DataObject newObjectInstance()
		{
			return new DataObjectXmlImpl();
		}
		
		@Override
		public DataArray newArrayInstance()
		{
			return new DataArrayXmlImpl();
		}
		
		@Override
		public Class getCommonsClass()
		{
			return Node.class;
		}
		
		@Override
		public DataArray arrayFromBlob(byte[] data)
		{
			return DataCommonXmlImpl.xmlArrayFromBlob(data);
		}
		
		@Override
		public byte[] acquirePacket(InputStream is) throws IOException
		{
			return LineReader.readByteLine(is, LineMode.Unix);
		}

		@Override
		public Object getNullObject()
		{
			return null;
		}
	};
	
	public static DataArray xmlArrayFromBlob(byte[] data)
	{
		try
		{
			Document doc = DocumentTools.parseDocument(new String(data));
			return new DataArrayXmlImpl(doc.getFirstChild());
		}
		catch (Exception e)
		{
			Mirror.propagateAnyway(e);
			return null;
		}
	}

	public static DataObject xmlObjectFromBlob(byte[] data)
	{
		try
		{
			Document doc = DocumentTools.parseDocument(new String(data));
			if(NODE_NAME_MARK_NEED_ADOPT.equals(doc.getFirstChild().getNodeName()))
			{
				return new DataObjectXmlImpl(doc.getFirstChild());
			}
			
			return new DataObjectXmlImpl(doc);
		}
		catch (Exception e)
		{
			Mirror.propagateAnyway(e);
			return null;
		}
	}
	
	protected static void adoptNodeTo(Node dst, boolean childs, String name, Node src)
	{
		Document doc = DocumentTools.getOwnerDocument(dst);
		
		if(childs)
		{
			DocumentTools.adoptAttributes(doc, dst, src);
			NodeList nl = src.getChildNodes();
			for(int i=0;i<nl.getLength();++i)
			{
				dst.appendChild(adoptOrCopy(doc, name, nl.item(i)));
			}
		}
		else
		{
			dst.appendChild(adoptOrCopy(doc, name, src));
		}
	}
	
	protected static Node adoptOrCopy(Document doc, String name, Node src)
	{
		if(null == name)
		{
			return DocumentTools.copyAttrAndAdopChilds(doc, createTag(doc, src.getNodeName()), src);
		}
		else
		{
			return DocumentTools.copyAttrAndAdopChilds(doc, createTag(doc, name), src);
		}
	}
	
	protected static boolean needAdopt(Node node)
	{
		return NODE_NAME_MARK_NEED_ADOPT.equals(node.getNodeName());
	}
	
	public static void applyNodeValue(Node subject, final Node n, final String name, Class type, Object value)
	{
		if(null == value)
		{
			if(null == type)
			{
				subject.removeChild(n);
			}
			else
			{
				n.setTextContent("");
			}
			return;
		}
		
		if(value instanceof DataObjectXmlImpl)
		{
			DataObjectXmlImpl s = (DataObjectXmlImpl) value;
			adoptNodeTo(n, needAdopt(s.node), null, s.node);
		}
		else if(value instanceof DataObject)
		{
			DataObjectXmlImpl dst = new DataObjectXmlImpl(n);
			DataReprezTools.copyInto(dst, (DataObject) value);
			applyNodeValue(subject, n, name, type, dst);
		}
		else
		{
			value = DataCommonAbstractImpl.wrapValueToStore(type, value);
			n.setTextContent(String.valueOf(value));
		}
	}
	
	protected static void keepSingleNode(Node subject, String name, Node keep)
	{
		NodeList nl = subject.getChildNodes();
		for(int i=0; i < nl.getLength();++i)
		{
			Node n = nl.item(i);
			if(n.getNodeName().equals(name) && !n.equals(keep))
			{
				subject.removeChild(n);
				--i;
			}
		}
	}

	protected static void setSubjectValue(Node xml, String nodeName, Object key, Class type, Object /*String or Node?*/ value)
	{
		String name = null;
		if(key instanceof String)
		{
			name = (String) key;
		}
		else
		{
			name = nodeName;
		}
		
		if(value instanceof DataArray)
		{
			String xKey = null == nodeName?name:nodeName;
			
			if(value instanceof DataArrayXmlImpl)
			{
				Document doc = DocumentTools.getOwnerDocument(xml);
				DataArrayXmlImpl src = (DataArrayXmlImpl) value;
				if(null != nodeName)
				{
					src.node = adoptOrCopy(doc, xKey, src.node);
					xml.appendChild(src.node);
					src.name = nodeName;
				}
				else
				{
					deleteKey(xml, xKey);
					NodeList cl = src.node.getChildNodes();
					for(int i=0;i<cl.getLength();++i)
					{
						xml.appendChild(adoptOrCopy(doc, xKey, cl.item(i)));
					}
					src.node = xml;
					src.name = xKey;
				}
			}
			else
			{
				DataArray src = (DataArray) value;
				DataArray dst = new DataArrayXmlImpl(xKey, xml);
				DataReprezTools.copyInto(dst, src);
			}
		}
		else
		{
			Node node = getOrCreateSubjectValue(xml, nodeName, key, true);
			if(key instanceof String)
			{
				keepSingleNode(xml, name, node);
			}
			applyNodeValue(xml, node, name, type, value);
		}
	}
	
	protected static void deleteKey(Node node, String key)
	{
		boolean attr = key.startsWith(ATTRIBUTE_NAME_PREFIX);
		
		if(attr)
		{
			NamedNodeMap nm = node.getAttributes();
			key = StringTools.getSubstringAfterFirstString(key, ATTRIBUTE_NAME_PREFIX);
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
					node.removeChild(n);
					--i;
				}
			}
		}
	}
	
	public static Node createTag(Document doc, String name)
	{
		if(name.equals("#text"))
		{
			return doc.createTextNode("");
		}
		else if(name.equals("#comment"))
		{
			return doc.createComment("");
		}
		else
		{
			return doc.createElement(name);
		}
	}
	

	
	
	protected static int getNumberOfNodesWithName(Node node, String name)
	{
		int ret = 0;
		NodeList nl = node.getChildNodes();
		for(int i=0; i < nl.getLength();++i)
		{
			Node n = nl.item(i);
			if(n.getNodeName().equals(name))
			{
				++ret;
			}
		}
		return ret;
	}
	
	protected static <C> C getValueAs(Node node, String nodeName, Object _key, Class<C> cls)
	{
		int nums = getNumberOfNodesWithName(node, null == nodeName?(String)_key:nodeName);
		
		if(0 == nums)
		{
			return null;
		}
		
		Node n = getOrCreateSubjectValue(node, nodeName, _key, false);
		
		if(null == cls || Object.class == cls)
		{
			if(nums > 1)
			{
				if(null == nodeName)
				{
					return (C) new DataArrayXmlImpl((String)_key, node);
				}
				else if(NODE_NAME_MARK_NEED_ADOPT.equals(nodeName) && n.getChildNodes().getLength() > 1)
				{
					return (C) new DataArrayXmlImpl(nodeName, n);
				}
			}
			
			return (C) tryExamineOrWrapObject(n);
		}
		
		if(String.class == cls)
		{
			return (C) CastTo.String.cast(n.getTextContent());
		}
		else if(byte[].class == cls)
		{
			return (C) Format.base64Decode((String)CastTo.String.cast(n.getTextContent()));
		}
		else if(int.class == cls || Integer.class == cls)
		{
			return (C) CastTo.Int.cast(n.getTextContent());
		}
		else if(long.class == cls || Long.class == cls)
		{
			return (C) CastTo.Long.cast(n.getTextContent());
		}
		else if(double.class == cls || Double.class == cls)
		{
			return (C) CastTo.Double.cast(n.getTextContent());
		}
		else if(boolean.class == cls || Boolean.class == cls)
		{
			return (C) CastTo.Boolean.cast(n.getTextContent());
		}
		else if(DataObject.class == cls)
		{
			return (C) new DataObjectXmlImpl(n);
		}
		else if(DataArray.class == cls)
		{
			if(_key instanceof String)
			{
				String key = (String) _key;
				boolean attr = key.startsWith(ATTRIBUTE_NAME_PREFIX);
				if(attr)
				{
					NamedNodeMap nm = node.getAttributes();
					key = StringTools.getSubstringAfterFirstString(key, ATTRIBUTE_NAME_PREFIX);
					Node ret = nm.getNamedItem(key);
					if(null != ret)
					{
						return (C) new Node[]{ret};
					}
					return null;
				}
				else
				{
					return (C) new DataArrayXmlImpl(key, node);
				}
			}
			else
			{
				return (C) new DataArrayXmlImpl(nodeName, n);
			}
		}
		
		return null;
	}
	
	protected static Node getOrCreateSubjectValue(Node node, String nodeName, Object _key, boolean create)
	{
		String key = nodeName;
		if(null == nodeName)
		{
			key = (String) _key;
		}
		boolean attr = key.startsWith(ATTRIBUTE_NAME_PREFIX);
		
		if(attr)
		{
			NamedNodeMap nm = node.getAttributes();
			key = StringTools.getSubstringAfterFirstString(key, ATTRIBUTE_NAME_PREFIX);
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
			int ind = 0;
			if(_key instanceof Integer)
			{
				ind = (int) _key;
			}
			int crnt = 0;
			for(int i=0; i < nl.getLength();++i)
			{
				Node n = nl.item(i);
				if(n.getNodeName().equals(key))
				{
					if(crnt == ind)
					{
						return n;
					}
					else
					{
						++crnt;
					}
				}
			}
			
			if(create)
			{
				Node add = null;
				if(key.equals("#text"))
				{
					add = DocumentTools.getOwnerDocument(node).createTextNode("");
				}
				else if(key.equals("#comment"))
				{
					add = DocumentTools.getOwnerDocument(node).createComment("");
				}
				else
				{
					add = DocumentTools.getOwnerDocument(node).createElement(key);
				}
				node.appendChild(add);
				return add;
			}
			
			return null;
		}
	}
	
	public static Object tryExamineOrWrapObject(Node n)
	{
		NodeList ch = n.getChildNodes();
		switch(ch.getLength())
		{
			case 0:
				return null;
			case 1:
				if(DocumentTools.isTextNode(ch.item(0)))
				{
					return n.getTextContent();
				}
			default:
				return new DataObjectXmlImpl(n);
		}
	}
	
	public static String xmlToString(Node node)
	{
		try
		{
			return DocumentTools.toString(node);
		}
		catch (TransformerException e)
		{
			Mirror.propagateAnyway(e);
			return null;
		}
	}
}