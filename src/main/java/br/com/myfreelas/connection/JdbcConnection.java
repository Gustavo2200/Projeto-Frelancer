package br.com.myfreelas.connection;

import java.sql.Connection;
import java.sql.DriverManager;

public class JdbcConnection {

    private static final String URL = "jdbc:postgresql://localhost:5432/Freelancer";
    private static final String USER = "postgres";
    private static final String PASSWORD = "1";

    public static Connection getConnection() {
        try{
            return DriverManager.getConnection(URL, USER, PASSWORD);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro na conexão", e);
        }
    }
}