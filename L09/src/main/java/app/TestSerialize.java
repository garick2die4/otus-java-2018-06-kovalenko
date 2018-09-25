package app;

import java.util.ArrayList;
import java.util.List;

import serialization.JsonProperty;
import serialization.JsonSerializable;

@JsonSerializable
public class TestSerialize
{
	@JsonProperty
	private final String s1;
	@JsonProperty
	private final String s2;
	@JsonProperty
	private List<String> array = new ArrayList<>();
	
	public TestSerialize(String s1, String s2)
	{
		this.s1 = s1;
		this.s2 = s2;
		array.add(s1);
		array.add(s2);
	}
}
