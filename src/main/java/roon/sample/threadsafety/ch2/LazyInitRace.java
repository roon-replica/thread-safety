package roon.sample.threadsafety.ch2;

import roon.sample.threadsafety.annotation.NotThreadSafe;

@NotThreadSafe
public class LazyInitRace<T, R> {
    private ExpensiveObject expensiveObject;

    // 두 스레드가 동시에 접근하면 경쟁 조건이 발생.
    // 서로 다른 인스턴스를 반환하게 될 수도 있다.
    public ExpensiveObject getInstance() {
        if (expensiveObject == null) {
            expensiveObject = new ExpensiveObject();
        }
        return expensiveObject;
    }

    class ExpensiveObject {

    }

}
