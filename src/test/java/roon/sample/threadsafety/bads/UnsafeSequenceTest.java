package roon.sample.threadsafety.bads;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.concurrent.*;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class UnsafeSequenceTest {
    @Autowired
    private UnsafeSequence unsafeSequence;

    @Test
    public void test_not_thread_safe() throws ExecutionException, InterruptedException {
        int COUNT = 10000;
        long start = System.currentTimeMillis();
        calc(() -> unsafeSequence.getNext(), COUNT);
        long end = System.currentTimeMillis();

        System.out.println("processing time(ms) = " + (end - start));
        int expectedNext = COUNT;
        int actualNext = unsafeSequence.getValue();

        System.out.println(actualNext + " " + expectedNext);
        assertNotEquals(expectedNext, actualNext);
    }

    @Test
    public void test_thread_safe_synchronized() throws ExecutionException, InterruptedException {
        int COUNT = 10000;
        long start = System.currentTimeMillis();
        calc(() -> unsafeSequence.getNextSynchronized(), COUNT);
        long end = System.currentTimeMillis();

        System.out.println("processing time(ms) = " + (end - start));

        int expectedNext = COUNT;
        int actualNext = unsafeSequence.getValue();

        System.out.println(actualNext + " " + expectedNext);
        assertEquals(expectedNext, actualNext);
    }

    // 공통 부분 따로 빼냄.
    // Supplier로 함수를 파라미터로 넘겨줬음
    // 근데 전달받은 supplier를 어케 써야할지 조금 헷갈렸었음.
    // input, output만 생각했다면 쉽게 파악했을텐데..
    public void calc(Supplier<Integer> supplier, int count) throws InterruptedException, ExecutionException {
        ExecutorService executorService = Executors.newFixedThreadPool(8);
        unsafeSequence.setValue(0);

        for (int i = 0; i < count; i++) {
            executorService.submit(supplier::get);
        }

        executorService.awaitTermination(1000, TimeUnit.MILLISECONDS);
    }

}
