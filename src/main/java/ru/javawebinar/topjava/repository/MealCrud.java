package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealCrud {
    Meal create(Meal meal);

    List<Meal> getAll();

    Meal read(int id);

    Meal update(Meal meal);

    void delete(int id);
}
