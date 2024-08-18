# Intro::

> 이펙티브 자바 정리본입니다.
> 

# 결론

- Cloneable을 구현하는 모든 클래스는 clone을 재정의해야 한다. 이때 접근 제한자는 public으로, 반환 타입은 클래스 자신으로 변경한다.
- 미 메서드는 가장 먼저 super.clone을 호출한 뒤 필요한 필드를 전부 적절히 수정한다.
    - 필요시 깊은 복사를 한다는 의미
- Cloneable을 이미 구현한 클래스를 확장한다면 어쩔 수 없이 clone이 잘 작동하도록 구현해야하지만, 그렇지 않은 경우 복사 생성자와 복사 팩터리라는 더 나은 객체 복사 방식을 제공할 수 있다.
    
    ```java
    // 복사 생성자
    public Yum(Yum yum) {...};
    
    // 복사 팩터리
    public static Yum newInstance(Yum yum) {...};
    ```
    
- 새로운 인터페이스를 만들 때는 절대 Cloneable을 확장해서는 안되며, 새로운 클래스도 이를 구현해서는 안된다.

# Cloneable 인터페이스

- Object의 protected 메서드인 clone의 동작 방식을 결정합니다.
- Cloneable을 구현한 클래스의 인스턴스에서 clone을 호출하면 그 객체의 필드들을 하나하나 복사한 객체를 반환하며, 그렇지 않은 클래스의 인스턴스에서 호출하면 CloneNotSupportedException을 던집니다.
- 인터페이스를 구현한다는 것은 일반적으로 해당 클래스가 그 인터페이스에서 정의한 기능을 제공한다고 선언하는 행위이지만, Clonable의 경우에는 상위 클래스에 정의된 protected 메서드의 동작 방식을 변경한 것 입니다.
- 실무에서 Cloneable을 구현한 클래스는 clone 메서드를 public으로 제공하며, 사용자는 당연히 복제가 제대로 이뤄지리라 기대합니다.

# Object clone 명세

> clone 메서드의 일반 규약은 허술하다.
> 
- 이 객체의 복사본을 생성해 반환한다. 복사의 정확한 뜻은 그 객체를 구현한 클래스에 따라 다를 수 있다. 일반적인 의도는 다음과 같다. 어떤 객체 x에 대해 다음 식은 참이다.
    - x.clone() != x
    - x.clone().getClass() == x.getClass()
- 하지만 이상의 요구를 반드시 만족해야 하는 것은 아니다.
- 다음 식도 일반적으로 참이지만, 필수는 아니다.
    - x.clone().equals(x)
- 관례상, 이 메서드가 반환하는 객체는 super.clone을 호출해 얻어야 한다. 이 클래스와(Object를 제외한) 모든 상위 클래스가 이 관례를 따른다면 다음 식은 참이다.
    - x.clone().getClass() == x.getClass()
- 관례상, 반환된 객체와 원본 객체는 독립적이어야 한다. 이를 만족하려면 super.clone으로 얻은 객체의 필드 중 하나 이상을 반환 전에 수정해야 할 수도 있다.

clone 메서드가 super.clone이 아닌, 생성자를 호출해 언든 인스턴스를 반환해도 컴파일러는 불평하지 않겠지만, 이 클래스의 하위 클래스에서 super.clone을 호출한다면 잘못된 클래스의 객체가 만들어져, 결국 하위 클래스의 clone 메서드가 제대로 동작하지 않게 됩니다.

예시로, 클래스 B가 클래스 A를 상속할 때, 하위 클래스인 B의 clone은 B 타입 객체를 반환해야 합니다. 그런데 A의 clone이 자신의 생성자, 즉 new A(…)로 생성된 객체를 반환한다면 B의 clone도 A 타입 객테를 반환할 수 밖에 없습니다. 달리 말해 super.clone을 연쇄적으로 호출하도록 구현해두면 clone이 처음 호출된 상위 클래스의 객체가 만들어집니다.

# 제대로 동작하는 clone 메서드 구현

## 재귀적 호출 방식

- 쓸데없는 복사를 지양한다는 점에서, 불변 클래스는 굳이 clone 메서드를 제공하지 않는 게 좋다.

```java
// PhoneNumber에 clone 메서드 추가 (79쪽)
public final class PhoneNumber implements Cloneable {
    private final short areaCode, prefix, lineNum;

    public PhoneNumber(int areaCode, int prefix, int lineNum) {
        this.areaCode = rangeCheck(areaCode, 999, "지역코드");
        this.prefix   = rangeCheck(prefix,   999, "프리픽스");
        this.lineNum  = rangeCheck(lineNum, 9999, "가입자 번호");
    }

    private static short rangeCheck(int val, int max, String arg) {
        if (val < 0 || val > max)
            throw new IllegalArgumentException(arg + ": " + val);
        return (short) val;
    }

		...

    // 코드 13-1 가변 상태를 참조하지 않는 클래스용 clone 메서드 (79쪽)
    @Override public PhoneNumber clone() {
        try {
            return (PhoneNumber) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();  // 일어날 수 없는 일이다.
        }
    }
}
```

