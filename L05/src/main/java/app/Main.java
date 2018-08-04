package app;

import java.lang.System;
import java.lang.management.ManagementFactory;


// Статистика:
// числа, средние за минуту
//
//			время работы(сек)	запуски 
// -XX:+UseSerialGC 
//    
// Young   ~35			 		~195
// Old     ~18 			 		~70
//
// -XX:+UseParallelGC -XX:+UseParallelOldGC
//
// Young 	~25 		 		~90
// Old 		~35 		 		~25
//
// -XX:+UseParNewGC -XX:+UseConcMarkSweepGC
//
// Young 	~22 				~100
// Old		~32					~68
//
// -XX:+UseG1GC
//
// Young 	~40 				~215
// Old		~10 				~25
//

class Main
{
	private static int SIZE = 5 * 1000 * 1000;

	
    public static void main(String... args)
    {
    	System.out.println("Pid: " + ManagementFactory.getRuntimeMXBean().getName());
    	
    	GCListener gcListener = new GCListener();
    	gcListener.start();
		
        while (true)
        {
            int local = SIZE;
            Object[] array = new Object[local];

            for (int i = 0; i < local; i++) {
                array[i] = new String(new char[0]);
            }
            System.out.println("Created " + local + " objects.");
        }
    }
}