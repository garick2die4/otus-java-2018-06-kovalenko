package app;

import java.lang.System;

import mcache.IMCache;
import mcache.MCache;

class Main
{
    public static void main(String... args) throws InterruptedException
    {
    	{
        	System.out.println("Eternal cache example");
            IMCache<Integer, BigObject> cache = new MCache<>(200, 0, 0);
    		cacheTest(cache, 400);
            cache.dispose();
            System.out.println();
    	}

    	{
        	System.out.println("Eternal cache example");
            IMCache<Integer, BigObject> cache = new MCache<>(250, 0, 0);
    		cacheTest(cache, 250);
            cache.dispose();
            System.out.println();
    	}

    	{
    		System.out.println("Life cache example");
    		MCache<Integer, BigObject> cache = new MCache<>(150, 1000, 0);
    		cacheTest(cache, 150);
            cache.dispose();
            System.out.println();
    	}
    	
    	{
    		System.out.println("Access cache example");
    		MCache<Integer, BigObject> cache = new MCache<>(150, 0, 1000);
    		cacheTest(cache, 150);
    		cache.dispose();
    		System.out.println();
    	}
    }
    
    private static void cacheTest(
    	IMCache<Integer, BigObject> cache,
    	int readCount)  throws InterruptedException
    {
        putToCache(cache, readCount);

    	System.out.println("Before gc:");
        readCache(cache, readCount);

        Thread.sleep(1000);

        System.out.println("After waiting:");
        readCache(cache, readCount);
        
        System.gc();

        System.out.println("After gc:");
        readCache(cache, readCount);
    }

    private static void putToCache(IMCache<Integer, BigObject> cache, int objectsCount)
    {
        for (int i = 0; i < objectsCount; i++)
        {
            cache.put(i, new BigObject(i));
        }
    }
    
    private static void readCache(IMCache<Integer, BigObject> cache, int readSize)
    {
    	int sum = 0;
        for (int i = 0; i < readSize; i++)
        {
        	BigObject obj = cache.get(i);
        	if (obj != null)
        		sum++;
        }
        System.out.println("Cache hits: " + cache.getHitCount());
        System.out.println("Cache misses: " + cache.getMissCount());
        System.out.println("Soft references: " + sum);
    }
    
    static class BigObject
    {
    	final int id;
        final byte[] array = new byte[1024 * 1024];

        public BigObject(int id)
        {
        	this.id = id;
        }
        
        @Override
		public String toString()
        {
        	return "" + id;
        }
    }

}