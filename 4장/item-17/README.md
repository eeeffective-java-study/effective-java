# <변경 가능성을 최소화하라>
> ## 불변 클래스 :  인스턴스 내부 값을 수정할 수 없는 클래스
- 불변 인스턴스에 간직된 정보는 고정되어 객체가 파괴되는 순간까지 절대 달라지지 않는다.
- String, BigInteger, BigDecimal 등이 여기 속한다.
- 불변 클래스는 가변 클래스보다 설계하고 구현이 쉬우며, **오류가 생길 여지도 적고 훨씬 안전하다.**

> ## 클래스를 불변으로 만들기 위한 다섯 가지 규칙
  1. 객체의 상태를 변경하는 메서드를 제공하지 않는다.
  2. 클래스를 확장할 수 없도록 한다.
     - 하위 클래스에서 부주의하게, 객체의 상태를 변하게 만드는 사태를 막아준다.
  3. 모든 필드를 final로 선언한다
     -  새로 생성된 인스턴스를 동기화 없이 다른 스레드로 건네도 문제없이 동작하게끔 필요하다.
  4. 모든 필드를 private로 선언한다.
     - 필드가 참조하는 가변 객체를 클라이언트에서 직접 접근해 수정하는 일을 막는다.
  5. 자신 외에는 내부의 가변 컴포넌트에 접근할 수 없도록 한다.
     - 클래스에 가변 객체를 참조하는 필드 한개라도 있다면 클라이언트에서 그 객체의 참조를 얻을 수 없도록 한다.
  
  > ## <불변 객체의 예시>
  ```java
  package effectivejava.ex03.immutable;

import java.util.Objects;

public class Money {
    private final int amount;

    private Money(int amount) {
        this.amount = amount;
    }
    public static Money createMoney(int amount){
        return new Money(amount);
    }
    public Money plus(Money money){
        return new Money(amount + money.amount);
    }
    
    public Money minus(Money money){
        return new Money(this.amount - money.amount);
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return amount == money.amount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount);
    }
}
  ```
  - add, minus 등의 메서드는 인스턴스 자신을 수정하는 것이 아니라 새로운 Money 인스턴스를 만들어서 반환해준다.
  - 이처럼 피연산자에 함수를 적용해서 그 결과를 반환하지만, 피연산자 자체는 그대로인 프로그래밍 패턴을 함수형 프로그래밍이라고 한다.
  - 이와 달리, 절차적 혹은 

> ## 불변 클래스의 장점
- 가변 클래스보다 설계하고 구현하고 사용하기 쉽다.
- 불변 객체는 근본적으로 스레드 안전하기 때문에 따로 동기화할 필요 없다.
- 방어적 복사가 필요 없다. 아무리 복사해도 원본과 차이가 없기 때문에, 복사 자체의 큰 의미가 없다. 불변 클래스는 clone 이나 복사 생성자를 제공하지 않는 것이 옳다.