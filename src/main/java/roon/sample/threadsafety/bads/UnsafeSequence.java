package roon.sample.threadsafety.bads;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import roon.sample.threadsafety.annotation.NotThreadSafe;

@Setter
@Getter
@Component
@NotThreadSafe
public class UnsafeSequence {
    private int value;

    // 이 코드는 멀티 스레드에서 제대로 동작하지 않음!
    // value++은 하나의 코드가 아니기 때문
    // 다른 스레드가 연산 도중에 끼어 들 수 있다.
    public int getNext() {
        return value++;
    }

    public synchronized int getNextSynchronized() {
        return value++;
    }
}
