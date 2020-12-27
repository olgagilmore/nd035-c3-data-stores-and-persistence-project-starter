package com.udacity.jdnd.course3.critter.user;

import com.udacity.jdnd.course3.critter.DataModel.Customer;
import com.udacity.jdnd.course3.critter.DataModel.Employee;
import com.udacity.jdnd.course3.critter.DataModel.Pet;
import com.udacity.jdnd.course3.critter.Service.EmployeeService;
import com.udacity.jdnd.course3.critter.Service.PetService;
import com.udacity.jdnd.course3.critter.Service.CustomerService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Users.
 *
 * Includes requests for both customers and employees. Splitting this into separate user and customer controllers
 * would be fine too, though that is not part of the required scope for this class.
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    CustomerService customerService;
    @Autowired
    PetService petService;
    @Autowired
    EmployeeService employeeService;

    @PostMapping("/customer")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO){
        //Customer customer = (Customer) userService.save(convertCustomerDTOToCustomer(customerDTO));
        List<Long> petIds = customerDTO.getPetIds();
        List<Pet> pets = new ArrayList<>();
        if (petIds !=null) {
            for (Long petId : petIds) {
                pets.add(petService.getPet(petId));
            }
        }
        Customer customer = convertCustomerDTOToCustomer(customerDTO);
        customer.setPets(pets);
        Customer savedCustomer = customerService.save(customer);
        return convertCustomerToCustomerDTO(savedCustomer);

        //throw new UnsupportedOperationException();
    }

    @GetMapping("/customer")
    public List<CustomerDTO> getAllCustomers(){
        //throw new UnsupportedOperationException();
        return customerService.getAllCustomers().stream().
                map(this::convertCustomerToCustomerDTO).collect(Collectors.toList());
    }

    @GetMapping("/customer/pet/{petId}")
    public CustomerDTO getOwnerByPet(@PathVariable long petId){
        //throw new UnsupportedOperationException();
        Customer owner= petService.getPet(petId).getOwner();

        return convertCustomerToCustomerDTO(owner);
    }

    @PostMapping("/employee")
    public EmployeeDTO saveEmployee(@RequestBody EmployeeDTO employeeDTO) {
        //throw new UnsupportedOperationException();
        Employee employee = new Employee();

        String name = employeeDTO.getName();
        Set<EmployeeSkill> skills = employeeDTO.getSkills();
        Set<DayOfWeek> daysAvailable = employeeDTO.getDaysAvailable();
        long id = employeeDTO.getId();

        List<EmployeeSkill> skillsList;
        List<DayOfWeek> daysAvailableList;

        if (skills != null) {
            skillsList = new ArrayList<>(skills);
        } else {
            skillsList = new ArrayList<>();
        }

        if (daysAvailable != null) {
            daysAvailableList = new ArrayList<>(daysAvailable);
        } else {
            daysAvailableList = new ArrayList<>();
        }

        employee.setName(name);
        employee.setId(id);
        employee.setDaysAvailable(daysAvailableList);
        employee.setSkills(skillsList);

        Employee savedEmployee = employeeService.save(employee);

        //Employee employee= employeeService.save(convertEmployeeDTOToEmployee(employeeDTO));
        return convertEmployeeToEmployeeDTO(savedEmployee);

    }

    @PostMapping("/employee/{employeeId}")
    public EmployeeDTO getEmployee(@PathVariable long employeeId) {
        //throw new UnsupportedOperationException();
        Employee employee= employeeService.getById(employeeId);
        return convertEmployeeToEmployeeDTO(employee);
    }

    @PutMapping("/employee/{employeeId}")
    public void setAvailability(@RequestBody Set<DayOfWeek> daysAvailable, @PathVariable long employeeId) {
        //throw new UnsupportedOperationException();
        //employeeService.setAvailabilty(daysAvailable, employeeId);

        Employee employee = employeeService.getById(employeeId);
        List<DayOfWeek> daysAvailableList = new ArrayList<DayOfWeek>(daysAvailable);
        employee.setDaysAvailable(daysAvailableList);
        employeeService.save(employee);
    }

    @GetMapping("/employee/availability")
    public List<EmployeeDTO> findEmployeesForService(@RequestBody EmployeeRequestDTO employeeDTO) {
        //throw new UnsupportedOperationException();
        List<Employee> employees = employeeService.getEmployeesForService(employeeDTO.getSkills(), employeeDTO.getDate());

        return employees.stream().map(this::convertEmployeeToEmployeeDTO).collect(Collectors.toList());
    }

    private CustomerDTO convertCustomerToCustomerDTO(Customer customer){
        CustomerDTO customerDTO = new CustomerDTO();
        BeanUtils.copyProperties(customer, customerDTO);
        if (customer.getPets() != null) {
            customerDTO.setPetIds(customer.getPets().stream().
                    map(pet -> pet.getId()).collect(Collectors.toList()));
        }

        return customerDTO;
    }

    private Customer convertCustomerDTOToCustomer(CustomerDTO customerDTO){
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDTO, customer);
        if (customerDTO.getPetIds()!=null) {
            customer.setPets(customerDTO.getPetIds().stream().
                    map(petId-> petService.getPet(petId)).collect(Collectors.toList()));
        }
        return customer;
    }

    private EmployeeDTO convertEmployeeToEmployeeDTO(Employee employee){
        EmployeeDTO employeeDTO = new EmployeeDTO();
        BeanUtils.copyProperties(employee, employeeDTO);
        employeeDTO.setSkills(new HashSet<>(employee.getSkills()));
        employeeDTO.setDaysAvailable(new HashSet<>(employee.getDaysAvailable())) ;


        return employeeDTO;
    }

    private Employee convertEmployeeDTOToEmployee(EmployeeDTO employeeDTO){
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);

        return employee;
    }

}
