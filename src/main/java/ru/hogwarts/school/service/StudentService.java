package ru.hogwarts.school.service;

import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.Collection;

public interface StudentService {
    Student add(Student student);

    Student get(Long studentId);

    Student update(Long studentId, Student student);

    void remove(Long studentId);

    Collection<Student> filterByAge(Integer age);

    Collection<Student> filterByAge(Integer minAge, Integer maxAge);

    Faculty getFaculty(Long studentId);

    Collection<Student> getRecentList(int number);

    Collection<String> getNamesList(Character startLetter);

    Integer getCount();

    Double getAverageAge();
}
