package app;

import java.util.HashMap;
import java.util.Map;

import serialization.JsonProperty;
import serialization.JsonSerializable;

@JsonSerializable
public class TestObjectWithMap<K, V>
{
	@JsonProperty
	private Map<K, V> map = new HashMap<>();

	public TestObjectWithMap()
	{
	}
	
	public void add(K key, V value)
	{
		map.put(key, value);
	}
}
