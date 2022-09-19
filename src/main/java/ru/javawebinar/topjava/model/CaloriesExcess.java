package ru.javawebinar.topjava.model;

public class CaloriesExcess {
    private int calories = 0;
    private boolean value = false;
    private final int maxCalories;

    public CaloriesExcess(int maxCalories){
        this.maxCalories = maxCalories;
    }

    public void updateData(int calories){
        addCalories(calories);
        updateValue();
    }

    private void addCalories(int calories) {
        this.calories += calories;
    }

    private void updateValue() {
        this.value = (calories>maxCalories);
    }

    public boolean getValue() {
        return value;
    }
}
