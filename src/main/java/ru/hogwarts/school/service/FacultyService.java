package ru.hogwarts.school.service;

import ru.hogwarts.school.model.Faculty;

import java.util.Collection;

public interface FacultyService {
    Faculty add(Faculty faculty);

    Faculty get(Long facultyId);

    Faculty update(Long facultyId, Faculty faculty);

    void remove(Long facultyId);

    Collection<Faculty> filterByColor(String color);
}
