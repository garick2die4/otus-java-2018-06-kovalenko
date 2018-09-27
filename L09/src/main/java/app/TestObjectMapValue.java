package app;

import serialization.JsonProperty;
import serialization.JsonSerializable;

@JsonSerializable
public final class TestObjectMapValue
{
	@JsonProperty
	private final String f1;
	@JsonProperty
	private final Integer f2;
	
	public TestObjectMapValue(String f1, Integer f2)
	{
		this.f1 = f1;
		this.f2 = f2;
	}
}
