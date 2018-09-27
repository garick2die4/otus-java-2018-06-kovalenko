package app;

import serialization.JsonProperty;
import serialization.JsonSerializable;

@JsonSerializable
public class TestObjectWithAnotherObject
{
	@JsonProperty
	private final String f;
	
	@JsonProperty
	private final TestObjectWithPrimitiveTypes obj;
	
	public TestObjectWithAnotherObject(String f, TestObjectWithPrimitiveTypes obj)
	{
		this.f = f;
		this.obj = obj;
	}
}
