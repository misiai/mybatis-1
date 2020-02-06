package com.misiai.dao;

import com.misiai.bean.Employee;

import java.util.ArrayList;
import java.util.List;

public interface EmployeeDynamicSql {
    public Employee findByEm(Employee employee);

    public Employee findByEmByTrim(Employee employee);

    public Employee findByIdOrEmail(Employee employee);

    public List<Employee> findByForeach(List<Integer> ids);

    List<Employee> findByLike(String lastName);

    public void updateByAny(Employee employee);
}
