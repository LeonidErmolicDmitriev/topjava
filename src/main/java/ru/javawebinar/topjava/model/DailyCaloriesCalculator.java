package ru.javawebinar.topjava.model;

public class DailyCaloriesCalculator {
    private int calories = 0;
    private boolean excess = false;
    private final int maxCalories;

    public DailyCaloriesCalculator(int maxCalories) {
        this.maxCalories = maxCalories;
    }

    public DailyCaloriesCalculator(boolean excess) {
        this.excess = excess;
        this.maxCalories = -1;
    }

    public void updateData(int calories) {
        addCalories(calories);
        updateValue();
    }

    private void addCalories(int calories) {
        this.calories += calories;
    }

    private void updateValue() {
        if (this.maxCalories >= 0) {
            this.excess = (calories > maxCalories);
        }
    }

    public boolean isExcess() {
        return excess;
    }
}
