package eu.javaexperience.datareprez;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import eu.javaexperience.collection.CollectionTools;
import eu.javaexperience.collection.map.MapTools;
import eu.javaexperience.datareprez.example.DataSerialisationTestObject;
import eu.javaexperience.reflect.CastTo;

/**
 * TODO check prevent cyclic references
 * 
 * TODO test cross type conversion (XML accepts Java instance)
 * TODO DataReprezTools.put, especially when dealing with different
 * implementation of Data{Object,Array}
 * 
 * TODO add map as an addable type.
 * */
public abstract class DataReprezTest
{
	protected final DataCommon prototye;
	
	public DataReprezTest(DataCommon prototype)
	{
		this.prototye = prototype;
	}
	
	public DataObject createNewObject()
	{
		return prototye.newObjectInstance();
	}

	public DataArray createNewArray()
	{
		return prototye.newArrayInstance();
	}
	
	public DataObject createFullFeature()
	{
		DataObject obj = createNewObject();
		obj.putString("string", "string");
		DataArray arr = createNewArray();
		arr.putString("array string");
		arr.putString("array string");
		obj.putArray("array", arr);
		obj.putBlob("blob", "blob".getBytes());
		obj.putBoolean("boolean", true);
		obj.putDouble("double", Math.PI);
		obj.putInt("int", Integer.MIN_VALUE);
		obj.putLong("long", Long.MAX_VALUE);
		obj.putNull("null");
		
		DataObject so = prototye.newObjectInstance();
		so.putString("string", "obj string");
		obj.putObject("object", so);
		
		return obj;
	}
	
	public void assertFullFeaturedBounded(DataObject obj)
	{
		assertTrue(obj.isString("string"));
		assertTrue(obj.has("string"));
		assertEquals("string", obj.getString("string"));
		assertEquals("string", obj.optString("string"));
		assertEquals("string", obj.optString("string", "hello"));
		assertNull(obj.optString("none"));
		
		assertTrue(obj.isArray("array"));
		assertTrue(obj.has("array"));
		assertTrue(obj.getArray("array") instanceof DataArray);
		assertTrue(obj.optArray("array") instanceof DataArray);
		assertTrue(obj.optArray("array", createNewArray()) instanceof DataArray);
		assertNull(obj.optArray("none"));
		
		assertTrue(obj.isBlob("blob"));
		assertTrue(obj.has("blob"));
		assertArrayEquals("blob".getBytes(), obj.getBlob("blob"));
		assertArrayEquals("blob".getBytes(), obj.optBlob("blob"));
		assertArrayEquals("blob".getBytes(), obj.optBlob("blob", new byte[] {10}));
		assertNull(obj.optBlob("none"));
		
		assertTrue(obj.isBoolean("boolean"));
		assertTrue(obj.has("blob"));
		assertTrue(obj.getBoolean("boolean"));
		assertTrue(obj.optBoolean("boolean"));
		assertTrue(obj.optBoolean("boolean", false));
		assertNull(obj.optBoolean("none"));
		
		
		assertTrue(obj.isDouble("double"));
		assertTrue(obj.has("double"));
		assertEquals(Math.PI, obj.getDouble("double"), 0.00000001);
		assertEquals(Math.PI, obj.optDouble("double"), 0.00000001);
		assertEquals(Math.PI, obj.optDouble("double", -1.0), 0.00000001);
		assertNull(obj.optDouble("none"));
		
		assertTrue(obj.isInt("int"));
		assertTrue(obj.has("int"));
		assertEquals(Integer.MIN_VALUE, obj.getInt("int"));
		assertEquals((Integer) Integer.MIN_VALUE, obj.optInt("int"));
		assertEquals(Integer.MIN_VALUE, obj.optInt("int", 10));
		assertNull(obj.optInt("none"));
		
		assertTrue(obj.isLong("long"));
		assertTrue(obj.has("long"));
		assertEquals(Long.MAX_VALUE, obj.getLong("long"));
		assertEquals((Long)Long.MAX_VALUE, obj.optLong("long"));
		assertEquals(Long.MAX_VALUE, obj.optLong("long", 10));
		assertNull(obj.optLong("none"));
		
		assertTrue(obj.isNull("null"));
		
		assertTrue(obj.isObject("object"));
		assertTrue(obj.getObject("object") instanceof DataObject);
		assertTrue(obj.optObject("object") instanceof DataObject);
		assertTrue(obj.optObject("object", null) instanceof DataObject);
		
		DataObject o = obj.getObject("object");
		assertTrue(o.isString("string"));
		assertTrue(o.has("string"));
		assertEquals("obj string", o.getString("string"));
		assertEquals("obj string", o.optString("string"));
		assertEquals("obj string", o.optString("string", "hello"));
		assertNull(o.optString("none"));
		
		
		DataArray arr = obj.getArray("array");
		assertEquals("array string", arr.getString(0));
		
		Set<String> keys = new HashSet<>();
		CollectionTools.inlineAdd(keys, "boolean", "blob", "int", "long", "double", "string", "object", "array", "null");
		
		assertEquals(keys, CollectionTools.inlineAdd(new HashSet<>(), obj.keys()));
		assertEquals(9, obj.size());
	}
	
