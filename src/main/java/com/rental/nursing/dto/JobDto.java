package com.rental.nursing.dto;

import java.time.Instant;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class JobDto {
	private Long id;
	private String name;
	private String city;
	private String info;
	private Instant startTime;
	private Instant endTime;
	private Double salary;
	private Long employerId;
	private Long nurseId;
	private Double longitude;
	private Double latitude;
	// in logic if JobDto has nurse, it is taken
	private boolean open;
}