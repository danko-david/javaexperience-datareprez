package eu.javaexperience.datareprez.convertFrom;

public interface ModifiableObject extends DataLike
{
	public boolean set(String key, Object value);
	public void unset(String key);
}
