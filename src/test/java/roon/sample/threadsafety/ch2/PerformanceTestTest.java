package roon.sample.threadsafety.ch2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

class PerformanceTestTest {
    PerformanceTest performanceTest = new PerformanceTest();

    Vector<Integer> vector = new Vector<>();
    List<Integer> list = new ArrayList<>();

    @BeforeEach
    public void init() {
        addElements();
    }

    private void addElements() {
        int RANGE = 2_000_000;

        performanceTest.addElements(RANGE, vector, () -> 1);
        performanceTest.addElements(RANGE, list, () -> 1);
    }

    // RANGE가 작을 때는 synchronized 안 쓰는 list가 확실히 빠른데
    // 1백만 이상 커질수록 오히려 vector가 더 성능 좋아짐...
    // vector.add나 arrayList.add 코드는 거의 똑같고 add 자체가 짧아서 synchronized가 별 타격 없는듯
    @Test
    public void test_add_synchronized하면_얼마나느린지() {
        assertTrue(true);
    }

    // set(idx, element)는 확실히 메서드가 조금 더 커서 그런지 synchronized 안붙은 arrayList가 더 빠른듯함
    @Test
    public void test_remove_synchronized하면_얼마나느린지() {
        updateAll(vector, () -> 2);
        updateAll(list, () -> 2);

        assertTrue(vector.isEmpty());
        assertTrue(list.isEmpty());
    }

    private <T> void updateAll(List<T> list, Supplier<T> supplier) {
        performanceTest.updateAll(list, supplier);
    }


}