# Intro::

> 이펙티브 자바 정리본입니다.
> 

# 재정의하지 않는 것이 좋은 상황

- **각 인스턴스가 본질적으로 고유한 경우**
    - 클래스가 단순히 데이터를 저장하기 위한 구조가 아니라, 특정 동작이나 작업 수행을 목적으로 설계된 경우. 이 경우 객체의 행위가 동일하더라도, 객체를 비교하는 것은 적절하지 않을 수 있습니다.
    - 예를 들어, Thread 클래스는 특정 작업을 수행하기 위해 사용되므로, 각 Thread 객체는 본질적으로 고유하며, 행위가 같더라도 인스턴스 간의 비교는 적절하지 않습니다.
- **인스턴스의 논리적 동치성을 검사할 일이 없는 경우**
    - 쉽게 말해 굳이 비교할 이유가 없는 경우라고 생각해도 된다.
- **상위 클래스에서 재정의한 `equals`가 하위 클래스에도 딱 들어맞는 경우.**
    - 대부분의 Set 구현체는 상위 클래스가 구현한 `equals` 를 상속받아 사용합니다.
- **클래스가 private이거나 package-private이고 `equals` 메서드를 호출할 일이 없다는 경우**

## 그럼 언제 재정의 해야하는가?

> 객체 식별성(두 객체가 물리적으로 같은가)이 아니라 논리적 동치성을 확인해야 하는데, 상위 클래스의 equals가 논리적 동치성을 비교하도록 재정의되지 않았을 때 equals를 재정의 해야합니다.
> 

# Object 명세 규약

## 반사성(reflexivity)

> null이 아닌 모든 참조 값 x에 대해, x.equals(x)는 true 입니다.
`a = a`
> 

## 대칭성(symmetry)

> null이 아닌 모든 참조 값 x, y에 대해, x.equals(y)가 true면 y.equals(x)도 true 입니다.
`a = b → b = a`
> 

```java
// 코드 10-1 잘못된 코드 - 대칭성 위배! (54-55쪽)
public final class CaseInsensitiveString {
    private final String s;

    public CaseInsensitiveString(String s) {
        this.s = Objects.requireNonNull(s);
    }

    // 대칭성 위배!
    @Override public boolean equals(Object o) {
        if (o instanceof CaseInsensitiveString)
            return s.equalsIgnoreCase(
                    ((CaseInsensitiveString) o).s);
        if (o instanceof String)  // 한 방향으로만 작동한다!
            return s.equalsIgnoreCase((String) o);
        return false;
    }
//    // 수정한 equals 메서드 (56쪽)
//    @Override public boolean equals(Object o) {
//        return o instanceof CaseInsensitiveString &&
//                ((CaseInsensitiveString) o).s.equalsIgnoreCase(s);
//    }

    // 문제 시연 (55쪽)
    public static void main(String[] args) {
        CaseInsensitiveString cis = new CaseInsensitiveString("Polish");
        String s = "polish";

        List<CaseInsensitiveString> list = new ArrayList<>();
        list.add(cis);

        System.out.println(list.contains(s));
    }
}
```

위의 예시에서 `CaseInsensitiveString.equals(String) = true` 지만 `String.equals(CaseInsensitiveString) = false`이기 때문에 대칭성이 위배된다. 이는 `CaseInsensitiveString`을 `String`과도 연동하겠다는 과욕에서 생기는 문제이고 이는 버려야 하는 생각이다.

## 추이성(transitivity)

> null이 아닌 모든 참조 값 x, y, z에 대해, x.equals(y)가 true이고 y.equals(z)도 true이면 x.equals(z)도 true 입니다.
`a = b ∧ b = c → a = c`
> 

## 일관성(consistency)

> null이 아닌 모든 참조 값 x, y에 대해, x.equals(y)를 반복해서 호출하면 항상 값이 일관되어야 합니다.
> 

두 객체가 같다면 앞으로도 영원히 같아야 합니다. 가변 객체는 비교 시점에 따라 서로 다를 수도 있는 반면, 불변 객체는 한번 다르면 끝까지 달라야 합니다.

특히 클래스가 불변이든 가변이든 equals의 판단에 신뢰할 수 없는 자원이 끼어들게 해서는 안됩니다.

## null-아님

