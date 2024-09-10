# 이왕이면 제네릭 타입으로 만들라

```java
public class Stack {
    private Object[] elements;
    private int size = 0;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    public Stack() {
        elements = new Object[DEFAULT_INITIAL_CAPACITY];
    }

    public Object push(Object item) {
        ensureCapacity();
        elements[size++] = item;
        return item;
    }

    public Object pop() {
        if (size == 0)
            throw new EmptyStackException();
        Object result = elements[--size];
        elements[size] = null; // 다 쓴 참조 해제
        return result;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    private void ensureCapacity() {
        if (elements.length == size)
            elements = Arrays.copyOf(elements, 2 * size + 1);
    }
}
```

- 위의 코드는 Item 7에서 다뤘던 스택 코드 예제이다.

```java
public static void main(String[] args) {
    Stack stack = new Stack();
    stack.push(1);
    String item = (String) stack.pop();
    System.out.println(item);
}
```

- 이 클래스는 제네릭 타입이어야 마땅한데, 이 상태에서 위의 코드를 보면 런타임 오류가 발생할 수 있다는 것을 알 수 있다.

```java
public class Stack<E> {
    private E[] elements;
    private int size = 0;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    public Stack() {
        // 컴파일 에러 발생!
        elements = new E[DEFAULT_INITIAL_CAPACITY];
    }
…
}
```
- E와 같은 실체화 불가 타입으로는 배열을 만들 수 없다.

### 우회방법1 - 배열을 Object로 생성하고 Generic으로 형 변환
```java
@SuppressWarnings("unchecked")
public Stack() {
    // 배열 elements는 push(E)로 넘어온 E 인스턴스만 담는다.
    // 따라서 타입 안전성을 보장하지만,
    // 이 배열의 런타임 타입은 E[]가 아닌 Object[]이다.
    elements = (E[]) new Object[DEFAULT_INITIAL_CAPACITY];
}
```

### 우회방법2 - elements의 타입을 Object[]로 바꾸고 pop에서 형변환
```java
public E pop() {
    if (size == 0) {
        throw new EmptyStackException();
    }
    //push() 에서 E 타입만 허용하기 때문에 이 형변환은 안전함이 보장됨.
    @SuppressWarnings("unchecked")
    E result = (E) elements[--size];
    elements[size] = null; // 다 쓴 참조 해제
    return result;
}
```

### 1과 2 방법의 차이는?

- 방법 1 
  - 배열을 E[]로 선언하여 E타입만 받음을 명시해준다! (타입 안전)
  - 하지만 런타임에는 E[]가 아닌 Object[]로 동작 
  - 힙 오염의 가능성 존재(런타임에 Object[]로 동작하므로)
- 방법 2 
  - 애초에 Object[] 배열이므로 힙 오염의 가능성 X 
  - 그렇다면 pop 시 타입 보장은? 
  - push로 E 만 들어오므로 Object[]에 저장되는 데이터가 모두 E 타입임이 보장 
  - 메서드 호출 시마다 타입 캐스팅을 해주어야 한다.