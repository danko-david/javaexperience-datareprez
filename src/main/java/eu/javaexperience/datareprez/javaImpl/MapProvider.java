package eu.javaexperience.datareprez.javaImpl;

import java.util.Map;

//TODO Refactor: Use SimpleGet<Map<String,Object>> or Supplier<Map<String,Object>> instead
public interface MapProvider
{
	public Map<String,Object> newMapForObject();
}