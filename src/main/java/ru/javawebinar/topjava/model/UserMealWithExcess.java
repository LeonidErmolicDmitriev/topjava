package ru.javawebinar.topjava.model;

import java.time.LocalDateTime;

public class UserMealWithExcess {
    private final LocalDateTime dateTime;

    private final String description;

    private final int calories;
    private final DailyCaloriesCalculator calculator;

    public UserMealWithExcess(LocalDateTime dateTime, String description, int calories, boolean excess) {
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
        this.calculator = new DailyCaloriesCalculator(excess);
    }

    public UserMealWithExcess(LocalDateTime dateTime, String description, int calories, DailyCaloriesCalculator calculator) {
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
        this.calculator = calculator;
    }

    @Override
    public String toString() {
        return "UserMealWithExcess{" +
                "dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                ", excess=" + calculator.isExcess() +
                '}';
    }
}
