package core;

import java.util.ArrayList;
import java.util.List;

public class TestResults
{
	private List<TestResult> results  = new ArrayList<>();
	
	public TestResults()
	{
	}
	
	public void add(String name, TestResultType type, String message)
	{
		results.add(new TestResult(name, type, message));		
	}
	
	public void add(String name, TestResultType type)
	{
		results.add(new TestResult(name, type, ""));		
	}
	
	public boolean isEmpty()
	{
		return results.isEmpty();
	}
	
	public List<TestResult> results()
	{
		return new ArrayList<>(results);
	}
	
}
