package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealCrud {
    void create(Meal meal);

    List<Meal> getAll();

    Meal read(int id);

    void update(Meal meal);

    void delete(int id);
}