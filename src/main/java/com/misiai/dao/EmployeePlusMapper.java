package com.misiai.dao;

import com.misiai.bean.Employee;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface EmployeePlusMapper {
    public Employee findById(Integer id);

    public Employee findEmpAndDept(Integer id);

    public Employee findEmpAndDeptStep(Integer id);

    List<Employee> findEmployeesByDeptId(Integer id);
}
