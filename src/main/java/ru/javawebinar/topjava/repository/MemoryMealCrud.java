package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MemoryMealCrud implements MealCrud {

    private final Map<Integer, Meal> meals = new ConcurrentHashMap<>();

    private final AtomicInteger id = new AtomicInteger(0);

    public MemoryMealCrud() {
        List<Meal> mealsList = MealsUtil.getInitialMeals();
        for (Meal meal : mealsList) {
            create(new Meal(meal.getDateTime(), meal.getDescription(), meal.getCalories()));
        }
    }

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
        Meal oldMeal = meals.replace(meal.getId(), meal);
        return (oldMeal == null ? null : meal);
    }

    @Override
    public void delete(int id) {
        meals.remove(id);
    }
}
