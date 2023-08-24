package com.rental.nursing.service;

import java.util.List;

import com.rental.nursing.dto.EmployerDto;
import com.rental.nursing.entity.Employer;

public interface EmployerService {
	ValidateServiceResult<EmployerDto> createEmployer(EmployerDto dto);

	ValidateServiceResult<EmployerDto> updateEmployer(Long id, EmployerDto newEmployer);

	ValidateServiceResult<List<EmployerDto>> getEmployers();

	ValidateServiceResult<EmployerDto> getEmployerById(Long id);

	ValidateServiceResult<Boolean> deleteEmployer(Long id);

	EmployerDto employerToDto(Employer employer);
}
