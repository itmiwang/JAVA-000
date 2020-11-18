package com.miwang.springHomework03.dataBaseDemo;

import com.miwang.springHomework03.commons.Constants;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.pool.HikariPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * @author guozq
 * @date 2020-11-18-4:12 下午
 */
public class HikariDemo {
    public static void main(String[] args) throws Exception {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(Constants.jdbcUrl);
        hikariConfig.setUsername(Constants.jdbcUsername);
        hikariConfig.setPassword(Constants.jdbcPassword);
        hikariConfig.setMaximumPoolSize(10);
        hikariConfig.setMaxLifetime(1800000);
        hikariConfig.setIdleTimeout(600000);
        HikariDataSource ds = new HikariDataSource(hikariConfig);
        Connection connection = ds.getConnection();

        // select
        String query = "SELECT * FROM p_account";
        Statement queryStatement = connection.createStatement();
        ResultSet resultSet = queryStatement.executeQuery(query);
        while (resultSet.next()) {
            String id = resultSet.getString("id");
            String name = resultSet.getString("name");
            System.out.println("id:" + id + ", name:" + name);
        }
        resultSet.close();

        // insert
        String insert = "INSERT INTO `spring_database`.`p_account`(`id`, `name`) VALUES ('2', '李四');";
        Statement insertStatement = connection.createStatement();
        insertStatement.execute(insert);

        // update
        String update = "UPDATE `spring_database`.`p_account` SET `name` = '张三1' WHERE `id` = '1';";
        Statement updateStatement = connection.createStatement();
        updateStatement.executeUpdate(update);

        // delete
        String delete = "DELETE FROM `spring_database`.`p_account` WHERE `id` = '2';";
        Statement deleteStatement = connection.createStatement();
        deleteStatement.executeUpdate(delete);
        connection.close();
    }
}
