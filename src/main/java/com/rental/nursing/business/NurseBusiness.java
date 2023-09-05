package com.rental.nursing.business;

import java.util.List;
import java.util.Optional;

import com.rental.nursing.dto.NurseDto;
import com.rental.nursing.entity.Nurse;

public interface NurseBusiness {
	Optional<Nurse> createNurse(NurseDto nurseDto);

	List<Nurse> getAllNurses();

	Optional<Nurse> getNurseById(Long id);

	Optional<Nurse> updateNurse(Nurse nurse, NurseDto newNurseDto);

	void deleteNurse(Nurse nurse);
}
