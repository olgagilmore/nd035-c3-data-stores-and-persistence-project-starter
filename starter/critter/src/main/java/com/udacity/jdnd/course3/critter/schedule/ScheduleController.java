package com.udacity.jdnd.course3.critter.schedule;

import com.udacity.jdnd.course3.critter.DataModel.Employee;
import com.udacity.jdnd.course3.critter.DataModel.Pet;
import com.udacity.jdnd.course3.critter.DataModel.Schedule;
import com.udacity.jdnd.course3.critter.Service.EmployeeService;
import com.udacity.jdnd.course3.critter.Service.PetService;
import com.udacity.jdnd.course3.critter.Service.ScheduleService;
import com.udacity.jdnd.course3.critter.user.EmployeeSkill;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Schedules.
 */
@RestController
@RequestMapping("/schedule")
public class ScheduleController {
    @Autowired
    EmployeeService employeeService;
    @Autowired
    ScheduleService scheduleService;
    @Autowired
    PetService petService;

    @PostMapping
    public ScheduleDTO createSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        //throw new UnsupportedOperationException();

        //List<Employee> employees = employeeService.getEmployeesForService(scheduleDTO.getActivities(), scheduleDTO.getDate());

        Schedule schedule = convertScheduleDTOToSchedule(scheduleDTO);

        List<Long> employeeIds = scheduleDTO.getEmployeeIds();
        List<Long> petIds = scheduleDTO.getPetIds();

        List<Employee> employees = employeeIds.stream().map(employeeId -> {
            Employee employee = employeeService.getById(employeeId);
            return employee;
        }).collect(Collectors.toList());

        List<Pet> pets = petService.getAllPetsByIds(petIds);

        schedule.setEmployees(employees);
        schedule.setPets(pets);


        Schedule savedSchedule = scheduleService.saveSchedule(schedule);

        return convertScheduleToScheduleDTO(savedSchedule);
    }

    @GetMapping
    public List<ScheduleDTO> getAllSchedules() {
        //throw new UnsupportedOperationException();
        List<Schedule> schedules = scheduleService.getAllSchedules();

        List<ScheduleDTO> scheduleDTOs = new ArrayList<>();
        for(Schedule schedule : schedules){
            scheduleDTOs.add(convertScheduleToScheduleDTO(schedule));
        }

        return scheduleDTOs;
    }

    @GetMapping("/pet/{petId}")
    public List<ScheduleDTO> getScheduleForPet(@PathVariable long petId) {

        //throw new UnsupportedOperationException();
        List<Schedule> schedules = scheduleService.getSchedulesForPet(petId);

        return schedules.stream().map(this::convertScheduleToScheduleDTO).collect(Collectors.toList());
    }

    @GetMapping("/employee/{employeeId}")
    public List<ScheduleDTO> getScheduleForEmployee(@PathVariable long employeeId) {
        //throw new UnsupportedOperationException();
        List<Schedule> schedules = scheduleService.getScheduleForEmployee(employeeId);

        return schedules.stream().map(this::convertScheduleToScheduleDTO).collect(Collectors.toList());

    }

    @GetMapping("/customer/{customerId}")
    public List<ScheduleDTO> getScheduleForCustomer(@PathVariable long customerId) {
        //throw new UnsupportedOperationException();

        List<Schedule> schedules = scheduleService.getScheduleForCustomer(customerId);

        return schedules.stream().map(this::convertScheduleToScheduleDTO).collect(Collectors.toList());

    }

    private ScheduleDTO convertScheduleToScheduleDTO(Schedule schedule) {
        ScheduleDTO scheduleDTO = new ScheduleDTO();

        Set<EmployeeSkill> skills;

        if(schedule.getActivities() != null){
            skills = new HashSet<EmployeeSkill>(schedule.getActivities());
        }else{
            skills = new HashSet<EmployeeSkill>();
        }

        scheduleDTO.setActivities(skills);
        scheduleDTO.setDate(schedule.getDate());
        List<Long> employeeIds = schedule.getEmployees().stream().map(Employee::getId).collect(Collectors.toList());
        scheduleDTO.setEmployeeIds(employeeIds);
        List<Long> petIds = schedule.getPets().stream().map(Pet::getId).collect(Collectors.toList());
        scheduleDTO.setPetIds(petIds);
        scheduleDTO.setId(schedule.getId());

        return scheduleDTO;
    }

    private Schedule convertScheduleDTOToSchedule(ScheduleDTO scheduleDTO) {
        Schedule schedule = new Schedule();

        BeanUtils.copyProperties(scheduleDTO, schedule);

        return schedule;
    }
}
