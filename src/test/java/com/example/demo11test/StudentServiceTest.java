package com.example.demo11test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles(value = "test")

public class StudentServiceTest {
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private StudentService studentService;

    @Test
    void checkworkingStudent() throws Exception{
        Student student = new Student();
        student.setName("Genoveffa");
        student.setSurname("Bianca");
        student.setWorking(true);
        Student studentDb = studentRepository.save(student);

        assertThat(studentDb.getId()).isNotNull();

        Student studentFromService = studentService.setStudentsWorking(student.getId(), true);

        assertThat(studentFromService.getId()).isNotNull();
        assertThat(studentFromService.isWorking()).isTrue();

        Student studentFromFind = studentRepository.findById(studentDb.getId()).get();
        assertThat(studentFromFind).isNotNull();
        assertThat(studentFromFind.getId()).isNotNull();
        assertThat(studentFromFind.getId()).isEqualTo(studentDb.getId());
        assertThat(studentFromFind.isWorking()).isTrue();
    }
}
