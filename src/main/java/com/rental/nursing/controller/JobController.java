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

import com.rental.nursing.dto.JobDto;
import com.rental.nursing.dto.NurseJobUpdateDto;
import com.rental.nursing.exception.NotFoundException;
import com.rental.nursing.exception.SavingDataException;
import com.rental.nursing.exception.UpdateDataException;
import com.rental.nursing.exception.ValidationException;
import com.rental.nursing.service.JobService;

import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/job")
public class JobController {

	@Autowired
	private JobService jobService;

	@PostMapping
	public ResponseEntity<?> createJob(@RequestBody JobDto dto) {
		try {
			JobDto jobDto = jobService.createJob(dto);
			return new ResponseEntity<>(jobDto, HttpStatus.CREATED);
		} catch (SavingDataException e) {
			return new ResponseEntity<>(e.getMessages(), HttpStatus.BAD_REQUEST);
		} catch (IllegalStateException e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (EntityNotFoundException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping
	public ResponseEntity<List<JobDto>> getAllJobs() {
		List<JobDto> jobs = jobService.getAllJobs();
		return jobs.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(jobs);
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updateJob(@PathVariable("id") Long id, @RequestBody JobDto updatedJobDto) {
		try {
			JobDto jobDto = jobService.updateJob(id, updatedJobDto);
			return new ResponseEntity<>(jobDto, HttpStatus.OK);
		} catch (SavingDataException e) {
			return new ResponseEntity<>(e.getMessages(), HttpStatus.BAD_REQUEST);
		} catch (EntityNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PutMapping("/nurse/{id}")
	public ResponseEntity<?> updateOrRemoveJobNurse(@PathVariable("id") Long id,
			@RequestBody NurseJobUpdateDto nurseJobUpdateDto) {
		try {
			JobDto jobDto = jobService.updateOrRemoveJobNurse(id, nurseJobUpdateDto);
			return new ResponseEntity<>(jobDto, HttpStatus.OK);
		} catch (UpdateDataException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (SavingDataException e) {
			return new ResponseEntity<>(e.getMessages(), HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<JobDto> getJobById(@PathVariable("id") Long id) {
		try {
			JobDto jobDto = jobService.getJobById(id);
			return ResponseEntity.ok(jobDto);
		} catch (NotFoundException e) {
			return ResponseEntity.notFound().build();
		} catch (ValidationException e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteJob(@PathVariable("id") Long id) {
		try {
			jobService.deleteJob(id);
			return ResponseEntity.ok().build();
		} catch (UpdateDataException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (EntityNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}
}
