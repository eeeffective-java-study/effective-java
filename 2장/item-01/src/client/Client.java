package client;

import framework.register_and_access.DriverManager;
import framework.service_interface.Connection;

public class Client {
    public static void main(String[] args) {
        Client client = new Client();

        client.successExample();
        client.failExample();
    }

    private void successExample() {
        Connection connection = DriverManager.getConnection("jdbc:mysql:localhost:3306/my");

        connection.updateQuery("update query");
    }

    private void failExample() {
        Connection connection = DriverManager.getConnection("jdbc:postgres:localhost:3306/my");

        connection.updateQuery("update query");
    }
}
