package com.the_daul_intra.mini.controller;

import com.the_daul_intra.mini.dto.entity.EmployeeProfile;
import com.the_daul_intra.mini.service.EmployeeProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class EmployeeProfileController {
    @Autowired
    private EmployeeProfileService employeeProfileService;

    @GetMapping("/employees")
    public String list(Model model) {
        List<EmployeeProfile> employees = employeeProfileService.getAllEmployees();
        model.addAttribute("employees", employees);
        return "employees";
    }

    @GetMapping("/employee/{id}")
    public String detail(@PathVariable Long id, Model model) {
        EmployeeProfile employee = employeeProfileService.getEmployee(id);
        model.addAttribute("employee", employee);
        return "employee_detail";
    }

    @PostMapping("/employee")
    public String create(@ModelAttribute EmployeeProfile employee) {
        employeeProfileService.saveEmployee(employee);
        return "redirect:/employees";
    }

    @PutMapping("/employee/{id}")
    public String update(@PathVariable Long id, @ModelAttribute EmployeeProfile employee) {
        employeeProfileService.saveEmployee(employee);
        return "redirect:/employee/" + id;
    }

    @DeleteMapping("/employee/{id}")
    public String delete(@PathVariable Long id) {
        employeeProfileService.deleteEmployee(id);
        return "redirect:/employees";
    }
}
