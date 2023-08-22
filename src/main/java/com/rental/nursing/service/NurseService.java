package com.rental.nursing.service;

import java.util.List;

import com.rental.nursing.dto.NurseDto;
import com.rental.nursing.entity.Nurse;

public interface NurseService {
	NurseDto createNurse(NurseDto dto);

	List<NurseDto> getAllNurses();

	NurseDto nurseToDto(Nurse nurse);
}
