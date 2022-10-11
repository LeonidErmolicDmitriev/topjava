package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MemoryMealCrud implements MealCrud {

    private final Map<Integer, Meal> meals = new ConcurrentHashMap<>();

    private final AtomicInteger id = new AtomicInteger(0);

    @Override
    public Meal create(Meal meal) {
        meal.setId(id.getAndIncrement());
        return meals.put(meal.getId(), meal);
    }

    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(meals.values());
    }

    @Override
    public Meal read(int id) {
        return meals.get(id);
    }

    @Override
    public Meal update(Meal meal) {
        return meals.replace(meal.getId(), meal);
    }

    @Override
    public void delete(int id) {
        meals.remove(id);
    }
}
