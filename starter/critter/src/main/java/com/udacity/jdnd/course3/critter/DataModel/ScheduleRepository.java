package com.udacity.jdnd.course3.critter.DataModel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    List<Schedule> findAllByEmployeesId(long employeeId);
    List<Schedule> findAllByPetsId(long petId);

   /* @Query("SELECT sp from SCHEDULE_PET sp INNER JOIN Pet p " +
            "ON sp.PET_ID = p.id WHERE p.owner.id = :customerId ")
    List<Schedule> findAllByCustomerId(@Param("customerId") Long customerId);*/

}
