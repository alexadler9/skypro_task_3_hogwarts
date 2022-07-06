package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collection;
import java.util.Set;

@RestController
@RequestMapping("/faculty")
public class FacultyController {
    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @PostMapping
    public ResponseEntity<Faculty> addFaculty(@RequestBody Faculty faculty) {
        Faculty addedFaculty = facultyService.add(faculty);
        return ResponseEntity.ok(addedFaculty);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Faculty> getFaculty(@PathVariable Long id) {
        Faculty faculty = facultyService.get(id);
        if (faculty == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(faculty);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Faculty> updateFaculty(@PathVariable Long id, @RequestBody Faculty faculty) {
        Faculty updatedFaculty = facultyService.update(id, faculty);
        if (updatedFaculty == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(updatedFaculty);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeFaculty(@PathVariable Long id) {
        facultyService.remove(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/filterByColorOrName")
    public ResponseEntity<Collection<Faculty>> filterFacultiesByColorOrName(@RequestParam String param) {
        return ResponseEntity.ok(facultyService.filterByColorOrName(param));
    }

    @GetMapping("/students/{id}")
    public ResponseEntity<Set<Student>> getStudents(@PathVariable Long id) {
        Set<Student> students = facultyService.getStudents(id);
        if (students == null) {
                return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(students);
    }
}