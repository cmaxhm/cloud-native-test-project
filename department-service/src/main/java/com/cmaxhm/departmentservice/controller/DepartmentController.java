package com.cmaxhm.departmentservice.controller;

import com.cmaxhm.departmentservice.client.EmployeeClient;
import com.cmaxhm.departmentservice.model.Department;
import com.cmaxhm.departmentservice.service.DepartmentService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/departments")
@RequiredArgsConstructor
@Slf4j
class DepartmentController {
  private final DepartmentService departmentService;
  private final EmployeeClient employeeClient;

  @RateLimiter(name = "department-service", fallbackMethod = "fallbackFindAllRateLimiter")
  @GetMapping
  public ResponseEntity<List<Department>> findAll() {
    List<Department> departments = this.departmentService.findAll();

    return ResponseEntity.ok(departments);
  }

  @PostMapping
  public ResponseEntity<Department> create(@RequestBody Department department) {
    this.departmentService.create(department);

    return ResponseEntity.ok(department);
  }

  @GetMapping("/{departmentId}")
  public ResponseEntity<Department> findById(@PathVariable Long departmentId) {
    Optional<Department> optionalDepartment = this.departmentService.findById(departmentId);

    if (optionalDepartment.isEmpty()) return ResponseEntity.notFound().build();

    Department department = optionalDepartment.get();

    department.setEmployees(this.employeeClient.findAllByDepartmentId(departmentId));

    return ResponseEntity.ok(optionalDepartment.get());
  }

  @TimeLimiter(name = "department-service", fallbackMethod = "fallbackFindAllTimeLimiter")
  @GetMapping("/async")
  public CompletableFuture<ResponseEntity<List<Department>>> findAllAsync() {
    return CompletableFuture.supplyAsync(() -> ResponseEntity.ok(this.departmentService.findAll()));
  }

  private ResponseEntity<List<Department>> fallbackFindAllRateLimiter(Throwable throwable) {
    log.error("Department service too many requests {}", throwable.getMessage());

    return new ResponseEntity<>(HttpStatus.TOO_MANY_REQUESTS);
  }

  private CompletableFuture<ResponseEntity<List<Department>>> fallbackFindAllTimeLimiter(Throwable throwable) {
    log.error("Department service timed out {}", throwable.getMessage());

    return CompletableFuture.supplyAsync(() -> new ResponseEntity<>(HttpStatus.REQUEST_TIMEOUT));
  }
}
