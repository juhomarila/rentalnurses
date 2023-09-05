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
import com.rental.nursing.logging.NurseLogger;
import com.rental.nursing.service.JobService;

@RestController
@RequestMapping("/job")
public class JobController {

	@Autowired
	private JobService jobService;

	private final NurseLogger logger;

	@Autowired
	public JobController(NurseLogger logger) {
		this.logger = logger;
	}

	@PostMapping
	public ResponseEntity<?> createJob(@RequestBody JobDto dto) {
		logger.postLogStart("createJob");
		var vsr = jobService.createJob(dto);
		logger.postLogEnd("createJob");
		return vsr.getVr().validated ? new ResponseEntity<>(vsr.getT(), HttpStatus.OK)
				: new ResponseEntity<>(vsr.getVr().getErrorMsg(),
						vsr.getVr().validated ? HttpStatus.BAD_REQUEST : HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@GetMapping
	public ResponseEntity<List<JobDto>> getAllJobs() {
		logger.getLogStart("getAllJobs");
		var vsr = jobService.getAllJobs();
		logger.getLogEnd("getAllJobs");
		return vsr.getT().isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(vsr.getT());
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updateJob(@PathVariable("id") Long id, @RequestBody JobDto updatedJobDto) {
		logger.putLogStart("updateJob");
		var vsr = jobService.updateJob(id, updatedJobDto);
		logger.putLogEnd("updateJob");
		return vsr.getVr().validated ? new ResponseEntity<>(vsr.getT(), HttpStatus.OK)
				: new ResponseEntity<>(vsr.getVr().getErrorMsg(),
						vsr.getVr().validated ? HttpStatus.BAD_REQUEST : HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@PutMapping("/nurse/{id}")
	public ResponseEntity<?> updateOrRemoveJobNurse(@PathVariable("id") Long id,
			@RequestBody NurseJobUpdateDto nurseJobUpdateDto) {
		logger.putLogStart("updateOrRemoveJobNurse");
		var vsr = jobService.updateOrRemoveJobNurse(id, nurseJobUpdateDto);
		logger.putLogEnd("updateOrRemoveJobNurse");
		return vsr.getVr().validated ? new ResponseEntity<>(vsr.getT(), HttpStatus.OK)
				: new ResponseEntity<>(vsr.getVr().getErrorMsg(),
						vsr.getVr().validated ? HttpStatus.BAD_REQUEST : HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getJobById(@PathVariable("id") Long id) {
		logger.getLogStart("getJobById");
		var vsr = jobService.getJobById(id);
		logger.getLogEnd("getJobById");
		return vsr.getVr().validated ? new ResponseEntity<>(vsr.getT(), HttpStatus.OK)
				: new ResponseEntity<>(vsr.getVr().getErrorMsg(),
						vsr.getVr().validated ? HttpStatus.BAD_REQUEST : HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteJob(@PathVariable("id") Long id) {
		logger.deleteLogStart("getEmployerById");
		var vsr = jobService.deleteJob(id);
		logger.deleteLogEnd("getEmployerById");
		return vsr.getVr().validated ? new ResponseEntity<>(vsr.getT(), HttpStatus.OK)
				: new ResponseEntity<>(vsr.getVr().getErrorMsg(),
						vsr.getVr().validated ? HttpStatus.BAD_REQUEST : HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
