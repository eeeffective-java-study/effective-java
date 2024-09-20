# Intro::

> 이펙티브 자바 정리본입니다.
> 

# 결론

- 열거 타입은 정수 상수보다 읽기 쉽고, 안전하며 강력합니다.
- 대다수 열거 타입이 명시적 생성자나 메서드 없이 쓰이지만, 각 상수를 특정 데이터와 연결짓거나 상수마다 다르게 동작하게 할 때는 필요합니다.
- 열거 타입 상수 일부가 같은 동작을 공유한다면 전략 열거 타입 패턴을 사용합시다.

# 왜 열거 타입을 사용해야 하는가?

```java
// 정수 열거 패턴
public static final int APPLE_FUJI = 0;
public static final int APPLE_PIPPIN = 0;
...
```

- 정수 열거 패턴
    - 타입 안전을 보장할 방법이 없습니다.
    - 컴파일러가 경고를 하지 않습니다.
- 문자열 열거 패턴
    - 상수의 의미를 출력할 수 있다는 점은 좋지만, 하드코딩을 유발합니다.
    - 런타임 버그를 유발합니다.

# 열거 타입

- 열거 타입 자체는 클래스입니다.
- 상수 하나당 자신의 인스턴스를 하나씩 만들어 public static final 필드로 공개합니다.
- 열거 타입은 생성자를 제공하지 않으므로 사실상 final 이며, 클라이언트가 인스턴스를 직접 생성하거나 확장할 수 없으니 인스턴스들은 딱 하나만 존재하도록 보장됩니다.
    - 싱글턴은 원소가 하나뿐인 열거 타입이라 할 수 있고, 반대로 열거타입은 싱글턴을 일반화한 형태라고 할 수 있습니다.
- 컴파일타임 타입 안전성을 제공합니다.
- 열거 타입에는 각자의 이름공간이 있어서 이름이 같은 상수도 공존가능합니다.
- 임의의 메서드나 필드를 추가할 수 있고, 임의의 인터페이스를 구현하게 할 수 있습니다.
- 열거 타입의 제거된 상수를 참조하는 클라이언트는 제거된 상수를 참조하는 줄에서 디버깅에 유용한 컴파일 오류를 확인할 수 있습니다.
- 열거 타입 상수는 생성자에서 자신의 인스턴스를 맵에 추가할 수 없습니다.
- 열거 타입의 정적 필드 중 열거 타입의 생성자에서 접근할 수 있는 것은 상수 변수뿐입니다.
- switch 문은 열거 타입의 상수별 동작을 구현하는데 적합하지 않지만, 기존 열거 타입에 상수별 동작을 혼합해 넣을 때는 좋은 선택이 될 수 있습니다.

```java
public enum Planet {
    MERCURY(3.302e+23, 2.439e6),
    VENUS  (4.869e+24, 6.052e6),
    EARTH  (5.975e+24, 6.378e6),
    MARS   (6.419e+23, 3.393e6),
    JUPITER(1.899e+27, 7.149e7),
    SATURN (5.685e+26, 6.027e7),
    URANUS (8.683e+25, 2.556e7),
    NEPTUNE(1.024e+26, 2.477e7);

    private final double mass;           // 질량(단위: 킬로그램)
    private final double radius;         // 반지름(단위: 미터)
    private final double surfaceGravity; // 표면중력(단위: m / s^2)

    // 중력상수(단위: m^3 / kg s^2)
    private static final double G = 6.67300E-11;

    // 생성자
    Planet(double mass, double radius) {
        this.mass = mass;
        this.radius = radius;
        surfaceGravity = G * mass / (radius * radius);
    }

    public double mass()           { return mass; }
    public double radius()         { return radius; }
    public double surfaceGravity() { return surfaceGravity; }

    public double surfaceWeight(double mass) {
        return mass * surfaceGravity;  // F = ma
    }
}
```

## 상수가 더 다양한 기능을 제공해줬으면 할 때

```java
// 코드 34-6 상수별 클래스 몸체(class body)와 데이터를 사용한 열거 타입 (215-216쪽)
public enum Operation {
		// 상수별 메서드 구
    PLUS("+") {
        public double apply(double x, double y) { return x + y; }
    },
    MINUS("-") {
        public double apply(double x, double y) { return x - y; }
    },
    TIMES("*") {
        public double apply(double x, double y) { return x * y; }
    },
    DIVIDE("/") {
        public double apply(double x, double y) { return x / y; }
    };

    private final String symbol;

    Operation(String symbol) { this.symbol = symbol; }

    @Override public String toString() { return symbol; }

    public abstract double apply(double x, double y);

    // 코드 34-7 열거 타입용 fromString 메서드 구현하기 (216쪽)
    private static final Map<String, Operation> stringToEnum =
            Stream.of(values()).collect(
                    toMap(Object::toString, e -> e));

    // 지정한 문자열에 해당하는 Operation을 (존재한다면) 반환한다.
    public static Optional<Operation> fromString(String symbol) {
        return Optional.ofNullable(stringToEnum.get(symbol));
    }
}
```

## 전략 열거 타입

```java

// 코드 34-9 전략 열거 타입 패턴 (218-219쪽)
enum PayrollDay {
    MONDAY(WEEKDAY), TUESDAY(WEEKDAY), WEDNESDAY(WEEKDAY),
    THURSDAY(WEEKDAY), FRIDAY(WEEKDAY),
    SATURDAY(WEEKEND), SUNDAY(WEEKEND);

    private final PayType payType;

    PayrollDay(PayType payType) { this.payType = payType; }
    
    int pay(int minutesWorked, int payRate) {
        return payType.pay(minutesWorked, payRate);
    }

    // 전략 열거 타입
    enum PayType {
        WEEKDAY {
            int overtimePay(int minsWorked, int payRate) {
                return minsWorked <= MINS_PER_SHIFT ? 0 :
                        (minsWorked - MINS_PER_SHIFT) * payRate / 2;
            }
        },
        WEEKEND {
            int overtimePay(int minsWorked, int payRate) {
                return minsWorked * payRate / 2;
            }
        };

        abstract int overtimePay(int mins, int payRate);
        private static final int MINS_PER_SHIFT = 8 * 60;

        int pay(int minsWorked, int payRate) {
            int basePay = minsWorked * payRate;
            return basePay + overtimePay(minsWorked, payRate);
        }
    }

    public static void main(String[] args) {
        for (PayrollDay day : values())
            System.out.printf("%-10s%d%n", day, day.pay(8 * 60, 1));
    }
}

```

# 그래서 열거 타입 언제 쓸까?

- 필요한 원소를 컴파일타임에 다 알 수 있는 상수집합이라면 항상 열거 타입을 사용하면 됩니다.
    - 열거 타입에 정의된 상수 개수가 영원히 고정 불변일 필요는 없습니다.


# References::

이펙티브 자바 / 조슈아 블로크 지음 (프로그래밍 인사이트)