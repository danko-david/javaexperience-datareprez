package eu.javaexperience.datareprez;

import java.util.Map;

import eu.javaexperience.datareprez.convertFrom.ObjectLike;

/**
 * A JSON, BSON és egyéb olyan adatábrázoló objektumok fölé épített interface, ami segítségével könnyen implementációt válthatunk  
 * 
 * */
public interface DataObject extends DataCommon, ObjectLike
{
	public void putString(String key,String val);
	public void putLong(String key,long val);
	public void putDouble(String key,double val);
	public void putInt(String key,int val);
	public void putBoolean(String key,boolean val);
	public void putObject(String key,DataObject val);
	public void putArray(String key,DataArray val);
	public void putBlob(String key,byte[] blob);
	public void putNull(String key);
	
	public String getString(String key);
	public long getLong(String key);
	public double getDouble(String key);
	public int getInt(String key);
	public boolean getBoolean(String key);
	public DataObject getObject(String key);
	public DataArray getArray(String key);
	public byte[] getBlob(String key);
	
	@Override
	public Object get(String key);
	
	public String optString(String key);
	public long optLong(String key);
	public double optDouble(String key);
	public int optInt(String key);
	public boolean optBoolean(String key);
	public DataObject optObject(String key);
	public DataArray optArray(String key);
	public byte[] optBlob(String key);
	public Object opt(String key);
	
	public String optString(String key,String def);
	public long optLong(String key,long def);
	public double optDouble(String key,double val);
	public int optInt(String key,int def);
	public boolean optBoolean(String key,boolean def);
	public DataObject optObject(String key,DataObject def);
	public DataArray optArray(String key,DataArray def);
	public byte[] optBlob(String key,byte[] blob);
	public Object opt(String key,Object o);
	
	public void remove(String key);
	
	public boolean isString(String key);
	public boolean isLong(String key);
	public boolean isDouble(String key);
	public boolean isInt(String key);
	public boolean isBoolean(String key);
	public boolean isObject(String key);
	public boolean isArray(String key);
	public boolean isBlob(String key);
	public boolean isNull(String key);
	
	public Map<String, Object> asJavaMap();
	public byte[] toBlob();
}