package ru.javawebinar.topjava.model;

import java.time.LocalDateTime;

public class UserMealWithExcess {
    private final LocalDateTime dateTime;

    private final String description;

    private final int calories;

    private final boolean excess;

    private final CaloriesExcess caloriesExcess;

    public UserMealWithExcess(LocalDateTime dateTime, String description, int calories, boolean excess) {
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
        this.excess = excess;
        this.caloriesExcess = null;
    }

    public UserMealWithExcess(LocalDateTime dateTime, String description, int calories, CaloriesExcess caloriesExcess) {
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
        this.caloriesExcess = caloriesExcess;
        this.excess = false;
    }

    @Override
    public String toString() {
        return "UserMealWithExcess{" +
                "dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                ", excess=" + ((caloriesExcess==null)?excess:caloriesExcess.getValue()) +
                '}';
    }
    public LocalDateTime getDateTime(){
        return dateTime;
    }
}
