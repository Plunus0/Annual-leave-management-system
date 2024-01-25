package com.the_daul_intra.mini.controller;

import com.the_daul_intra.mini.dto.entity.EmployeeProfile;
import com.the_daul_intra.mini.repository.EmployeeProfileRepository;
import com.the_daul_intra.mini.repository.EmployeeRepository;
import com.the_daul_intra.mini.service.EmployeeProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/admin/employee")
public class EmployeeProfileController {


    private EmployeeProfileService employeeProfileService;
    private EmployeeProfileRepository employeeProfileRepository;
    private EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeProfileController(EmployeeProfileService employeeProfileService, EmployeeProfileRepository employeeProfileRepository, EmployeeRepository employeeRepository){
        this.employeeProfileService = employeeProfileService;
        this.employeeProfileRepository = employeeProfileRepository;
        this.employeeRepository = employeeRepository;
    }



/*

    @GetMapping("/employeeProfileAdd")
    public String list(Model model) {
        List<EmployeeProfile> employees = employeeProfileService.getAllEmployees();
        model.addAttribute("employees", employees);
        return "employeeList";
    }
*/

    @GetMapping("/employee_detail/{id}")
    public String detail(@PathVariable Long id, Model model) {
        EmployeeProfile employee = employeeProfileService.getEmployee(id);
        model.addAttribute("employee", employee);
        System.out.println("testtttt");
        return "employee_detail";
    }

    @PostMapping("/employee_detail/{id}")
    public String create(@ModelAttribute EmployeeProfile employee) {
        employeeProfileService.saveEmployee(employee);
        return "redirect:/admin/employee/employeeList";
    }


    @PutMapping("/employee_detail/{id}")
    public String update(@PathVariable Long id, @ModelAttribute EmployeeProfile employee) {
        employeeProfileService.saveEmployee(employee);
        return "redirect:/admin/employee/employee_detail" + id;
    }

    @DeleteMapping("/employee_detail/{id}")
    public String delete(@PathVariable Long id) {
        employeeProfileService.deleteEmployee(id);
        return "redirect:/admin/employee/employeeList";
    }
}
