package eu.javaexperience.datareprez.xmlImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import eu.javaexperience.collection.NullCollection;
import eu.javaexperience.datareprez.abstractImpl.DataArrayAbstractImpl;
import eu.javaexperience.datareprez.abstractImpl.DataProtocol;
import eu.javaexperience.datareprez.convertFrom.DataReprezType;
import eu.javaexperience.document.DocumentTools;

public class DataArrayXmlImpl extends DataArrayAbstractImpl
{
	protected Node node;
	protected String name;
	
	public DataArrayXmlImpl(String name, Node owner)
	{
		this.node = owner;
		this.name = name;
	}
	
	public DataArrayXmlImpl(Node owner)
	{
		this.node = owner;
		this.name = DataCommonXmlImpl.NODE_NAME_MARK_NEED_ADOPT;
	}
	
	
	public DataArrayXmlImpl()
	{
		this.node = DocumentTools.createEmptyDocument().createElement(DataCommonXmlImpl.NODE_NAME_MARK_NEED_ADOPT);
		name = DataCommonXmlImpl.NODE_NAME_MARK_NEED_ADOPT;
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
	protected <T> void setSubjectValue(int index, Class<T> cls, T value)
	{
		DataCommonXmlImpl.setSubjectValue(node, name, index, cls, value);
	}

	@Override
	protected <T> T getValueAs(int index, Class<T> retType)
	{
		return DataCommonXmlImpl.getValueAs(node, name, index, retType);
	}

	@Override
	protected DataProtocol getProtocolHandler()
	{
		return DataCommonXmlImpl.PROTOCOL;
	}

	@Override
	public int size()
	{
		return fillNodes(NullCollection.INSTANCE);
	}
	
	@Override
	public String toString()
	{
		return "DataArrayXmlImpl: "+name+", "+DataCommonXmlImpl.xmlToString(node);
	}
}