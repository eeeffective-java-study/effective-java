# private 생성자나 열거 타입으로 싱글턴임을 보증하라

### 싱글턴
- 인스턴스를 오직 하나만 생성할 수 있는 클래스
- 무상태 객체, 혹은 설계상 유일해야 하는 시스템 컴포넌트

#### 클래스를 싱글턴으로 만들면 이를 사용하는 클라이언트를 테스트하기가 어려워질 수 있음.
- 인터페이스를 구현해서 만든 싱글턴이 아니면 mock 구현이 어려움.

```java
// public static final 필드 방식의 싱글턴
public class Elvis {
    public static final Elvis INSTANCE = new Elvis();
    
    private Elvis() {}
}
```
- private 생성자는 상수 필드인 Elvis.INSTANCE를 초기화 할 때 딱 한번만 호출됨.
- 리플렉션 API를 사용해 private 생성자를 호출하는게 아니라면 전체 시스템에서 인스턴스가 하나 뿐임이 보장됨.
- 해당 클래스가 싱글턴임이 API에 명백히 드러남.
- 간결함
```java
// 정적 팩터리 방식의 싱글턴
public class Elvis {
    private static final Elvis INSTANCE = new Elvis();
    
    private Elvis() {}
    
    public static Elvis getInstance() { 
        return INSTANCE;
    }
}
```
- API를 변경하지 않고 싱글턴이 아니게 변경할 수 있음.
- 호출하는 스레드별로 다른 인스턴스를 넘겨주게 할 수도 있음.
- 정적 팩터리를 제네릭 싱글턴 팩터리로 만들 수 있음.
- 정적 팩터리의 메서드 참조를 공급자(supplier)로 사용할 수 있음.

