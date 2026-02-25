package com.cmaxhm.departmentservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class Department {
  private Long id;
  private String name;
  private List<Employee> employees;
}
