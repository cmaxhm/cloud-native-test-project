package com.cmaxhm.departmentservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Employee {
  private Long id;
  private Long departmentId;
  private String name;
  private String position;
}
