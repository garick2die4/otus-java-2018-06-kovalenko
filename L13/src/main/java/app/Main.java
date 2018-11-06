package app;

import java.lang.System;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Main
{
    public static void main(String... args) throws Exception
    {
    	Comparator<Integer> comp = (x,y) -> Integer.compare(x, y);
    	
    	List<Integer> arr1 = Arrays.asList(4, 5, 1, 54, 11, 43, 87, 0, 13, 11, 44, 99, 2);
    	List<Integer> result = new Sorter<Integer>().sort(arr1, comp);
    	System.out.println(result);
    	
    	List<Integer> arr2 = generateArr(10_000_000);
    	
    	{
    		long ts = System.currentTimeMillis();
    		new ArrayList<>(arr2).sort(comp);
    		long total = System.currentTimeMillis() - ts;
    		System.out.println("Single thread: " + total);
    	}
    	
    	{
    		long ts = System.currentTimeMillis();
    		new Sorter<Integer>().sort(arr2, comp);
    		long total = System.currentTimeMillis() - ts;
    		System.out.println("4 threads: " + total);
    	}
    }
    
    private static List<Integer> generateArr(int size)
    {
    	List<Integer> arr = new ArrayList<>(size);
    	for (int i = 0; i < size; i++)
    	{
    		arr.add(new Integer((int)(Math.random() * size)));
    	}
    	return arr;
    }
}