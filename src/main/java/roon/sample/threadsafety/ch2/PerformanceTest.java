package roon.sample.threadsafety.ch2;

import java.util.List;
import java.util.function.Supplier;

public class PerformanceTest {
    protected <T> void addElements(int range, List<T> list, Supplier<T> supplier) {
        long start = System.currentTimeMillis();

        for (int i = 0; i < range; i++) {
            list.add(supplier.get());
        }

        long end = System.currentTimeMillis();

        System.out.println("processing time(ms) = " + (end - start));
    }

    protected <T> void updateAll(List<T> list, Supplier<T> supplier) {
        long start = System.currentTimeMillis();

        for (int i = 0; i < list.size(); i++) {
            list.set(i, supplier.get());
        }

        long end = System.currentTimeMillis();

        System.out.println("processing time(ms) = " + (end - start));
    }

}
