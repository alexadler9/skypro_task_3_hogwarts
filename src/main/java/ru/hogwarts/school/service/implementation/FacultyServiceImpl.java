package ru.hogwarts.school.service.implementation;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FacultyServiceImpl implements FacultyService {
    private final Map<Long, Faculty> facultyRepository;
    private Long id = 0L;

    public FacultyServiceImpl() {
        facultyRepository = new HashMap<>();
    }

    @Override
    public Faculty add(Faculty faculty) {
        faculty.setId(++id);
        return facultyRepository.put(id, faculty);
    }

    @Override
    public Faculty get(Long facultyId) {
        if (facultyRepository.containsKey(facultyId)) {
            return facultyRepository.get(facultyId);
        }
        return null;
    }

    @Override
    public Faculty update(Faculty faculty) {
        if (facultyRepository.containsKey(faculty.getId())) {
            facultyRepository.put(faculty.getId(), faculty);
            return faculty;
        }
        return null;
    }

    @Override
    public Faculty remove(Long facultyId) {
        return facultyRepository.remove(facultyId);
    }

    @Override
    public Collection<Faculty> filterByColor(String color) {
        return facultyRepository.values().stream()
                .filter(f -> f.getColor().equals(color))
                .collect(Collectors.toList());
    }
}
