package com.example.javafxproject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;

public class DatabaseConnection {

    public static Connection databaseLink;

    public static Connection getConnection() {
                try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            databaseLink = DriverManager.getConnection("jdbc:mysql://localhost:3306/recipemanager", "root", "5035");
        } catch (Exception e) {
        }
        return databaseLink;
    }

    public static ObservableList<Recipe> getRecipe(){
        Connection connection = getConnection();
        ObservableList<Recipe> list = FXCollections.observableArrayList();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("select * from recipe");
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                list.add(new Recipe(Integer.parseInt(resultSet.getString("id")), resultSet.getString("name"), resultSet.getString("category"), resultSet.getString("instruction")));
            }
        } catch (Exception e) {
        }
        return list;
    }
}