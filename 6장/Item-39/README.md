# Intro::

> 이펙티브 자바 정리본입니다.
> 

# 결론

- 애너테이션을 통해 가독성을 개선할 수 있습니다.
- 애너테이션을 선언하고 이를 처리하는 부분에서는 코드 양이 늘어나며, 처리 코드가 복잡해 오류가 날 가능성도 커짐을 인지해야합니다.
- 자바 프로그래머라면 예외 없이 자바가 제공하는 애너테이션 타입들은 사용해야합니다.

# 명명 패턴의 단점

1. 오타가 나면 안된다.
2. 올바른 프로그램 요소에서만 사용되리라 보증할 방법이 없다.
3. 프로그램 요소를 매개변수로 전달할 마땅한 방법이 없다.

# Annotation

```java
@Retention(RetentionPolicy.RUNTIME)// 런타임에도 유지되어야 한다는 뜻
@Target(ElementType.METHOD)// 메서드 선언에서만 사용되어야 한다는 뜻
public @interface Test {
}
```

애너테이션 선언에 다는 애너테이션을 메타애너테이션이라 합니다.

매개변수 없는 정적 메서드 전용이라는 제약을 컴파일러에게 강제하고자 한다면 적절한 애너테이션 처리기를 직접 구현해야 합니다. 위와 같이 단순히 대상에 마킨하는 애너테이션을 마커 애너테이션이라고 합니다.

```java
public class RunTests {
    public static void main(String[] args) throws Exception {
        int tests = 0;
        int passed = 0;
        Class<?> testClass = Class.forName("item39.Sample");

        for (Method m : testClass.getDeclaredMethods()) {
            if (m.isAnnotationPresent(Test.class)) {
                tests++;
                try {
                    m.invoke(null);
                    passed++;
                } catch (InvocationTargetException wrappedExc) {
                    Throwable exc = wrappedExc.getCause();
                    System.out.println(m + " 실패: " + exc);
                } catch (Exception exc) {
                    System.out.println("잘못 사용한 @Test: " + m);
                }
            }
        }
        System.out.printf("성공: %d, 실패: %d%n", passed, tests - passed);
    }
}
```

## 특정 예외를 던져야 하는 경우

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ExceptionTest {
    Class<? extends Throwable> value();
    // Class<? extends Throwable>[] value();// 배열로 받는 방법
}
```

## 반복 가능 애너테이션

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Repeatable(ExceptionTestContainer.class)
public @interface ExceptionTest {
    Class<? extends Throwable> value();
}
```

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ExceptionTestContainer {
    ExceptionTest[] value();
}
```

@Repeatable을 단 애너테이션은 하나의 프로그램 요소에 여러 번 달 수 있습니다.

### 주의점

1. @Repeatable을 단 애너테이션을 반환하는 컨테이너 애너테이션을 하나 더 정의하고, @Repeatable에 이 컨테이너 애너테이션의 class 객체를 매개변수로 전달해야 합니다.
2. 컨테이너 애너테이션은 내부 애너테이션 타입의 배열을 반환하는 value 메서드를 정의해야 합니다.
3. 컨테이너 애너테이션 타입에는 적절한 보존 정책(@Retention)과 적용 대상(@Target)을 명시해야 합니다.
4. 반복 가능 애너테이션을 처리할 때, 달려 있는 수와 상관없이 모두 겁사하려면 둘을 따따로 확인해야 합니다.
    
    ```java
    if (m.isAnnotationPresent(ExceptionTest.class)
                        || m.isAnnotationPresent(ExceptionTestContainer.class))
    ```
    

---

# References::

이펙티브 자바 / 조슈아 블로크 지음 (프로그래밍 인사이트)