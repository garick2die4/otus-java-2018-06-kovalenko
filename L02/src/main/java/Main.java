import java.lang.System;
import java.util.function.Supplier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.HashMap;

public class Main
{
    public static void main(String... args) throws InterruptedException
    {

//        calcM(() -> new char[0]); // 24
//        calcM(() -> new String("")); // 32
//        calcM(() -> new String(new char[0])); // 56
//        calcM(() -> new FooEmpty()); // 16
//        calcM(() -> new Foo2Shorts()); // 24
//        calcM(() -> new Foo4Shorts()); // 24
//        calcM(() -> new Foo5Shorts()); // 32
//        calcM(() -> new Foo2Ints()); // 24
//        calcM(() -> new Foo3Ints()); // 32
//        calcM(() -> new ArrayList<>()); // 40
//        calcM(() -> new Integer(0)); // 24
//        int arr[] = {0, 0, 0, 0, 0};
//        calcM(() -> Arrays.asList(arr)); // 64
//        int arr2[] = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
//        calcM(() -> Arrays.asList(arr2)); // 64
//        calcM(() -> Arrays.asList(new FooEmpty())); // 80
//        calcM(() -> Arrays.asList(new FooEmpty(),
//                new FooEmpty(),
//                new FooEmpty(),
//                new FooEmpty(),
//                new FooEmpty())); // 176
//        calcM(() -> new HashSet<>()); // 80
//        calcM(() -> new HashMap<>()); // 64
//        HashMap<Integer, FooEmpty> m = new HashMap<>();
//        m.put(1, new FooEmpty());
//        calcM(() -> new HashMap<>(m)); // 152

        HashMap<Integer, FooEmpty> m1 = new HashMap<>();
        for (int i = 0; i < 5; ++i)
            m1.put(i, new FooEmpty());
        calcM(() -> new HashMap<>(m1)); // 392
    }

    private static void calcM(Supplier supplier) throws InterruptedException
    {
        System.out.println("Element size: " +  MemoryCalc.calcMemory(supplier));
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