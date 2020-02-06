package com.misiai.dao;

import com.misiai.bean.Employee;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface EmployeeMapper {

    //返回多条记录的map: k就是列名，v就是对应的值
    @MapKey("id")
    public Map<String, Object> findResultsMap();

    //返回一条记录的map: k就是列名，v就是对应的值
    public Map<String, Object> findResultMapById(Integer id);

    public List<Employee> findAll();

    public Employee findByIdAndLastName(@Param("id") Integer id, @Param("lastName") String lastName);

    public Employee findByMap(Map<String, Object> map);

    public Employee findById(Integer id);

    public Boolean insert(Employee employee);

    public Boolean update(Employee employee);

    public Boolean deleteById(Integer id);
}
