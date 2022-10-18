package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        List<Meal> meals = MealsUtil.getInitialMeals();
        meals.forEach(meal -> save(meal, 1));
        meals = Arrays.asList(
                new Meal(LocalDateTime.of(2021, Month.JANUARY, 30, 11, 0), "Завтрак чемпиона", 750),
                new Meal(LocalDateTime.of(2021, Month.JANUARY, 30, 12, 0), "Обед чемпиона", 1200),
                new Meal(LocalDateTime.of(2021, Month.JANUARY, 30, 21, 0), "Легкий ужин", 200),
                new Meal(LocalDateTime.of(2021, Month.JANUARY, 31, 0, 0), "Ночной перекус на бульваре роз", 100),
                new Meal(LocalDateTime.of(2021, Month.JANUARY, 31, 11, 0), "Завтрак чемпиона", 1200),
                new Meal(LocalDateTime.of(2021, Month.JANUARY, 31, 12, 0), "Обед чемпиона", 750),
                new Meal(LocalDateTime.of(2021, Month.JANUARY, 31, 21, 0), "Ужин чемпиона", 250)
        );
        meals.forEach(meal -> save(meal, 2));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            getUserRepository(userId).put(meal.getId(), meal);
            return meal;
        }
        // handle case: update, but not present in storage
        return getUserRepository(userId).computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        return (getUserRepository(userId).remove(id) != null);
    }

    @Override
    public Meal get(int id, int userId) {
        return getUserRepository(userId).get(id);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return filteredByPredicate(meal -> true, userId);
    }

    @Override
    public List<Meal> getFiltered(int userId, LocalDateTime startDate, LocalDateTime endDate) {
        return filteredByPredicate(meal -> DateTimeUtil.isBetweenHalfOpen(meal.getDateTime(), startDate, endDate), userId);
    }

    private List<Meal> filteredByPredicate(Predicate<Meal> filter, int userId) {
        return getUserRepository(userId)
                .values()
                .stream()
                .filter(filter)
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }

    private Map<Integer, Meal> getUserRepository(int userId) {
        return repository.computeIfAbsent(userId, meals -> new ConcurrentHashMap<>());
    }
}

