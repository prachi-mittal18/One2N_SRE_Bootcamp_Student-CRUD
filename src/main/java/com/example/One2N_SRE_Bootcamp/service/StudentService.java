package com.example.One2N_SRE_Bootcamp.service;


import com.example.One2N_SRE_Bootcamp.model.Student;
import com.example.One2N_SRE_Bootcamp.repository.StudentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    @Autowired
    private StudentRepo studentRepo;

    //create
    public Student saveStudent(Student student) {
        return studentRepo.save(student);
    }

    //read all
    public List<Student> getAllStudents() {
        return studentRepo.findAll();
    }

    //read by id
    public Student getStudentById(Long id) {
        return studentRepo.findById(id).orElseThrow(()-> new RuntimeException("Student not found with id :"+ id));
    }

    //update

    public Student updateStudent(Long id, Student studentDetails) {
        Student existingStudent = getStudentById(id);
        existingStudent.setFirstName(studentDetails.getFirstName());
        existingStudent.setLastName(studentDetails.getLastName());
        existingStudent.setEmail(studentDetails.getEmail());
        return studentRepo.save(existingStudent);
    }

    //Delete
    public void deleteStudentById(Long id) {
        Student existingStudent = getStudentById(id);
        studentRepo.delete(existingStudent);
    }



}
