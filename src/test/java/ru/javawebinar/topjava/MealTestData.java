package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int MEAL_ID = START_SEQ + 17;
    public static final int USER_MEAL_ID = START_SEQ + 3;
    public static final int ADMIN_MEAL_ID = START_SEQ + 12;

    public static final Meal userMeal = new Meal(MEAL_ID, LocalDateTime.of(2022, Month.OCTOBER, 20, 13, 0),
            "T E S T", 15);
    public static final Meal adminMeal = new Meal(ADMIN_MEAL_ID, LocalDateTime.of(2021, Month.JANUARY, 30, 20, 0),
            "Ужин", 500);
    public static final Meal adminMeal31_1 = new Meal(START_SEQ + 13, LocalDateTime.of(2021, Month.JANUARY, 31, 0, 0),
            "Еда на граничное значение", 100);
    public static final Meal adminMeal31_2 = new Meal(START_SEQ + 14, LocalDateTime.of(2021, Month.JANUARY, 31, 10, 0),
            "Завтрак", 1000);
    public static final Meal adminMeal31_3 = new Meal(START_SEQ + 15, LocalDateTime.of(2021, Month.JANUARY, 31, 13, 0),
            "Обед", 500);
    public static final Meal adminMeal31_4 = new Meal(START_SEQ + 16, LocalDateTime.of(2021, Month.JANUARY, 31, 20, 0),
            "Ужин", 410);

    public static final List<Meal> adminMeals31 = Arrays.asList(
            adminMeal31_4,
            adminMeal31_3,
            adminMeal31_2,
            adminMeal31_1
    );
    public static final List<Meal> adminMeals = Arrays.asList(
            adminMeal31_4,
            adminMeal31_3,
            adminMeal31_2,
            adminMeal31_1,
            adminMeal,
            new Meal(START_SEQ + 11, LocalDateTime.of(2021, Month.JANUARY, 30, 13, 0), "Обед", 1000),
            new Meal(START_SEQ + 10, LocalDateTime.of(2021, Month.JANUARY, 30, 11, 0), "Завтрак", 500)
    );

    public static Meal getNew() {
        return new Meal(null, LocalDateTime.of(LocalDate.of(2022, Month.SEPTEMBER, 30), LocalTime.MIN), "Test", 1555);
    }

    public static Meal getUpdated() {
        Meal updated = new Meal(userMeal);
        updated.setDescription("t_e_s_t");
        updated.setCalories(51);
        updated.setDateTime(LocalDateTime.of(LocalDate.of(2022, Month.SEPTEMBER, 1), LocalTime.MIN));
        return updated;
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingRecursiveFieldByFieldElementComparator().isEqualTo(expected);
    }
}
