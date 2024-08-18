# Intro::

> 이펙티브 자바 정리본입니다.
> 

# 결론

- 순서를 고려해야 하는 값 클래스를 작성한다면 꼭 Comparable 인터페이스를 구현하여 여러 장점을 얻을 수 있다.
    - 인스턴스들을 쉽게 정렬하고, 검색하고, 비교 기능을 제공하는 컬렉션과 어루러진다.
- compareTo 메서드에서 필드의 값을 비교할 때 < 와 > 연산자는 쓰지 말아라
    - 대신 박싱된 기본 타입 클래스가 제공하는 정적 compare 메서드나 Comparator 인터페이스가 제공하는 비교자 생성 메서드를 사용하자.

# Comparable 이란?

- Comparable을 구현한 했다는 것은 그 클래스의 인스턴스들에는 자연적인 순서가 있다는 것을 의미합니다.

## compareTo

- `compareTo` 메서드는 `Comparable` 인터페이스의 유일한 메서드입니다.

Object의 equals와 성격은 같지만, 단순 동치성 비교에 더해 순서까지 비교할 수 있으며, 제네릭합니다. 그래서 Comparable을 구현한 객체들의 배열은 다음과 같이 손쉽게 정렬 가능합니다.

```java
Arrays.sort(a);
```

# 그래서 왜 쓰는 걸까?

- Comparable을 구현한다면 이 인터페이스를 활용하는 수많은 제네릭 알고리즘과 컬렉션의 힘을 누릴 수 있습니다.
- 사실상 자바 플랫폼 라이브러리의 모든 값 클래스와 열거 타입이 Comparable을 구현했습니다.

알파벳, 숫자, 연대 같이 순서가 명확한 값 클래스를 작성한다면 반드시 Comparable 인터페이스를 구현합시다.

```java
public interface Comparable<T> {
	int compareTo(T t);
}
```

# compareTo 메서드의 일반 규약

이 객체와 주어진 객체의 순서를 비교합니다. 이객체가 주어진 객체보다 작으면 음의 정수를, 같으면 0을, 크면 양의 정수를 반환합니다. 이 객체와 비교할 수 없는 타입의 객체가 주어지면 ClassCastException을 던집니다.

- 두 객체 참조의 순서를 바꿔 비교해도 예상한 결과가 나와야 합니다.
    - `a == b 이면 b == a`, `a < b 이면 b > a`, …
- 첫 번째가 두 번째보다 크고 두번째가 세번째보다 크면 첫 번째는 세 번째보다 커야합니다.
- 크기가 같은 객체들끼리는 어떤 객체와 비교하더라도 같아야 합니다.

위의 세 규약은 compareTo 메서드로 수행하는 동치성 검사도 equals 규약과 똑갗이 반사성, 대칭성, 추이성을 충족해야 함을 뜻합니다. 

### 권고 규약

- compareTo 메서드로 수행한 동치성 테스트의 결과가 equals와 같아야 합니다.
    - 이를 잘 지키면 compareTo로 줄지은 순서와 equals의 결과가 일관되게 됩니다.
    - compareRo와 equals 순서가 일관되지 않은 클래스도 여전히 동작하지만, 이 클래스의 객체를 정렬된 컬렉션에 넣으면 해당 컬렉션이 구현한 인터페이스(Collection, Set 혹은 Map)에 정의된 동작과 엇박자를 낼 것입니다.

## 주의사항

equals 규약과 똑같다.

- 기존 클래스를 확장한 구체 클래스에서 새로운 값 컴포넌트를 추가했다면 compareTo 규약을 지킬 방법이 없다. 이를 해결하기 위해 확장 대신 독립된 클래스를 만들고 뷰를 제공하는 메서드를 사용하면 된다.

# CompareTo 메서드 작성 요령

- Comparable은 타입을 인수로 받는 제네릭 인터페이스이므로 compareTo 메서드의 인수 타입은 컴파일타임에 정해집니다.
    - 입력 인수의 타입을 확인하거나 형변환할 필요가 없다는 의미
    
    ```java
    class MyString implements Comparable<String> {
        private String value;
    
        public MyString(String value) {
            this.value = value;
        }
    
        @Override
        public int compareTo(String other) {
            return this.value.compareTo(other);
        }
    }
    ```
    
    `MyString` 클래스는 `Comparable<String>`을 구현합니다. 따라서 `compareTo` 메서드는 `String` 타입의 인수를 받습니다. 이 메서드 안에서는 `other`라는 인수가 이미 `String` 타입이라는 것이 확정되어 있기 때문에, 이 인수를 `String` 타입으로 변환할 필요가 없습니다.
    
- null을 인수로 넣어 호출하면 NullPointerException을 던져야 합니다.

Comparable을 구현하지 않은 필드나 표준이 아닌 순서로 비교해야 한다면 비교자(Comparator)를 대신 사용합니다. 비교자를 직접 만들거나 자바가 제공하는 것 중에 골라 사용하면 됩니다.

```java
// 코드 14-1 객체 참조 필드가 하나뿐인 비교자 (90쪽)
public final class CaseInsensitiveString
        implements Comparable<CaseInsensitiveString> {
    private final String s;

    public CaseInsensitiveString(String s) {
        this.s = Objects.requireNonNull(s);
    }

		...

    // 자바가 제공하는 비교자를 사용해 클래스를 비교한다.
    public int compareTo(CaseInsensitiveString cis) {
        return String.CASE_INSENSITIVE_ORDER.compare(s, cis.s);
    }

    public static void main(String[] args) {
        Set<CaseInsensitiveString> s = new TreeSet<>();
        for (String arg : args)
            s.add(new CaseInsensitiveString(arg));
        System.out.println(s);
    }
}
```

CompareTo 메서드에서 관계 연산자 < 와 > 를 사용하는 이전 방식은 거추장스럽고 오류를 유발하니 추천하지 않습니다.

```java
// PhoneNumber를 비교할 수 있게 만든다. (91-92쪽)
public final class PhoneNumber implements Cloneable, Comparable<PhoneNumber> {
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

//    // 코드 14-2 기본 타입 필드가 여럿일 때의 비교자 (91쪽)
//    public int compareTo(PhoneNumber pn) {
//        int result = Short.compare(areaCode, pn.areaCode);
//        if (result == 0)  {
//            result = Short.compare(prefix, pn.prefix);
//            if (result == 0)
//                result = Short.compare(lineNum, pn.lineNum);
//        }
//        return result;
//    }

    // 코드 14-3 비교자 생성 메서드를 활용한 비교자 (92쪽)
    // 약간의 성능 저하
    private static final Comparator<PhoneNumber> COMPARATOR =
            comparingInt((PhoneNumber pn) -> pn.areaCode)
                    .thenComparingInt(pn -> pn.prefix)
                    .thenComparingInt(pn -> pn.lineNum);

    public int compareTo(PhoneNumber pn) {
        return COMPARATOR.compare(this, pn);
    }

    public static void main(String[] args) {
        NavigableSet<PhoneNumber> s = new TreeSet<PhoneNumber>();
        for (int i = 0; i < 10; i++)
            s.add(randomPhoneNumber());
        System.out.println(s);
    }
}
```

---

# References::

이펙티브 자바 / 조슈아 블로크 지음 (프로그래밍 인사이트)