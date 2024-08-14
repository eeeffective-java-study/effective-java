# Intro::

> 이펙티브 자바 정리본입니다.
> 

# 재정의가 필요한 이유

> 그렇지 않으면 hashCode 일반 규약을 어기게 되어 해당 클래스의 인스턴스를 HashMap이나 HashSet 같은 컬렉션의 원소로 사용할 때 문제를 일으킬 것입니다.
> 

# Object 명세

- equals 비교에서 사용되는 정보가 변경되지 않았다면, 애플리케이션이 실행되는 동안 그 객체의 hashCode 메서드는 몇 번을 호출해도 일관되게 항상 같은 값을 반환해야 합니다. 단, 애플리케이션을 다시 실행한다면 이 값이 달라져도 상관없습니다.
- equals(Object)가 두 객체를 같다고 판단했다면, 두 객체의 hashCode는 똑같은 값을 반환해야 합니다.
- equals(Object)가 두 객체를 다르다고 판단했더라도, 두 객체의 hashCode가 서로 다른 값을 반환할 필요는 없습니다. 단, 다른 객체에 대해서는 다른 값을 반환해야 해시테이블의 성능이 좋아집니다.

해당 명세를 통해 두번째 조항이 hashCode 재정의를 잘못했을 때 크게 문제가 되는 것을 알 수 있습니다. **즉, 논리적으로 같은 객체는 같은 해시코드를 반환해야 합니다.**

# 좋은 hashCode를 작성하기 위한 요령

1. int 변수 result를 선언한 후 값 c로 초기화한다. 이때 c는 해당 객체의 첫번째 핵심 필드를 2.a  방식으로 계산한 해시코드입니다.
2. 해당 객테의 나머지 핵심 필드 f 각각에 대해 다음 작업을 수행합니다.
    1. 해당 필드의 해시코드 c를 계산합니다.
        1. **기본 타입 필드**라면, Type.hashCode(f)를 수행합니다. 여기서 Type은 해당 기본 타입의 박식 클래시입니다.
        2. **참조 타입 필드**면서 이 클래스의 equals 메서드가 이 필드의 equals를 재귀적으로 호출해 비교한다면, 이 필드의 hashCode를 재귀적으로 호출합니다. 계산이 더 복잡해질 것 같으면, 이 필드의 표준형을 만들어 그 표준형의 hashCode를 호출합니다. 필드의 값이 null이면 0을 사용합니다.(전통적으로 0을 사용)
        3. **필드가 배열**이라면, 핵심 원소 각각을 별도 필드처럼 다니다. 이상의 규칙을 재귀적으로 적용해 각 핵심 원소의 해시코드를 계산한 다음, 단계 2.b방식으로 갱신합니다. 배열에 핵심 원소가 하나도 없다면 단순히 상수(0을 추천)를 사용합니다. 모든 원소가 핵심 원소라면 Arrays.hashCode를 사용합니다.
    2. 단계 2.a 에서 계산한 해시코드 c로 result를 갱신합니다.
        
        ```java
        result = 31 * result + c;
        ```
        
3. result를 반환합니다.

```java
// equals를 재정의하면 hashCode로 재정의해야 함을 보여준다. (70-71쪽)
public final class PhoneNumber {
    private final short areaCode, prefix, lineNum;

    public PhoneNumber(int areaCode, int prefix, int lineNum) {
        this.areaCode = rangeCheck(areaCode, 999, "area code");
        this.prefix   = rangeCheck(prefix,   999, "prefix");
        this.lineNum  = rangeCheck(lineNum, 9999, "line num");
    }

    private static short rangeCheck(int val, int max, String arg) {
        if (val < 0 || val > max)
            throw new IllegalArgumentException(arg + ": " + val);
        return (short) val;
    }

    @Override public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof PhoneNumber))
            return false;
        PhoneNumber pn = (PhoneNumber)o;
        return pn.lineNum == lineNum && pn.prefix == prefix
                && pn.areaCode == areaCode;
    }

    // hashCode 없이는 제대로 동작하지 않는다. 다음 셋 중 하나를 활성화하자.

//    // 코드 11-2 전형적인 hashCode 메서드 (70쪽)
//    @Override public int hashCode() {
//        int result = Short.hashCode(areaCode);
//        result = 31 * result + Short.hashCode(prefix);
//        result = 31 * result + Short.hashCode(lineNum);
//        return result;
//    }

//    // 코드 11-3 한 줄짜리 hashCode 메서드 - 성능이 살짝 아쉽다. (71쪽)
//    @Override public int hashCode() {
//        return Objects.hash(lineNum, prefix, areaCode);
//    }

//    // 해시코드를 지연 초기화하는 hashCode 메서드 - 스레드 안정성까지 고려해야 한다. (71쪽)
//    private int hashCode; // 자동으로 0으로 초기화된다.
//
//    @Override public int hashCode() {
//        int result = hashCode;
//        if (result == 0) {
//            result = Short.hashCode(areaCode);
//            result = 31 * result + Short.hashCode(prefix);
//            result = 31 * result + Short.hashCode(lineNum);
//            hashCode = result;
//        }
//        return result;
//    }

    public static void main(String[] args) {
        Map<PhoneNumber, String> m = new HashMap<>();
        m.put(new PhoneNumber(707, 867, 5309), "제니");
        System.out.println(m.get(new PhoneNumber(707, 867, 5309)));
    }
}
```

# 주의할 점

1. **성능을 높인답시고 해시코드를 계산할 때 핵심 필드를 생략해서는 안됩니다.**
    1. 속도는 빨라지겠지만, 해시 품질이 나빠져 해시 테이블의 성능을 심각하게 떨어뜨릴 수 있습니다.
2. **hashCode가 반환하는 값의 생성 규칙을 API 사용자에게 자세히 공표하지 말아야합니다.**
    1. 그래야 클라이언트가 이 값에 의지하지 않게 되고, 추후에 계산 방식을 바꿀 수도 있습니다.

---

# References::

이펙티브 자바 / 조슈아 블로크 지음 (프로그래밍 인사이트)