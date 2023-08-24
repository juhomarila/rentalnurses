package com.rental.nursing.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rental.nursing.dto.NurseDto;
import com.rental.nursing.logging.NurseLogger;
import com.rental.nursing.service.NurseService;
import com.rental.nursing.service.ValidateServiceResult;

@RestController
@RequestMapping("/nurse")
public class NurseController {

	@Autowired
	private NurseService nurseService;

	private final NurseLogger logger;

	@Autowired
	public NurseController(NurseLogger logger) {
		this.logger = logger;
	}

	@PostMapping
	public ResponseEntity<?> createNurse(@RequestBody NurseDto dto) {
		logger.postLogStart("createNurse");
		ValidateServiceResult<NurseDto> vsr = nurseService.createNurse(dto);
		logger.postLogEnd("createNurse");
		return vsr.getVr().validated ? new ResponseEntity<>(vsr.getT(), HttpStatus.OK)
				: new ResponseEntity<>(vsr.getVr().getErrorMsg(),
						vsr.getVr().validated ? HttpStatus.BAD_REQUEST : HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@GetMapping
	public ResponseEntity<List<NurseDto>> getAllNurses() {
		logger.getLogStart("getAllNurses");
		ValidateServiceResult<List<NurseDto>> vsr = nurseService.getAllNurses();
		logger.getLogEnd("getAllNurses");
		return vsr.getT().isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(vsr.getT());
	}
}
