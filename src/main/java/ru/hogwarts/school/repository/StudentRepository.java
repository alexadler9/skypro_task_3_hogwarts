package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.hogwarts.school.model.Student;

import java.util.Collection;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Collection<Student> findAllByAge(int age);

    Collection<Student> findAllByAgeBetween(int minAge, int maxAge);

    @Query(value = "SELECT * FROM student ORDER BY id DESC LIMIT :number", nativeQuery = true)
    Collection<Student> getRecent(int number);

    @Query(value = "SELECT COUNT(*) FROM student", nativeQuery = true)
    Integer getCount();

    @Query(value = "SELECT AVG(age) FROM student", nativeQuery = true)
    Double getAverageAge();
}
