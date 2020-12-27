package com.udacity.jdnd.course3.critter.DataModel;

import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface PetRepository extends JpaRepository<Pet, Long > {
    List<Pet> getPetsByOwner_Id(Long ownerId);
}
