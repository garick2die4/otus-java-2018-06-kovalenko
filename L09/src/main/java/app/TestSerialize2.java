package app;

import serialization.JsonProperty;
import serialization.JsonSerializable;

@JsonSerializable
public class TestSerialize2
{
	@JsonProperty
	private final String ss1;
	@JsonProperty
	private final int f2;
	
	@JsonProperty
	private final TestSerialize obj;
	
	public TestSerialize2(String s1, int f2, TestSerialize obj)
	{
		this.ss1 = s1;
		this.f2 = f2;
		this.obj = obj;
	}
}