	public void assertFullFeaturedUnbounded(DataObject obj)
	{
		assertEquals("string", obj.get("string"));
		
		//it's depends from the implementation
		Object blob = obj.get("blob");
		if(blob instanceof String)
		{
			assertEquals("YmxvYg==", blob);
		}
		else
		{
			assertArrayEquals("blob".getBytes(), (byte[]) blob);
		}
		
		assertEquals(Boolean.TRUE, CastTo.Boolean.cast(obj.get("boolean")));
		assertEquals(Math.PI,  (double) CastTo.Double.cast(obj.get("double")), 0.00000001);
		assertEquals(Integer.MIN_VALUE, CastTo.Int.cast(obj.getInt("int")));
		assertEquals(Long.MAX_VALUE, CastTo.Long.cast(obj.getLong("long")));
		assertTrue(obj.isNull("null"));
		assertTrue(obj.get("object") instanceof DataObject);
		assertTrue(obj.opt("object") instanceof DataObject);
		assertTrue(obj.optObject("object") instanceof DataObject);

		DataObject o = (DataObject) obj.get("object");
		assertTrue(o.isString("string"));
		assertTrue(o.has("string"));
		assertEquals("obj string", o.get("string"));
		assertEquals("obj string", o.opt("string"));
		assertEquals("obj string", o.opt("string", "hello"));
		assertNull(o.optString("none"));
		
		
		assertTrue(obj.get("array") instanceof DataArray);
		assertTrue(obj.opt("array") instanceof DataArray);
		
		DataArray arr = (DataArray) obj.get("array");
		assertEquals("array string", arr.get(0));
	}
	
	public void assertAsJavaObject(DataObject obj)
	{
		Map<String, Object> java = obj.asJavaMap();
		
		assertEquals("string", java.get("string"));
		assertEquals(CollectionTools.inlineArrayList("array string", "array string"), (List) java.get("array"));
		Object blob = java.get("blob");
		if(blob instanceof byte[])
		{
			assertArrayEquals("blob".getBytes(), (byte[]) blob);
		}
		else
		{
			assertEquals("YmxvYg==", blob);
		}
		
		assertEquals(Boolean.TRUE, CastTo.Boolean.cast(java.get("boolean")));
		assertEquals(Math.PI, CastTo.Double.cast(java.get("double")));
		assertEquals(Integer.MIN_VALUE, CastTo.Int.cast(java.get("int")));
		assertEquals(Long.MAX_VALUE, CastTo.Long.cast(java.get("long")));
		//assertNull(java.get("null"));
		assertEquals(MapTools.inlineHashMap("string", "obj string"), java.get("object"));
	}
	
