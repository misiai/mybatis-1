package com.misiai.test;

import com.misiai.bean.Department;
import com.misiai.bean.Employee;
import com.misiai.dao.*;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test01 {

    /**
     * 封装了一个获取SqlSession的公共函数
     *
     * @return
     * @throws IOException
     */
    public SqlSession getSqlSession() throws IOException {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        return sqlSessionFactory.openSession();
    }

    @Test
    public void test01() throws IOException {
        String resource = "mybatis-config.xml";
        //由于该mybatis-config.xml文件，在类路径（源码目录）的根下，所以我们这里就没写更多路径，
        // 如果你不是类路径下，就需要补全路径。
        InputStream inputStream = Resources.getResourceAsStream(resource);
        //1.根据xml文件，新建sqlSessionFactory
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

        //2.获取SqlSession，直接执行已经映射的SQL语句。
        SqlSession sqlSession = sqlSessionFactory.openSession();

        Employee employee = (Employee) sqlSession.selectOne("com.misiai.mapper.selectEmployee", 1);
        System.out.println(employee);

        sqlSession.close();
    }

    /**
     * 测试接口式编程
     */
    @Test
    public void test02() throws IOException {

        //此处是获取SqlSession，和之前的步骤一样，我们只是把它封装成一个公共函数
        SqlSession sqlSession = getSqlSession();
        //这一步就不一样了，之前我们是调用的sqlSession.selectOne()等等方法，然后传入的是namespace+id和参数，
        // 但是这一步我们是调用的getMapper方法，传入的是EmployeeMapper.class
        //这样mybatis就会自动为我们创建代理类，然后我们就可以直接使用接口的方法了。
        // 注意：我们这里并没有接口类的具体实现类，因为mybatis创建的代理类相当于就是实现类了。
        //而要mybatis通过接口文件找到对应的mapper.xml配置文件，那么mapper.xml配置文件必须满足两个要求：
        //  - namespace：必须是接口类的全类名限定符，这样mybatis才能找到。
        // - id：必须是接口类的对应的方法名。
        EmployeeMapper employeeMapper = sqlSession.getMapper(EmployeeMapper.class);

        Employee employee = employeeMapper.findById(1);
        System.out.println("employee = " + employee);

        sqlSession.close();
    }

    /**
     * 测试基本增删改
     */
    @Test
    public void test03() throws IOException {
        SqlSession sqlSession = getSqlSession();
        EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
        Employee employee = new Employee("Kuan", "hello@misiai.com", "男");

        // 测试增加
        // Boolean insert = mapper.insert(employee);
        // System.out.println("insert = " + insert);

        // 测试修改
        // employee.setLastName("Wudao");
        // employee.setId(3);
        // Boolean update = mapper.update(employee);
        // System.out.println("update = " + update);

        // 测试删除

        Boolean delete = mapper.deleteById(3);
        System.out.println("delete = " + delete);

        sqlSession.commit();//记得提交
    }

    @Test
    public void test04() throws IOException {
        SqlSession sqlSession = getSqlSession();
        EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
        Employee employee = new Employee("TestInsert", "TestInsert@misiai.com", "男");

        // mapper.insert(employee);
        // 获取自增的id
        // System.out.println(employee.getId());

        //多个参数
        // Employee testInsert = mapper.findByIdAndLastName(4, "TestInsert");
        // System.out.println("testInsert = " + testInsert);

        //使用map
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", 4);
        map.put("lastName", "TestInsert");

        Employee byMap = mapper.findByMap(map);
        System.out.println("byMap = " + byMap);
        sqlSession.commit();//记得提交
    }

    /**
     * 查询select
     */
    @Test
    public void test05() throws IOException {
        SqlSession sqlSession = getSqlSession();
        EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
        // Employee employee = new Employee("TestInsert", "TestInsert@misiai.com", "男");

        // List<Employee> all = mapper.findAll();
        // for (Employee employee : all) {
        //     System.out.println("employee = " + employee);
        // }

        Map<String, Object> resultMap = mapper.findResultMapById(4);
        System.out.println(resultMap);
        // {gender=男, last_name=TestInsert, id=4, email=TestInsert@misiai.com}
        Map<String, Object> resultMaps = mapper.findResultsMap();
        //{1=Employee{id=1, lastName='Misiai', email='admin@misiai.com', gender='男'},
        // 4=Employee{id=4, lastName='TestInsert', email='TestInsert@misiai.com', gender='男'}}
        System.out.println(resultMaps);
        sqlSession.commit();//记得提交
    }

    @Test
    public void test06() throws IOException {
        SqlSession sqlSession = getSqlSession();
        EmployeePlusMapper mapper = sqlSession.getMapper(EmployeePlusMapper.class);

        // Employee byId = mapper.findById(4);
        // System.out.println("byId = " + byId);

        // 复杂的resultMap
        // Employee empAndDept = mapper.findEmpAndDept(5);
        // System.out.println("empAndDept = " + empAndDept);

        Employee empAndDeptStep = mapper.findEmpAndDeptStep(6);
        System.out.println("empAndDeptStep = " + empAndDeptStep);
        System.out.println(empAndDeptStep.getDepartment());
        sqlSession.commit();//记得提交
    }

    /**
     * 根据部门信息查询旗下所有员工（列表collection）
     */
    @Test
    public void test08() throws IOException {
        SqlSession sqlSession = getSqlSession();
        DepartmentMapper mapper = sqlSession.getMapper(DepartmentMapper.class);
        // List<Employee> empsById = mapper.findEmpsById(1);
        // System.out.println("empsById = " + empsById);

        Department employeesByIdStep = mapper.findEmployeesByIdStep(1);
        System.out.println(employeesByIdStep.getDepartmentName());
        // System.out.println("employeesByIdStep = " + employeesByIdStep);


        sqlSession.commit();
    }


    @Test
    public void testDynamic() throws IOException {
        SqlSession sqlSession = getSqlSession();

        EmployeeDynamicSql mapper = sqlSession.getMapper(EmployeeDynamicSql.class);
        Employee employee = new Employee();
        employee.setId(5);
        employee.setGender("男");
        employee.setEmail("admin1@misiai.com");
        // Employee byEm = mapper.findByEm(employee);
        // System.out.println("byEm = " + byEm);

        // 测试trim
        // Employee byEmByTrim = mapper.findByEmByTrim(employee);
        // System.out.println("byEmByTrim = " + byEmByTrim);

        // 测试choose
        // Employee byIdOrEmail = mapper.findByIdOrEmail(employee);
        // System.out.println("byIdOrEmail = " + byIdOrEmail);

        //测试set
        // mapper.updateByAny(employee);

        // 测试foreach
        // List<Employee> byForeach = mapper.findByForeach(Arrays.asList(5, 6));
        // for (Employee foreach : byForeach) {
        //     System.out.println("foreach = " + foreach);
        // }

        List<Employee> employeeList = mapper.findByLike("m");
        System.out.println("employeeList = " + employeeList);
        sqlSession.commit();
    }

    @Test
    public void testCache() throws IOException {

        /*测试二级缓存*/

        SqlSession sqlSession = getSqlSession();
        EmployeeCacheMapper mapper = sqlSession.getMapper(EmployeeCacheMapper.class);

        SqlSession sqlSession2 =  getSqlSession();
        EmployeeCacheMapper mapper2 = sqlSession2.getMapper(EmployeeCacheMapper.class);

        //1
        Employee byId = mapper.findById(5);
        System.out.println("byId = " + byId);
        sqlSession.close();
        //2
        Employee byId2 = mapper2.findById(5);
        System.out.println("byId2 = " + byId2);
        sqlSession2.close();

    }

    @Test
    public void test() throws IOException {
        SqlSession sqlSession = getSqlSession();


        sqlSession.commit();
    }
}
