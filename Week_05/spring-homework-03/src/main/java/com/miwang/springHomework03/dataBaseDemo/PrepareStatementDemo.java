package com.miwang.springHomework03.dataBaseDemo;

import com.miwang.springHomework03.commons.Constants;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * @author guozq
 * @date 2020-11-18-3:47 下午
 */
public class PrepareStatementDemo {
    public static void main(String[] args) throws Exception{
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection(Constants.jdbcUrl, Constants.jdbcUsername, Constants.jdbcPassword);

        // select
        String query = "SELECT * FROM p_account";
        PreparedStatement queryStatement = connection.prepareStatement(query);
        ResultSet resultSet = queryStatement.executeQuery(query);
        while (resultSet.next()) {
            String id = resultSet.getString("id");
            String name = resultSet.getString("name");
            System.out.println("id:" + id + ", name:" + name);
        }
        resultSet.close();

        // insert
        String insert = "INSERT INTO `spring_database`.`p_account`(`id`, `name`) VALUES ('2', '李四');";
        PreparedStatement insertStatement = connection.prepareStatement(insert);
        insertStatement.execute(insert);

        // update
        String update = "UPDATE `spring_database`.`p_account` SET `name` = '张三1' WHERE `id` = '1';";
        PreparedStatement updateStatement = connection.prepareStatement(update);
        updateStatement.executeUpdate(update);

        // delete
        String delete = "DELETE FROM `spring_database`.`p_account` WHERE `id` = '2';";
        PreparedStatement deleteStatement = connection.prepareStatement(delete);
        deleteStatement.executeUpdate(delete);
        connection.close();
    }
}
