package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MealsUtil {

    public static final int CALORIES_PER_DAY = 2000;

    public static List<Meal> getInitialMeals() {
        return Arrays.asList(
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );
    }

    public static void main(String[] args) {
        List<Meal> meals = getInitialMeals();
        List<MealTo> mealsTo = filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), CALORIES_PER_DAY);
        mealsTo.forEach(System.out::println);
    }

    public static List<MealTo> allByStreams(List<Meal> meals, int caloriesPerDay) {
        return filteredByStreams(meals, null, null, caloriesPerDay);
    }

    public static List<MealTo> filteredByStreams(List<Meal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesSumByDate = meals.stream()
                .collect(
                        Collectors.groupingBy(Meal::getDate, Collectors.summingInt(Meal::getCalories))
                );
        return getStream(meals, startTime, endTime)
                .map(meal -> createTo(meal, caloriesSumByDate.get(meal.getDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }

    private static Stream<Meal> getStream(List<Meal> meals, LocalTime startTime, LocalTime endTime) {
        if (startTime == null && endTime == null) {
            return meals.stream();
        } else {
            LocalTime filterStartTime = (startTime == null) ? LocalTime.of(0, 0) : startTime;
            LocalTime filterEndTime = (endTime == null) ? LocalTime.of(23, 59, 59, 999) : endTime;
            return meals.stream()
                    .filter(meal -> TimeUtil.isBetweenHalfOpen(meal.getTime(), filterStartTime, filterEndTime));
        }
    }

    private static MealTo createTo(Meal meal, boolean excess) {
        return new MealTo(meal.getId(), meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess);
    }
}
