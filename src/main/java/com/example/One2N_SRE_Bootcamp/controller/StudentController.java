package com.example.One2N_SRE_Bootcamp.controller;

import com.example.One2N_SRE_Bootcamp.model.Student;
import com.example.One2N_SRE_Bootcamp.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/students")
public class StudentController {

    private StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    //post api
    @PostMapping
   public ResponseEntity<Student> createStudent(@RequestBody Student student){
        Student savedStudent = studentService.saveStudent(student);
        return new ResponseEntity<>(savedStudent, HttpStatus.CREATED);
    }


    //get all api
    @GetMapping
    public List<Student> getAllStudents(){
        return studentService.getAllStudents();
    }

    //get buy student id api
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id){
    return  new ResponseEntity<>(studentService.getStudentById(id), HttpStatus.OK);
    }

    //update api
    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudentById(@PathVariable Long id, @RequestBody Student studentDetails){
        Student updatedStudent = studentService.updateStudent(id, studentDetails);
        return ResponseEntity.ok(updatedStudent);
    }

    //delete student
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStudentById(@PathVariable Long id){
        studentService.deleteStudentById(id);
        return ResponseEntity.ok("Student deleted successfully");
    }


}
