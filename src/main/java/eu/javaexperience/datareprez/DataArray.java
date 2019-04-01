package eu.javaexperience.datareprez;

import eu.javaexperience.datareprez.convertFrom.ArrayLike;
 
public interface DataArray extends DataCommon, Iterable<Object>, ArrayLike
{
	public void putString(String val);
	public void putLong(long val);
	public void putDouble(double val);
	public void putInt(int val);
	public void putBoolean(boolean val);
	public void putObject(DataObject val);
	public void putArray(DataArray val);
	public void putBlob(byte[] blob);
	public void putNull();
	
	public void putString(int i,String val);
	public void putLong(int i,long val);
	public void putDouble(int i,double val);
	public void putInt(int i,int val);
	public void putBoolean(int i,boolean val);
	public void putObject(int i,DataObject val);
	public void putArray(int i,DataArray val);
	public void putBlob(int i,byte[] blob);
	public void putNull(int i);
	
	public String getString(int i);
	public long getLong(int i);
	public double getDouble(int i);
	public int getInt(int i);
	public boolean getBoolean(int i);
	public DataObject getObject(int i);
	public DataArray getArray(int i);
	public byte[] getBlob(int i);
	
	public String optString(int i);
	public long optLong(int i);
	public double optDouble(int i);
	public int optInt(int i);
	public boolean optBoolean(int i);
	public DataObject optObject(int i);
	public DataArray optArray(int i);
	public byte[] optBlob(int i);
	public Object opt(int i);
	
	public String optString(int i,String def);
	public long optLong(int i,long def);
	public double optDouble(int i,double val);
	public int optInt(int i,int def);
	public boolean optBoolean(int i,boolean def);
	public DataObject optObject(int i,DataObject def);
	public DataArray optArray(int i,DataArray def);
	public byte[] optBlob(int i,byte[] blob);
	public Object opt(int i,Object def);
	
	public void unset(int i);
	
	public boolean isString(int i);
	public boolean isLong(int i);
	public boolean isDouble(int i);
	public boolean isInt(int i);
	public boolean isBoolean(int i);
	public boolean isObject(int i);
	public boolean isArray(int i);
	public boolean isNull(int i);
	public boolean isBlob(int i);
	
	public Object[] asJavaArray();
}