package ru.hogwarts.school.controller;

import net.minidev.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.service.implementation.FacultyServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.hogwarts.school.SchoolApplicationConstants.*;

@WebMvcTest(FacultyController.class)
public class FacultyControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FacultyRepository facultyRepository;

    @SpyBean
    private FacultyServiceImpl facultyService;

    @InjectMocks
    private FacultyController facultyController;

    @Test
    public void shouldReturnFacultyWhenAddingNewFaculty() throws Exception {
        JSONObject facultyObject = new JSONObject();
        facultyObject.put("name", FACULTY_1.getName());
        facultyObject.put("color", FACULTY_1.getColor());

        when(facultyRepository.save(any(Faculty.class))).thenReturn(FACULTY_1);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/faculty")
                        .content(facultyObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(FACULTY_1.getId()))
                .andExpect(jsonPath("$.name").value(FACULTY_1.getName()))
                .andExpect(jsonPath("$.color").value(FACULTY_1.getColor()));
    }

    @Test
    public void shouldReturnFacultyWhenGettingExistingFaculty() throws Exception {
        when(facultyRepository.findById(any(Long.class))).thenReturn(Optional.of(FACULTY_1));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/{id}", FACULTY_1.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(FACULTY_1.getId()))
                .andExpect(jsonPath("$.name").value(FACULTY_1.getName()))
                .andExpect(jsonPath("$.color").value(FACULTY_1.getColor()));
    }

    @Test
    public void shouldReturnNotFoundWhenGettingNonExistingFaculty() throws Exception {
        when(facultyRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/{id}", FACULTY_1.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturnFacultyWhenUpdatingExistingFaculty() throws Exception {
        Faculty faculty = FACULTY_1;
        faculty.setName(FACULTY_1.getName() + " update");

        JSONObject facultyObject = new JSONObject();
        facultyObject.put("name", faculty.getName());
        facultyObject.put("color", faculty.getColor());

        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/faculty/{id}", faculty.getId())
                        .content(facultyObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(faculty.getId()))
                .andExpect(jsonPath("$.name").value(faculty.getName()))
                .andExpect(jsonPath("$.color").value(faculty.getColor()));
    }

    @Test
    public void shouldReturnOkWhenDeletingFaculty() throws Exception {
        doNothing().when(facultyRepository).deleteById(any(Long.class));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/faculty/{id}", FACULTY_1.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnFilteredCollectionByColorOrName() throws Exception {
        when(facultyRepository.findAllByColorOrNameIgnoreCase(FACULTY_1.getColor())).thenReturn(List.of(FACULTY_1));
        when(facultyRepository.findAllByColorOrNameIgnoreCase(FACULTY_2.getName())).thenReturn(List.of(FACULTY_2));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/filterByColorOrName?param={1}", FACULTY_1.getColor())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(FACULTY_1.getId()));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/filterByColorOrName?param={1}", FACULTY_2.getName())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(FACULTY_2.getId()));
    }
}
