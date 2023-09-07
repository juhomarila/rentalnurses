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
import com.rental.nursing.logging.NurseLogger;
import com.rental.nursing.service.EmployerService;

import jakarta.annotation.security.PermitAll;

@RestController
@RequestMapping("/employer")
public class EmployerController {

	@Autowired
	private EmployerService employerService;

	private final NurseLogger logger;

	@Autowired
	public EmployerController(NurseLogger logger) {
		this.logger = logger;
	}

	@PostMapping
	@PermitAll
	public ResponseEntity<?> createEmployer(@RequestBody EmployerDto dto) {
		logger.postLogStart("createEmployer");
		var vsr = employerService.createEmployer(dto);
		logger.postLogEnd("createEmployer");
		return vsr.getVr().validated ? new ResponseEntity<>(vsr.getT(), HttpStatus.OK)
				: new ResponseEntity<>(vsr.getVr().getErrorMsg(),
						vsr.getVr().validated ? HttpStatus.BAD_REQUEST : HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updateEmployer(@PathVariable("id") Long id, @RequestBody EmployerDto updatedDto) {
		logger.putLogStart("updateEmployer");
		var vsr = employerService.updateEmployer(id, updatedDto);
		logger.putLogEnd("updateEmployer");
		return vsr.getVr().validated ? new ResponseEntity<>(vsr.getT(), HttpStatus.OK)
				: new ResponseEntity<>(vsr.getVr().getErrorMsg(),
						vsr.getVr().validated ? HttpStatus.BAD_REQUEST : HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@GetMapping
	@PermitAll
	public ResponseEntity<List<EmployerDto>> getAllEmployers() {
		System.out.println("Received createEmployer request");
		logger.getLogStart("getAllEmployers");
		var vsr = employerService.getEmployers();
		logger.getLogEnd("getAllEmployers");
		return vsr.getT().isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(vsr.getT());
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getEmployerById(@PathVariable("id") Long id) {
		logger.getLogStart("getEmployerById");
		var vsr = employerService.getEmployerById(id);
		logger.getLogEnd("getEmployerById");

		return vsr.getVr().validated ? new ResponseEntity<>(vsr.getT(), HttpStatus.OK)
				: new ResponseEntity<>(vsr.getVr().getErrorMsg(),
						vsr.getVr().validated ? HttpStatus.BAD_REQUEST : HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteEmployer(@PathVariable("id") Long id) {
		logger.deleteLogStart("deleteEmployer");
		var vsr = employerService.deleteEmployer(id);
		logger.deleteLogEnd("deleteEmployer");
		return vsr.getVr().validated ? new ResponseEntity<>(vsr.getT(), HttpStatus.OK)
				: new ResponseEntity<>(vsr.getVr().getErrorMsg(),
						vsr.getVr().validated ? HttpStatus.BAD_REQUEST : HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
