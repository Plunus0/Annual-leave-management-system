package com.the_daul_intra.mini.controller;

import com.the_daul_intra.mini.dto.entity.Employee;
import com.the_daul_intra.mini.service.EmployeeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping(value = "/admin")
public class IndexController {
/*

    @GetMapping("/index")
    public String index() {
        return "index";
    }
*/


    @GetMapping("/index")
    public String showEmployeeList(Model model) {
        List<Employee> employees = EmployeeService.findAll(); // employeeService는 직원 정보를 가져오는 서비스입니다.
        model.addAttribute("employees", employees);
        return "employee/List";
    }

}
