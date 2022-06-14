package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.hogwarts.school.model.Faculty;

import java.util.Collection;

@Repository
public interface FacultyRepository extends JpaRepository<Faculty, Long> {
    Collection<Faculty> findAllByColor(String color);

    @Query("SELECT f FROM Faculty f WHERE UPPER(f.name) = UPPER(:param) OR UPPER(f.color) = UPPER(:param)")
    Collection<Faculty> findAllByColorOrNameIgnoreCase(@Param("param") String param);
}
