package eu.javaexperience.datareprez.convertFrom;

public interface ClassObjectLike extends ObjectLike
{
	public ClassObjectLike createNew(String classIdentifier);
	public String getClassIdentifier();
}
