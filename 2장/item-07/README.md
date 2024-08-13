# 다 쓴 객체 참조를 해제하라

### 문제점: 메모리 누수

자바는 가비지 컬렉터를 통해 메모리를 자동으로 관리함. 하지만 프로그래머가 의도치 않게 객체 참조를 유지하면 가비지 컬렉터가 해당 객체를 회수하지 못해 메모리 누수가 발생할 수 있음.

### 예시: 간단한 스택 구현
```java
public class Stack {
private Object[] elements;
private int size = 0;
private static final int DEFAULT_INITIAL_CAPACITY = 16;

    public Stack() {
        elements = new Object[DEFAULT_INITIAL_CAPACITY];
    }

    public void push(Object e) {
        ensureCapacity();
        elements[size++] = e;
    }

    public Object pop() {
        if (size == 0)
            throw new EmptyStackException();
        return elements[--size];
    }

    private void ensureCapacity() {
        if (elements.length == size)
            elements = Arrays.copyOf(elements, 2 * size + 1);
    }
}
```

이 스택 구현에는 메모리 누수 문제가 있는데, pop() 메서드는 스택에서 요소를 제거하지만, 배열에서 해당 참조를 제거하지 않음. 따라서 가비지 컬렉터가 이 객체를 회수할 수 없게 됨.

### 해결책: 다 쓴 참조 해제

pop() 메서드를 다음과 같이 수정하여 문제를 해결할 수 있음:

```java
public Object pop() {
    if (size == 0)
        throw new EmptyStackException();
    Object result = elements[--size];
    elements[size] = null; // 다 쓴 참조 해제
    return result;
}
```

이렇게 수정하면 더 이상 사용하지 않는 객체 참조를 명시적으로 null로 설정하여 가비지 컬렉터가 해당 객체를 회수할 수 있게됨.

### 주의사항

객체 참조를 null로 설정하는 것은 예외적인 경우에만 수행해야 함.

변수의 범위를 최소화하여 참조를 자동으로 관리하는 것이 더 좋은 방법임.

캐시, 리스너, 콜백 등에서도 메모리 누수에 주의

### WeakHashMap

```java
import java.util.WeakHashMap;

public class WeakHashMapExample {
    public static void main(String[] args) {
        WeakHashMap<Key, String> map = new WeakHashMap<>();
        
        Key key1 = new Key("Key1");
        Key key2 = new Key("Key2");
        
        map.put(key1, "Value1");
        map.put(key2, "Value2");
        
        System.out.println(map.size());  // 출력: 2
        
        key1 = null;  // key1에 대한 강한 참조 제거
        System.gc();  // 가비지 컬렉션 실행 요청
        
        System.out.println(map.size());  // 출력: 1 (key1 관련 엔트리 제거됨)
    }
    
    static class Key {
        String name;
        Key(String name) { this.name = name; }
    }
}
```

#### 약한 참조(Weak References) 사용:

WeakHashMap은 키(key)에 대해 약한 참조를 사용함.
이는 가비지 컬렉터가 해당 키 객체를 더 이상 사용하지 않는다고 판단하면 언제든지 해당 엔트리를 삭제할 수 있음을 의미함.


#### 메모리 관리:

주로 캐시 구현에 사용됨.
메모리가 부족해지면 가비지 컬렉터가 WeakHashMap의 엔트리를 자동으로 제거할 수 있어, 메모리 누수를 방지