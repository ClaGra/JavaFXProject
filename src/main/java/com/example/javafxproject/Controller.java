package com.example.javafxproject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

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

    ObservableList<Recipe> list;

    int index = -1;

    Connection connection = null; // null = undefined
    PreparedStatement preparedStatement = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateTable();
    }

    public void updateTable(){
        idColumnRM.setCellValueFactory(new PropertyValueFactory<Recipe, Integer>("id"));
        nameColumnRM.setCellValueFactory(new PropertyValueFactory<Recipe, String>("name"));
        categoryColumnRM.setCellValueFactory(new PropertyValueFactory<Recipe, String>("category"));
        instructionColumnRM.setCellValueFactory(new PropertyValueFactory<Recipe, String>("instruction"));

        list = DatabaseConnection.getRecipe();
        tableViewRM.setItems(list);

        categorySearch.textProperty().addListener((observable, oldValue, newValue) ->
                tableViewRM.setItems(filterList(list, newValue)));
    }

    public boolean searchCategory(Recipe recipe, String searchText){
        return (recipe.getCategory().toLowerCase().contains(searchText.toLowerCase()));}

    private ObservableList<Recipe> filterList(List<Recipe> list, String searchText) {
        List<Recipe> filteredList = new ArrayList<>();
        for (Recipe recipe : list) {
            if (searchCategory(recipe, searchText)) filteredList.add(recipe);
        }
        return FXCollections.observableList(filteredList);
    }

    @FXML
    public void getSelected(MouseEvent event) {
        index = tableViewRM.getSelectionModel().getSelectedIndex();
        if (index <= -1){
            return;
        }
        inputRecipeRM.setText(nameColumnRM.getCellData(index).toString());
        inputCategoryRM.setText(categoryColumnRM.getCellData(index).toString());
        inputInstructionRM.setText(instructionColumnRM.getCellData(index).toString());
    }

    @FXML
    public void addRecipe(MouseEvent event) {
        connection = DatabaseConnection.getConnection();
        try{
            preparedStatement = connection.prepareStatement("insert into recipe(name,category,instruction)values(?,?,?)");
            preparedStatement.setString(1, inputRecipeRM.getText());
            preparedStatement.setString(2, inputCategoryRM.getText());
            preparedStatement.setString(3, inputInstructionRM.getText());
            preparedStatement.execute();
            updateTable();
        }catch (Exception e){
        }
    }

    @FXML
    public void editRecipe(MouseEvent event) {
        connection = DatabaseConnection.getConnection();
        // try means happy path (optimistically everything in these {} should be done
        try{
            preparedStatement = connection.prepareStatement("update recipe set name= ?, category= ?, instruction= ? where id= ?");
            preparedStatement.setString(1, inputRecipeRM.getText());
            preparedStatement.setString(2, inputCategoryRM.getText());
            preparedStatement.setString(3, inputInstructionRM.getText());
            preparedStatement.setInt(4, Integer.parseInt(String.valueOf(tableViewRM.getItems().get(tableViewRM.getSelectionModel().getSelectedIndex()).getId())));
            preparedStatement.execute();
            updateTable();
        // catch means if an exception occurs do this
        }catch (Exception e){
        }
    }

    @FXML
    public void deleteRecipe(MouseEvent event) {
        connection = DatabaseConnection.getConnection();
        try{
            preparedStatement = connection.prepareStatement("delete from recipe where id= ?");
            preparedStatement.setInt(1, Integer.parseInt(String.valueOf(tableViewRM.getItems().get(tableViewRM.getSelectionModel().getSelectedIndex()).getId())));
            preparedStatement.execute();
            updateTable();
        }catch (Exception e){
        }
    }

}

