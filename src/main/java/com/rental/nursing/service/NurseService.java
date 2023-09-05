package com.rental.nursing.service;

import java.util.List;

import com.rental.nursing.dto.NurseDto;
import com.rental.nursing.entity.Nurse;

public interface NurseService {
	ValidateServiceResult<NurseDto> createNurse(NurseDto dto);

	ValidateServiceResult<NurseDto> updateNurse(Long id, NurseDto dto);

	ValidateServiceResult<List<NurseDto>> getAllNurses();

	ValidateServiceResult<NurseDto> getNurseById(Long id);

	ValidateServiceResult<Boolean> deleteNurse(Long id);

	NurseDto nurseToDto(Nurse nurse);
}
