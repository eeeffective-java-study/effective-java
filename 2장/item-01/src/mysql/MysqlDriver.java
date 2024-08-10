package mysql;


import framework.provider.Driver;
import framework.service_interface.Connection;

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
