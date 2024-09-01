# 인터페이스는 구현하는 쪽을 생각해 설계해라
> 릴리스한 후 default method 같은 걸로 수정할 생각하지 마라 

## 이유
- 상호 운용성을 생각해라
- default method로 수정하는 데 한계가 있음

ex. default method 추가 후 자바 플랫폼 라이브러리 수정
```java
static class SynchronizedCollection<E> implements Collection<E>, Serializable {
    
    final Collection<E> c;  // Backing Collection
    final Object mutex;     // Object on which to synchronize

    @Override
    public boolean removeIf(Predicate<? super E> filter) {
        synchronized (mutex) {return c.removeIf(filter);}
    }
    
}
```
하지만 다른 플랫폼은 각자 코드를 변경해야 함

## 결론

### 기존 인터페이스 변경은 자제하기

### 릴리스 전 테스트하기
- 해당 인터페이스를 최소 세 가지 방법으로 구현해 보기
- 각 인터페이스의 용도에 따른 클라이언트를 여러 개 생성해 테스트하기