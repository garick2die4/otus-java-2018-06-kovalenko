package app;

import serialization.JsonProperty;
import serialization.JsonSerializable;

@JsonSerializable
public class TestSerialize
{
	@JsonProperty
	private final String s1;
	@JsonProperty
	private final String s2;
	
	public TestSerialize(String s1, String s2)
	{
		this.s1 = s1;
		this.s2 = s2;
	}
}
