package app;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import serialization.JsonProperty;
import serialization.JsonSerializable;

@JsonSerializable
public final class TestObjectWithList<T>
{
	@JsonProperty
	private List<T> array;

	public TestObjectWithList(T... array)
	{
		this.array = new ArrayList<>(Arrays.asList(array));
	}
	
}
