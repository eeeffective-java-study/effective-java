# 인터페이스는 타입을 정의하는 용도로만 사용하라

> -albe의 용도로 사용해라 (상수 정의용 인터페이스 같은 거 하지 마라)


## 이유
### 클래스 내부에서 사용하는 경우, 내부 구현이므로 API로 노출하면 안 됨
- 클라이언트가 이 상수를 직접적으로 사용할 경우, 상수에 종속됨
- 해당 상수가 필요 없어져도 다음 릴리스에 반영할 수 없음
### 모든 하위 클래스의 이름 공간이 그 인터페이스가 정의한 함수들로 오염됨
- 실제 물리적인 공간이 아니고, 해당 이름을 사용하지 못함을 뜻함
- ex. 
    ```java
    public interface A {
        int SUCCESS = 0;
        int ERROR = 1;
    }
    
    public interface B {
        int SUCCESS = 200;
        int FILE_NOT_FOUND = 404;
        int ACCESS_DENIED = 403;
    }
  
    public class Hello implements A, B {
        // 띠용 상황 발생
    }
    ```
  

## 그럼 어떻게 하냐면
### 연관된 클래스나 인터페이스 자체에 추가
### Enum 사용
### 유틸리티 클래스
- ex.
  ```java
    public class PhysicalConstants {
      private PhysicalConstants() { }
    
      public static final double AVOGADROS_NUMBER = 6.022_140_857e23;
    
      public static final double BOLTZMANN_CONST  = 1.380_648_52e-23;
    
      public static final double ELECTRON_MASS    = 9.109_383_56e-31;
      }
  ```
- 상수 모을 때, 개인적으로 final class 썼었습니다,,,