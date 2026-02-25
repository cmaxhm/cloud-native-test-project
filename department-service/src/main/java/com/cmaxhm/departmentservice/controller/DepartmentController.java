package com.cmaxhm.departmentservice.controller;

import com.cmaxhm.departmentservice.model.Department;
import com.cmaxhm.departmentservice.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/departments")
@RequiredArgsConstructor
@Slf4j
class DepartmentController {
  private final DepartmentRepository departmentRepository;

  @GetMapping
  public ResponseEntity<List<Department>> getAll() {
    return ResponseEntity.ok(this.departmentRepository.findAll());
  }

  @PostMapping
  public ResponseEntity<Department> create(@RequestBody Department department) {
    this.departmentRepository.create(department);

    return ResponseEntity.ok(department);
  }

  @GetMapping("/{departmentId}")
  public ResponseEntity<Department> getById(@PathVariable Long departmentId) {
    return ResponseEntity.ok(this.departmentRepository.findById(departmentId).orElse(null));
  }
}
