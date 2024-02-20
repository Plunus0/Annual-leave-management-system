package com.the_daul_intra.mini.controller;

import com.the_daul_intra.mini.dto.entity.Employee;
import com.the_daul_intra.mini.dto.entity.EmployeeProfile;
import com.the_daul_intra.mini.dto.request.EmployeePostRequest;
import com.the_daul_intra.mini.dto.response.EmployeeListResponse;
import com.the_daul_intra.mini.repository.EmployeeProfileRepository;
import com.the_daul_intra.mini.repository.EmployeeRepository;
import com.the_daul_intra.mini.service.EmployeeService;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    public String showEmployeeList(
            @RequestParam(value = "page", defaultValue = "1") @Min(value = 1, message = "최소 페이지는 1페이지 입니다.") Integer page,
            @RequestParam(value = "size", defaultValue = "20") Integer size,
            @RequestParam(value = "retire", defaultValue = "N") String retire,
            Model model) {
        boolean isRetireY = "Y".equals(retire);
        model.addAttribute("isRetireY", isRetireY);
        Page<EmployeeListResponse> empList = employeeService.getEmpPagingList(page, size, retire);
        model.addAttribute("empList", empList);

        return "employeeList";
    }

/*    @GetMapping("/retiredEmployee")
    public String getRetiredEmployee(Model model) {
        List<Employee> employees = employeeRepository.findByEmployeeProfileRetirementDateIsNotNull();
        model.addAttribute("employees", employees);
        return "retiredEmployee";
    }*/



}

