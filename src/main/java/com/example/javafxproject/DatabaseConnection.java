package com.example.javafxproject;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DatabaseConnection {

    // this code block establishes a connection to the database
    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/recipemanager", "root", "5035");
        } catch (ClassNotFoundException e) {
            System.out.println("Exception in loading JDBC driver.");
            e.printStackTrace();
            return null;
        }catch (SQLException e) {
            System.out.println("Exception in querying database.");
            e.printStackTrace();
            return null;
        }
    }

    // this code block retrieves a list of recipe objects from the database
    public static List<Recipe> getRecipes() {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM recipe");
             ResultSet resultSet = preparedStatement.executeQuery()) {

            ArrayList<Recipe> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add(new Recipe(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("category"),
                        resultSet.getString("instruction")
                ));
            }
            return list;

        } catch (SQLException e) {
            System.out.println("Exception in retrieving recipes from the database");
            return Collections.emptyList();
        }
    }
}
