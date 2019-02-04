package eu.javaexperience.datareprez;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;

import eu.javaexperience.collection.map.BulkTransitMap;
import eu.javaexperience.reflect.Mirror;

public abstract class DataObjectTest
{
	protected final DataCommon prototye;
	
	public DataObjectTest(DataCommon prototype)
	{
		this.prototye = prototype;
	}
	
	//TODO set null and check (has and null)
	
	public static Map<String, Object> createFullFeaturedNative()
	{
		Map<String, Object> ret = new BulkTransitMap<>();
		ret.put("string", "string");
		ret.put("array", Mirror.emptyObjectArray);
		ret.put( "blob", "blob".getBytes());
		ret.put("boolean", true);
		ret.put("double", Math.PI);
		ret.put("int", Integer.MIN_VALUE);
		ret.put("long", Long.MAX_VALUE);
		ret.put("null", null);
		ret.put("object", new BulkTransitMap<>());
		
		return ret;
	}
	
	public DataObject createFullFeature()
	{
		DataObject obj = prototye.newObjectInstance();
		obj.putString("string", "string");
		obj.putArray("array", prototye.newArrayInstance());
		obj.putBlob( "blob", "blob".getBytes());
		obj.putBoolean("boolean", true);
		obj.putDouble("double", Math.PI);
		obj.putInt("int", Integer.MIN_VALUE);
		obj.putLong("long", Long.MAX_VALUE);
		obj.putNull("null");
		obj.putObject("object", prototye.newObjectInstance());
		
		return obj;
	}
	
	public void assertFullFeaturedBounded(DataObject obj)
	{
		assertEquals("string", obj.getString("string"));
		assertTrue(obj.getArray("array") instanceof DataArray);
		assertArrayEquals("blob".getBytes(), obj.getBlob("blob"));
		assertTrue(obj.getBoolean("boolean"));
		assertEquals(Math.PI, obj.getDouble("double"), 0.00000001);
		assertEquals(Integer.MIN_VALUE, obj.getInt("int"));
		assertEquals(Long.MAX_VALUE, obj.getLong("long"));
		assertTrue(obj.isNull("null"));
		assertTrue(obj.getObject("object") instanceof DataObject);
	}
	
	public void assertFullFeaturedUnbounded(DataObject obj)
	{
		assertEquals("string", obj.get("string"));
		assertTrue(obj.get("array") instanceof DataArray);
		assertArrayEquals("blob".getBytes(), (byte[])obj.get("blob"));
		//assertTrue(obj.get("boolean"));
		//assertEquals(Math.PI, obj.get("double"), 0.00000001);
		//assertEquals(Integer.MIN_VALUE, obj.getInt("int"));
		//assertEquals(Long.MAX_VALUE, obj.getLong("long"));
		assertTrue(obj.isNull("null"));
		assertTrue(obj.get("object") instanceof DataObject);
	}
	
	
	@Test
	public void test_types_comes_back()
	{
		DataObject full = createFullFeature();
		System.out.println(new String(full.toBlob()));
		assertFullFeaturedBounded(full);
	}
	
	/*@Test
	public void test_types_comes_unbound_back()
	{
		DataObject full = createFullFeature();
		System.out.println(new String(full.toBlob()));
		assertFullFeaturedUnbounded(full);
	}*/
}
