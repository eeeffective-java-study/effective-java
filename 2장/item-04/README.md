# 인스턴스화를 막으려거든 private 생성자를 사용하라

### 정적 멤버만 담은 유틸리티 클래스에서 주로 활용함.
- java.util.Arrays 참고
- item-01 /src/framework/register_and_access/DriverManager 참고

### 추상 클래스로 만드는 것으로는 인스턴스화를 막을 수 없다.
- 하위 클래스를 만들어서 인스턴스화 할 수 있기 때문

### private 생성자를 추가해 클래스의 인스턴스화를 막을 수 있다.