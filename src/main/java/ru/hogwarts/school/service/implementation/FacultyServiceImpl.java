package ru.hogwarts.school.service.implementation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collection;
import java.util.Set;

@Service
public class FacultyServiceImpl implements FacultyService {
    private final FacultyRepository facultyRepository;

    private static final Logger LOG = LoggerFactory.getLogger(FacultyServiceImpl.class);

    public FacultyServiceImpl(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    @Override
    public Faculty add(Faculty faculty) {
        LOG.info("Was invoked method for create faculty");
        faculty.setId(null);
        Faculty newFaculty = facultyRepository.save(faculty);
        LOG.debug("New faculty has been created: {}", newFaculty);
        return newFaculty;
    }

    @Override
    public Faculty get(Long facultyId) {
        LOG.info("Was invoked method for get faculty {}", facultyId);
        return facultyRepository.findById(facultyId).orElse(null);
    }

    @Override
    public Faculty update(Long facultyId, Faculty faculty) {
        LOG.info("Was invoked method for update faculty {}", facultyId);
        faculty.setId(facultyId);
        Faculty updatedFaculty = facultyRepository.save(faculty);
        LOG.debug("Faculty has been updated: {}", updatedFaculty);
        return updatedFaculty;
    }

    @Override
    public void remove(Long facultyId) {
        LOG.info("Was invoked method for delete faculty {}", facultyId);
        facultyRepository.deleteById(facultyId);
    }

    @Override
    public Collection<Faculty> filterByColorOrName(String param) {
        LOG.info("Was invoked method for filter faculties by param {}", param);
        return facultyRepository.findAllByColorOrNameIgnoreCase(param);
    }

    @Override
    public Set<Student> getStudents(Long facultyId) {
        LOG.info("Was invoked method for get students for faculty {}", facultyId);
        return facultyRepository.findById(facultyId).map(Faculty::getStudents).orElse(null);
    }
}
