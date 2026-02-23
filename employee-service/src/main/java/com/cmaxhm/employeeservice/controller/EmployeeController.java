package com.cmaxhm.employeeservice.controller;

import com.cmaxhm.employeeservice.model.Employee;
import com.cmaxhm.employeeservice.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/employees")
public class EmployeeController {
  private final EmployeeRepository employeeRepository;

  @GetMapping
  public ResponseEntity<List<Employee>> findAll() {
    return ResponseEntity.ok(this.employeeRepository.findAll());
  }

  @GetMapping("/departments/{departmentId}")
  public ResponseEntity<List<Employee>> findAllByDepartmentId(@PathVariable Long departmentId) {
    return ResponseEntity.ok(this.employeeRepository.findByDepartmentId(departmentId));
  }

  @PostMapping
  public ResponseEntity<Void> create(@RequestBody Employee employee) {
    this.employeeRepository.create(employee);

    return ResponseEntity
      .created(URI.create("/api/v1/employees".concat(employee.getId().toString())))
      .build();
  }
}
