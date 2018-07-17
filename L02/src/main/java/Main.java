import java.lang.System;
import java.util.function.Supplier;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.HashMap;

public class Main
{
    public static void main(String... args) throws InterruptedException
    {
        calcM("char[0]", () -> new char[0], 1000000);
        calcM("String(\"\")", () -> new String(""));
        calcM("String(new char[0])", () -> new String(new char[0]));
        calcM("FooEmpty()", () -> new FooEmpty());
        calcM("Foo2Shorts()", () -> new Foo2Shorts());
        calcM("Foo4Shorts()", () -> new Foo4Shorts());
        calcM("Foo5Shorts()", () -> new Foo5Shorts());
        calcM("Foo2Ints()", () -> new Foo2Ints());
        calcM("Foo3Ints()", () -> new Foo3Ints());
        calcM("Integer(0)", () -> new Integer(0));

        {
            int[] arr10 = new int[10];
            calcM("int arr[10]", () -> Arrays.asList(Arrays.copyOf(arr10, 10)));
        }
        {
            int arr100[] = new int[100];
            calcM("int arr[100]", () -> Arrays.asList(Arrays.copyOf(arr100, 100)));
        }
        {
            int arr1000[] = new int[1000];
            calcM("int arr[1000]", () -> Arrays.asList(Arrays.copyOf(arr1000, 1000)));
        }

        calcM("ArrayList<>()", () -> new ArrayList<>());
        for (int i = 0; i < 4; ++i)
        {
            long sz = Math.round(Math.pow(10, i));
            calcM("ArrayList " + sz, () -> createList(sz)); // 80
        }

        for (int i = 0; i < 4; ++i)
        {
            long sz = Math.round(Math.pow(10, i));
            calcM("HashMap " + sz, () -> createMap(sz));
        }
    }

    private static HashMap<Integer, FooEmpty> createMap(long sz)
    {
        HashMap<Integer, FooEmpty> m = new HashMap<>();
        for (int j = 0; j < sz; ++j)
        {
            m.put(j, new FooEmpty());
        }
        return m;
    }
    private static List<FooEmpty> createList(long sz)
    {
        List<FooEmpty> arr = new ArrayList<>();
        for (long j = 0; j < sz; ++j)
            arr.add(new FooEmpty());
        return arr;
    }
    private static void calcM(String text, Supplier supplier) throws InterruptedException
    {
        System.out.println(text + ": " +  MemoryCalc.calcMemory(supplier));
    }
    private static void calcM(String text, Supplier supplier, int size) throws InterruptedException
    {
        System.out.println(text + ": " +  MemoryCalc.calcMemory(supplier, size));
    }

    private static class FooEmpty
    {
    }

    private static class Foo2Shorts
    {
        private short f1 = 0;
        private short f11 = 0;
    }

    private static class Foo4Shorts
    {
        private short f1 = 0;
        private short f11 = 0;
        private short f12 = 0;
        private short f13 = 0;
    }

    private static class Foo5Shorts
    {
        private short f1 = 0;
        private short f11 = 0;
        private short f12 = 0;
        private short f13 = 0;
        private short f14 = 0;
    }

    private static class Foo2Ints
    {
        private int f1 = 0;
        private int f11 = 0;
    }

    private static class Foo3Ints
    {
        private int f1 = 0;
        private int f11 = 0;
        private int f12 = 0;
    }
}