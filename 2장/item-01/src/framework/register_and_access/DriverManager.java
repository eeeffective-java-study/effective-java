package framework.register_and_access;

import framework.provider.Driver;
import framework.service_interface.Connection;
import mysql.MysqlDriver;

import java.util.ArrayList;
import java.util.List;

public class DriverManager {
    private static final List<Driver> DRIVERS = new ArrayList<>();

    static {
        // 여기서 기본 드라이버들 등록
        registerDriver(new MysqlDriver());
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