	@Test
	public void test_types_comes_back()
	{
		DataObject full = createFullFeature();
		assertFullFeaturedBounded(full);
		assertAsJavaObject(full);
	}
	
	@Test
	public void testOptReturnsNull()
	{
		DataObject obj = createNewObject();
		assertNull(obj.opt("unknown"));
		assertNull(obj.optString("unknown"));
		assertNull(obj.optBlob("unknown"));
		assertNull(obj.optObject("unknown"));
		assertNull(obj.optArray("unknown"));
	}
	
	@Test
	public void test_types_comes_unbound_back()
	{
		DataObject full = createFullFeature();
		assertFullFeaturedUnbounded(full);
		assertAsJavaObject(full);
	}
	
	@Test
	public void test_empty_nulls()
	{
		DataObject obj = createNewObject();
		assertNull(obj.opt("any"));
		assertNull(obj.optString("string"));
		assertNull(obj.optObject("object"));
		assertNull(obj.optArray("array"));
		assertNull(obj.optBlob("blob"));
		assertNull(obj.opt("any"));
	}
	
	@Test
	public void test_set_remove_nulls()
	{
		DataObject obj = createFullFeature();
		assertFullFeaturedBounded(obj);
		
		
		obj.remove("object");
		obj.remove("string");
		obj.remove("array");
		obj.remove("blob");
		obj.remove("boolean");
		obj.remove("double");
		obj.remove("int");
		obj.remove("long");
		
		assertNull(obj.opt("any"));
		assertNull(obj.optString("string"));
		assertNull(obj.optObject("object"));
		assertNull(obj.optArray("array"));
		assertNull(obj.optBlob("blob"));
	}
	
	public static DataObject virtualTransfer1(DataObject obj) throws IOException
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		obj.sendDataObject(obj, baos);
		ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
		return obj.receiveDataObject(bais);
	}
	
	public static DataObject virtualTransfer2(DataCommon comm, DataObject obj) throws IOException
	{
		byte[] data = obj.toBlob();
		return comm.objectFromBlob(data);
	}
	
	public static DataObject virtualTransfer3(DataCommon comm, DataObject obj) throws IOException
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataSender sender = comm.newDataSender(baos);
		sender.send(obj);
		baos.flush();
		ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
		DataReceiver rec = comm.newDataReceiver(bais);
		return rec.receiveDataObject();
	}
	
	@Test
	public void test_send_receive_basic_bound_1() throws IOException
	{
		DataObject ret = virtualTransfer1(createFullFeature());
		assertFullFeaturedBounded(ret);
		assertAsJavaObject(ret);
	}
	
	@Test
	public void test_send_receive_basic_unbound_1() throws IOException
	{
		DataObject ret = virtualTransfer1(createFullFeature());
		assertFullFeaturedUnbounded(ret);
		assertAsJavaObject(ret);
	}
	
	@Test
	public void test_send_receive_basic_bound_2() throws IOException
	{
		DataObject ret = virtualTransfer2(prototye, createFullFeature());
		assertFullFeaturedBounded(ret);
		assertAsJavaObject(ret);
	}
	
	@Test
	public void test_send_receive_basic_unbound_2() throws IOException
	{
		DataObject ret = virtualTransfer2(prototye, createFullFeature());
		assertFullFeaturedUnbounded(ret);
		assertAsJavaObject(ret);
	}
	
	@Test
	public void test_send_receive_basic_bound_3() throws IOException
	{
		DataObject ret = virtualTransfer3(prototye, createFullFeature());
		assertFullFeaturedBounded(ret);
		assertAsJavaObject(ret);
	}
	
	@Test
	public void test_send_receive_basic_unbound_3() throws IOException
	{
		DataObject ret = virtualTransfer3(prototye, createFullFeature());
		assertFullFeaturedUnbounded(ret);
		assertAsJavaObject(ret);
	}
	
	
	@Test
	public void test_get_values()
	{
		DataObject obj = createFullFeature();
		
		assertEquals(true, obj.getBoolean("boolean"));
		assertEquals("string", obj.getString("string"));
		assertEquals("blob", new String(obj.getBlob("blob")));
		assertEquals(Math.PI, obj.getDouble("double"), 0.001);
		assertEquals(Integer.MIN_VALUE, obj.getInt("int"));
		assertEquals(Long.MAX_VALUE, obj.getLong("long"));
		assertTrue(obj.isNull("null"));
		
		assertTrue(obj.getArray("array") instanceof DataArray);
		assertTrue(obj.getObject("object") instanceof DataObject);
		
		obj.putObject("object", prototye.newObjectInstance());
		
		DataArray arr = createNewArray();
		arr.putString("array string");
		obj.putArray("array", arr);
	}
	
	@Test
	public void test_override_keys()
	{
		DataObject obj = createFullFeature();
		
		obj.putString("boolean", "string");
		obj.putString("blob", "string");
		obj.putString("double", "string");
		obj.putString("int", "string");
		obj.putString("long", "string");
		obj.putString("null", "string");
		obj.putString("array", "string");
		obj.putString("object", "string");
		
		assertEquals("string", obj.get("string"));
		assertEquals("string", obj.get("boolean"));
		assertEquals("string", obj.get("blob"));
		assertEquals("string", obj.get("double"));
		assertEquals("string", obj.get("int"));
		assertEquals("string", obj.get("long"));
		assertEquals("string", obj.get("null"));
		assertEquals("string", obj.get("array"));
		assertEquals("string", obj.get("object"));
	}
	
