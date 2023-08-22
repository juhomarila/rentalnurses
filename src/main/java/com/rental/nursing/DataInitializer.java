package com.rental.nursing;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Component;

import com.github.javafaker.Faker;
import com.rental.nursing.dao.EmployerDao;
import com.rental.nursing.dao.NurseDao;
import com.rental.nursing.dto.EmployerDto;
import com.rental.nursing.dto.EmployerRatingDto;
import com.rental.nursing.dto.JobDto;
import com.rental.nursing.dto.NurseDto;
import com.rental.nursing.dto.NurseRatingDto;
import com.rental.nursing.entity.Employer;
import com.rental.nursing.entity.EmployerRating;
import com.rental.nursing.entity.Nurse;
import com.rental.nursing.entity.NurseRating;
import com.rental.nursing.service.EmployerService;
import com.rental.nursing.service.JobService;
import com.rental.nursing.service.NurseService;
import com.rental.nursing.service.RatingService;

import jakarta.annotation.PostConstruct;

@Component
public class DataInitializer {
	private final EmployerService employerService;
	private final NurseService nurseService;
	private final RatingService ratingService;
	private final JobService jobService;
	private final EmployerDao employerRepository;
	private final NurseDao nurseRepository;

	public DataInitializer(EmployerService employerService, NurseService nurseService, RatingService ratingService,
			EmployerDao employerRepository, NurseDao nurseRepository, JobService jobService) {
		this.employerService = employerService;
		this.nurseService = nurseService;
		this.ratingService = ratingService;
		this.employerRepository = employerRepository;
		this.nurseRepository = nurseRepository;
		this.jobService = jobService;
	}

	@PostConstruct
	public void init() {
//		generateDefaultEmployers();
//		generateDefaultNurses();
//		List<Employer> employers = employerRepository.findAll();
//		List<Nurse> nurses = nurseRepository.findAll();
//		generateDefaultEmployerRatings(employers, nurses);
//		generateDefaultNurseRatings(employers, nurses);
//		generateDefaultJobs(employers, nurses);
	}

	private void generateDefaultEmployers() {
		Faker faker = new Faker();
		for (int i = 1; i <= 50; i++) {
			Employer employer = new Employer();
			employer.setName(faker.company().name());
			employer.setAddress(faker.address().fullAddress());
			employer.setCity(faker.address().city());
			employer.setBic(generateValidYtunnus());
			employer.setZipCode(Long.valueOf(12345));
			employer.setSector("Hammashoito");
			employer.setInfo(faker.lorem().characters(25));
			employer.setJoined(Instant.now());
			employer.setVerified(true);
			EmployerDto employerDto = employerService.employerToDto(employer);
			employerService.createEmployer(employerDto);
		}
	}

	private void generateDefaultNurses() {
		Faker faker = new Faker();
		for (int i = 1; i <= 50; i++) {
			Nurse nurse = new Nurse();
			nurse.setFirstName(faker.name().firstName());
			nurse.setLastName(faker.name().lastName());
			nurse.setCity(faker.address().city());
			nurse.setZipCode(Long.valueOf(98765));
			nurse.setSector("Hammashoito");
			nurse.setInfo(faker.lorem().characters(25));
			nurse.setJoined(Instant.now());
			nurse.setVerified(true);
			NurseDto nurseDto = nurseService.nurseToDto(nurse);
			nurseService.createNurse(nurseDto);
		}
	}

	private void generateDefaultEmployerRatings(List<Employer> employers, List<Nurse> nurses) {
		Faker faker = new Faker();
		Random random = new Random();
		for (Employer employer : employers) {
			for (Nurse nurse : nurses) {
				EmployerRating rating = new EmployerRating();
				rating.setRating(random.nextInt(5) + 1);
				rating.setAdded(Instant.now());
				rating.setNurse(nurse);
				rating.setEmployer(employer);
				rating.setComment(faker.lorem().characters(25));
				EmployerRatingDto employerRatingDto = ratingService.employerRatingToDto(rating);
				ratingService.createEmployerRating(employerRatingDto);
			}
		}
	}

	private void generateDefaultNurseRatings(List<Employer> employers, List<Nurse> nurses) {
		Faker faker = new Faker();
		Random random = new Random();
		for (Nurse nurse : nurses) {
			for (Employer employer : employers) {
				NurseRating rating = new NurseRating();
				rating.setRating(random.nextInt(5) + 1);
				rating.setAdded(Instant.now());
				rating.setNurse(nurse);
				rating.setEmployer(employer);
				NurseRatingDto nurseRatingDto = ratingService.nurseRatingToDto(rating);
				ratingService.createNurseRating(nurseRatingDto);
			}
		}
	}

	private void generateDefaultJobs(List<Employer> employers, List<Nurse> nurses) {
		Faker faker = new Faker();
		Random random = new Random();

		for (Employer employer : employers) {
			for (int i = 1; i <= 50; i++) {
				JobDto jobDto = new JobDto();
				jobDto.setName(employer.getName());
				jobDto.setCity(employer.getCity());
				jobDto.setInfo(faker.lorem().characters(25));
				Long randomLong = random.nextLong(360 * 60) + Long.valueOf((long) 0.5);
				Instant currentInstant = Instant.now();
				Instant startTime = currentInstant.plus(randomLong, ChronoUnit.MINUTES);
				Instant endTime = startTime.plus(8 * 60, ChronoUnit.MINUTES);
				jobDto.setStartTime(startTime);
				jobDto.setEndTime(endTime);
				jobDto.setSalary(random.nextDouble(250.5) + 50.5);
				jobDto.setEmployerId(employer.getId());
				jobDto.setLatitude(random.nextDouble() * (63.3 - 60.05) + 60.05);
				jobDto.setLongitude(random.nextDouble() * (27.5 - 21.15) + 21.15);

				// Assign a random nurse to every second job
				if (i % 3 == 0 && !nurses.isEmpty()) {
					Nurse randomNurse = nurses.get(random.nextInt(nurses.size()));
					jobDto.setNurseId(randomNurse.getId());
				}

				jobService.createJob(jobDto);
			}
		}
	}

	public static String generateValidYtunnus() {
		Random random = new Random();
		int alkuosa = 1000000 + random.nextInt(9000000); // Generate a random 7-digit number
		int[] kerroin = { 7, 9, 10, 5, 8, 4, 2 };

		while (true) {
			int[] tulo = new int[7];
			for (int i = 0; i < 7; i++) {
				tulo[i] = Character.getNumericValue(Integer.toString(alkuosa).charAt(i)) * kerroin[i];
			}

			int summa = 0;
			for (int value : tulo) {
				summa += value;
			}

			int jakojäännös = summa % 11;

			if (jakojäännös != 1) {
				String tarkistusnumero;
				if (jakojäännös == 0) {
					tarkistusnumero = "0";
				} else {
					tarkistusnumero = Integer.toString(11 - jakojäännös);
				}

				String yTunnus = alkuosa + "-" + tarkistusnumero;
				return yTunnus;
			}

			alkuosa = 1000000 + random.nextInt(9000000); // Generate a new random 7-digit number
		}
	}

}