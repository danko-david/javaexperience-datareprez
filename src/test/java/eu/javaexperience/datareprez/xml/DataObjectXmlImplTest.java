package eu.javaexperience.datareprez.xml;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import eu.javaexperience.collection.map.OneShotMap;
import eu.javaexperience.datareprez.DataArray;
import eu.javaexperience.datareprez.DataObject;
import eu.javaexperience.datareprez.DataObjectTest;
import eu.javaexperience.datareprez.DataReprezDialectTools;
import eu.javaexperience.datareprez.DataReprezTools;
import eu.javaexperience.datareprez.convertFrom.DataWrapper;
import eu.javaexperience.datareprez.xmlImpl.DataObjectXmlImpl;
import eu.javaexperience.datareprez.xmlImpl.DataOperatorXmlCommon;
import eu.javaexperience.reflect.FieldNameDialect;

public class DataObjectXmlImplTest extends DataObjectTest
{
	public DataObjectXmlImplTest()
	{
		super(new DataObjectXmlImpl());
	}
	
	protected static final DataWrapper DATA_WRAPPER = DataReprezTools.combineWrappers
	(
		DataReprezTools.WRAP_ARRAY_COLLECTION_MAP,
		DataReprezTools.WRAP_DATA_LIKE,
		DataReprezDialectTools.generateFieldDialectWrapper("DataXmlObject")
	);
	
	public static class Root
	{
		@FieldNameDialect(dialect="DataXmlObject", name="root")
		public Object root;
		
		public Root(Object o)
		{
			this.root = o;
		}
	}
	
	public static class SerConfig
	{
		@FieldNameDialect(dialect="DataXmlObject", name="-name")
		public String name;
	
		@FieldNameDialect(dialect="DataXmlObject", name="prop")
		public ArrayList<Prop> prop = new ArrayList<>();
		
		@FieldNameDialect(dialect="DataXmlObject", name="etc")
		public ArrayList<String> etcs = new ArrayList<>();
	}
	
	
	public static class Prop
	{
		@FieldNameDialect(dialect="DataXmlObject", name="-name")
		public final String name;
		
		@FieldNameDialect(dialect="DataXmlObject", name="#text")
		public final String value;
		
		public Prop(String name, String value)
		{
			this.name = name;
			this.value = value;
		}
	}
	
	@Test
	public void test_javaObjectSerialize()
	{
		SerConfig cfg = new SerConfig();
		cfg.prop.add(new Prop("port", "80"));
		cfg.prop.add(new Prop("host", "127.0.0.1"));
		
		DataObject obj = (DataObject) DATA_WRAPPER.wrap(DATA_WRAPPER, DataObjectXmlImpl.INSTANCE, new Root(cfg));
		Assert.assertEquals("<root><prop name=\"port\">80</prop><prop name=\"host\">127.0.0.1</prop></root>", obj.toString());
	}
	
	@Test
	public void test_javaObjectSerialize2()
	{
		SerConfig cfg = new SerConfig();
		cfg.name = "root config";
		cfg.prop.add(new Prop("port", "9001"));
		cfg.prop.add(new Prop("host", "localhost"));
		cfg.prop.add(new Prop("user", "root"));
		cfg.prop.add(new Prop("password", "s3cr37"));
		
		cfg.etcs.add("str1");
		cfg.etcs.add("str2");
		
		DataObject obj = (DataObject) DATA_WRAPPER.wrap(DATA_WRAPPER, DataObjectXmlImpl.INSTANCE, new Root(cfg));
		Assert.assertEquals("<root name=\"root config\"><etc>str1</etc><etc>str2</etc><prop name=\"port\">9001</prop><prop name=\"host\">localhost</prop><prop name=\"user\">root</prop><prop name=\"password\">s3cr37</prop></root>", obj.toString());
	}
	
	@Test
	public void test_javaObjectSerialize3()
	{
		SerConfig cfg = new SerConfig();
		cfg.name = "root config";
		cfg.prop.add(new Prop("port", "9001"));
		cfg.prop.add(new Prop("host", "localhost"));
		cfg.prop.add(new Prop("user", "root"));
		cfg.prop.add(new Prop("password", "s3cr37"));
		
		cfg.etcs.add("str1");
		cfg.etcs.add("str2");
		
		DataObject obj = (DataObject) DATA_WRAPPER.wrap(DATA_WRAPPER, DataObjectXmlImpl.INSTANCE, new OneShotMap<String, Root>("map", new Root(cfg)));
		Assert.assertEquals("<map><root name=\"root config\"><etc>str1</etc><etc>str2</etc><prop name=\"port\">9001</prop><prop name=\"host\">localhost</prop><prop name=\"user\">root</prop><prop name=\"password\">s3cr37</prop></root></map>", obj.toString());
	}
	
	
	@Test
	public void test_buildArrays()
	{
		DataArray arr = DataObjectXmlImpl.INSTANCE.newArrayInstance();
		arr.putDouble(3.14);
		arr.putString("szöveg");
		
		final String N = DataOperatorXmlCommon.NODE_NAME_MARK_NEED_ADOPT;
		
		Assert.assertEquals(arr.toString(), "<"+N+"><"+N+">3.14</"+N+"><"+N+">szöveg</"+N+"></"+N+">");
		
		DataObject obj = DataObjectXmlImpl.INSTANCE.newObjectInstance();
		obj.putArray("arr", arr);
		
		Assert.assertEquals(obj.toString(), "<"+N+"><arr>3.14</arr><arr>szöveg</arr></"+N+">");
		
		DataObject root = DataObjectXmlImpl.INSTANCE.newObjectInstance();
		root.putObject("root", obj);
		
		System.out.println(root);
		Assert.assertEquals(root.toString(), "<root><arr>3.14</arr><arr>szöveg</arr></root>");
	}
}
