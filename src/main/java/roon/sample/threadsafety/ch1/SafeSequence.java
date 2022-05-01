package roon.sample.threadsafety.ch1;

import lombok.Getter;
import roon.sample.threadsafety.annotation.ThreadSafe;

import java.util.concurrent.atomic.AtomicInteger;   // 숫자나 객체 참조를 복합 연산 -> 단일 연산으로 바꿔줄 수 있도록하는 클래스

@Getter
@ThreadSafe
public class SafeSequence {
    private AtomicInteger count = new AtomicInteger(0);

    public int getNext() {
        return count.incrementAndGet();
    }
}
