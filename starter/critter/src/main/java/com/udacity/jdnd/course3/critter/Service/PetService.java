package com.udacity.jdnd.course3.critter.Service;

import com.udacity.jdnd.course3.critter.DataModel.Customer;
import com.udacity.jdnd.course3.critter.DataModel.Pet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.udacity.jdnd.course3.critter.DataModel.PetRepository;

import java.util.List;

@Service
public class PetService {
    @Autowired
    PetRepository petRepository;
    @Autowired
    CustomerService customerService;

    public Pet savePet(Pet pet) {

        Pet returnedPet = petRepository.save(pet);

        if (returnedPet.getOwner() !=null) {
            customerService.addPetToCustomer(pet, pet.getOwner());
        }
        return pet;

    }

   /* public Pet savePet(Pet pet) {
        Pet returnedPet = petRepository.save(pet);
        Customer customer = returnedPet.getOwner();
        customer.addPet(returnedPet);
        customerRepository.save(customer);
        return returnedPet;
    }
*/
    public Pet getPet(Long id) {
        return petRepository.getOne(id);
    }

    public List<Pet> getPets(){
        return petRepository.findAll();
    }
    public List < Pet > getPetsByOwner(Long ownerId) {
        return petRepository.getPetsByOwner_Id(ownerId);
    }

    public List<Pet> getAllPetsByIds(List<Long> ids){
        return petRepository.findAllById(ids);
    }
}
