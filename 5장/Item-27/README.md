# 비검사 경고를 제거하라

## 요약
할 수 있는 한 모든 비검사 경고를 제거하라

## @SuppressWarnings("unchecked")
- 타입 안전하다고 확신할 수 있다면 이 에너테이션을 달아서 경고를 숨기자.


```java
public class Chooser<T> {
    private final T[] choiceArray;
    
    public Chooser(Collection<T> choices) {
        @SuppressWarnings("unchecked")
        choiceArray = (T[]) choice.toArray();
    }
}
```

- 위의 코드에서 타입 안전하다고 확신할 수 있는 이유는 choices의 타입이 Collection<T>이고 choiceArray가 T[]이므로 실제로는 안전하기 때문.
