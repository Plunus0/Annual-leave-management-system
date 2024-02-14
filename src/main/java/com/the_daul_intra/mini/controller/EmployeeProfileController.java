package com.the_daul_intra.mini.controller;

import com.the_daul_intra.mini.dto.response.EmployeeDetailResponse;
import com.the_daul_intra.mini.repository.EmployeeProfileRepository;
import com.the_daul_intra.mini.repository.EmployeeRepository;
import com.the_daul_intra.mini.service.EmployeeProfileService;
import com.the_daul_intra.mini.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/admin/employee")
public class EmployeeProfileController {

    @Autowired
    private EmployeeService employeeService;
    private EmployeeProfileService employeeProfileService;
    private EmployeeProfileRepository employeeProfileRepository;
    private EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeProfileController(EmployeeService employeeService,EmployeeProfileService employeeProfileService, EmployeeProfileRepository employeeProfileRepository, EmployeeRepository employeeRepository){
        this.employeeProfileService = employeeProfileService;
        this.employeeProfileRepository = employeeProfileRepository;
        this.employeeRepository = employeeRepository;
        this.employeeService = employeeService;
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
    public String showEmployeeDetail(@PathVariable Long id, Model model) {
        // EmployeeDetailResponse 객체를 가져옵니다.
        EmployeeDetailResponse employeeDetailResponse = employeeProfileService.getEmployeeDetail(id);

        // Model에 EmployeeDetailResponse 객체를 추가합니다. 뷰에서는 "employeeDetail" 이름으로 접근 가능합니다.
        model.addAttribute("employeeDetail", employeeDetailResponse);

        // "employee_detail" 뷰를 반환합니다.
        return "employee_detail";
    }

//    @PostMapping("/employee_detail/{id}")
//    public String create(@ModelAttribute EmployeeProfile employee) {
//        employeeProfileService.saveEmployee(employee);
//        return "redirect:/admin/employee/employeeList";
//    }


    @GetMapping("/employeeUpdate/{id}")
    public String update(@PathVariable Long id, Model model) {
        EmployeeDetailResponse employeeDetailResponse = employeeProfileService.getEmployeeDetail(id);
        if (employeeDetailResponse == null) {
            // EmployeeDetailResponse 객체가 null인 경우 에러 처리
            // 예: return "error_page";
        }
        model.addAttribute("employeeDetail", employeeDetailResponse);
        return "employeeUpdate";
    }

    @PutMapping("/employeeUpdate/{id}")
    public String putUpdate(@PathVariable Long id, @ModelAttribute EmployeeDetailResponse employeeDetail) {
        // EmployeeDetailResponse 객체를 업데이트합니다.
        employeeProfileService.updateEmployeeDetail(id, employeeDetail);

        // 업데이트가 완료되면 employee_detail 페이지로 리다이렉트합니다.
        return "redirect:/admin/employee/employee_detail/" + id;
    }




//    @DeleteMapping("/employee_detail/{id}")
//    public String delete(@PathVariable Long id) {
//        employeeProfileService.deleteEmployee(id);
//        return "redirect:/admin/employee/employeeList";
//    }
}