/*************************** DataArray tests **********************************/
	
	protected DataArray createDeepArray()
	{
		DataArray root = createNewArray();
		DataArray next = root;
		for(int i=0;i<5;++i)
		{
			DataArray now = createNewArray();
			next.putArray(now);
			next = now;
		}
		
		next.putString("string");
		
		return root;
	}
	
	@Test
	public void test_deep_arrays()
	{
		DataArray deep = createDeepArray();
		
		assertNotNull(deep.getArray(0));
		assertNotNull(deep.getArray(0).getArray(0));
		assertNotNull(deep.getArray(0).getArray(0).getArray(0));
		assertNotNull(deep.getArray(0).getArray(0).getArray(0).getArray(0));
		assertNotNull(deep.getArray(0).getArray(0).getArray(0).getArray(0).getArray(0));
		assertEquals("string", deep.getArray(0).getArray(0).getArray(0).getArray(0).getArray(0).getString(0));
	}
	
	public DataArray createSampleArray()
	{
		DataArray arr = createNewArray();
		//0
		arr.putBoolean(true);
		//1
		arr.putInt(Integer.MIN_VALUE);
		//2
		arr.putLong(Long.MAX_VALUE);
		//3
		arr.putDouble(Math.PI);
		//4
		arr.putString("string");
		//5
		arr.putBlob("blob".getBytes());
		
		DataArray a = createNewArray();
		a.putString("array string");
		a.putString("array string");
		//6
		arr.putArray(a);
		
		DataObject obj = createNewObject();
		obj.putString("string", "obj string");
		//7
		arr.putObject(obj);
		
		//8
		arr.putNull();
		
		return arr;
	}
	
	public void assertFullFeaturedBounded(DataArray obj)
	{
		assertTrue(obj.isString(4));
		assertEquals("string", obj.getString(4));
		assertEquals("string", obj.optString(4));
		assertEquals("string", obj.optString(4, "hello"));
		assertNull(obj.optString(24));
		
		assertTrue(obj.isBlob(5));
		assertArrayEquals("blob".getBytes(), obj.getBlob(5));
		assertArrayEquals("blob".getBytes(), obj.optBlob(5));
		assertArrayEquals("blob".getBytes(), obj.optBlob(5, new byte[] {10}));
		
		assertTrue(obj.isBoolean(0));
		assertTrue(obj.getBoolean(0));
		assertTrue(obj.optBoolean(0));
		assertTrue(obj.optBoolean(0, false));
		assertNull(obj.optBoolean(20));
		
		
		assertTrue(obj.isDouble(3));
		assertEquals(Math.PI, obj.getDouble(3), 0.00000001);
		assertEquals(Math.PI, obj.optDouble(3), 0.00000001);
		assertEquals(Math.PI, obj.optDouble(3, -1.0), 0.00000001);
		assertNull(obj.optDouble(32));
		
		assertTrue(obj.isInt(1));
		assertEquals(Integer.MIN_VALUE, obj.getInt(1));
		assertEquals((Integer) Integer.MIN_VALUE, obj.optInt(1));
		assertEquals(Integer.MIN_VALUE, obj.optInt(1, 10));
		assertNull(obj.optInt(21));
		
		assertTrue(obj.isLong(2));
		assertEquals(Long.MAX_VALUE, obj.getLong(2));
		assertEquals((Long)Long.MAX_VALUE, obj.optLong(2));
		assertEquals(Long.MAX_VALUE, obj.optLong(2, 10));
		assertNull(obj.optLong(22));
		
		assertTrue(obj.isNull(8));
		
		assertTrue(obj.isObject(7));
		assertTrue(obj.getObject(7) instanceof DataObject);
		assertTrue(obj.optObject(7) instanceof DataObject);
		assertTrue(obj.optObject(7, null) instanceof DataObject);
		
		DataObject o = obj.getObject(7);
		assertTrue(o.isString("string"));
		assertTrue(o.has("string"));
		assertEquals("obj string", o.getString("string"));
		assertNull(o.optString("none"));
		
		assertTrue(obj.isArray(6));
		assertTrue(obj.getArray(6) instanceof DataArray);
		assertTrue(obj.optArray(6) instanceof DataArray);
		assertTrue(obj.optArray(6, createNewArray()) instanceof DataArray);
		assertNull(obj.optArray(26));
		
		DataArray arr = obj.getArray(6);
		assertEquals("array string", arr.getString(0));
		assertEquals("array string", arr.getString(1));
		
		assertEquals(9, obj.size());
	}
	
	public void assertFullFeaturedUnbounded(DataArray obj)
	{
		assertEquals("string", obj.get(4));
		
		//it's depends from the implementation
		Object blob = obj.get(5);
		if(blob instanceof String)
		{
			assertEquals("YmxvYg==", blob);
		}
		else
		{
			assertArrayEquals("blob".getBytes(), (byte[]) blob);
		}
		
		assertEquals(Boolean.TRUE, CastTo.Boolean.cast(obj.get(0)));
		assertEquals(Math.PI,  (double) CastTo.Double.cast(obj.get(3)), 0.00000001);
		assertEquals(Integer.MIN_VALUE, CastTo.Int.cast(obj.getInt(1)));
		assertEquals(Long.MAX_VALUE, CastTo.Long.cast(obj.getLong(2)));
		assertTrue(obj.isNull(8));
		assertTrue(obj.get(7) instanceof DataObject);
		assertTrue(obj.opt(7) instanceof DataObject);
		assertTrue(obj.optObject(7) instanceof DataObject);

		DataObject o = (DataObject) obj.get(7);
		assertTrue(o.isString("string"));
		assertTrue(o.has("string"));
		assertEquals("obj string", o.get("string"));
		assertEquals("obj string", o.opt("string"));
		assertEquals("obj string", o.opt("string", "hello"));
		assertNull(o.optString("none"));
		
		
		assertTrue(obj.get(6) instanceof DataArray);
		assertTrue(obj.opt(6) instanceof DataArray);
		
		DataArray arr = (DataArray) obj.get(6);
		assertEquals("array string", arr.get(0));
	}

	
	public void assertAsJavaArray(DataArray obj)
	{
		List<Object> java = obj.asJavaList();
		
		assertEquals("string", java.get(4));
		assertEquals(CollectionTools.inlineArrayList("array string", "array string"), java.get(6));
		Object blob = java.get(5);
		if(blob instanceof byte[])
		{
			assertArrayEquals("blob".getBytes(), (byte[]) blob);
		}
		else
		{
			assertEquals("YmxvYg==", blob);
		}
		
		assertEquals(Boolean.TRUE, CastTo.Boolean.cast(java.get(0)));
		assertEquals(Math.PI, CastTo.Double.cast(java.get(3)));
		assertEquals(Integer.MIN_VALUE, CastTo.Int.cast(java.get(1)));
		assertEquals(Long.MAX_VALUE, CastTo.Long.cast(java.get(2)));
		assertEquals("obj string", ((Map)java.get(7)).get("string"));
		assertNull(java.get(8));
	}
	
	@Test
	public void test_array_types_comes_back()
	{
		DataArray full = createSampleArray();
		assertFullFeaturedBounded(full);
		assertAsJavaArray(full);
	}
	
	public static DataArray virtualTransfer1(DataArray obj) throws IOException
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		obj.sendDataArray(obj, baos);
		ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
		return obj.receiveDataArray(bais);
	}
	
	public static DataArray virtualTransfer2(DataCommon comm, DataArray arr) throws IOException
	{
		byte[] data = arr.toBlob();
		return comm.arrayFromBlob(data);
	}
	
	public static DataArray virtualTransfer3(DataCommon comm, DataArray arr) throws IOException
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataSender sender = comm.newDataSender(baos);
		sender.send(arr);
		baos.flush();
		ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
		DataReceiver rec = comm.newDataReceiver(bais);
		return rec.receiveDataArray(bais);
	}
	
	@Test
	public void test_array_send_receive_basic_bound_1() throws IOException
	{
		DataArray ret = virtualTransfer1(createSampleArray());
		assertFullFeaturedBounded(ret);
		assertAsJavaArray(ret);
	}
	
	@Test
	public void test_array_send_receive_basic_unbound_1() throws IOException
	{
		DataArray ret = virtualTransfer1(createSampleArray());
		assertFullFeaturedUnbounded(ret);
		assertAsJavaArray(ret);
	}
	
	@Test
	public void test_array_send_receive_basic_bound_2() throws IOException
	{
		DataArray ret = virtualTransfer2(prototye, createSampleArray());
		assertFullFeaturedBounded(ret);
		assertAsJavaArray(ret);
	}
	
	@Test
	public void test_array_send_receive_basic_unbound_2() throws IOException
	{
		DataArray ret = virtualTransfer2(prototye, createSampleArray());
		assertFullFeaturedUnbounded(ret);
		assertAsJavaArray(ret);
	}
	
	@Test
	public void test_array_send_receive_basic_bound_3() throws IOException
	{
		DataArray ret = virtualTransfer3(prototye, createSampleArray());
		assertFullFeaturedBounded(ret);
		assertAsJavaArray(ret);
	}
	
	@Test
	public void test_array_send_receive_basic_unbound_3() throws IOException
	{
		DataArray ret = virtualTransfer3(prototye, createSampleArray());
		assertFullFeaturedUnbounded(ret);
		assertAsJavaArray(ret);
	}
	
	@Test
	public void test_array_remove_nulls()
	{
		DataArray arr = createSampleArray();
		assertFullFeaturedBounded(arr);
		
		for(int i=0;i<9;++i)
		{
			arr.unset(0);
		}
		assertEquals(0, arr.size());
	}
	
	@Test
	public void testArrayOptReturnsNull()
	{
		DataArray obj = createSampleArray();
		assertNull(obj.opt(20));
		assertNull(obj.optString(20));
		assertNull(obj.optBlob(20));
		assertNull(obj.optObject(20));
		assertNull(obj.optArray(20));
	}
	
	@Test
	public void test_override_indexes()
	{
		DataArray arr = createSampleArray();
		
		arr.putString(0, "string");
		arr.putString(1, "string");
		arr.putString(2, "string");
		arr.putString(3, "string");
		arr.putString(4, "string");
		arr.putString(5, "string");
		arr.putString(6, "string");
		arr.putString(7, "string");
		
		assertEquals("string", arr.get(0));
		assertEquals("string", arr.get(1));
		assertEquals("string", arr.get(2));
		assertEquals("string", arr.get(3));
		assertEquals("string", arr.get(4));
		assertEquals("string", arr.get(5));
		assertEquals("string", arr.get(6));
		assertEquals("string", arr.get(7));
	}
	
	@Test
	public void test_add_deep_array()
	{
		DataObject root = prototye.newObjectInstance();
		DataArray arr = prototye.newArrayInstance();
		
		DataObject inner = prototye.newObjectInstance();
		inner.putString("name", "test");
		arr.putObject(inner);
		
		root.putArray("array", arr);
		
		assertEquals("test", root.getArray("array").getObject(0).get("name"));
	}
	
	@Test
	public void test_add_deep_array_transferred() throws IOException
	{
		DataObject root = prototye.newObjectInstance();
		
		DataArray arr = root.newArrayInstance();
		
		for(int i=0;i<3;++i)
		{
			DataObject inner = arr.newObjectInstance();
			inner.putString("name", "test");
			arr.putObject(inner);
		}
		
		root.putArray("array.tag", arr);
		
		DataObject rec = virtualTransfer1(root);
		System.out.println(rec);
		
		assertEquals("test", rec.getArray("array.tag").getObject(0).get("name"));
		
		
/*
		DataArray indicated = save.newArrayInstance();
		
		for(Element ind:page.select(".connecting-items .item-name"))
		{
			DataObject obj = save.newObjectInstance();
			obj.putString("name", ind.text());
			indicated.putObject(obj);
		}
		save.putArray("indicated.tag", indicated);
*/
	}