> null이 아닌 모든 참조 값 x에 대해, x.equals(null)은 false 입니다.
> 

instanceOf는 (두 번째 피연산자와 무관하게) 첫 번째 피연산자가 null이면 false를 반환하기 때문에 입력이 null이면 타입 확인 단계에서 false를 반환하기 때문에 명시적으로 null 검사를 하지 않아도 됩니다.

## 상위 클래스에 없는 필드값을 추가한 상황

### 대칭성 위배

```java
// 단순한 불변 2차원 정수 점(point) 클래스 (56쪽)
public class Point {
    private final int x;
    private final int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override public boolean equals(Object o) {
        if (!(o instanceof Point))
            return false;
        Point p = (Point)o;
        return p.x == x && p.y == y;
    }
}
```

```java
// Point에 값 컴포넌트(color)를 추가 (56쪽)
public class ColorPoint extends Point {
    private final Color color;

    public ColorPoint(int x, int y, Color color) {
        super(x, y);
        this.color = color;
    }

    // 코드 10-2 잘못된 코드 - 대칭성 위배! (57쪽)
    @Override public boolean equals(Object o) {
        if (!(o instanceof ColorPoint))
            return false;
        return super.equals(o) && ((ColorPoint) o).color == color;
    }
}
```

```java
  public static void main(String[] args) {
      // 첫 번째 equals 메서드(코드 10-2)는 대칭성을 위배한다. (57쪽)
      // p.equals(cp) = true 이지만 cp.equals(p) = false 이다.
      Point p = new Point(1, 2);
      ColorPoint cp = new ColorPoint(1, 2, Color.RED);
      System.out.println(p.equals(cp) + " " + cp.equals(p));
  }
```

### 추이성 위배

```java
// 단순한 불변 2차원 정수 점(point) 클래스 (56쪽)
public class Point {
    private final int x;
    private final int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override public boolean equals(Object o) {
        if (!(o instanceof Point))
            return false;
        Point p = (Point)o;
        return p.x == x && p.y == y;
    }
}
```

```java
// Point에 값 컴포넌트(color)를 추가 (56쪽)
public class ColorPoint extends Point {
    private final Color color;

    public ColorPoint(int x, int y, Color color) {
        super(x, y);
        this.color = color;
    }

    // 코드 10-3 잘못된 코드 - 추이성 위배! (57쪽)
    @Override public boolean equals(Object o) {
        if (!(o instanceof Point))
            return false;

        // o가 일반 Point면 색상을 무시하고 비교한다.
        if (!(o instanceof ColorPoint))
            return o.equals(this);

        // o가 ColorPoint면 색상까지 비교한다.
        return super.equals(o) && ((ColorPoint) o).color == color;
    }
}
```

```java
public static void main(String[] args) {
      // 첫 번째 equals 메서드(코드 10-2)는 대칭성을 위배한다. (57쪽)
      // p.equals(cp) = true 이지만 cp.equals(p) = false 이다.
      Point p = new Point(1, 2);
      ColorPoint cp = new ColorPoint(1, 2, Color.RED);
      System.out.println(p.equals(cp) + " " + cp.equals(p));

      // 두 번째 equals 메서드(코드 10-3)는 추이성을 위배한다. (57쪽)
      // p1.equals(p2) = true, p2.equals(p3) = true, p1.equals(p3) = false
      // a = b = c 가 만족하지 않는다.
      ColorPoint p1 = new ColorPoint(1, 2, Color.RED);
      Point p2 = new Point(1, 2);
      ColorPoint p3 = new ColorPoint(1, 2, Color.BLUE);
      System.out.printf("%s %s %s%n", p1.equals(p2), p2.equals(p3), p1.equals(p3));
  }
```

### 리스코프 치환 원칙 위배

```java
// 단순한 불변 2차원 정수 점(point) 클래스 (56쪽)
public class Point {
    private final int x;
    private final int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // 코드 10-4 잘못된 코드 - 리스코프 치환 원칙 위배! (59쪽)
    // 같은 구현 클래스인 경우와 비교할 때만 true를 반환하기 때문에 괜찮아 보입니다.
    // 하지만 Point의 하위 클래스는 어디서든 Point로써 활용될 수 있어야 합니다.
    @Override public boolean equals(Object o) {
        if (o == null || o.getClass() != getClass()) {
            return false;
        }
        Point p = (Point) o;
        return p.x == x && p.y == y;
    }
}
```

