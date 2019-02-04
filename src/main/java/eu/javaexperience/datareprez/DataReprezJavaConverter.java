package eu.javaexperience.datareprez;

public interface DataReprezJavaConverter
{
	public Object wrap(Object in);
	public Object extract(Object in);
	
	public Object extractValue(DataObject object, String key);
	public Object extractValue(DataArray array, int index);
	
	public boolean putValue(DataObject object, String key, Object value);
	public boolean putValue(DataArray arr, int index, Object value);
}
