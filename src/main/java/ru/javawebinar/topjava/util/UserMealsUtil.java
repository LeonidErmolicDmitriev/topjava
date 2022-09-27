package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.DailyCaloriesCalculator;
import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
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
        Map<LocalDate, Integer> caloriesPerDayCalc = new HashMap<>();
        meals.forEach(meal -> caloriesPerDayCalc.merge(meal.getDateTime().toLocalDate(), meal.getCalories(), Integer::sum));
        List<UserMealWithExcess> result = new ArrayList<>();
        meals.forEach(meal -> {
            if (TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                boolean isCaloriesExceeded = (caloriesPerDayCalc.get(meal.getDateTime().toLocalDate()) > caloriesPerDay);
                result.add(new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(),
                        isCaloriesExceeded));
            }
        });
        return result;
    }

    public static List<UserMealWithExcess> filteredByCyclesOptional(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, DailyCaloriesCalculator> caloriesPerDayCalc = new HashMap<>();
        List<UserMealWithExcess> result = new ArrayList<>();
        meals.forEach(meal -> selectMealByTimeWithCaloriesCalculation(startTime, endTime, caloriesPerDay,
                caloriesPerDayCalc, result, meal));
        return result;
    }

    private static void selectMealByTimeWithCaloriesCalculation(LocalTime startTime, LocalTime endTime, int caloriesPerDay, Map<LocalDate, DailyCaloriesCalculator> caloriesPerDayCalc, List<UserMealWithExcess> result, UserMeal meal) {
        DailyCaloriesCalculator calculator = caloriesPerDayCalc.computeIfAbsent(meal.getDateTime().toLocalDate(),
                localDate -> new DailyCaloriesCalculator(caloriesPerDay));
        calculator.updateData(meal.getCalories());
        if (TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime)) {
            result.add(new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(), calculator));
        }
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesPerDayCalc = meals
                .stream()
                .collect(Collectors.groupingBy(
                        meal -> meal.getDateTime().toLocalDate(), Collectors.summingInt(UserMeal::getCalories)));
        return meals
                .stream()
                .filter(meal -> TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime))
                .map(meal -> new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(),
                        caloriesPerDayCalc.get(meal.getDateTime().toLocalDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }

    public static List<UserMealWithExcess> filteredByStreamsOptional(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        return meals
                .stream()
                .collect(new Collector<UserMeal, List<UserMealWithExcess>, List<UserMealWithExcess>>() {
                    final Map<LocalDate, DailyCaloriesCalculator> caloriesPerDayCalc = new ConcurrentHashMap<>();

                    public Supplier<List<UserMealWithExcess>> supplier() {
                        return ArrayList::new;
                    }

                    public BiConsumer<List<UserMealWithExcess>, UserMeal> accumulator() {
                        return ((result, meal) ->
                                selectMealByTimeWithCaloriesCalculation(startTime, endTime, caloriesPerDay,
                                        caloriesPerDayCalc, result, meal));
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
                        return Collections.unmodifiableSet(
                                EnumSet.of(Characteristics.UNORDERED, Collector.Characteristics.IDENTITY_FINISH));
                    }

                });
    }
}
