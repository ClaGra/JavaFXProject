package com.example.javafxproject;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

public class Controller implements Initializable {

    @FXML
    private TextField inputCategoryRM;
    @FXML
    private TextField inputInstructionRM;
    @FXML
    private TextField inputRecipeRM;
    @FXML
    private TableView<Recipe> tableViewRM;
    @FXML
    private TableColumn<Recipe, Integer> idColumnRM;
    @FXML
    private TableColumn<Recipe, String> categoryColumnRM;
    @FXML
    private TableColumn<Recipe, String> instructionColumnRM;
    @FXML
    private TableColumn<Recipe, String> nameColumnRM;
    @FXML
    private TextField categorySearch;
    private ObservableList<Recipe> list;
    private int index = -1;

    // this code block is called when the controller is initialized
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateTable();
    }

    // this code block sets up the table columns and populates it with data
    public void updateTable() {
        idColumnRM.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumnRM.setCellValueFactory(new PropertyValueFactory<>("name"));
        categoryColumnRM.setCellValueFactory(new PropertyValueFactory<>("category"));
        instructionColumnRM.setCellValueFactory(new PropertyValueFactory<>("instruction"));

        list = FXCollections.observableList(DatabaseConnection.getRecipes());
        tableViewRM.setItems(list);

        categorySearch.textProperty().addListener((observable, oldValue, newValue) ->
                Platform.runLater(() -> tableViewRM.setItems(filterList(list, newValue))));
    }

    // this code block is used to check if a category contains the search text
    public boolean searchCategory(Recipe recipe, String searchText) {
        return recipe.getCategory().toLowerCase().contains(searchText.toLowerCase());
    }

    // this code block filters the list of recipes based on the search text
    private ObservableList<Recipe> filterList(List<Recipe> list, String searchText) {
        ArrayList<Recipe> filteredList = new ArrayList<>();
        for (Recipe recipe : list) {
            if (searchCategory(recipe, searchText)) {
                filteredList.add(recipe);
            }
        }
        return FXCollections.observableArrayList(filteredList);
    }

    // this code block updates the input fields with the details of the selected recipe
    @FXML
    public void getSelected() {
        index = tableViewRM.getSelectionModel().getSelectedIndex();
        if (index < 0) {
            return;
        }
        inputRecipeRM.setText(nameColumnRM.getCellData(index));
        inputCategoryRM.setText(categoryColumnRM.getCellData(index));
        inputInstructionRM.setText(instructionColumnRM.getCellData(index));
    }

    // this code block adds a new recipe to the database
    @FXML
    public void addRecipe() {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO recipe(name, category, instruction) VALUES (?, ?, ?)")) {
            preparedStatement.setString(1, inputRecipeRM.getText());
            preparedStatement.setString(2, inputCategoryRM.getText());
            preparedStatement.setString(3, inputInstructionRM.getText());
            preparedStatement.execute();
            updateTable();
        } catch (SQLException e) {
            System.out.println("Exception in adding the recipe to the database.");
        }
    }

    // this code block edits a recipe in the database
    @FXML
    public void editRecipe() {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("UPDATE recipe SET name = ?, category = ?, instruction = ? WHERE id = ?")) {
            preparedStatement.setString(1, inputRecipeRM.getText());
            preparedStatement.setString(2, inputCategoryRM.getText());
            preparedStatement.setString(3, inputInstructionRM.getText());
            preparedStatement.setInt(4, list.get(index).getId());
            preparedStatement.execute();
            updateTable();
        } catch (SQLException e) {
            System.out.println("Exception in updating the recipe in the database.");
        }
    }

    // this code block deletes a recipe from the database
    @FXML
    public void deleteRecipe() {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM recipe WHERE id = ?")) {
            preparedStatement.setInt(1, list.get(index).getId());
            preparedStatement.execute();
            updateTable();
        } catch (SQLException e) {
            System.out.println("Exception in deleting the recipe from the database.");
        }
    }
}
