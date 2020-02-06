package com.misiai.dao;

import com.misiai.bean.Department;
import com.misiai.bean.Employee;

import java.util.List;

public interface DepartmentMapper {
    public Department findById(Integer id);

    List<Employee> findEmpsById(Integer id);

    Department findEmployeesByIdStep(Integer id);
}
