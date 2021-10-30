package eu.javaexperience.datareprez;

import java.io.Serializable;

import eu.javaexperience.datareprez.convertFrom.DataReprezType;

public interface DataLike extends Serializable
{
	public DataReprezType getDataReprezType();
}