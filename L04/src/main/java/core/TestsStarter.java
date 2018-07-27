package core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import annot.After;
import annot.Before;
import annot.Test;

public class TestsStarter
{
	public TestResults runTestsForClass(String className)
	{
		Object testObj = null;
		Method beforeMethod = null;
		Method afterMethod = null;
		List<Method> testMethods = new ArrayList<>();
		
		TestResults results = new TestResults();
		
		ClassLoader loader = TestsStarter.class.getClassLoader();
		try
		{
			Class<?> testClass = loader.loadClass(className);
			
			testObj = testClass.newInstance();
			
			for (Method method : testClass.getMethods())
			{
				if (method.getAnnotation(Before.class) != null && beforeMethod == null)
				{
					beforeMethod = method;
				}
				else if (method.getAnnotation(After.class) != null && afterMethod == null)
				{
					afterMethod = method;
					
				}
				else if (method.getAnnotation(Test.class) != null)
				{
					testMethods.add(method);
				}
			}
		}
		catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException e)
		{
			results.add(className, TestResultType.ERROR, e.getMessage());
		}
		
		if (!results.isEmpty())
			return results;
		
		if (beforeMethod != null)
		{
			try
			{
				beforeMethod.invoke(testObj);
			}
			catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
			{
				results.add(beforeMethod.getName(), TestResultType.ERROR, e.getMessage());
			}
		}

		if (!results.isEmpty())
			return results;
		
		for (Method testMethod : testMethods)
		{
			try
			{
				testMethod.invoke(testObj);
				
				results.add(testMethod.getName(), TestResultType.OK);
			}
			catch (IllegalAccessException | IllegalArgumentException e)
			{
				results.add(testMethod.getName(), TestResultType.ERROR, e.getMessage());
			}
			catch (InvocationTargetException e)
			{
				if (e.getTargetException() instanceof AssertionError)
				{
					AssertionError err = (AssertionError)e.getTargetException();
					results.add(testMethod.getName(), TestResultType.FAILED, err.getMessage());
				}
				else
				{
					results.add(testMethod.getName(), TestResultType.ERROR, e.getMessage());
				}
			}

		}
		
		if (afterMethod != null)
		{
			try
			{
				afterMethod.invoke(testObj);
			}
			catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
			{
				results.add(afterMethod.getName(), TestResultType.ERROR, e.getMessage());
			}
		}

		return results;
	}
}
