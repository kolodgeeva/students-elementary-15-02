package com.hillel.students.controller;

import com.hillel.students.domain.student.Student;
import com.hillel.students.domain.student.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class StudentController {

    StudentRepository studentRepository;

    @Autowired
    public StudentController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @GetMapping("/addStudentForm")
    public String showForm(Student student) {
        return  "add-student";
    }

    @PostMapping("/addStudent")
    public String addStudent(@Valid Student student, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "add-student";
        }

        studentRepository.save(student);
        model.addAttribute("students", studentRepository.findAll());
        return "students";

    }

    @GetMapping("/")
    public String getStudents(Model model) {
        model.addAttribute("students", studentRepository.findAll());
        return "students";
    }

    @GetMapping("/delete/{id}")
    public String deleteStudent(@PathVariable("id") long id, Model model) {

        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid student id: " + id));

        studentRepository.delete(student);

        model.addAttribute("students", studentRepository.findAll());
        return "students";
    }

    @GetMapping("/edit/{id}")
    public String editStudent(@PathVariable("id") long id, Model model) {

        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid student id: " + id));

        model.addAttribute("student", student);
        return "edit-student";
    }

    @PostMapping("/update/{id}")
    public String updateStudent(@PathVariable("id") long id,
                                @Valid Student student,
                                BindingResult bindingResult,
                                Model model) {

        if (bindingResult.hasErrors()) {
            student.setId(id);
            return "edit-student";
        }

        studentRepository.save(student);

        model.addAttribute("students", studentRepository.findAll());
        return "students";
    }

}
