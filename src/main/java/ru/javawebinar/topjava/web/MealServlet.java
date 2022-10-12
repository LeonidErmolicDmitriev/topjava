package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.repository.MealCrud;
import ru.javawebinar.topjava.repository.MemoryMealCrud;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final MealCrud mealCrud = new MemoryMealCrud();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getParameterMap().isEmpty()) {
            setListRequestAttributes(request);
            log.debug("forward to meals");
            request.getRequestDispatcher("/meals.jsp").forward(request, response);
        } else {
            switch (request.getParameter("func")) {
                case "add":
                    setMealRequestAttributes(request, new Meal(null, null, null, 0));
                    log.debug("forward to add meal");
                    request.getRequestDispatcher("/meal.jsp").forward(request, response);
                    break;
                case "update":
                    Meal meal = mealCrud.read(Integer.parseInt(request.getParameter("id")));
                    setMealRequestAttributes(request, meal);
                    log.debug("forward to add meal");
                    request.getRequestDispatcher("/meal.jsp").forward(request, response);
                    break;
                case "delete":
                    log.debug("delete meal");
                    mealCrud.delete(Integer.parseInt(request.getParameter("id")));
                default:
                    log.debug("redirect to meals");
                    response.sendRedirect("meals");
            }
        }
    }

    private void setListRequestAttributes(HttpServletRequest request) {
        List<MealTo> mealsTo = MealsUtil.allByStreams(mealCrud.getAll(), MealsUtil.CALORIES_PER_DAY);
        request.setAttribute("mealsTo", mealsTo);
        request.setAttribute("formatter", FORMATTER);
    }

    private void setMealRequestAttributes(HttpServletRequest request, Meal meal) {
        request.setAttribute("formatter", FORMATTER);
        request.setAttribute("meal", meal);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        LocalDateTime dateTime = LocalDateTime.parse(request.getParameter("dateTime"));
        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));
        if (request.getParameter("id").isEmpty()) {
            log.debug("create meal");
            mealCrud.create(new Meal(dateTime, description, calories));
        } else {
            Integer id = Integer.parseInt(request.getParameter("id"));
            log.debug("update meal");
            mealCrud.update(new Meal(id, dateTime, description, calories));
        }
        log.debug("forward to meals");
        response.sendRedirect("meals");
    }
}
