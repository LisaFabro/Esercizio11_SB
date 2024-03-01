package com.example.demo11test;

import io.micrometer.common.lang.NonNull;
import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNullFields;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/student")
public class StudentController {
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private StudentService studentService;

    @PostMapping("/new")
    public @ResponseBody Student createStudent(@RequestBody Student student){
        return studentRepository.save(student);
    }
    @GetMapping("/all")
    public @ResponseBody List<Student> getAll(){
        return studentRepository.findAll();
    }
    @GetMapping("/{id}")
    public @ResponseBody Student getStudent(@PathVariable long id){
        if(studentRepository.existsById(id)){
        return studentRepository.getReferenceById(id);
        }else{
            return null;
        }
    }

//    modifica id cancellando uno student e ricreandolo
    @PutMapping("/update/{id}")
    public @ResponseBody Student updateStudent(@PathVariable long id, @RequestBody @Nonnull Student student){
        if(studentRepository.existsById(id)){
            studentRepository.deleteById(id);
            return studentRepository.save(student);}
        else{return null;}
    }

    @PutMapping("/{id}/working")
    public @ResponseBody Student setStudentWorking(@PathVariable long id, @RequestParam("working") boolean working){
        return studentService.setStudentsWorking(id, working);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteStudent(@PathVariable long id){
        studentRepository.deleteById(id);
    }
}
