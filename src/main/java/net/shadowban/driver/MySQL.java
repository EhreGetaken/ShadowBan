package net.shadowban.driver;

import java.sql.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class MySQL {

    public static String host;
    public static String user;
    public static String password;
    public static String database;
    public static String port;
    public static Connection con;
    public static Connection getConnection() {
        return con;
    }
    public static void connect() {
        if (con == null)
            try {
                Class.forName("com.mysql.jdbc.Driver");
                con = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?user=" +
                        user + "&password=" + password + "&autoReconnect=true");
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
    }
    public static void close()
    {
        if (con != null)
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }
    public static void update(String sql) {

        if(isConnected()) {
            new FutureTask<>(new Runnable() {

                @Override
                public void run() {
                    try {
                        Statement s = con.createStatement();
                        s.executeUpdate(sql);
                        s.close();
                    } catch (SQLException e) {
                        System.out.println("An error occured while executing mysql update (" + e.getMessage() + ")!");
                    }
                }
            }, 1).run();
        }
    }

    public static void createTable() {
        update("CREATE TABLE IF NOT EXISTS GuildAPI (GUILD VARCHAR(100), CHANNEL VARCHAR(100), RESULT VARCHAR(100), PRIMARY KEY (GUILD))");
        update("CREATE TABLE IF NOT EXISTS MemberAPI (MEMBER VARCHAR(100), FLAGGED VARCHAR(100), TYPE VARCHAR(100), PRIMARY KEY (MEMBER))");
    }

    public static void readMySQL()
    {

        user = "user";
        password = "password";
        database = "database";
        host = "localhost";
        port = "3306";
    }

    public static boolean isConnected() {
        if (con != null) {
            return true;
        } else {
            return false;
        }
    }

    public static ResultSet getResult(String qry) {
        if(isConnected()) {
            try {
                final FutureTask<ResultSet> task = new FutureTask<ResultSet>(new Callable<ResultSet>() {

                    PreparedStatement ps;

                    @Override
                    public ResultSet call() throws Exception {
                        ps = con.prepareStatement(qry);

                        return ps.executeQuery();
                    }
                });

                task.run();

                return task.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        } else {
            connect();
        }

        return null;
    }

}
