package com.udacity.jdnd.course3.critter.DataModel;

import com.udacity.jdnd.course3.critter.user.EmployeeSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;

@Repository
@Transactional
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Query("select e from Employee e where :skills in e.skills and :dayOfWeek member of e.daysAvailable")
    List<Employee> findEmployeePerSkillAndAvailability(@Param("skills") Set<EmployeeSkill> skills,
                                                       DayOfWeek dayOfWeek);

    List<Employee> findAllBySkillsInAndDaysAvailableContains(Set<EmployeeSkill> skills, DayOfWeek dayOfWeek);
}
