package mysql;


import framework.service_interface.Connection;

public class MysqlConnection implements Connection {
    @Override
    public void updateQuery(String query) {
        System.out.println("MySql 업데이트 로직 실행");
    }
}
