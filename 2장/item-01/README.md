# 생성자 대신 정적 팩터리 메서드를 고려하라

정적 팩터리 메서드 : 클래스의 인스턴스를 반환하는 단순한 정적 메서드

```java
public static Boolean valueOf(boolean b) {
    return b ? Boolean.TRUE : Boolean.FALSE;
}
```

### 이름을 가질 수 있다.

- 생성자에 넘기는 매개변수와 생성자 자체만으로는 반환될 객체의 특성을 제대로 설명하지 못함.

### 호출될 때마다 인스턴스를 새로 생성하지 않아도 된다.

### 반환 타입의 하위 타입 객체를 반환할 수 있는 능력이 있다.

### 입력 매개변수에 따라 매번 다른 클래스의 객체를 반환할 수 있다.

### 정적 팩터리 메서드를 작성하는 시점에는 반환할 객체의 클래스가 존재하지 않아도 된다.

- 서비스 제공자 프레임워크를 만드는 근간이 됨.
  - 서비스 인터페이스 : 구현체의 동작을 정의
  - 제공자 등록 API : 제공자가 구현체를 등록할 때 사용
  - 서비스 접근 API : 클라이언트가 서비스의 인스턴스를 얻을 때 사용
  - 서비스 제공자 인터페이스 : 서비스 구현체를 인스턴스로 만들 때 팩토리 역할


### ServiceLoader

- 자바6부터 지원되는 공용 서비스 제공자 프레임워크
- 서비스 제공자가 구현한 서비스 jar파일 내 META-INF/services 폴더에 서비스 인터페이스의 구현 위치를 등록해놓으면 자동으로 찾을 수 있는 look-up 메커니즘을 지원

1. 서비스 제공자는 제공자 인터페이스를 구현한다.
```java
public class MysqlDriver implements Driver {
    @Override
    public Connection connect(String url) {
        // 복잡한 커넥션 로직
        return new MysqlConnection();
    }

    @Override
    public String getName() {
        return "MysqlDriver";
    }

    @Override
    public boolean acceptsURL(String url) {
        return url.startsWith("jdbc:mysql:");
    }
}
```

2. 프레임워크의 look up 메커니즘에서 찾을 수 있도록 형식에 알맞는 file을 남김. ServiceLoader에서는 class path에 포함된 jar 파일의 META-INF/services 폴더를 순회하며 등록 가능한 구현체가 있는지 찾음.

```java
# META-INF/services/com.jdbc.driver.Driver 경로에 구현하고 배포하면
com.mysql.driver.MysqlDriver
```

3. 서비스 제공자 API에서 서비스 로더를 통해 새롭게 등록되는 DB를 자동으로 등록 함.
```java
public final class DriverManager {

    private DriverManager() {}
    private static final List<Driver> DRIVERS = new ArrayList<>();

    static {
        // 서비스 로더를 통해 구현체들을 등록
        ServiceLoader<Driver> loader = ServiceLoader.load(Driver.class);
        loader.forEach(driver -> DRIVERS.add(driver));
    }

    // 서비스 접근 API
    public static Connection getConnection(String url) {
        for (Driver driver : DRIVERS) {
            if (driver.acceptsURL(url)) {
                return driver.connect(url);
            }
        }
        throw new IllegalArgumentException("No suitable driver found for " + url);
    }

    // 제공자 등록 API
    public static void registerDriver(Driver driver) {
        DRIVERS.add(driver);
    }
}
```