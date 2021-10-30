# Javaexperience's DataReprez

Abstract object/array based data representation layer.

## Goals

The main goal was to switch between storage and network transmit format without
rewrite the management code.  
Later this made it easier to write a generalize parser for different file formats
and made it easier to integrate different formats into a single data processing system.

## Maven package

Maven packages available from a custom repository you can add to your pom:

```xml
<project ...>
	...
	<repositories>
		<repository>
			<id>jvx-repo</id>
			<name>Javaexprience-custom-repo</name>
			<url>https://maven.javaexperience.eu/</url>
		</repository>
	</repositories>
	
	...
	
	<dependencies>
		...
		<dependency>
		<groupId>javaexperience</groupId>
			<artifactId>datareprez</artifactId>
			<version>1.2</version>
		</dependency>
	</dependencies>
```

## Features

### Functions
This abstraction layer's interface prescribe/provides functions to:
- create empty object and array instances **DataCommon.new{Object, Array}Instance**
- get the contained values
	- by type guessing **Data{Object, Array}.get**
	- by exact getters **Data{Object, Array}.get\*** (throws DataReprezException if no value present)
	- by optional getting **Data{Object, Array}.opt\***
	- by optional default getting **Data{Object, Array}.opt\*({String,int}, \*)**
- set values **Data{Object, Array}.put\*()**
- type checks **Data{Object, Array}.is\***
- remove contained values **DataArray.remove** and **DataArray.unset**
- get keys/size
- export to Java primitive: **DataObject.asJavaMap** and **DataArray.asJavaList**
- get backend implementation **DataCommon.getImpl**
- store and transfer objects and arrays:
	- generic:
		- serialize object/array **DataCommon.toBlob()**
		- deserialize object/array **DataCommon.{object, array}FromBlob**
	- send/receive, store/read objects and arrays:
		- read/write single object {Input, Output}Stream **DataCommon.{send, receive}Data{Object, Array}**
		- create reader/writer for continus streams: **DataCommon.newData{Reader, Sender}**

### Supported property types
- null
- boolean
- int
- long
- double
- blob (byte[] type)
- String
- DataObject
- DataArray



## Examples

### Serialize POJO

```java

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

	public static void main(String[] args)
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
```

Outputs:

```
Sample object in Java: DataObjectJavaImpl: {
	"name":"Person name"
	"age":"20"
	"address":"{
	"country":"Hungary"
	"zip":"3030"
	"street":"Main street"
}"
	"lat":"20.0"
	"lng":"44.543"
	"nicks":"[mr. mayday, dr. doomsday]"
}
Sample object in JSON: DataObjectJsonImpl: {"address":{"zip":3030,"country":"Hungary","street":"Main street"},"lng":44.543,"name":"Person name","age":20,"lat":20,"nicks":["mr. mayday","dr. doomsday"]}
Sample object in XML: DataObjectXmlImpl: <DataReprezRoot><name>Person name</name><age>20</age><address><country>Hungary</country><zip>3030</zip><street>Main street</street></address><lat>20.0</lat><lng>44.543</lng><nicks>mr. mayday</nicks><nicks>dr. doomsday</nicks></DataReprezRoot>
```

### Build Objects

TODO

### Parse existing structures

TODO

## Where is this project used?

It is used in the javaexperience-rpc package and in its dependencies:

#### [Javaexperience RPC](https://github.com/danko-david/javaexperience-rpc)
The RPC layer is implemented to operate over DataObjects and DataArrays.
By switching the main prototype of the RPC server, it can be switch eg.: from JSON
to XML. If the transfer size is critical, this can be replaced by BSON or MsgPack
to shrink the package sizes.

#### [Javaexperience Teasite](https://github.com/danko-david/javaexperience-teasite)
[DataObjectTeaVMImpl](https://github.com/danko-david/javaexperience-teasite/blob/master/src/main/java/eu/javaexperience/teavm/datareprez/DataObjectTeaVMImpl.java) implementation to handle browser's native Object (JSObject) and Array (JSArray).
It has two purposes:
- Using this with the existing serialization facilities it was easy to integrate the RPC server for ajax and WebSocket platforms.
- Build native javascript objects and arrays without creating TeaVM wrapper classes for all specific natives.

## Implementation details

### Working principle

The implementation of wrapper classes are just overlays. Behind the scenes the
functions operate over the backend Map/List, JSONObject/JSONArray, and XML Node.
So when the `getImpl` is called, the raw backed object is returned without
any DataObject and DataArray instance inside its structure.
When the getObject is called, before returning, the backend types
(Map, JSONObject, Node) is wrapped into an instance of DataObject class.
So you access and edit the backed types through these overlays.

### Abstract implementations

After implementing more of these overlays, i found it really repetitive.
So i create the `DataObjectAbstractImpl` and `DataArrayAbstractImpl` to bound
the repetitive functions together and relay the calls to some simple functions
which is faster to implement.

### How to implement your type

In this release, all the three basic implementations is rewritten to use the
intermediate implementation abstract classes. So by viewing the classes
Data{Object, Array}{Java, Json, Xml}Impl` you can get a good pricture how this works.
However other functionalities, like null management and serialization/deserialization
is implemented by the DataProtocol classes, which is also covered in the abstract implementation

### XML implementation

XML was a bit tricky. It doesn't have array types, so i handle arrays as multiple values in the parent object eg.:

```xml
	<object>
		<array>entry1</array>
		<array>entry2</array>
	</object>
```

```java
	//array is accessed this way:
	DataArray arr = root.getObject("object").getArray("array");
```




