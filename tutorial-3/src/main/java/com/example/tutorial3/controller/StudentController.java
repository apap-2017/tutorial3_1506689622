package com.example.tutorial3.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.tutorial3.model.StudentModel;
import com.example.tutorial3.service.InMemoryStudentService;
import com.example.tutorial3.service.StudentService;

@Controller
public class StudentController {
	private final StudentService studentService;
	
	public StudentController() {
		studentService = new InMemoryStudentService();
	}
	
	@RequestMapping("/student/add")
	public String add(@RequestParam(value = "npm", required = true) String npm, 
					@RequestParam(value = "name", required = true) String name,
					@RequestParam(value = "gpa", required = true) double gpa) {
		StudentModel student = new StudentModel(npm, name, gpa);
		studentService.addStudent(student);
		return "add";
	}
	
	@RequestMapping("/student/view")
	public String view(Model model, @RequestParam(value = "npm", required = true) String npm) {
		StudentModel student = studentService.selectStudent(npm);
		model.addAttribute("student", student);
		return "view";
	}
	
	@RequestMapping("/student/viewall")
	public String viewAll(Model model) {
		List<StudentModel> students = studentService.selectAllStudents();
		model.addAttribute("students", students);
		return "viewall";
	}
	
	/* Latihan Soal No.1 View Student dengan Path Variable
	 * NOTE		 : 	pada salah satu value mapping dibuat "/student/view/" 
	 * 				agar tidak ambigu dengan mapping pada method view yaitu 
	 * 				"student/view" akses di browser untuk tanpa input npm menjadi
	 * 				"localhost:8080/student/view/"								*/
	
	@RequestMapping(value = {"/student/view/", "student/view/{npm}"})
	public String viewPath(@PathVariable Optional<String> npm, Model model) {
		if(npm.isPresent()) {
			StudentModel student = studentService.selectStudent(npm.get());
			if(student == null) {
				model.addAttribute("npm", npm.get());
				model.addAttribute("statement", " tidak ditemukan.");
			}else {
				model.addAttribute("student", student);
				return "view";
			}
		}else {
			model.addAttribute("npm", "");
			model.addAttribute("statement", " kosong.");
		}	
		return "errorNPMview";
	}
	
	/* Latihan Soal No. 2 Delete Student dengan Path Variable*/
	@RequestMapping(value = {"/student/delete", "student/delete/{npm}"})
	public String deletePath(@PathVariable Optional<String> npm, Model model) {
		if(npm.isPresent()) {
			StudentModel student = studentService.selectStudent(npm.get());
			if(student == null) {
				model.addAttribute("npm", npm.get());
				model.addAttribute("statement", " tidak ditemukan.");
			}
			else {
				int i = 0;
				while(i < studentService.selectAllStudents().size()) {
					if(studentService.selectAllStudents().get(i).getNpm().equals(npm.get())) {
						model.addAttribute("student", student);
						studentService.selectAllStudents().remove(i);
					}
					i++;
				}
				return "delete";
			}
		}else {
			model.addAttribute("npm", "");
			model.addAttribute("statement", " kosong.");
		}
		return "errorDelete";
	}
} 
