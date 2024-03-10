package com.example.demo.controllers;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.demo.entities.Employee;
import com.example.demo.exceptions.UserNotFoundException;
import com.example.demo.exceptions.emptyDBexception;



@Controller
public class EmpController {
	
    Logger log = LoggerFactory.getLogger(EmpController.class);
	
	@Autowired
	private com.example.demo.repository.EmpRepository repo;
	
	@GetMapping("/employees")
	@ResponseBody
	public Iterable<com.example.demo.entities.Employee> getallemployees() {
		if(repo.count()==0) {
			throw new emptyDBexception("Database is Empty!");
		}
		log.info("Employees found!");
		return repo.findAll();
	}
	
	@GetMapping("/employees/{id}")
	@ResponseBody
	public Employee getemployee(@PathVariable Integer id) {
		if(repo.existsById(id)==false) {
			throw new UserNotFoundException("Employee with ID: "+id+" is not available!");
		}
		return repo.findById(id).get();
	}
	
	@PostMapping("/employees")
	@ResponseBody
	public ResponseEntity<Object> createemployee(@RequestBody Employee emp){
		emp.setId(repo.count()+1);
		com.example.demo.entities.Employee savedEmp = repo.save(emp);
		log.info("Saved the employee in database!");
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/id").buildAndExpand(savedEmp.getId()).toUri();
		log.info("Returning URI to saved employee!");
		return ResponseEntity.created(location).build();
	}
	
	@DeleteMapping("/employees/{id}")
	public HttpEntity<?> deleteemployee(@PathVariable int id){
		if(repo.existsById(id)==false) {
			throw new UserNotFoundException("Employee with ID: "+id+" doesn't exist!");
		}
		repo.deleteById(id);
		log.info("Employee deleted!");
		return ResponseEntity.EMPTY;
	}
	
	@PutMapping("/employees/{id}")
	@ResponseBody
	public ResponseEntity<Employee> updateEmployee(@PathVariable int id,@RequestBody Employee employeeDetails) {
        Employee updateEmployee = repo.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Employee not exist with id: " + id));

        updateEmployee.setFname(employeeDetails.getFname());
        updateEmployee.setLname(employeeDetails.getLname());
        updateEmployee.setEmail(employeeDetails.getEmail());

        repo.save(updateEmployee);
        log.info("Employee is updated!");
        return ResponseEntity.ok(updateEmployee);
    }
	
}
