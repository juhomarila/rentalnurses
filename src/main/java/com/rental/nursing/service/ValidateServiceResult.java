package com.rental.nursing.service;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ValidateServiceResult<T> {
	private T t;
	private ValidationResult vr;

	public ValidateServiceResult(T t, ValidationResult vr) {
		this.t = t;
		this.vr = vr;
	}
}
