package eu.javaexperience.datareprez.xmlImpl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import javax.xml.transform.TransformerException;

import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import eu.javaexperience.collection.PublisherCollection;
import eu.javaexperience.datareprez.DataArray;
import eu.javaexperience.datareprez.DataCommonAbstractImpl;
import eu.javaexperience.datareprez.DataObject;
import eu.javaexperience.datareprez.DataReceiver;
import eu.javaexperience.datareprez.DataReprezTools;
import eu.javaexperience.datareprez.DataSender;
import eu.javaexperience.reflect.CastTo;
import eu.javaexperience.reflect.Mirror;
import eu.javaexperience.text.Format;
import eu.javaexperience.text.StringTools;
import eu.javaexperience.document.DocumentTools;

public abstract class DataOperatorXmlCommon extends DataCommonAbstractImpl
{
	static DataObjectXmlImpl objPrototype = new DataObjectXmlImpl();
	static DataArrayXmlImpl arrPrototype = new DataArrayXmlImpl();
	
	public static final String NODE_NAME_MARK_NEED_ADOPT = "DataObjectXml_DataReprezentationObject_withoutRootNode_that_need_to_be_adopted";
	
	protected final Node node;
	
	public DataOperatorXmlCommon(Node n)
	{
		node = n;
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
	public void sendDataObject(DataObject dat, OutputStream os) throws IOException
	{
		DataOperatorXmlCommon.sendObject(dat,os);
	}

	public static DataObject receiveObject(InputStream is) throws IOException
	{
		byte b[] = new byte[1000];
		int ep =0;
		int i=0;
		if((i=is.read())==-1)
			throw new IOException("Socket closed");
		b[ep]=(byte)i;
		ep++;
		while((i=is.read())!=-1)
		{
			if(i==10)
				break;
			b[ep]=(byte)i;
			ep++;
			if(ep==b.length) b = Arrays.copyOf(b, ep*2);
		}
		return xmlObjectFromBlob(Arrays.copyOf(b, ep));
	}
	
	public static void sendObject(DataObject dat,OutputStream os) throws IOException
	{
		os.write(xmlToString(((DataOperatorXmlCommon)dat).node).getBytes());
		os.write('\n');
		os.flush();
	}

	public static void sendArray(DataArray dat,OutputStream os) throws IOException
	{
		os.write(((DataArrayXmlImpl)dat).toString().getBytes());
		os.write('\n');
		os.flush();
	}
	
	public static DataArray receiveArray(InputStream is) throws IOException
	{
		byte b[] = new byte[1000];
		int ep =0;
		int i=0;
		if((i=is.read())==-1)
			throw new IOException("A socket bezárult");
		b[ep]=(byte)i;
		ep++;
		while((i=is.read())!=-1)
		{
			if(i==10)
				break;
			b[ep]=(byte)i;
			ep++;
			if(ep==b.length) b = Arrays.copyOf(b, ep*2);
		}
		String ki = new String(Arrays.copyOf(b, ep));
		return ki.length()==0?null:new DataArrayXmlImpl();
	}
	
	@Override
	public DataObject receiveDataObject(InputStream is) throws IOException
	{
		return receiveObject(is);
	}

	@Override
	public void sendDataArray(DataArray dat, OutputStream os) throws IOException
	{
		sendArray(dat, os);
	}

	@Override
	public DataArray receiveDataArray(InputStream is) throws IOException
	{
		return receiveArray(is);
	}

	@Override
	public DataSender newDataSender(final OutputStream os)
	{
		return new XmlDataSender(os);
	}

	@Override
	public DataReceiver newDataReceiver(final InputStream is)
	{
		return new XmlDataReceiver(is);
	}
	
	@Override
	public boolean isNull(Object o)
	{
		return null == o || o == JSONObject.NULL;
	}
	
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
			return new DataObjectXmlImpl(doc);
		}
		catch (Exception e)
		{
			Mirror.propagateAnyway(e);
			return null;
		}
	}
	
	@Override
	public DataArray arrayFromBlob(byte[] data)
	{
		return xmlArrayFromBlob(data);
	}

	@Override
	public DataObject objectFromBlob(byte[] data)
	{
		return xmlObjectFromBlob(data);
	}
	
	protected abstract Node getOrCreateSubjectValue(Object key, boolean create);
	
	protected static void adoptSubNodesTo(Node dst, Node srcRoot)
	{
		Document doc = DocumentTools.getOwnerDocument(dst);
		NodeList nl = srcRoot.getChildNodes();
		for(int i=0;i<nl.getLength();++i)
		{
			dst.appendChild(doc.adoptNode(nl.item(i)));
		}
	}
	
	protected static void adoptNodeTo(Node dst, boolean childs, String name, Node src)
	{
		Document doc = DocumentTools.getOwnerDocument(dst);
		/*if(dst.getNodeName().equals("map"))
			System.out.println();*/
		
		
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
	
	protected boolean needAdopt()
	{
		return NODE_NAME_MARK_NEED_ADOPT.equals(node.getNodeName());
	}
	
	public static void applyNodeValue(final DataOperatorXmlCommon subject, final Node n, final String name, Object value)
	{
		if(null == value)
		{
			n.setTextContent("");
			//n.setTextContent(null);
			return;
		}
		
/*		boolean childs = 
				false;//subject.node.getChildNodes().getLength() != 0;
	*/	
		
		if(value instanceof DataArrayXmlImpl)
		{
			DataArrayXmlImpl arr = ((DataArrayXmlImpl)value);
			arr.fillNodes(new PublisherCollection<Node>()
			{
				@Override
				public boolean add(Node obj)
				{
					adoptNodeTo(n, false, subject.getKeyOrName(name), obj);
					//adoptSubNodesTo(n, obj);
					return false;
				}
			});
		}
		else if(value instanceof DataObjectXmlImpl)
		{
			//objektumból => tömbbe.
			if(subject instanceof DataArray)
			{
				DataObjectXmlImpl s = (DataObjectXmlImpl) value;
				DocumentTools.copyAttrAndAdopChilds
				(
					DocumentTools.getOwnerDocument(subject.node),
					n,
					s.node
				);
				//adoptNodeTo(subject.node, true, name, s.node);
			}
			else
			{
				//objektumból => objektumba
				DataObjectXmlImpl s = (DataObjectXmlImpl) value;
				adoptNodeTo(n, s.needAdopt(), null, s.node);
			}
			//adoptNodeTo(n, childs, subject.getKeyOrName(name), (((DataObjectXmlImpl)value).node));
		}
		else if(value instanceof DataObject)
		{
			DataObjectXmlImpl dst = new DataObjectXmlImpl(n);
			DataReprezTools.copyInto(dst, (DataObject) value);
			applyNodeValue(subject, n, name, dst);
		}
		else if(value instanceof DataArray)
		{
			DataArrayXmlImpl dst = new DataArrayXmlImpl(n.getNodeName(), n);
			DataReprezTools.copyInto(dst, (DataArray) value);
			applyNodeValue(subject, n, name, dst);
		}
		else
		{
			n.setTextContent(String.valueOf(value));
		}
	}
	
	protected void setSubjectValue(Object key, Object /*String or Node?*/ value)
	{
		String name = null;
		if(key instanceof String)
		{
			name = (String) key;
		}
		else
		{
			name = ((DataArrayXmlImpl)this).name;
		}
		
		if(this instanceof DataObject && value instanceof DataArray)
		{
			DataArray src = (DataArray) value;
			DataArrayXmlImpl arr = new DataArrayXmlImpl(name, node);
			for(int i=0;i<src.size();++i)
			{
				arr.setSubjectValue(i, src.get(i));
			}
		}
		else
		{
			applyNodeValue(this, getOrCreateSubjectValue(key, true), name, value);
		}
	}
	
	protected <C> C getValueOpt(Object key, Class<C> cls, C obj)
	{
		C ret = getValueAs(key, cls, true);
		if(null != ret)
		{
			return obj;
		}
		
		return obj;
	}
	
	protected <C> C getValueAs(Object _key, Class<C> cls, boolean mayNull)
	{
		Node n = getOrCreateSubjectValue(_key, false);
		if(null == n)
		{
			if(!mayNull)
			{
				throw new RuntimeException("Value not preset under key: "+_key);
			}
			else
			{
				return null;
			}
		}
		
		if(null == cls)
		{
			if(null != n && (n.hasChildNodes() || n.hasAttributes()))
			{
				return (C) n
						//.getTextContent()
						;
			}
			return null;
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
		else if(Node.class == cls)
		{
			return (C) n;
		}
		else if(Node[].class == cls && _key instanceof String)
		{
			String key = (String) _key;
			boolean attr = key.startsWith("-");
			if(attr)
			{
				NamedNodeMap nm = node.getAttributes();
				key = StringTools.getSubstringAfterFirstString(key, "-");
				Node ret = nm.getNamedItem(key);
				if(null != ret)
				{
					return (C) new Node[]{ret};
				}
				return null;
			}
			else
			{
				ArrayList<Node> toArr = new ArrayList<>();
				NodeList nl = node.getChildNodes();
				for(int i=0; i < nl.getLength();++i)
				{
					Node item = nl.item(i);
					if(item.getNodeName().equals(key))
					{
						toArr.add(item);
					}
				}
				
				return (C) toArr.toArray(DocumentTools.emptyNodeArrayInstance);
			}
		}
		
		return null;
	}
	
	public byte[] toBlob()
	{
		StringBuilder sb = new StringBuilder();
		try
		{
			NodeList nl = node.getChildNodes();
			for(int i=0;i<nl.getLength();++i)
			{
				DocumentTools.toString(nl.item(i), sb);
			}
			return sb.toString().getBytes();
		}
		catch (TransformerException e)
		{
			Mirror.propagateAnyway(e);
			return null;
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
	
	protected String getKeyOrName(String key)
	{
		if(null == key || this instanceof DataArray)
		{
			return ((DataArrayXmlImpl) this).name;
		}
		return key;
	}
	
	public static String xmlToString(Node node)
	{
		try
		{
			if(node.getChildNodes().getLength() == 1)
				return DocumentTools.toString(node.getFirstChild());
			else
				return DocumentTools.toString(node);
		}
		catch (TransformerException e)
		{
			Mirror.propagateAnyway(e);
			return null;
		}
	}
	
	protected static Object tryExamineOrWrapObject(Node n)
	{
		NodeList ch = n.getChildNodes();
		switch(ch.getLength())
		{
			case 0:
				return n;
			case 1:
				if(DocumentTools.isTextNode(ch.item(0)))
				{
					return n.getTextContent();
				}
			default:
				return new DataObjectXmlImpl(n);
		}
	}
	
	public Object get(String key)
	{
		Object o = getValueAs(key, null, true);
		if(o instanceof Node)
			return tryExamineOrWrapObject((Node) o);
		//TODO if(o instanceof Node)
		//	return new DataArrayXmlImpl(key, node);
		return o;
	}

	public Object opt(String key)
	{
		Object o = getValueAs(key, null, true);
		if(o instanceof Node)
			return new DataObjectXmlImpl((Node) o);
		if(o instanceof Node[])
			return new DataArrayXmlImpl(key, node);
		return o;
	}

	public Object opt(String key, Object obj)
	{
		Object o = getValueAs(key, null, true);
		if(o == null)
			return obj;
		if(o instanceof Node)
			return new DataObjectXmlImpl((Node) o);
		if(o instanceof Node[])
			return new DataArrayXmlImpl(key, node);
		return o;
	}
	
	@Override
	public String toString()
	{
		return xmlToString(node);
	}
	
	@Override
	public Class getCommonsClass()
	{
		return DataOperatorXmlCommon.class;
	}
}