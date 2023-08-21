package com.rental.nursing.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class PaymentDto {
	private Long id;
	private Long jobId;
	private boolean paid;
	private Double amount;
}
