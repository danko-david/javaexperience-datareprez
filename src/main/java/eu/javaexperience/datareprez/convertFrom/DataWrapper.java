package eu.javaexperience.datareprez.convertFrom;

import eu.javaexperience.datareprez.DataCommon;

public interface DataWrapper
{
	public DataCommon wrap
	(
		DataWrapper topWrapper,
		DataCommon prototype,
		Object o
	);
}
