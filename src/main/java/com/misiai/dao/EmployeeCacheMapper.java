package com.misiai.dao;

import com.misiai.bean.Employee;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface EmployeeCacheMapper {



    public Employee findById(Integer id);
}
