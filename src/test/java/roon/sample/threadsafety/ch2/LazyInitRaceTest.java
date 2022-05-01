package roon.sample.threadsafety.ch2;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

class LazyInitRaceTest {
    LazyInitRace<String, Integer> lazyInitRace = new LazyInitRace();

    @Test
    public void test_LazyInitRace() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(8);
        Set<Future<String>> futures = new HashSet<>();

        for (int i = 0; i < 10; i++) {
            Future<String> res = executorService.submit(() -> lazyInitRace.getInstance().toString());
            futures.add(res);
        }

        executorService.awaitTermination(1000, TimeUnit.MILLISECONDS);

        Set<String> addresses = new HashSet<>();

        for (Future<String> future : futures) {
            addresses.add(future.get());
        }

        System.out.println(addresses.size());
        assertEquals(10, addresses.size());
    }

}