```java
// Point의 평범한 하위 클래스 - 값 컴포넌트를 추가하지 않았다. (59쪽)
public class CounterPoint extends Point {
    private static final AtomicInteger counter = new AtomicInteger();

    public CounterPoint(int x, int y) {
        super(x, y);
        counter.incrementAndGet();
    }
    public static int numberCreated() { return counter.get(); }
}
```

```java
public class CounterPointTest {
    // 단위 원 안의 모든 점을 포함하도록 unitCircle을 초기화한다. (58쪽)
    private static final Set<Point> unitCircle = Set.of(
            new Point( 1,  0), new Point( 0,  1),
            new Point(-1,  0), new Point( 0, -1));

    public static boolean onUnitCircle(Point p) {
        return unitCircle.contains(p);
    }

    public static void main(String[] args) {
        Point p1 = new Point(1,  0);
        Point p2 = new CounterPoint(1,  0);

        // true를 출력한다.
        System.out.println(onUnitCircle(p1));

        // true를 출력해야 하지만, Point의 equals가 getClass를 사용해 작성되었다면
        // 클래스 자체가 다르기 때문에 그렇지 않다.
        // 리스코프 치환 원칙에 따르면, 어떤 타입에 있어 중요한 속성이라면 그 하위
        // 타입에서도 똑같이 잘 작동해야한다.
        System.out.println(onUnitCircle(p2));
    }
}
```

결국 구체 클래스의 하위 클래스에서 값을 추가할 방법은 없지만 우회 방법을 통해 구현 가능하다. 이는 상속 대신 컴포지션을 사용하는 방식이다.

```java
// 코드 10-5 equals 규약을 지키면서 값 추가하기 (60쪽)
public class ColorPoint {
    private final Point point;
    private final Color color;

    public ColorPoint(int x, int y, Color color) {
        point = new Point(x, y);
        this.color = Objects.requireNonNull(color);
    }

    /**
     * 이 ColorPoint의 Point 뷰를 반환한다.
     */
    public Point asPoint() {
        return point;
    }

    @Override public boolean equals(Object o) {
        if (!(o instanceof ColorPoint))
            return false;
        ColorPoint cp = (ColorPoint) o;
        return cp.point.equals(point) && cp.color.equals(color);
    }

    @Override public int hashCode() {
        return 31 * point.hashCode() + color.hashCode();
    }
}
```

# 양질의 equals 메서드 구현 방법

1. `==` 연산자를 사용해 입력이 자기 자신의 참조인지 확인합니다.
2. `instanceOf` 연산자로 입력이 올바른 타입인지 확인합니다.
3. 입력을 올바른 타입으로 형변환합니다.
4. 입력 객체와 자기 자신의 대응되는 핵심 필드들이 모두 일치하는지 하나씩 검사합니다.

`equals`를 다 구현했다면 대칭적인지, 추이성이 있는지, 일관적인지 체크를 해보자. 물론 나머지 요인인 반사성과 `null-아님`도 만족해야 하지만, 이 둘이 문제되는 경우는 별로 없다.

```java
// 코드 10-6 전형적인 equals 메서드의 예 (64쪽)
public final class PhoneNumber {
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

    @Override public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof PhoneNumber))
            return false;
        PhoneNumber pn = (PhoneNumber)o;
        return pn.lineNum == lineNum && pn.prefix == prefix
                && pn.areaCode == areaCode;
    }

    // 나머지 코드는 생략 - hashCode 메서드는 꼭 필요하다(아이템 11)!
}
```

# 주의사항

- `equals`를 재정의할 땐 `hashCode`도 반드시 재정의해야합니다.
- 너무 복잡하게 해결하려 들지 말아야 합니다. 필드들의 동치성만 검사해도 `equals` 규약을 어렵지 않게 지킬 수 있습니다.
- `Object` 외의 타입을 매개변수로 받는 `equals` 메서드는 선언하지 말아야 합니다.

# 참고

equals(hashCode도 마찬가지)를 작성하고 테스트하는 일은 반복적인 일이기 때문에 대신해줄 오픈소스인 AutoValue를 사용해도 된다.

---

# References::

이펙티브 자바 / 조슈아 블로크 지음 (프로그래밍 인사이트)