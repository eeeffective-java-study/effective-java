package framework.register_and_access;

import framework.provider.Driver;
import framework.service_interface.Connection;

import java.util.HashMap;
import java.util.Map;

public class DriverManagerUseReflection {
    private static final Map<String, Driver> drivers = new HashMap<>();

    public static Connection getConnection(String url) {
        String[] parts = url.split(":");
        if (parts.length < 2) {
            throw new IllegalArgumentException("Invalid URL format");
        }
        String protocol = parts[1];

        Driver driver = drivers.get(protocol);
        if (driver == null) {
            driver = loadDriver(protocol);
        }

        return driver.connect(url);
    }

    private static Driver loadDriver(String protocol) {
        String driverClassName = getDriverClassName(protocol);
        try {
            Class<?> driverClass = Class.forName(driverClassName);
            Driver driver = (Driver) driverClass.getDeclaredConstructor().newInstance();
            drivers.put(protocol, driver);
            return driver;
        } catch (Exception e) {
            throw new RuntimeException("Failed to load driver for " + protocol, e);
        }
    }

    private static String getDriverClassName(String protocol) {
        // 여기서는 간단히 프로토콜 이름으로 클래스 이름을 만들었지만,
        // 실제로는 설정 파일이나 다른 방식으로 매핑할 수 있음
        return "com.example.db." + protocol.substring(0, 1).toUpperCase() + protocol.substring(1) + "Driver";
    }
}
