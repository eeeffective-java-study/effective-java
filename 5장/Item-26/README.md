# 로 타입은 사용하지 말라

클래스와 인터페이스 선언에 타입 매개변수가 쓰이면, 이를 **제네릭 클래스** 혹은 **제네릭 인터페이스**라고 한다.

```java
// 대표적인 제네릭 인터페이스 List
public interface List<E> extends SequencedCollection<E> {
    int size();


    boolean isEmpty();
    
    .
    .
    .
}
```

각각의 제네릭 타입은 일련의 매개변수화 타입을 정의함.

예를 들어, List<String>은 원소의 타입이 String인 리스트를 뜻하는 매개변수화 타입이다.

String이 정규 타입 매개변수 E에 해당하는 실제 타입 매개변수임.

### 로 타입
- 제네릭 타입을 정의하면 그에 딸린 로 타입도 함께 정의되는데, 로 타입이란 제네릭 타입에서 타입 매개변수를 전혀 사용하지 않는 것을 의미함.
- List<E>의 로 타입은 List이다.
- 로 타입은 타입 선언에서 제네릭 타입 정보가 전부 지워진 것 처럼 동작하는데, **제네릭 도입 전 코드와 호환되기 위한 궁여지책**

```java
public class RawType {
    private final Collection stamps = ...;
    
    // 아무 오류 없이 컴파일 됨
    public void test() {
        stamps.add(new Coin());
    }
}
```

- 오류는 가능한 한 발생 즉시, 이상적으로는 컴파일 할 때 발견해야하는데, 해당 코드는 오류가 발생하고 한참 뒤인 런타임 시점에서야 알 수 있다.

- 제네릭을 활용하면 변수의 선언 시점에서 타입을 제한할 수 있으므로, 잘못된 타입이 들어올 경우 그 즉시 오류를 알아챌 수 있다.

```java
public class GenericType {
    private final Collection<Stamp> stamps = ...;
    
    // 컴파일 에러 발생!
    public void test() {
        stamps.add(new Coin());
    }
}
```

### List, List<Object>의 차이점은?
- 언뜻 보면 둘의 차이는 없어보인다. List, List<Object> 둘 다 모든 타입을 허용할 수 있기 때문이다.
- 그러나 둘의 차이는 매개변수의 타입 파라미터를 사용 할 때, 안할 때 차이가 생긴다.

```java
import java.util.ArrayList;

public static void main(String[] args) {
    List<String> strings = new ArrayList<>();
    unsafeAdd(strings, Integer.valueOf(42));
    //첫 번째 인수인 strings에서 컴파일 에러 발생!
    safeAdd(strings, Integer.valueOf(42));
}

private static void unsafeAdd(List list, Object o) {
    list.add(o);
}

private static void safeAdd(List<Object> list, Object o) {
    list.add(o);
}
```

- 매개변수로 List를 받는 메서드에서는 List<String>을 넘길 수 있지만, List<Object> 메서드를 받는 메서드에서는 List<String>을 넘길 수 없다.
- 이는 제네릭의 불공변 속성 때문.(item 28)
- 즉, 제네릭을 사용하면 타입 안전성을 챙길 수 있어 컴파일 시점에 예상치 못한 에러를 잡을 수 있음.

### 그러면 List<Object>, List<?>의 차이점은?
- 비 한정적 와일드카드를 사용하면 모든 타입을 받을 수 있기 때문에, 타입 파라미터에서 Object를 사용하는 것과 다를 게 없어보임.
- 그러나 **비 한정적 와일드카드는 null 외에는 어떤 원소도 넣을 수 없다는 특징을 가지고있음.**
