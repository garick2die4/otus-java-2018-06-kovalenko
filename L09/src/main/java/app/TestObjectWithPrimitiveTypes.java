package app;

import serialization.JsonProperty;
import serialization.JsonSerializable;

@JsonSerializable
public class TestObjectWithPrimitiveTypes
{
	@JsonProperty
	private final String stringField;

	@JsonProperty
	private final int intField;

	@JsonProperty
	private final boolean booleanField;

	@JsonProperty
	private final double doubleField;

	public TestObjectWithPrimitiveTypes(String stringField, int intField, boolean booleanField, double doubleField)
	{
		this.stringField = stringField;
		this.intField = intField;
		this.booleanField = booleanField;
		this.doubleField = doubleField;
	}
}
