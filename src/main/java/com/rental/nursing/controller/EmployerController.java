package com.rental.nursing.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rental.nursing.dto.EmployerDto;
import com.rental.nursing.exception.EmployerNotFoundException;
import com.rental.nursing.exception.SavingDataException;
import com.rental.nursing.service.EmployerService;

import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/employer")
public class EmployerController {

	@Autowired
	private EmployerService employerService;

	@PostMapping
	public ResponseEntity<?> createEmployer(@RequestBody EmployerDto dto) {
		try {
			EmployerDto employerDto = employerService.createEmployer(dto);
			return new ResponseEntity<>(employerDto, HttpStatus.CREATED);
		} catch (SavingDataException e) {
			return new ResponseEntity<>(e.getMessages(), HttpStatus.BAD_REQUEST);
		} catch (IllegalStateException e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updateEmployer(@PathVariable("id") Long id, @RequestBody EmployerDto newDto) {
		try {
			EmployerDto employerDto = employerService.updateEmployer(id, newDto);
			return new ResponseEntity<>(employerDto, HttpStatus.OK);
		} catch (SavingDataException e) {
			return new ResponseEntity<>(e.getMessages(), HttpStatus.BAD_REQUEST);
		} catch (EntityNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping
	public ResponseEntity<List<EmployerDto>> getAllEmployers() {
		List<EmployerDto> employers = employerService.getEmployers();
		return employers.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(employers);
	}

	@GetMapping("/{id}")
	public ResponseEntity<EmployerDto> getEmployerById(@PathVariable("id") Long id) {
		try {
			EmployerDto employerDto = employerService.getEmployerById(id);
			return ResponseEntity.ok(employerDto);
		} catch (EmployerNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteEmployer(@PathVariable("id") Long id) {
		try {
			employerService.deleteEmployer(id);
			return ResponseEntity.ok().build();
		} catch (EntityNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}
}
