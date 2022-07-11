package ru.hogwarts.school.service.implementation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collection;
import java.util.Comparator;
import java.util.Set;
import java.util.stream.Stream;

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

    @Override
    public String getLongestName() {
        LOG.info("Was invoked method for get longest faculty name");
        String longestName = facultyRepository.findAll().stream()
                .map(Faculty::getName)
                .max(Comparator.comparingInt(String::length)).orElse(null);
        LOG.debug("Longest faculty name: {}", longestName);
        return longestName;
    }

    @Override
    public Long getTestValue() {
        LOG.info("Was invoked method for testing stream");
        long startTime = System.nanoTime();
        long sum = Stream.iterate(1L, a -> a + 1)
                .limit(10_000_000)
                .parallel()
                .reduce(0L, Long::sum);
        long endTime = System.nanoTime();
        LOG.debug("Method duration in ms: {}", (endTime - startTime) / 1000000);
        return sum;
    }
}
