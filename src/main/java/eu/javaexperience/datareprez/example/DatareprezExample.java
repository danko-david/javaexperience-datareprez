package eu.javaexperience.datareprez.example;

import java.util.Map;
import java.util.Map.Entry;

import eu.javaexperience.collection.map.SmallMap;
import eu.javaexperience.datareprez.DataCommon;
import eu.javaexperience.datareprez.javaImpl.DataObjectJavaImpl;
import eu.javaexperience.datareprez.jsonImpl.DataObjectJsonImpl;
import eu.javaexperience.datareprez.xmlImpl.DataObjectXmlImpl;

public class DatareprezExample
{
	public static void demonstratePojoSeralization()
	{
		Map<String, DataCommon> wellKnowns = new SmallMap<>();
		wellKnowns.put("Java", DataObjectJavaImpl.PROTOTYPE);
		wellKnowns.put("JSON", DataObjectJsonImpl.PROTOTYPE);
		wellKnowns.put("XML", DataObjectXmlImpl.PROTOTYPE);
		
		DataSerialisationTestObject sample = DataSerialisationTestObject.createSampleObject();
		
		for(Entry<String, DataCommon> proto: wellKnowns.entrySet())
		{
			System.out.println("Sample object in "+proto.getKey()+": "+sample.serialize(proto.getValue()));
		}
	}
	
	public static void demonstrateBuildObject()
	{
		
		
	}
	
	public static void main(String[] args)
	{
		System.out.println("demonstratePojoSeralization()");
		demonstratePojoSeralization();
		
		
		
	}
}
