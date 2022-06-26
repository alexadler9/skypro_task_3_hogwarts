package ru.hogwarts.school.service.implementation;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public Student add(Student student) {
        student.setId(null);
        return studentRepository.save(student);
    }

    @Override
    public Student get(Long studentId) {
        return studentRepository.findById(studentId).orElse(null);
    }

    @Override
    public Student update(Long studentId, Student student) {
        student.setId(studentId);
        return studentRepository.save(student);
    }

    @Override
    public void remove(Long studentId) {
        studentRepository.deleteById(studentId);
    }

    @Override
    public Collection<Student> filterByAge(int age) {
        return studentRepository.findAllByAge(age);
    }

    @Override
    public Collection<Student> filterByAge(int minAge, int maxAge) {
        return studentRepository.findAllByAgeBetween(minAge, maxAge);
    }

    @Override
    public Faculty getFaculty(Long studentId) {
        return studentRepository.findById(studentId).map(Student::getFaculty).orElse(null);
    }

    @Override
    public Collection<Student> getRecentList(int number) {
        return studentRepository.getRecent(number);
    }

    @Override
    public Integer getCount() {
        return studentRepository.getCount();
    }

    @Override
    public Double getAverageAge() {
        return studentRepository.getAverageAge();
    }
}
