# Intro::

> 이펙티브 자바 정리본입니다.
> 

# 결론

- ordinal 메서드를 사용해서 열거 타입의 몇 번째 위치인지 확인하지 말아야 합니다.
- 값의 중복과 더미 상수를 만들어야 하는 등, 다양한 문제점을 유발합니다.
- oridinal 메서드 대신 인스턴스 필드를 사용하면 됩니다.

```java
// 인스턴스 필드에 정수 데이터를 저장하는 열거 타입 (222쪽)
public enum Ensemble {
    SOLO(1), DUET(2), TRIO(3), QUARTET(4), QUINTET(5),
    SEXTET(6), SEPTET(7), OCTET(8), DOUBLE_QUARTET(8),
    NONET(9), DECTET(10), TRIPLE_QUARTET(12);

    private final int numberOfMusicians;
    Ensemble(int size) { this.numberOfMusicians = size; }
    public int numberOfMusicians() { return numberOfMusicians; }
}
```

---

# References::

이펙티브 자바 / 조슈아 블로크 지음 (프로그래밍 인사이트)