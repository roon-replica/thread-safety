package roon.sample.threadsafety.ch1;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

class SafeSequenceTest {
    SafeSequence safeSequence = new SafeSequence();

    @Test
    public void testAtomicOperation() throws ExecutionException, InterruptedException {
        int COUNT = 10000;
        long start = System.currentTimeMillis();
        calc(() -> safeSequence.getNext(), COUNT);
        long end = System.currentTimeMillis();

        System.out.println("processing time(ms) = " + (end - start));
        int expectedNext = COUNT;
        int actualNext = safeSequence.getCount().get();

        System.out.println(actualNext + " " + expectedNext);

        assertEquals(COUNT, actualNext);
    }

    public void calc(Supplier<Integer> supplier, int count) throws InterruptedException, ExecutionException {
        ExecutorService executorService = Executors.newFixedThreadPool(8);
        safeSequence.getCount().set(0);

        for (int i = 0; i < count; i++) {
            executorService.submit(supplier::get);
        }

        executorService.awaitTermination(1000, TimeUnit.MILLISECONDS);
    }

}