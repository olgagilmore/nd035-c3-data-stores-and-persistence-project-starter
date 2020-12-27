package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.DataModel.Customer;
import com.udacity.jdnd.course3.critter.DataModel.Pet;
import com.udacity.jdnd.course3.critter.Service.CustomerService;
import com.udacity.jdnd.course3.critter.Service.PetService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Pets.
 */
@RestController
@RequestMapping("/pet")
public class PetController {

    @Autowired
    PetService petService;
    @Autowired
    CustomerService customerService;

    @PostMapping
    public PetDTO savePet(@RequestBody PetDTO petDTO) {
        //throw new UnsupportedOperationException();
        Customer customer = null;
        if ((Long) petDTO.getOwnerId() != null) {
            customer = customerService.getCustomerById(petDTO.getOwnerId());
        }
        Pet pet = convertPetDTOToPet(petDTO);
        pet.setOwner(customer);
        Pet savedPet = petService.savePet(pet);
        return convertPetToPetDTO(savedPet);
    }

    @GetMapping("/{petId}")
    public PetDTO getPet(@PathVariable long petId) {
        //throw new UnsupportedOperationException();
        Pet pet =petService.getPet(petId);
        if (pet !=null) {
            return convertPetToPetDTO(pet);
        }
        return null;
    }

    @GetMapping
    public List<PetDTO> getPets(){
        //throw new UnsupportedOperationException();
        return petService.getPets().
                stream().
                map(this::convertPetToPetDTO).
                collect(Collectors.toList());
    }

    @GetMapping("/owner/{ownerId}")
    public List<PetDTO> getPetsByOwner(@PathVariable long ownerId) {
        //throw new UnsupportedOperationException();
        return petService.getPetsByOwner(ownerId)
                .stream()
                .map(this::convertPetToPetDTO)
                .collect(Collectors.toList());
    }

    private Pet convertPetDTOToPet(PetDTO petDTO) {
        Pet pet = new Pet();
        BeanUtils.copyProperties(petDTO, pet);
        return pet;
    }

    private PetDTO convertPetToPetDTO(Pet pet) {
        PetDTO petDTO = new PetDTO();
        BeanUtils.copyProperties(pet, petDTO);
        if (pet.getOwner()!=null) {
            petDTO.setOwnerId(pet.getOwner().getId());
        }
        return petDTO;
    }
}
