package com.cmaxhm.departmentservice.controller;

import com.cmaxhm.departmentservice.client.EmployeeClient;
import com.cmaxhm.departmentservice.model.Department;
import com.cmaxhm.departmentservice.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/departments")
@RequiredArgsConstructor
@Slf4j
class DepartmentController {
  private final DepartmentRepository departmentRepository;
  private final EmployeeClient employeeClient;

  @GetMapping
  public ResponseEntity<List<Department>> getAll() {
    List<Department> departments = this.departmentRepository.findAll();

    departments.forEach(department -> department.setEmployees(this.employeeClient.findAllByDepartmentId(department.getId())));

    return ResponseEntity.ok(departments);
  }

  @PostMapping
  public ResponseEntity<Department> create(@RequestBody Department department) {
    this.departmentRepository.create(department);

    return ResponseEntity.ok(department);
  }

  @GetMapping("/{departmentId}")
  public ResponseEntity<Department> getById(@PathVariable Long departmentId) {
    Optional<Department> optionalDepartment = this.departmentRepository.findById(departmentId);

    if (optionalDepartment.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    Department department = optionalDepartment.get();

    department.setEmployees(this.employeeClient.findAllByDepartmentId(departmentId));

    return ResponseEntity.ok(department);
  }
}
