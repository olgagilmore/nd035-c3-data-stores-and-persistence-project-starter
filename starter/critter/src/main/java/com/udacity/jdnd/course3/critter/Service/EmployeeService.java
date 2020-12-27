package com.udacity.jdnd.course3.critter.Service;

import com.udacity.jdnd.course3.critter.DataModel.Employee;
import com.udacity.jdnd.course3.critter.DataModel.EmployeeRepository;
import com.udacity.jdnd.course3.critter.user.EmployeeSkill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class EmployeeService {
    @Autowired
    EmployeeRepository employeeRepository;

    public Employee save(Employee employee) {
        return employeeRepository.save(employee);
    }

    public Employee getById(Long Id ) {
        return employeeRepository.getOne(Id);
    }



    /*public void setAvailabilty(Set<DayOfWeek> daysAvailable,  long employeeId) {
        Employee employee =  employeeRepository.getOne(employeeId);
        if (employee !=null) {
            employee.setDaysAvailable(daysAvailable);
        }
    }*/

    public List<Employee> getEmployeesForService(Set<EmployeeSkill> skills, LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        List<Employee> availableEmployees = employeeRepository.findAllBySkillsInAndDaysAvailableContains(skills, dayOfWeek);

        List<Employee> employeeList = new ArrayList<>();
        availableEmployees.forEach(mEmployee -> {
            if(mEmployee.getSkills().containsAll(skills)){ employeeList.add(mEmployee); }
        });
        return employeeList;



    }
}
