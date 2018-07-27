package tests;

import annot.After;
import annot.Before;
import annot.Test;

public class SomeTest
{
	@Before
	public void setUp()
	{
		System.out.println("SomeTest setUp\n");
	}
	
	@After
	public void shutDown()
	{
		System.out.println("SomeTest shutDown\n");
	}
	
	@Test
	public void test_func1()
	{
		System.out.println("SomeTest test_func1\n");
	}
	
	@Test
	public void test_func2()
	{
		System.out.println("SomeTest test_func1\n");
	}

}
