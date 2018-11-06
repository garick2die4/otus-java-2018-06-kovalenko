package app;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Sorter<T>
{
	private static int THREADS_COUNT = 4;
	private static int THREADING_THRESHOLD = 10;
	
	public List<T> sort(List<T> arr, Comparator<? super T> comp) throws InterruptedException
	{
		if (arr.size() < THREADING_THRESHOLD)
		{
			List<T> res = new ArrayList<>(arr);
			res.sort(comp);
			return res;
		}
		
		List<List<T>> arrs = new ArrayList<>(THREADS_COUNT);
		int chunksz = arr.size() / THREADS_COUNT;
		int n = 0;
		int start = 0;
	    while(n != THREADS_COUNT)
	    {
	        n++;
	        int end = (n != THREADS_COUNT) ? start + chunksz : arr.size();
	        arrs.add(new ArrayList<>(arr.subList(start, end)));
	        start += chunksz;
	    }
	    
	    List<Thread> threads = new ArrayList<>();
	    for (int index = 0; index < arrs.size(); index++)
	    {
	    	Thread thr = new Thread(new SortRunnable(arrs.get(index), comp));
	    	thr.start();
	    	threads.add(thr);
	    }
	    
	    for (int i = 0; i < threads.size(); i++)
			threads.get(i).join();
	    
	    while(arrs.size() != 1)
	    {
	    	arrs = reduce(arrs, comp);
	    }
	    return arrs.get(0);
	}
	
	private List<List<T>> reduce(List<List<T>> arrs, Comparator<? super T> comp)
	{
		List<List<T>> result = new ArrayList<>(arrs.size() / 2);
		for (int i = 0; i < arrs.size(); i += 2)
			result.add(merge_arr(arrs.get(i), arrs.get(i + 1), comp));
		return result;
	}
	
	private List<T> merge_arr(List<T> leftArr, List<T> rightArr, Comparator<? super T> comp)
	{
		List<T> res = new ArrayList<>(leftArr.size() + rightArr.size());
		int leftIndex = 0;
		int rightIndex = 0;
		while(leftIndex < leftArr.size() || rightIndex < rightArr.size())
		{
			if (comp.compare(leftArr.get(leftIndex), rightArr.get(rightIndex)) < 0)
				res.add(leftArr.get(leftIndex++));
			else
				res.add(rightArr.get(rightIndex++));
			
			if (leftIndex == leftArr.size())
			{
				for (int i = rightIndex; i < rightArr.size(); i++)
					res.add(rightArr.get(i));
				break;
			}
			if (rightIndex == rightArr.size())
			{
				for (int i = leftIndex; i < leftArr.size(); i++)
					res.add(leftArr.get(i));
				break;
			}
		}
		return res;
	}
	
	private class SortRunnable implements Runnable
	{
		private List<T> arr;
		private Comparator<? super T> comp;
		
		SortRunnable(List<T> arr, Comparator<? super T> comp)
		{
			this.arr = arr;
			this.comp = comp;
		}
		
		@Override
		public void run()
		{
			arr.sort(comp);
		}
		
	}
}
