package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;

@RestController
@RequestMapping("/student")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public ResponseEntity<Student> addStudent(@RequestBody Student student) {
        Student addedStudent = studentService.add(student);
        return ResponseEntity.ok(addedStudent);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudent(@PathVariable Long id) {
        Student student = studentService.get(id);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable Long id, @RequestBody Student student) {
        Student updatedStudent = studentService.update(id, student);
        if (updatedStudent == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(updatedStudent);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeStudent(@PathVariable Long id) {
        studentService.remove(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/filterByAge")
    public ResponseEntity<Collection<Student>> filterStudentsByAge(@RequestParam Integer age) {
        return ResponseEntity.ok(studentService.filterByAge(age));
    }

    @GetMapping("/filterByAgeBetween")
    public ResponseEntity<Collection<Student>> filterStudentsByAgeBetween(@RequestParam Integer minAge, @RequestParam Integer maxAge) {
        return ResponseEntity.ok(studentService.filterByAge(minAge, maxAge));
    }

    @GetMapping("/faculty/{id}")
    public ResponseEntity<Faculty> getFaculty(@PathVariable Long id) {
        Faculty faculty = studentService.getFaculty(id);
        if (faculty == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(faculty);
    }

    @GetMapping("/getRecentList")
    public ResponseEntity<Collection<Student>> getRecentStudentsList(@RequestParam int number) {
        return ResponseEntity.ok(studentService.getRecentList(number));
    }

    @GetMapping("/getNamesList")
    public ResponseEntity<Collection<String>> getStudentNamesList(@RequestParam Character startLetter) {
        return ResponseEntity.ok(studentService.getNamesList(startLetter));
    }

    @GetMapping("/count")
    public Integer getStudentsCount() {
        return studentService.getCount();
    }

    @GetMapping("/averageAge")
    public Double getStudentsAverageAge() {
        return studentService.getAverageAge();
    }

    @GetMapping("/printNames")
    public void printStudentsNames() {
        studentService.printNames();
    }

    @GetMapping("/printNamesSynchronized")
    public void printStudentsNamesSynchronized() {
        studentService.printNamesSynchronized();
    }
}
