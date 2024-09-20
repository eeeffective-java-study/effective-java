# Intro::

> 이펙티브 자바 정리본입니다.
> 

# 결론

- ordinal을 쓰는 것은 일반적으로 좋지 않으니, 대신 EnumMap을 사용합시다.

# 예시 클래스

```java
class Plant {
    enum LifeCycle { ANNUAL, PERENNIAL, BIENNIAL }

    final String name;
    final LifeCycle lifeCycle;

    Plant(String name, LifeCycle lifeCycle) {
        this.name = name;
        this.lifeCycle = lifeCycle;
    }

    @Override public String toString() {
        return name;
    }
}
```

## ordinal 메서드로 인덱스를 얻는 코드

```java
public static void main(String[] args) {
    Plant[] garden = {
            new Plant("바질",    LifeCycle.ANNUAL),
            new Plant("캐러웨이", LifeCycle.BIENNIAL),
            new Plant("딜",      LifeCycle.ANNUAL),
            new Plant("라벤더",   LifeCycle.PERENNIAL),
            new Plant("파슬리",   LifeCycle.BIENNIAL),
            new Plant("로즈마리", LifeCycle.PERENNIAL)
    };

    Set<Plant>[] plantsByLifeCycleArr =
            (Set<Plant>[]) new Set[Plant.LifeCycle.values().length];
    for (int i = 0; i < plantsByLifeCycleArr.length; i++)
        plantsByLifeCycleArr[i] = new HashSet<>();
    for (Plant p : garden)
        plantsByLifeCycleArr[p.lifeCycle.ordinal()].add(p);
    // 결과 출력
    for (int i = 0; i < plantsByLifeCycleArr.length; i++) {
        System.out.printf("%s: %s%n",
                Plant.LifeCycle.values()[i], plantsByLifeCycleArr[i]);
    }
}
```

- 배열은 제네릭과 호환되지 않으니 비검사 형변환을 수행해야 하고 깔끔히 컴파일되지 않을 것입니다.
- 배열은 각 인덱스의 의미를 모르니 출력 결과에 직접 레이블을 달아야 합니다.
- 정확한 정숫값을 사용한다는 것을 직접 보증해야 합니다.

## EnumMap을 사용하는 방식

```java
public static void main(String[] args) {
    Plant[] garden = {
            new Plant("바질",    LifeCycle.ANNUAL),
            new Plant("캐러웨이", LifeCycle.BIENNIAL),
            new Plant("딜",      LifeCycle.ANNUAL),
            new Plant("라벤더",   LifeCycle.PERENNIAL),
            new Plant("파슬리",   LifeCycle.BIENNIAL),
            new Plant("로즈마리", LifeCycle.PERENNIAL)
    };

    Map<LifeCycle, Set<Plant>> plantsByLifeCycle =
            new EnumMap<>(Plant.LifeCycle.class);
    for (Plant.LifeCycle lc : Plant.LifeCycle.values())
        plantsByLifeCycle.put(lc, new HashSet<>());
    for (Plant p : garden)
        plantsByLifeCycle.get(p.lifeCycle).add(p);
    System.out.println(plantsByLifeCycle);
    
    // 코드 37-3 스트림을 사용한 코드 1 - EnumMap을 사용하지 않는다! (228쪽)
    System.out.println(Arrays.stream(garden)
            .collect(groupingBy(p -> p.lifeCycle)));

    // 코드 37-4 스트림을 사용한 코드 2 - EnumMap을 이용해 데이터와 열거 타입을 매핑했다. (228쪽)
    System.out.println(Arrays.stream(garden)
            .collect(groupingBy(p -> p.lifeCycle,
                    () -> new EnumMap<>(LifeCycle.class), toSet())));
}
```

- 배열은 실질적으로 열거 타입 상수를 값으로 매핑하는 일을 하기 때문에 Map을 사용할 수도 있을 것입니다.
- 배열 방식보다 더 짧고, 명료하고, 안전하며 성능도 비등합니다.
- 안전하게 형변환하고, 맵의 키인 열거 타입이 그 자체로 문자열을 제공하니 출력 결과에 직접 레이블을 달 일도 없습니다.
- 배열과는 다르게 인덱스 계산하는 과정에서의 오류가 날 가능성이 없습니다.
- groupingBy 메서드는 mapFactory 매개변수에 원하는 맵 구현체를 명시해 호출할 수 있습니다.

```java
// 코드 37-6 중첩 EnumMap으로 데이터와 열거 타입 쌍을 연결했다. (229-231쪽)
public enum Phase {
    SOLID, LIQUID, GAS;
    public enum Transition {
        MELT(SOLID, LIQUID), FREEZE(LIQUID, SOLID),
        BOIL(LIQUID, GAS), CONDENSE(GAS, LIQUID),
        SUBLIME(SOLID, GAS), DEPOSIT(GAS, SOLID);

//        // 코드 37-7 EnumMap 버전에 새로운 상태 추가하기 (231쪽)
//        SOLID, LIQUID, GAS, PLASMA;
//        public enum Transition {
//            MELT(SOLID, LIQUID), FREEZE(LIQUID, SOLID),
//            BOIL(LIQUID, GAS), CONDENSE(GAS, LIQUID),
//            SUBLIME(SOLID, GAS), DEPOSIT(GAS, SOLID),
//            IONIZE(GAS, PLASMA), DEIONIZE(PLASMA, GAS);

        private final Phase from;
        private final Phase to;
        Transition(Phase from, Phase to) {
            this.from = from;
            this.to = to;
        }

        // 상전이 맵을 초기화한다.
        private static final Map<Phase, Map<Phase, Transition>>
                m = Stream.of(values()).collect(groupingBy(t -> t.from,
                () -> new EnumMap<>(Phase.class),
                toMap(t -> t.to, t -> t,
                        (x, y) -> y, () -> new EnumMap<>(Phase.class))));
        
        public static Transition from(Phase from, Phase to) {
            return m.get(from).get(to);
        }
    }

    // 간단한 데모 프로그램 - 깔끔하지 못한 표를 출력한다.
    public static void main(String[] args) {
        for (Phase src : Phase.values()) {
            for (Phase dst : Phase.values()) {
                Transition transition = Transition.from(src, dst);
                if (transition != null)
                    System.out.printf("%s에서 %s로 : %s %n", src, dst, transition);
            }
        }
    }
}
```

---

# References::

이펙티브 자바 / 조슈아 블로크 지음 (프로그래밍 인사이트)