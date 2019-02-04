package eu.javaexperience.datareprez.convertFrom;

public enum DataReprezType
{
	/**
	 * An element what's not java `null` but a placeholder for null values
	 * */
	NULL,
	
	/**
	 * Used for primitive DataReprez interface. (eg.: {@link ModifiableObject}, {@link DataPrimitiveSourceObject})
	 * */
	PRIMITIVE,
	
	/**
	 * A free bound, object like element. In java aspect is more similar to
	 * Map<String, Object> rather than the java.lnag.Object 
	 * */
	OBJECT,
	
	/**
	 * An array like element. in java aspect it like Object[] or
	 * Collection<Object>
	 * */
	ARRAY,
	
	/**
	 * For class base OOP languages (in comparison of prototype base), the
	 * only improvement compared to OBJECT is this type has a special
	 * predefined, bounded field, depending from  the runtime concrete class
	 * of the object. 
	 * */
	CLASS_OBJECT,
	
	/**
	 * An object, has a local resource bounded to, witch can not be serailized
	 * and moved to another process space.
	 * */
	RESOURCE
}
