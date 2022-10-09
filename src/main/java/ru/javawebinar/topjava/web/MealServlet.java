package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
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

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getParameterMap().isEmpty()) {
            setListRequestAttributes(request);
            log.debug("forward to meals");
            request.getRequestDispatcher("/meals.jsp").forward(request, response);
        } else {
            switch (request.getParameter("func")) {
                case "add":
                    setMealRequestAttributes(request, LocalDateTime.now(), "", 0, 0);
                    log.debug("forward to add meal");
                    request.getRequestDispatcher("/meal.jsp").forward(request, response);
                    break;
                case "update":
                    Meal meal = MealsUtil.getMealCRUD().read(Integer.valueOf(request.getParameter("id")));
                    setMealRequestAttributes(request, meal.getDateTime(), meal.getDescription(), meal.getCalories(),
                            meal.getId());
                    log.debug("forward to add meal");
                    request.getRequestDispatcher("/meal.jsp").forward(request, response);
                    break;
                case "delete":
                    log.debug("delete meal");
                    MealsUtil.getMealCRUD().delete(Integer.valueOf(request.getParameter("id")));
                default:
                    log.debug("redirect to meals");
                    response.sendRedirect("meals");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        if (request.getParameter("command").equals("save")) {
            int id = Integer.parseInt(request.getParameter("id"));
            LocalDateTime dateTime;
            try {
                dateTime = LocalDateTime.parse(request.getParameter("dateTime"));
            } catch (Exception e) {
                dateTime = LocalDateTime.now();
            }
            String description = request.getParameter("description");
            int calories = Integer.parseInt(request.getParameter("calories"));
            if (id == 0) {
                MealsUtil.getMealCRUD().create(dateTime, description, calories);
            } else {
                MealsUtil.getMealCRUD().update(id, dateTime, description, calories);
            }
        }
        log.debug("forward to meals");
        response.sendRedirect("meals");
    }

    private void setListRequestAttributes(HttpServletRequest request) {
        List<MealTo> mealsTo = MealsUtil.filteredByStreams(MealsUtil.getMeals(), null, null,
                MealsUtil.getCaloriesPerDay());
        request.setAttribute("mealsTo", mealsTo);
        request.setAttribute("formatter", formatter);
    }

    private void setMealRequestAttributes(HttpServletRequest request, LocalDateTime dateTime, String description,
                                          int calories, int id) {
        request.setAttribute("formatter", formatter);
        request.setAttribute("dateTime", dateTime);
        request.setAttribute("description", description);
        request.setAttribute("calories", calories);
        request.setAttribute("id", id);
    }

}
