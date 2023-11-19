package com.example.javafxproject;
import java.util.ArrayList;

public class Recipe {
    private int id;
    private String name;
    private String category;
    private String instruction;
    private ArrayList<Ingredient> ingredients;

    public Recipe(int id, String name, String category, String instruction) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.instruction = instruction;
        this.ingredients = new ArrayList<>();
    }

    // setters and getters
    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }
    public String getInstruction() {
        return instruction;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }
    public void setIngredient(String name, double quantity, String unit) {
        Ingredient ingredient = new Ingredient(name, quantity, unit);
        ingredients.add(ingredient);
    }
    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }
}
