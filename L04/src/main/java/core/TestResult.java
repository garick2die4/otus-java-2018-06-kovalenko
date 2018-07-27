package core;

public final class TestResult
{
	private final String name;
	private final TestResultType type;
	private final String message;
	
	public TestResult(String name, TestResultType type,  String message)
	{
		this.name = name;
		this.type = type;
		this.message = message;
	}
	
	public String name()
	{
		return name;
	}
	
	public TestResultType type()
	{
		return type;
	}
	
	public String message()
	{
		return message;
	}
}
