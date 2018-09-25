package app;

import serialization.JsonProperty;
import serialization.JsonSerializable;

@JsonSerializable
public class TestSerializable2
{
	@JsonProperty
	private final String ss1;
	@JsonProperty
	private final String ss2;
	
	@JsonProperty
	private final TestSerialize obj;
	
	public TestSerializable2(String s1, String s2, TestSerialize obj)
	{
		this.ss1 = s1;
		this.ss2 = s2;
		this.obj = obj;
	}
}
