package eu.javaexperience.datareprez;

public interface DataAccessor
{
	public boolean canHandle(Object subject);
	public Object get(Object subject, String key);
	//TODO public boolean remove(Object subject, String key);
	//TODO public boolean set(Object subject, String key, Object obj);
	public String[] keys(Object subject);
}