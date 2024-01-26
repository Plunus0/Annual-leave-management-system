package com.the_daul_intra.mini.controller;

import com.the_daul_intra.mini.dto.entity.Employee;
import com.the_daul_intra.mini.dto.entity.EmployeeProfile;
import com.the_daul_intra.mini.dto.request.EmployeePostRequest;
import com.the_daul_intra.mini.repository.EmployeeProfileRepository;
import com.the_daul_intra.mini.repository.EmployeeRepository;
import com.the_daul_intra.mini.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping(value = "/admin/employee")
public class EmployeeController {

    @Autowired
    private final EmployeeService employeeService;
    private final EmployeeRepository employeeRepository;
    private final EmployeeProfileRepository employeeProfileRepository;

    @Autowired
    public EmployeeController(EmployeeService employeeService, EmployeeRepository employeeRepository, EmployeeProfileRepository employeeProfileRepository) {
        this.employeeService = employeeService;
        this.employeeRepository = employeeRepository;
        this.employeeProfileRepository = employeeProfileRepository;
    }

    @PostMapping("/employeeAdd")
    public String addEmployee(@ModelAttribute EmployeePostRequest newEmployeePostRequest) {


        if (newEmployeePostRequest.getEmail() == null || newEmployeePostRequest.getEmail().isEmpty()) {
            // 'email' 필드가 null이거나 빈 문자열입니다.
            // 여기에 적절한 오류 처리 코드를 추가하세요.
            return "redirect:/admin/employee/employeeAdd";
        }
        employeeService.createEmployee(newEmployeePostRequest);

        //dto를 repository에 저장하는 서비스메서드호출
        return "redirect:/admin/employee/employeeList";
    }

    @GetMapping("/employeeAdd")
    public String addEmployeePage(Model model) {

        model.addAttribute( "employee", new Employee());
        return "employeeAdd";
    }


    @GetMapping("/employeeList")
    public String showEmployeeList(Model model) {
        List<EmployeeProfile> employees = employeeProfileRepository.findByRetirementDateIsNull();

        model.addAttribute("employees", employees);
        return "employeeList";
    }

    @GetMapping("/retiredEmployee")
    public String getRetiredEmployee(Model model) {
        List<Employee> employees = employeeRepository.findByEmployeeProfileRetirementDateIsNotNull();
        model.addAttribute("employees", employees);
        return "retiredEmployee";
    }



}

