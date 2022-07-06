package ru.hogwarts.school.service;

import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.Set;

public interface FacultyService {
    Faculty add(Faculty faculty);

    Faculty get(Long facultyId);

    Faculty update(Long facultyId, Faculty faculty);

    void remove(Long facultyId);

    Collection<Faculty> filterByColorOrName(String param);

    Set<Student> getStudents(Long facultyId);

    String getLongestName();

    Long getTestValue();
}
