package ru.hogwarts.school.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.implementation.StudentServiceImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static ru.hogwarts.school.SchoolApplicationConstants.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentControllerTests {
    @LocalServerPort
    private int port;

    @Autowired
    private StudentServiceImpl studentService;

    @Autowired
    private StudentController studentController;

    @Autowired
    private TestRestTemplate restTemplate;

    private final List<Student> tempStudents = new ArrayList<>();

    @BeforeEach
    public void fillDB() {
        for (Student student : STUDENTS) {
            tempStudents.add(studentService.add(student));
        }
    }

    @AfterEach
    public void clearDB() {
        for (Student student : tempStudents) {
            studentService.remove(student.getId());
        }
    }

    @Test
    public void shouldReturnStudentWhenAddingNewStudent() {
        Student student = new Student(null, "Test student " + (tempStudents.size() + 1), 18, null);
        ResponseEntity<Student> response = restTemplate.postForEntity("http://localhost:" + port + "/hogwarts/student", student, Student.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isNotNull();
        Assertions.assertThat(response.getBody().getId()).isNotNull();

        restTemplate.delete("http://localhost:" + port + "/hogwarts/student/" + response.getBody().getId());
    }

    @Test
    public void shouldReturnStudentWhenGettingExistingStudent() {
        Student student = tempStudents.get(0);
        ResponseEntity<Student> response = restTemplate.getForEntity("http://localhost:" + port + "/hogwarts/student/" + student.getId(), Student.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isNotNull();
        Assertions.assertThat(response.getBody().getId()).isEqualTo(student.getId());
    }

    @Test
    public void shouldReturnNotFoundStatusWhenGettingNonExistingStudent() {
        Student student = tempStudents.get(0);
        restTemplate.delete("http://localhost:" + port + "/hogwarts/student/" + student.getId());
        tempStudents.remove(0);

        ResponseEntity<Student> response = restTemplate.getForEntity("http://localhost:" + port + "/hogwarts/student/" + student.getId(), Student.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        Assertions.assertThat(response.getBody()).isNull();
    }

    @Test
    public void shouldReturnStudentWhenUpdatingExistingStudent() {
        Student student = tempStudents.get(0);
        student.setName(student.getName() + " update");
        HttpEntity<Student> entity = new HttpEntity<>(student);

        ResponseEntity<Student> response = restTemplate.exchange("http://localhost:" + port + "/hogwarts/student/" + student.getId(), HttpMethod.PUT, entity, Student.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isNotNull();
        Assertions.assertThat(response.getBody().getId()).isEqualTo(student.getId());
        Assertions.assertThat(response.getBody().getName()).isEqualTo(student.getName());
    }

    @Test
    public void shouldReturnOkWhenDeletingStudent() {
        Student student = tempStudents.get(0);
        ResponseEntity<Void> deleteResponse = restTemplate.exchange("http://localhost:" + port + "/hogwarts/student/" + student.getId(), HttpMethod.DELETE, null, Void.class);
        Assertions.assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        tempStudents.remove(0);

        ResponseEntity<Student> getResponse = restTemplate.getForEntity("http://localhost:" + port + "/hogwarts/student/" + student.getId(), Student.class);
        Assertions.assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        Assertions.assertThat(getResponse.getBody()).isNull();
    }

    @Test
    public void shouldReturnFilteredCollectionByAge() {
        int minAge = 18;
        int maxAge = 20;
        List<Student> filteredStudents = tempStudents.stream().filter(s -> (s.getAge() >= minAge) && (s.getAge() <= maxAge)).toList();

        String url = "http://localhost:" + port + "/hogwarts/student/filterByAgeBetween?minAge={minAge}&maxAge={maxAge}";
        ResponseEntity<Collection<Student>> response = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<>() {
        }, minAge, maxAge);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isNotNull();
        Assertions.assertThat(response.getBody()).containsAll(filteredStudents);
    }
}
