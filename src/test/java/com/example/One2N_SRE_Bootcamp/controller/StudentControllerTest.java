package com.example.One2N_SRE_Bootcamp.controller;

import com.example.One2N_SRE_Bootcamp.exception.StudentNotFoundException;
import com.example.One2N_SRE_Bootcamp.model.Student;
import com.example.One2N_SRE_Bootcamp.service.StudentService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// @WebMvcTest loads ONLY the web layer (controller + Spring MVC infra),
// not the full app context, DB, etc. — makes these tests fast and focused.
@WebMvcTest(StudentController.class)
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc; // simulates HTTP requests without starting a real server

    @MockBean
    private StudentService studentService; // testing the controller in isolation

    @Autowired
    private ObjectMapper objectMapper; // converts Java objects <-> JSON

    @Test
    void createStudent_returns201() throws Exception {
        Student input = new Student(null, "Jane", "Doe", "jane@example.com");
        Student saved = new Student(1L, "Jane", "Doe", "jane@example.com");
        when(studentService.saveStudent(any())).thenReturn(saved);

        mockMvc.perform(post("/api/v1/students")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("Jane"));
    }

    @Test
    void getAllStudents_returns200_withList() throws Exception {
        Student s1 = new Student(1L, "Jane", "Doe", "jane@example.com");
        Student s2 = new Student(2L, "John", "Smith", "john@example.com");
        when(studentService.getAllStudents()).thenReturn(List.of(s1, s2));

        mockMvc.perform(get("/api/v1/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void getStudentById_returns200_whenFound() throws Exception {
        Student student = new Student(1L, "Jane", "Doe", "jane@example.com");
        when(studentService.getStudentById(1L)).thenReturn(student);

        mockMvc.perform(get("/api/v1/students/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Jane"));
    }

    @Test
    void getStudentById_returns404_whenNotFound() throws Exception {
        when(studentService.getStudentById(99L)).thenThrow(new StudentNotFoundException(99L));

        mockMvc.perform(get("/api/v1/students/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateStudent_returns200() throws Exception {
        Student updated = new Student(1L, "Jane", "Updated", "jane@example.com");
        when(studentService.updateStudent(eq(1L), any())).thenReturn(updated);

        mockMvc.perform(put("/api/v1/students/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastName").value("Updated"));
    }

    @Test
    void deleteStudent_returns200() throws Exception {
        mockMvc.perform(delete("/api/v1/students/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Student deleted successfully"));
    }
}