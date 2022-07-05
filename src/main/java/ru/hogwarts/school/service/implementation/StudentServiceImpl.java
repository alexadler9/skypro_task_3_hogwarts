package ru.hogwarts.school.service.implementation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;

@Service
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;

    Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class);

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public Student add(Student student) {
        logger.info("Was invoked method for create student");
        student.setId(null);
        Student newStudent = studentRepository.save(student);
        logger.debug("New student has been created: {}", newStudent);
        return newStudent;
    }

    @Override
    public Student get(Long studentId) {
        logger.info("Was invoked method for get student {}", studentId);
        return studentRepository.findById(studentId).orElse(null);
    }

    @Override
    public Student update(Long studentId, Student student) {
        logger.info("Was invoked method for update student {}", studentId);
        student.setId(studentId);
        Student updatedStudent = studentRepository.save(student);
        logger.debug("Student has been updated: {}", updatedStudent);
        return updatedStudent;
    }

    @Override
    public void remove(Long studentId) {
        logger.info("Was invoked method for delete student {}", studentId);
        studentRepository.deleteById(studentId);
    }

    @Override
    public Collection<Student> filterByAge(Integer age) {
        logger.info("Was invoked method for filter students by age {}", age);
        return studentRepository.findAllByAge(age);
    }

    @Override
    public Collection<Student> filterByAge(Integer minAge, Integer maxAge) {
        logger.info("Was invoked method for filter students aged between {} and {}", minAge, maxAge);
        return studentRepository.findAllByAgeBetween(minAge, maxAge);
    }

    @Override
    public Faculty getFaculty(Long studentId) {
        logger.info("Was invoked method for get faculty for student {}", studentId);
        return studentRepository.findById(studentId).map(Student::getFaculty).orElse(null);
    }

    @Override
    public Collection<Student> getRecentList(int number) {
        logger.info("Was invoked method for get list of last {} students", number);
        return studentRepository.getRecent(number);
    }

    @Override
    public Integer getCount() {
        logger.info("Was invoked method for get count of students");
        Integer count = studentRepository.getCount();
        logger.debug("Count of students: {}", count);
        return count;
    }

    @Override
    public Double getAverageAge() {
        logger.info("Was invoked method for get average age of students");
        Double averageAge = studentRepository.getAverageAge();
        logger.debug("Average age of students: {}", averageAge);
        return averageAge;
    }
}
