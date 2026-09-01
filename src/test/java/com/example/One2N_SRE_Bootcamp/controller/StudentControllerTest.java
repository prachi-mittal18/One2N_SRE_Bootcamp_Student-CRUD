//package com.example.One2N_SRE_Bootcamp.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.web.servlet.MockMvc;
//
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(StudentController.class)
//class StudentControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private StudentService studentService;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Test
//    void createStudent_returns201() throws Exception {
//        Student student = new Student(null, "Jane", "Doe", "jane@example.com");
//        Student saved = new Student(1L, "Jane", "Doe", "jane@example.com");
//        when(studentService.createStudent(any())).thenReturn(saved);
//
//        mockMvc.perform(post("/api/v1/students")
//                        .contentType("application/json")
//                        .content(objectMapper.writeValueAsString(student)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.id").value(1));
//    }
//
//    @Test
//    void getStudentById_returns404_whenNotFound() throws Exception {
//        when(studentService.getStudentById(99L)).thenThrow(new StudentNotFoundException(99L));
//
//        mockMvc.perform(get("/api/v1/students/99"))
//                .andExpect(status().isNotFound());
//    }
//}