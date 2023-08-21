package com.rental.nursing.service;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ValidationResult {
	public List<String> errorMsg;
	public boolean validated;

	public ValidationResult(List<String> errorMsg, boolean validated) {
		this.errorMsg = errorMsg;
		this.validated = validated;
	}

	public ValidationResult(boolean validated) {
		this.validated = validated;
		this.errorMsg = new ArrayList<>();
	}
}
