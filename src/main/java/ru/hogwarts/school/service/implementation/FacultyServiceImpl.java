package ru.hogwarts.school.service.implementation;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FacultyServiceImpl implements FacultyService {
    private final FacultyRepository facultyRepository;

    public FacultyServiceImpl(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    @Override
    public Faculty add(Faculty faculty) {
        faculty.setId(null);
        return facultyRepository.save(faculty);
    }

    @Override
    public Faculty get(Long facultyId) {
        return facultyRepository.findById(facultyId).orElse(null);
    }

    @Override
    public Faculty update(Long facultyId, Faculty faculty) {
        faculty.setId(facultyId);
        return facultyRepository.save(faculty);
    }

    @Override
    public void remove(Long facultyId) {
        facultyRepository.deleteById(facultyId);
    }

    @Override
    public Collection<Faculty> filterByColorOrName(String param) {
        return facultyRepository.findAllByColorOrNameIgnoreCase(param);
    }

    @Override
    public Set<Student> getStudents(Long facultyId) {
        return facultyRepository.findById(facultyId).map(Faculty::getStudents).orElse(null);
    }
}
