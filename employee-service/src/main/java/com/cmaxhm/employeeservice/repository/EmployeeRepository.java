package com.cmaxhm.employeeservice.repository;

import com.cmaxhm.employeeservice.model.Employee;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class EmployeeRepository {
  private final List<Employee> employees;

  public EmployeeRepository() {
    this.employees = new ArrayList<>();
  }

  public List<Employee> findAll() {
    return employees;
  }

  public List<Employee> findByDepartmentId(Long departmentId) {
    return this.employees.stream()
      .filter(employee -> employee.getDepartmentId().equals(departmentId))
      .toList();
  }

  public void create(Employee employee) {
    this.employees.add(employee);
  }
}
