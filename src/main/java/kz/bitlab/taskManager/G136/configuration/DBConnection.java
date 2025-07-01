package kz.bitlab.taskManager.G136.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Configuration
public class DBConnection {
    private Connection connection;

    public DBConnection() {
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5433/taskManagerG136?useUnicode=true&serverTimezone=UTC", "postgres", "qwerty");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Bean
    public Connection getConnection() {
        return connection;
    }


}
