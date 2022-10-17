package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(meal -> save(meal, meal.getUserId()));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repository.put(meal.getId(), meal);
            return meal;
        }
        // handle case: update, but not present in storage
        return Objects.equals(repository.computeIfPresent(meal.getId(), (id, oldMeal) ->
                (oldMeal.getUserId() == userId) ? meal : oldMeal), meal) ? meal : null;
    }

    @Override
    public boolean delete(int id, int userId) {
        return repository.computeIfPresent(id, (mealId, meal) ->
                meal.getUserId() == userId ? null : meal) == null;
    }

    @Override
    public Meal get(int id, int userId) {
        Meal result = repository.get(id);
        return (result != null && result.getUserId() == userId) ? result : null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        return filteredByPredicate(meal -> meal.getUserId() == userId);
    }

    @Override
    public List<Meal> getFiltered(int userId, LocalDateTime startDate, LocalDateTime endDate) {
        return filteredByPredicate(meal -> meal.getUserId() == userId
                && DateTimeUtil.isBetweenHalfOpen(meal.getDateTime(), startDate, endDate));
    }

    private List<Meal> filteredByPredicate(Predicate<Meal> filter) {
        return repository
                .values()
                .stream()
                .filter(filter)
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }
}

