package app;

import java.lang.System;

import core.TestResult;
import core.TestResultType;
import core.TestResults;
import core.TestsStarter;
import tests.SomeTest;
import tests.SomeTest2;

public class Main
{
    public static void main(String... args) throws InterruptedException
    {
    	runTests(SomeTest.class);
    	runTests(SomeTest2.class);
    }
    
    private static void runTests(Class<?> testClass) 
    {
    	System.out.println("Run test " + testClass.getName());
    	
    	printResults(testClass.getName(), TestsStarter.run(testClass));
    	
    	System.out.println("-------------------------");
    }
    
    private static void printResults(String testName, TestResults results)
    {
    	System.out.println("Results:");
    	for (TestResult res : results.results())
    	{
    		if (res.type().equals(TestResultType.OK))
    		{
    			System.out.format("test '%s', result: %s\n", res.name(), getResultTypeAsString(res.type()));
    		}
    		else
    		{
    			
    			System.out.format("test '%s', result: %s, message: %s\n", res.name(), getResultTypeAsString(res.type()), res.message());
    		}
    	}
    }

    private static String getResultTypeAsString(TestResultType type)
    {
    	switch (type)
    	{
		case OK:
			return "OK";
		case FAILED:
			return "FAILED";
		case ERROR:
			return "ERROR";
		default:
			return "UnknownResultType";
		}
    }
}