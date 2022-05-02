### lock
- https://www.baeldung.com/java-concurrent-locks
- lock is a tool for controlling access to a shared resource by multiple threads
- 

### monitor (암묵적인 락)
- 암묵적인 락이라고 불리는 이유는 synchronized 키워드가 있는 메서드 또는 블록에 들어가면 자동으로 락을 획득하고,
<br/> 블록을 빠져나올 때 자동으로 락을 해제하기 때문인듯. 
- 자바에서 암묵적인 락은 mutual exclusive lock임
- 당연한 말이지만, 한 스레드가 synchronized 블록을 실행 중이라면 다른 스레드가 블록으로 들어올 수 없다
- 메서드에 synchronized를 쓰면 한 스레드만 동시에 처리가능하므로 성능이 나빠질 수 있다.
<br/> 쓰려면 최대한 작은 범위에 synchronized 써야할 듯?

### reentrant
- https://docs.oracle.com/cd/E36784_01/html/E36868/guide-3.html
- reentrant function behaves correctly if called simultaneously by several threads


### lock으로 상태 보호하기
- 모든 데이터를 lock으로 보호해야 하는 건 아님. 변경 가능한 데이터를 여러 스레드에서 접근할 때만 해당.
- 어떤 클래스의 모든 메서드에 synchronized를 붙인다고 해서 동기화가 확보되는 것은 아님.
<br/>모든 메서드에 synchronized를 붙여도 그 메서드들의 조합이 단일 연산이 되지 않기 때문

### 성능
- synchronized 블록의 범위를 줄이면 스레드 안전성을 유지하면서 동시성 향상 가능
- 그렇다고 너무 작게 줄여서도 안 된다고 함... (적정 수준 잘 모름)
- ConcurrentHashMap의 put() 구현을 보면 synchronized 블록의 범위를 최대한 작게 설정해 놓은듯함.
  ```java
  final V putVal(K key, V value, boolean onlyIfAbsent) {
        if (key == null || value == null) throw new NullPointerException();
        int hash = spread(key.hashCode());
        int binCount = 0;
        for (Node<K,V>[] tab = table;;) {
            Node<K,V> f; int n, i, fh; K fk; V fv;
            if (tab == null || (n = tab.length) == 0)
                tab = initTable();
            else if ((f = tabAt(tab, i = (n - 1) & hash)) == null) {
                if (casTabAt(tab, i, null, new Node<K,V>(hash, key, value)))
                    break;                   // no lock when adding to empty bin
            }
            else if ((fh = f.hash) == MOVED)
                tab = helpTransfer(tab, f);
            else if (onlyIfAbsent // check first node without acquiring lock
                     && fh == hash
                     && ((fk = f.key) == key || (fk != null && key.equals(fk)))
                     && (fv = f.val) != null)
                return fv;
            else {
                V oldVal = null;
                synchronized (f) {
                    if (tabAt(tab, i) == f) {
                        if (fh >= 0) {
                            binCount = 1;
                            for (Node<K,V> e = f;; ++binCount) {
                                K ek;
                                if (e.hash == hash &&
                                    ((ek = e.key) == key ||
                                     (ek != null && key.equals(ek)))) {
                                    oldVal = e.val;
                                    if (!onlyIfAbsent)
                                        e.val = value;
                                    break;
                                }
                                Node<K,V> pred = e;
                                if ((e = e.next) == null) {
                                    pred.next = new Node<K,V>(hash, key, value);
                                    break;
                                }
                            }
                        }
                        else if (f instanceof TreeBin) {
                            Node<K,V> p;
                            binCount = 2;
                            if ((p = ((TreeBin<K,V>)f).putTreeVal(hash, key,
                                                           value)) != null) {
                                oldVal = p.val;
                                if (!onlyIfAbsent)
                                    p.val = value;
                            }
                        }
                        else if (f instanceof ReservationNode)
                            throw new IllegalStateException("Recursive update");
                    }
                }
                if (binCount != 0) {
                    if (binCount >= TREEIFY_THRESHOLD)
                        treeifyBin(tab, i);
                    if (oldVal != null)
                        return oldVal;
                    break;
                }
            }
        }
        addCount(1L, binCount);
        return null;
    }

```