```java
public static void main(String[] args) {
    PhoneNumber pn = new PhoneNumber(707, 867, 5309);
    Map<PhoneNumber, String> m = new HashMap<>();
    m.put(pn, "제니");
    System.out.println(m.get(pn.clone()));
}
```

> 가변 상태를 참조하지 않기 때문에 문제가 없지만 가변 객체를 참조하는 순간 의도한 동작을 수행하지 못하게 된다.
> 

```java
// Stack의 복제 가능 버전 (80-81쪽)
public class Stack implements Cloneable {
    private Object[] elements;
    private int size = 0;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    public Stack() {
        this.elements = new Object[DEFAULT_INITIAL_CAPACITY];
    }

    ...

    // 코드 13-2 가변 상태를 참조하는 클래스용 clone 메서드
    @Override public Stack clone() {
        try {
            Stack result = (Stack) super.clone();
            result.elements = elements.clone();
            return result;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
```

```java
// clone이 동작하는 모습을 보려면 명령줄 인수를 몇 개 덧붙여서 호출해야 한다.
public static void main(String[] args) {
    Stack stack = new Stack();
    for (String arg : args)
        stack.push(arg);
    Stack copy = stack.clone();
    while (!stack.isEmpty())
        System.out.print(stack.pop() + " ");
    System.out.println();
    while (!copy.isEmpty())
        System.out.print(copy.pop() + " ");
}
```

위의 예시에서 elements 필드가 final이었다면 앞서의 방식은 작동하지 않는다. 이는 근본적인 문제로, 직렬화와 마찬가지로 Clonable 아키텍처는 `가변 객체를 참조하는 필드는 final로 선언하라` 는 일반 용법과 충돌한다. 그래서 복제할 수 있는 클래스를 만들기 위해 일부 필드에서 final 한정자를 제거해야 할 수도 있다.

## 순회 방식

```java
public class HashTable implements Clonable {
    private Entry buckets = ...;

    private static class Entry {
        final Object key;
        Object value;
        Entry next;

        Entry(Object key, Object value, Entry next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
        /**
         * 방식1
         * 이 엔트리가 가리키는 연결 리스트를 재귀적으로 복사
         */
        Entry deepCopy() {
            return new Entry(key, value, next == null ? null : next.deepCopy());
        }

        /**
         * 방식2
         * 엔트리 자신이 가리키는 연결 리스트를 반복적으로 복사한다.
         * 다음 노드를 순차적으로 복사하는 방식
         */
        Entry deepCopy() {
            Entry result = new Entry(key, value, next);
            for (Entry p = result; p.next != null; p = p.next) {
                p.next = new Entry(p.next.key, p.next.value, p.next.next);
            }
            return new Entry(key, value, next == null ? null : next.deepCopy());
        }

    }

    // 잘못된 clone 메서드 - 가변 상태를 공유한다!
    @Override public HashTable clone() {
        try {
            HashTable result = (HashTable) super.clone();
            result.buckets = buckets.clone();
            return result;
        } catch (CloneNotSupportedException e) {
            throw new AssertionsError() ;
        }
    }

    /**
     * 이 방식은 간단하며, 버킷이 너무 길지 않다면 잘 작동한다.
     * 하지만 연결 리스트를 복제하는 방법으로는 그다지 좋지 않다.
     */
    @Override public HashTable clone() {
        try {
            HashTable result = (HashTable) super.clone();
            result.buckets = new Entry[buckets.length];
            for (int i = 0; i < buckets.length; i++) {
                if (buckets[i] != null) {
                    result.buckets[i] = buckets[i].deepCopy();
                }
            }
            return result;
        } catch (CloneNotSupportedException e) {
            throw new AssertionsError() ;
        }
    }
}
```

## 복잡한 가변 객체를 복제하는 마지막 방법

- super.clone을 호출해 객체의 모든 필드를 초기 상태로 설정한 뒤, 원복 객테의 상태를 다시 생성하는 고수준 메서드를 호출한다.
    - HashTable의 예제에서 buckets 필드를 새로운 버킷 배열로 초기화한 다음 원본 테이블에 담긴 모든 키-값 쌍 각각에 대해 복제본 테이블의 put(key, value) 메서드를 호출해 똑같게 해주는 방식
    - 저수준 보다 느리고, 전체 Cloneable 아키텍처와는 어울리지 않는 방식이다.
- 생성자에서는 재정의될 수 있는 메서드를 호출하지 않아야 하는데 이는 clone도 마찬가지다.
    - 앞서 얘기한 put(key, value) 메서드는 final이거나 private이어야 한다.
- 상속해서 쓰기 위한 클래스 설계 방식 두가지 중 어느 쪽에서든, 상속용 클래스는 Cloneable을 구현해서는 안된다.

---

# References::

이펙티브 자바 / 조슈아 블로크 지음 (프로그래밍 인사이트)