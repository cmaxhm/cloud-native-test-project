package com.cmaxhm.departmentservice.service;

import com.cmaxhm.departmentservice.client.EmployeeClient;
import com.cmaxhm.departmentservice.model.Employee;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeService {
  private final EmployeeClient employeeClient;

  @Retry(name = "department-service", fallbackMethod = "fallbackFindAllByDepartmentId")
  public List<Employee> findAllByDepartmentId(Long departmentId) {
    return this.employeeClient.findAllByDepartmentId(departmentId);
  }

  private List<Employee> fallbackFindAllByDepartmentId(Long departmentId, Throwable throwable) {
    log.error("Error occurred while fetching employees for department with id: {}", departmentId, throwable.getMessage());

    return this.employeeClient.findAllByDepartmentId(departmentId);
  }
}