/**************************** DataReprezTools tests ***************************/
	
	@Test
	public void test_tool_put_values()
	{
		DataObject obj = createNewObject();
		assertEquals(String.class, DataReprezTools.put(obj, "string", "string"));
		
		DataArray arr = createNewArray();
		arr.putString("array string");
		arr.putString("array string");
		assertEquals(DataArray.class, DataReprezTools.put(obj, "array", arr));
		
		assertEquals(byte[].class, DataReprezTools.put(obj, "blob", "blob".getBytes()));
		assertEquals(Boolean.class, DataReprezTools.put(obj, "boolean", true));
		assertEquals(Double.class, DataReprezTools.put(obj, "double", Math.PI));
		assertEquals(Integer.class, DataReprezTools.put(obj, "int", Integer.MIN_VALUE));
		assertEquals(Long.class, DataReprezTools.put(obj, "long", Long.MAX_VALUE));
		assertEquals(Void.class, DataReprezTools.put(obj, "null", null));
		
		DataObject so = prototye.newObjectInstance();
		so.putString("string", "obj string");
		assertEquals(DataObject.class, DataReprezTools.put(obj, "object", so));
		
		assertFullFeaturedBounded(obj);
	}
	
	@Test
	public void test_serialization()
	{
		DataSerialisationTestObject sample = DataSerialisationTestObject.createSampleObject();
		DataObject ser = sample.serialize(prototye);
		assertEquals("Person name", ser.getString("name"));
		assertEquals(20, ser.getInt("age"));
		assertEquals(20, ser.getDouble("lat"), 0.001);
		assertEquals(44.543, ser.getDouble("lng"), 0.001);
		
		DataObject addr = ser.getObject("address");
		assertEquals("Hungary", addr.getString("country"));
		assertEquals(3030, addr.getInt("zip"));
		assertEquals("Main street", addr.getString("street"));
		
		DataArray nicks = ser.getArray("nicks");
		assertEquals(2, nicks.size());
		assertEquals("mr. mayday", nicks.getString(0));
		assertEquals("dr. doomsday", nicks.getString(1));
	}
}
