package tests;

import annot.Test;

public class SomeTest2
{
	@Test
	public void test_func1()
	{
		System.out.println("SomeTest2 test_func1\n");
	}
	
	@Test
	public void test_func2()
	{
		System.out.println("SomeTest2 test_func2\n");
		
		throw new AssertionError("someError");
	}
}
