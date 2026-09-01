package com.example.One2N_SRE_Bootcamp.repository;


import com.example.One2N_SRE_Bootcamp.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepo extends JpaRepository<Student, Long> {
}
