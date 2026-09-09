package com.example.One2N_SRE_Bootcamp.service;


import com.example.One2N_SRE_Bootcamp.exception.StudentNotFoundException;
import com.example.One2N_SRE_Bootcamp.model.Student;
import com.example.One2N_SRE_Bootcamp.repository.StudentRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class StudentService {
    // One logger per class, named after the class itself — standard convention.
    // This name shows up in log lines so you know which class emitted them.
    private static final Logger log = LoggerFactory.getLogger(StudentService.class);

    @Autowired
    private StudentRepo studentRepo;

    //create
    public Student saveStudent(Student student) {
        log.info("Creating new student with email={}", student.getEmail());
        Student saved = studentRepo.save(student);
        log.info("Student created successfully with id={}", saved.getId());
        return studentRepo.save(student);
    }

    //read all
    public List<Student> getAllStudents() {

        log.info("Fetching all students");
        List<Student> students = studentRepo.findAll();
        log.info("Found {} students", students.size());
        return studentRepo.findAll();
    }

    //read by id
    public Student getStudentById(Long id) {
        log.info("Fetching student with id={}", id);
        return studentRepo.findById(id).orElseThrow(()-> {
            log.warn("Student not found with id={}", id);
           return new StudentNotFoundException(id);
        });
    }

    //update

    public Student updateStudent(Long id, Student studentDetails) {
        log.info("Updating student with id={}", id);
        Student existingStudent = getStudentById(id);
        existingStudent.setFirstName(studentDetails.getFirstName());
        existingStudent.setLastName(studentDetails.getLastName());
        existingStudent.setEmail(studentDetails.getEmail());
        Student updated = studentRepo.save(existingStudent);
        log.info("Student with id={} updated successfully", id);
        return updated;
    }

    //Delete
    public void deleteStudentById(Long id) {
        log.info("Deleting student with id={}", id);
        Student existingStudent = getStudentById(id);
        studentRepo.delete(existingStudent);
        log.info("Student with id={} deleted successfully", id);
    }



}
