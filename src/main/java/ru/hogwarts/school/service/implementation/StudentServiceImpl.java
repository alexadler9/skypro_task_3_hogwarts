package ru.hogwarts.school.service.implementation;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {
    private final Map<Long, Student> studentRepository;
    private Long id = 0L;

    public StudentServiceImpl() {
        studentRepository = new HashMap<>();
    }

    @Override
    public Student add(Student student) {
        student.setId(++id);
        return studentRepository.put(id, student);
    }

    @Override
    public Student get(Long studentId) {
        if (studentRepository.containsKey(studentId)) {
            return studentRepository.get(studentId);
        }
        return null;
    }

    @Override
    public Student update(Student student) {
        if (studentRepository.containsKey(student.getId())) {
            studentRepository.put(student.getId(), student);
            return student;
        }
        return null;
    }

    @Override
    public Student remove(Long studentId) {
        return studentRepository.remove(studentId);
    }

    @Override
    public Collection<Student> filterByAge(int age) {
        return studentRepository.values().stream()
                .filter(s -> s.getAge() == age)
                .collect(Collectors.toList());
    }
}
