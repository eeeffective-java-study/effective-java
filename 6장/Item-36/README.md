# Intro::

> 이펙티브 자바 정리본입니다.
> 

# 결론

- 열거할 수 있는 타입을 한데 모아 집합 형태로 사용한다고 해도 비트 필드를 사용할 이유는 없다.
- EnumSet 클래스가 비트 필드 수준의 명료함과 성능을 제공하고 열거 타입의 장점까지 선사합니다.
- EnumSet의 유일한 단점은 불변 EnumSet을 만들 수 없다는 점입니다.
    - EnumSet의 장점을 포기하고 Collections.unmodifiableSet으로 EnumSet을 감싸 사용할 수는 있습니다.

# 비트 필드 열거 상수

```java
public class Text {
    public static final int STYLE_BOLD = 1 << 0;
    public static final int STYLE_ITALIC = 1 << 1;
    public static final int STYLE_UNDERLINE = 1 << 2;
    public static final int STYLE_STRIKETHROUGH = 1 << 3;

    public void applyStyles(int styles) { ... }
}
```

## 비트 필드 열거 상수의 특징

- 비트 필드를 사용하면 집합 연산을 효율적으로 수행할 수 있지만, 정수 열거 상수의 단점을 그대로 지닙니다.
- 비트 필드 값은 단순한 정수 열거 상수를 출력할 때보다 해석하기가 어렵습니다.
- 비트 필드 하나에 녹아 있는 모든 원소를 순회하기도 까다롭습니다.
- 최대 몇 비트가 필요한지를 API 작성 시 미리 예측하여 적절한 타입을 선택해야 합니다.

# 열거 타입 상수 집합 EnumSet

- 내부가 비트 벡터로 구현되어있습니다.
- 비트 필드와 비견되는 성능을 보여줍니다.
- 비트를 직접 다룰 때 흔히 겪는 오류들에서 벗어날 수 있습니다.

```java
public class Text {
    public enum Style {BOLD, ITALIC, UNDERLINE, STRIKETHROUGH}

    // 어떤 Set을 넘겨도 되나, EnumSet이 가장 좋다.
    public void applyStyles(Set<Style> styles) {
        System.out.printf("Applying styles %s to text%n",
                Objects.requireNonNull(styles));
    }

    // 사용 예
    public static void main(String[] args) {
        Text text = new Text();
        text.applyStyles(EnumSet.of(Style.BOLD, Style.ITALIC));
    }
}
```

---

# References::

이펙티브 자바 / 조슈아 블로크 지음 (프로그래밍 인사이트)