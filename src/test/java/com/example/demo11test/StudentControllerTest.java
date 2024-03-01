package com.example.demo11test;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(value = "test")
public class StudentControllerTest {
    @Autowired
    private StudentController studentController;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void contextLoad(){
        assertThat(studentController).isNotNull();
    }
    private Student createStudent() throws Exception{
        Student student = new Student(1l, "Mario", "Rossi", false);

        //ora converte in JSON
        String studentJSON = objectMapper.writeValueAsString(student);

        //ora richiesta al controller in modo che ci crea lo student, invece dell'uso del post
        MvcResult result = this.mockMvc.perform(post("/student/new").contentType(MediaType.APPLICATION_JSON)
                        .content(studentJSON)).andExpect(status().isOk()).andReturn();

        return objectMapper.readValue(result.getResponse().getContentAsString(), Student.class);
    }
    private Student getStudentFromId(Long id) throws Exception{
        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.get("/student/"+id))
                .andDo(print()).andExpect(status().isOk())
                .andReturn();
        try{
        String studentJSON = result.getResponse().getContentAsString();
        return objectMapper.readValue(studentJSON, Student.class);
        }catch (Exception e){return null;}
    }
    @Test
    void createStudentTest() throws Exception{
        Student student = createStudent();
        assertThat(student.getId()).isNotNull();
    }
    @Test
    void studentById() throws Exception{
        Student student = createStudent();
        assertThat(student.getId()).isNotNull();

        Student studentResponse = getStudentFromId(student.getId());
        assertThat(studentResponse.getId()).isEqualTo(student.getId());
    }
    @Test
    void updateIdStudent() throws Exception{
        Student student1 = createStudent();
        Student student2 = new Student(2L, "Mariangela", "Rossini", true);
        assertThat(student1.getId()).isNotNull();
        assertThat(student2.getId()).isNotNull();

        MvcResult result = this.mockMvc.perform(put("/student/update/"+student1.getId())
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(student2)))
                .andDo(print()).andExpect(status().isOk()).andReturn();

        Student studentFromResponse = objectMapper.readValue(result.getResponse().getContentAsString(), Student.class);
        assertThat(studentFromResponse).isNotNull();
        assertThat(studentFromResponse.getId()).isEqualTo(student2.getId());
    }
    @Test
    void deleteStudentById() throws Exception{
        Student student = createStudent();
        assertThat(student.getId()).isNotNull();

        this.mockMvc.perform(MockMvcRequestBuilders.delete("/student/delete/"+student.getId()))
                .andDo(print()).andExpect(status().isOk()).andReturn();
        Student studentFromResponse = getStudentFromId(student.getId());
        assertThat(studentFromResponse).isNull();
    }
    @Test
    void updateStudentWorking() throws Exception{
        Student student = createStudent();
        assertThat(student.getId()).isNotNull();

        MvcResult result = this.mockMvc.perform(put("/student/"+student.getId()+"/working?working=true"))
                .andDo(print()).andExpect(status().isOk()).andReturn();

        Student studentFromResponse = objectMapper.readValue(result.getResponse().getContentAsString(), Student.class);
        assertThat(studentFromResponse.getId()).isEqualTo(student.getId());
        assertThat(studentFromResponse.isWorking()).isEqualTo(true);
    }
}