package ru.hogwarts.school.service;

import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.Collection;

public interface StudentService {
    Student add(Student student);

    Student get(Long studentId);

    Student update(Long studentId, Student student);

    void remove(Long studentId);

    Collection<Student> filterByAge(int age);

    Collection<Student> filterByAge(int minAge, int maxAge);

    Faculty getFaculty(Long studentId);
}
