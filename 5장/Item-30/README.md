# 이왕이면 제네릭 메서드로 만들라

```java
public static Set union(Set s1, Set s2) {
    Set result = new HashSet(s1);
    result.addAll(s2);
    return result;
}
```

- 로 타입으로 사용해도 컴파일은 가능하지만 제네릭 컬렉션인 Set을 사용하는 부분에서 컴파일 경고가 발생한다.

```java
public static <E> Set<E> union(Set<E> s1, Set<E> s2) {
    Set<E> result = new HashSet<>(s1);
    result.addAll(s2);
    return result;
}
```

- 제네릭 메서드로 선언하게 되면 인수로 들어올 수 있는 매개변수 타입이 하나로 통일되어서 오류나 경고 없이 컴파일 되고, 형변환 하지 않아도 된다.

## 재귀적 타입 한정

```java
public interface Comparable<T> {
    int compareTo(T o);
}
```

- 여기서 타입 매개변수 T는 Comparable<T>를 구현한 타입이 비교할 수 있는 원소의 타입을 정의한다.
- Comparable을 구현한 원소의 컬렉션을 입력받는 메서드들은 주로 그 원소들을 정렬 혹은 검색하거나, 최솟값이나 최댓값을 구하는 식으로 이용된다.
- **이 기능을 수행하려면 컬렉션에 담긴 모든 원소가 상호 비교될 수 있어야 함.**

```java
public static <E extends Comparable<E>> E max(Collection<E> c) {
    ...
}
```

- 타입 한정인 <E extends Comparable<E>>는 "모든 타입 E는 자신과 비교할 수 있다" 라고 읽을 수 있음.

```java
public class Chooser<T> { 
    private final T[] choiceArray;
    
    public Chooser(Collection<? extends T> choices) {
        choiceArray = (T[]) choices.toArray();
    }
}
```