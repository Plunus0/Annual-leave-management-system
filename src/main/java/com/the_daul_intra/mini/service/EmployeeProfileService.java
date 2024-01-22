package com.the_daul_intra.mini.service;
import com.the_daul_intra.mini.dto.entity.EmployeeProfile;
import com.the_daul_intra.mini.repository.EmployeeProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeProfileService {
    @Autowired
    private EmployeeProfileRepository employeeProfileRepository;

    public List<EmployeeProfile> getAllEmployees() {
        return employeeProfileRepository.findAll();
    }

    public EmployeeProfile getEmployee(Long id) {
        return employeeProfileRepository.findById(id).orElse(null);
    }

    public void saveEmployee(EmployeeProfile employee) {
        employeeProfileRepository.save(employee);
    }

    public void deleteEmployee(Long id) {
        employeeProfileRepository.deleteById(id);
    }
}
