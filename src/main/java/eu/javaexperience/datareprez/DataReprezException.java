package eu.javaexperience.datareprez;

public class DataReprezException extends RuntimeException
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public DataReprezException(String text)
	{
		super(text);
	}
	
	public DataReprezException(Throwable t)
	{
		super(t);
	}
	
	public DataReprezException(String text, Throwable t)
	{
		super(text, t);
	}
}
