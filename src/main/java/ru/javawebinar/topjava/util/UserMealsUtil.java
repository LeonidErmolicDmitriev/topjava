package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.CaloriesExcess;
import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

        mealsTo = filteredByCyclesOptional(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));

        System.out.println(filteredByStreamsOptional(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> calories = new HashMap<>();
        meals.forEach(meal -> calories.merge(meal.getDateTime().toLocalDate(), meal.getCalories(), Integer::sum));
        List<UserMealWithExcess> result = new ArrayList<>();
        meals.forEach(meal -> {
            LocalTime time = meal.getDateTime().toLocalTime();
            if ((time.isAfter(startTime) || time.equals(startTime)) && time.isBefore(endTime)) {
                result.add(new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(), calories.get(meal.getDateTime().toLocalDate()) > caloriesPerDay));
            }
        });
        return result;
    }

    public static List<UserMealWithExcess> filteredByCyclesOptional(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, CaloriesExcess> calories = new HashMap<>();
        List<UserMealWithExcess> result = new ArrayList<>();
        meals.forEach(meal -> {
            LocalTime time = meal.getDateTime().toLocalTime();
            LocalDate date = meal.getDateTime().toLocalDate();
            CaloriesExcess excess = calories.getOrDefault(date, new CaloriesExcess(caloriesPerDay));
            excess.updateData(meal.getCalories());
            calories.putIfAbsent(date, excess);
            if ((time.isAfter(startTime) || time.equals(startTime)) && time.isBefore(endTime)) {
                result.add(new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess));
            }
        });
        return result;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> calories = meals
                .stream()
                .collect(Collectors.groupingBy(
                        meal -> meal.getDateTime().toLocalDate(), Collectors.summingInt(UserMeal::getCalories)));
        return meals
                .stream()
                .filter(meal -> {
                    LocalTime time = meal.getDateTime().toLocalTime();
                    return (time.isAfter(startTime) || time.equals(startTime)) && time.isBefore(endTime);
                })
                .map(meal -> new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(), calories.get(meal.getDateTime().toLocalDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }

    public static List<UserMealWithExcess> filteredByStreamsOptional(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        return meals
                .stream()
                .collect(new Collector<UserMeal, List<UserMealWithExcess>, List<UserMealWithExcess>>() {
                    final HashMap<LocalDate, CaloriesExcess> map = new HashMap<>();

                    public Supplier<List<UserMealWithExcess>> supplier() {
                        return ArrayList::new;
                    }

                    public BiConsumer<List<UserMealWithExcess>, UserMeal> accumulator() {
                        return ((userMealWithExcess, meal) -> {
                            LocalDate date = meal.getDateTime().toLocalDate();
                            CaloriesExcess excess = map.getOrDefault(date, new CaloriesExcess(caloriesPerDay));
                            map.putIfAbsent(date, excess);
                            excess.updateData(meal.getCalories());
                            LocalTime time = meal.getDateTime().toLocalTime();
                            if ((time.isAfter(startTime) || time.equals(startTime)) && time.isBefore(endTime))
                                userMealWithExcess.add(new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess));
                        });
                    }

                    public BinaryOperator<List<UserMealWithExcess>> combiner() {
                        return (list1, list2) -> {
                            list1.addAll(list2);
                            return list1;
                        };
                    }

                    public Function<List<UserMealWithExcess>, List<UserMealWithExcess>> finisher() {
                        return Function.identity();
                    }

                    public Set<Collector.Characteristics> characteristics() {
                        return Collections.unmodifiableSet(EnumSet.of(Collector.Characteristics.CONCURRENT, Collector.Characteristics.IDENTITY_FINISH));
                    }

                });
    }
}
