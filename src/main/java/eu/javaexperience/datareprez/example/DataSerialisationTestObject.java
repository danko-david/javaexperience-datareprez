package eu.javaexperience.datareprez.example;

import java.util.ArrayList;
import java.util.List;

import eu.javaexperience.datareprez.DataCommon;
import eu.javaexperience.datareprez.DataObject;
import eu.javaexperience.datareprez.DataReprezTools;

public class DataSerialisationTestObject
{
	public String name;
	public int age;
	public Address address;
	public double lat;
	public double lng;
	public List<String> nicks = new ArrayList<>();
	
	public static class Address
	{
		public String country;
		public int zip;
		public String street;
	}
	
	public static DataSerialisationTestObject createSampleObject()
	{
		DataSerialisationTestObject ret = new DataSerialisationTestObject();
		ret.name = "Person name";
		ret.age = 20;
		ret.address = new Address();
		ret.address.country = "Hungary";
		ret.address.zip = 3030;
		ret.address.street = "Main street";
		
		ret.lat = 20;
		ret.lng = 44.543;
		ret.nicks.add("mr. mayday");
		ret.nicks.add("dr. doomsday");
		return ret;
	}
	
	public DataObject serialize(DataCommon prototype)
	{
		return (DataObject) DataReprezTools.wrapRecursively(DataReprezTools.DATA_WRAPPER_BUILT_IN, prototype, this);
	}
}
