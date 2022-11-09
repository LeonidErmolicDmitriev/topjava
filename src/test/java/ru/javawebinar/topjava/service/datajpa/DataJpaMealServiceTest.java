package ru.javawebinar.topjava.service.datajpa;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.AbstractMealServiceTest;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.Profiles.DATAJPA;
import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles(DATAJPA)
public class DataJpaMealServiceTest extends AbstractMealServiceTest {
    @Test
    public void getWithUser() {
        Meal meal = service.getWithUser(ADMIN_MEAL_ID, ADMIN_ID);
        MEAL_MATCHER.assertMatch(meal, adminMeal1);
        USER_MATCHER.assertMatch(meal.getUser(), admin);
    }

    @Test
    public void getNotFoundWithMeals() {
        assertThrows(NotFoundException.class, () -> service.getWithUser(MealTestData.NOT_FOUND, USER_ID));
    }

    @Test
    public void getNotOwnWithMeals() {
        assertThrows(NotFoundException.class, () -> service.getWithUser(ADMIN_MEAL_ID, USER_ID));
    }
}
