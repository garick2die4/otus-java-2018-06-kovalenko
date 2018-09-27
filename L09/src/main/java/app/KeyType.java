package app;

import serialization.JsonProperty;
import serialization.JsonSerializable;

@JsonSerializable
public final class KeyType
{
	@JsonProperty
	private final String key;
	
	public KeyType(String k)
	{
		key = k;
	}
	
	@Override
	public boolean equals(Object o)
	{
		return key.equals(o);
	}
	
	@Override
	public int hashCode()
	{
		return key.hashCode();
	}
}
