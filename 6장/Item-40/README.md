# Intro::

> 이펙티브 자바 정리본입니다.
> 

# 결론

- 상위 클래스의 메서드를 재정의하려는 모든 메서드에는 @Override 를 습관화 합시다.
- 상위 클래스의 추상 메서드를 재정의한 경우에는 달지 않아도 상관없지만, 단다고 해서 해로울 것도 없습니다.

# 잘못 오버라이딩한 예시

```java
// 코드 40-1 영어 알파벳 2개로 구성된 문자열(바이그램)을 표현하는 클래스 - 버그를 찾아보자. (246쪽)
public class Bigram {
    private final char first;
    private final char second;

    public Bigram(char first, char second) {
        this.first  = first;
        this.second = second;
    }

    public boolean equals(Bigram b) {
        return b.first == first && b.second == second;
    }

    public int hashCode() {
        return 31 * first + second;
    }

    public static void main(String[] args) {
        Set<Bigram> s = new HashSet<>();
        for (int i = 0; i < 10; i++)
            for (char ch = 'a'; ch <= 'z'; ch++)
                s.add(new Bigram(ch, ch));
        System.out.println(s.size());
    }
}
```

- 위의 예시는 재정의를 의도하였지만, 실제로는 다중 정의가 되어버린 케이스입니다.
- @Override를 달고 컴파일하면 오류가 발생해 실수를 바로잡을 수 있습니다.

---

# References::

이펙티브 자바 / 조슈아 블로크 지음 (프로그래밍 인사이트)