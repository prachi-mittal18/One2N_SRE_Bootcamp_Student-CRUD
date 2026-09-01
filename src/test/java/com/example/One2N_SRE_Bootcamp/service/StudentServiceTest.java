package com.example.One2N_SRE_Bootcamp.service;

import com.example.One2N_SRE_Bootcamp.exception.StudentNotFoundException;
import com.example.One2N_SRE_Bootcamp.model.Student;
import com.example.One2N_SRE_Bootcamp.repository.StudentRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import java.util.Optional;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    @Mock
    private StudentRepo studentRepo;

    @InjectMocks
    private StudentService studentService;

    @Test
    void getStudentById_returnsStudent_whenExists() {
        Student student = new Student(1L, "Jane", "Doe", "jane@example.com");
        when(studentRepo.findById(1L)).thenReturn(Optional.of(student));

        Student result = studentService.getStudentById(1L);

        assertThat(result.getFirstName()).isEqualTo("Jane");
    }

    @Test
    void getStudentById_throwsException_whenNotFound() {
        when(studentRepo.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> studentService.getStudentById(99L))
                .isInstanceOf(StudentNotFoundException.class);
    }





}
