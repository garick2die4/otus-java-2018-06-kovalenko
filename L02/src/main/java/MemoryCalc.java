import java.util.function.Supplier;
import java.lang.Math;

public class MemoryCalc
{
    final static int SIZE = 10000;

    public static long calcMemory(Supplier supplier) throws InterruptedException
    {
        return calcMemory(supplier, SIZE);
    }

    public static long calcMemory(Supplier supplier, int size) throws InterruptedException
    {
        Object[] array = new Object[size];

        long memStart = getMem();
        for (int i = 0; i < size; i++)
        {
            array[i] = supplier.get();
        }
        long memEnd = getMem();
        array = null;
        Thread.sleep(1000);
        return Math.round((double)(memEnd - memStart) / (double)(size));
    }

    private static long getMem() throws InterruptedException
    {
        System.gc();
        Thread.sleep(10);
        Runtime runtime = Runtime.getRuntime();
        return runtime.totalMemory() - runtime.freeMemory();
    }
}