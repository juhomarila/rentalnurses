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
import com.rental.nursing.exception.SavingDataException;
import com.rental.nursing.service.NurseService;

@RestController
@RequestMapping("/nurse")
public class NurseController {

	@Autowired
	private NurseService nurseService;

	@PostMapping
	public ResponseEntity<?> createNurse(@RequestBody NurseDto dto) {
		try {
			NurseDto nurseDto = nurseService.createNurse(dto);
			return new ResponseEntity<>(nurseDto, HttpStatus.CREATED);
		} catch (SavingDataException e) {
			return new ResponseEntity<>(e.getMessages(), HttpStatus.BAD_REQUEST);
		} catch (IllegalStateException e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping
	public ResponseEntity<List<NurseDto>> getAllEmployers() {
		List<NurseDto> nurses = nurseService.getAllNurses();
		return nurses.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(nurses);
	}
}
