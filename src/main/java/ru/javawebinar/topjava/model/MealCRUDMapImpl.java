package ru.javawebinar.topjava.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MealCRUDMapImpl implements MealCRUD {

    private final Map<Integer, Meal> meals = new ConcurrentHashMap<>();

    private final AtomicInteger id = new AtomicInteger(0);

    public MealCRUDMapImpl(List<Meal> mealsList) {
        for (Meal meal : mealsList) {
            create(meal.getDateTime(), meal.getDescription(), meal.getCalories());
        }
    }

    @Override
    public void create(LocalDateTime dateTime, String description, int calories) {
        Integer currentId = id.getAndIncrement();
        meals.put(currentId, new Meal(dateTime, description, calories, currentId));
        meals.get(currentId);
    }

    @Override
    public List<Meal> getList() {
        return new ArrayList<>(meals.values());
    }

    @Override
    public Meal read(Integer id) {
        return meals.get(id);
    }

    @Override
    public void update(Integer id, LocalDateTime dateTime, String description, int calories) {
        Meal meal = meals.get(id);
        if (meal == null) {
            return;
        }
        meal.setDateTime(dateTime);
        meal.setDescription(description);
        meal.setCalories(calories);
    }

    @Override
    public void delete(Integer id) {
        Meal meal = meals.get(id);
        if (meal == null) {
            return;
        }
        meals.remove(id);
    }
}
