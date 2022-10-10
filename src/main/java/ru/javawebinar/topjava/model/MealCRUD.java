package ru.javawebinar.topjava.model;

import java.time.LocalDateTime;
import java.util.List;

public interface MealCRUD {
    void create(LocalDateTime dateTime, String description, int calories);

    List<Meal> getList();

    Meal read(Integer id);

    void update(Integer id, LocalDateTime dateTime, String description, int calories);

    void delete(Integer id);
}
