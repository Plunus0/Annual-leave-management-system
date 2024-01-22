package com.the_daul_intra.mini.controller;

import com.the_daul_intra.mini.dto.entity.Employee;
import com.the_daul_intra.mini.dto.request.EmployeePostRequest;
import com.the_daul_intra.mini.repository.EmployeeRepository;
import com.the_daul_intra.mini.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/admin")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeController(EmployeeService employeeService, EmployeeRepository employeeRepository) {
        this.employeeService = employeeService;
        this.employeeRepository = employeeRepository;
    }

    @PostMapping("/employee")
    public Employee createEmployee(@RequestBody EmployeePostRequest request) {
        System.out.println(request);
        return employeeService.createEmployee(request);

    }

    @PostMapping("/employeeAdd")
    public String addEmployee(@ModelAttribute Employee newEmployee, Model model) {

        // 직원 정보를 DB에 저장
        employeeRepository.save(newEmployee);

        // 모든 직원 정보를 가져와 model에 추가
        model.addAttribute("employees", employeeRepository.findAll());

        return "employee/List";
    }


}

