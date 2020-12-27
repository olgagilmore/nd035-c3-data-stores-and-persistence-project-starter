package com.udacity.jdnd.course3.critter.Service;

import com.udacity.jdnd.course3.critter.DataModel.*;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ScheduleService {
    @Autowired
    ScheduleRepository scheduleRepository;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    EmployeeRepository employeeRepository;

    public Schedule saveSchedule(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }

    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }

    public List<Schedule> getSchedulesForPet(long petId) {
        return scheduleRepository.findAllByPetsId(petId);
    }

    public List<Schedule> getScheduleForEmployee(long employeeId) {
        return scheduleRepository.findAllByEmployeesId(employeeId);
    }
    public List<Schedule> getScheduleForCustomer(Long customerId) {

        //return scheduleRepository.findAllByCustomerId(Id);
        String errorMessage = "Customer not found in Id : " + customerId;

        Optional<Customer> optionalCustomer= customerRepository.findById(customerId);
        Customer customer = (Customer) ((Optional<?>) optionalCustomer).orElseThrow(() -> new ObjectNotFoundException(customerId, errorMessage));
        List<Pet> pets = customer.getPets();

        ArrayList<Schedule> schedules = new ArrayList<>();
        for (Pet pet : pets) {
            schedules.addAll(scheduleRepository.findAllByPetsId(pet.getId()));
        }
        return schedules;
    }

}
