package eu.javaexperience.datareprez;

import eu.javaexperience.datareprez.convertFrom.ArrayLike;
import eu.javaexperience.interfaces.ObjectWithProperty;

public abstract class DataCommonAbstractImpl implements DataCommon
{
	@Override
	public DataObject fromObjectLike(ObjectWithProperty map)
	{
		DataObject ret = newObjectInstance();
		for(String s:map.keys())
		{
			DataReprezTools.put(ret, s, map.get(s));
		}
		return ret;
	}

	@Override
	public DataArray fromArrayLike(ArrayLike arr)
	{
		DataArray ret = newArrayInstance();
		for(int i=0;i<arr.size();++i)
		{
			DataReprezTools.put(ret, i, arr.get(i));
		}
		return ret;
	}
}
