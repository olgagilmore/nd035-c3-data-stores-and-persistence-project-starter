package com.udacity.jdnd.course3.critter.Service;

import com.udacity.jdnd.course3.critter.DataModel.Customer;
import com.udacity.jdnd.course3.critter.DataModel.Pet;
import com.udacity.jdnd.course3.critter.DataModel.User;
import com.udacity.jdnd.course3.critter.DataModel.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {
    @Autowired
    CustomerRepository customerRepository;

    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }
    public void addPetToCustomer(Pet pet, Customer customer) {
        List<Pet> pets = customer.getPets();

        if (pets != null)

            pets.add(pet);

        else {

            pets = new ArrayList<Pet>();

            pets.add(pet);

        }

        customer.setPets(pets);

        customerRepository.save(customer);

    }

    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id).orElse(null);
        /*Optional<Customer> optionalCustomer = customerRepository.findById(id);
        return optionalCustomer.orElse(null);*/
    }

    public List<Customer> getAllCustomers(){
        return customerRepository.findAll();
    }
}
