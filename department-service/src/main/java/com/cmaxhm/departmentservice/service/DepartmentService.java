package com.cmaxhm.departmentservice.service;

import com.cmaxhm.departmentservice.client.EmployeeClient;
import com.cmaxhm.departmentservice.model.Department;
import com.cmaxhm.departmentservice.repository.DepartmentRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DepartmentService {
  private final DepartmentRepository departmentRepository;
  private final EmployeeClient employeeClient;

  @CircuitBreaker(name = "department-service", fallbackMethod = "fallbackFindAll")
  public List<Department> findAll() {
    List<Department> departments = this.departmentRepository.findAll();

    departments.forEach(department -> department.setEmployees(this.employeeClient.findAllByDepartmentId(department.getId())));

    return departments;
  }

  @CircuitBreaker(name = "department-service", fallbackMethod = "fallbackFindById")
  public Optional<Department> findById(Long departmentId) {
    Optional<Department> optionalDepartment = this.departmentRepository.findById(departmentId);

    if (optionalDepartment.isEmpty()) return Optional.empty();

    Department department = optionalDepartment.get();

    department.setEmployees(this.employeeClient.findAllByDepartmentId(departmentId));

    return Optional.of(department);
  }

  public void create(Department department) {
    this.departmentRepository.create(department);
  }

  private List<Department> fallbackFindAll(Throwable throwable) {
    log.error("Error occurred while fetching departments", throwable);

    return this.departmentRepository.findAll();
  }

  private Optional<Department> fallbackFindById(Long departmentId, Throwable throwable) {
    log.error("Error occurred while fetching department with id: {}", departmentId, throwable.getMessage());

    return this.departmentRepository.findById(departmentId);
  }
}
