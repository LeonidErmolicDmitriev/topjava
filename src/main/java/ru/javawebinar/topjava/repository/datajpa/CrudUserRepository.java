package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.User;

@Transactional(readOnly = true)
public interface CrudUserRepository extends JpaRepository<User, Integer> {
    @Transactional
    @Modifying
    @Query("DELETE FROM User u WHERE u.id=:id")
    int delete(@Param("id") int id);

    User getByEmail(String email);

    @EntityGraph(value = "UserWithMeals", type = EntityGraph.EntityGraphType.LOAD)
    User getOneWithMealsById(int id);

    @EntityGraph(value = "UserWithRoles", type = EntityGraph.EntityGraphType.LOAD)
    User getOneWithRolesById(int id);

    default User getWithMeals(int id) {
        // Load user with items into persistence contex
        getOneWithMealsById(id);
        // Load user with roles into persistence context
        // (There is only one user instance by id within the persistence context)
        return getOneWithRolesById(id);
    }

}
