package ru.hogwarts.school;

import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.List;

public class SchoolApplicationConstants {
    public static final Faculty FACULTY_1 = new Faculty(1L, "Test faculty 1", "red", null);
    public static final Faculty FACULTY_2 = new Faculty(2L, "Test faculty 2", "green", null);
    public static final List<Faculty> FACULTIES = List.of(FACULTY_1, FACULTY_2);

    public static final Student STUDENT_1 = new Student(null, "Test student 1", 18, null);
    public static final Student STUDENT_2 = new Student(null, "Test student 2", 18, null);
    public static final Student STUDENT_3 = new Student(null, "Test student 3", 21, null);
    public static final Student STUDENT_4 = new Student(null, "Test student 4", 21, null);
    public static final List<Student> STUDENTS = List.of(STUDENT_1, STUDENT_2, STUDENT_3, STUDENT_4);
}
