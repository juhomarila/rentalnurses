package com.rental.nursing;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Component;

import com.github.javafaker.Faker;
import com.rental.nursing.business.PaymentBusiness;
import com.rental.nursing.controller.EmployerController;
import com.rental.nursing.controller.JobController;
import com.rental.nursing.controller.NurseController;
import com.rental.nursing.controller.RatingController;
import com.rental.nursing.dao.EmployerDao;
import com.rental.nursing.dao.NurseDao;
import com.rental.nursing.dto.EmployerDto;
import com.rental.nursing.dto.EmployerRatingDto;
import com.rental.nursing.dto.JobDto;
import com.rental.nursing.dto.NurseDto;
import com.rental.nursing.dto.NurseRatingDto;
import com.rental.nursing.entity.Employer;
import com.rental.nursing.entity.Nurse;

import jakarta.annotation.PostConstruct;

@Component
public class DataInitializer {
	private final EmployerController employerController;
	private final NurseController nurseController;
	private final RatingController ratingController;
	private final JobController jobController;
	private final PaymentBusiness paymentBusiness;
	private final EmployerDao employerRepository;
	private final NurseDao nurseRepository;

	public DataInitializer(EmployerController employerController, NurseController nurseController,
			RatingController ratingController, JobController jobController, PaymentBusiness paymentBusiness,
			EmployerDao employerRepository, NurseDao nurseRepository) {
		this.employerController = employerController;
		this.nurseController = nurseController;
		this.ratingController = ratingController;
		this.jobController = jobController;
		this.paymentBusiness = paymentBusiness;
		this.employerRepository = employerRepository;
		this.nurseRepository = nurseRepository;
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
		Random random = new Random();
		for (int i = 1; i <= 50; i++) {
			EmployerDto employer = new EmployerDto();
			employer.setName(faker.company().name());
			employer.setAddress(faker.address().fullAddress());
			employer.setCity(faker.address().city());
			employer.setBic(generateValidYtunnus());
			employer.setZipCode(Long.valueOf(12345));
			employer.setSector("Hammashoito");
			employer.setInfo(faker.lorem().characters(25));
			Long randomLong = random.nextLong(3 * 365) + Long.valueOf((long) 0);
			Instant currentInstant = Instant.now();
			Instant joined = currentInstant.minus(randomLong, ChronoUnit.DAYS);
			employer.setJoined(joined);
			employer.setVerified(true);
			// EmployerDto employerDto = employerService.employerToDto(employer);
			employerController.createEmployer(employer);
		}
	}

	private void generateDefaultNurses() {
		Faker faker = new Faker();
		Random random = new Random();
		for (int i = 1; i <= 50; i++) {
			NurseDto nurse = new NurseDto();
			nurse.setFirstName(faker.name().firstName());
			nurse.setLastName(faker.name().lastName());
			nurse.setCity(faker.address().city());
			nurse.setZipCode(Long.valueOf(98765));
			nurse.setSector("Hammashoito");
			nurse.setInfo(faker.lorem().characters(25));
			Long randomLong = random.nextLong(3 * 365) + Long.valueOf((long) 0);
			Instant currentInstant = Instant.now();
			Instant joined = currentInstant.minus(randomLong, ChronoUnit.DAYS);
			nurse.setJoined(joined);
			nurse.setVerified(true);
			// NurseDto nurseDto = nurseService.nurseToDto(nurse);
			nurseController.createNurse(nurse);
		}
	}

	private void generateDefaultEmployerRatings(List<Employer> employers, List<Nurse> nurses) {
		Faker faker = new Faker();
		Random random = new Random();
		for (Employer employer : employers) {
			for (Nurse nurse : nurses) {
				EmployerRatingDto rating = new EmployerRatingDto();
				rating.setRating(random.nextInt(5) + 1);
				rating.setAdded(Instant.now());
				rating.setNurseId(nurse.getId());
				rating.setEmployerId(employer.getId());
				rating.setComment(faker.lorem().characters(25));
				// EmployerRatingDto employerRatingDto =
				// ratingService.employerRatingToDto(rating);
				ratingController.createEmployerRating(rating);
			}
		}
	}

	private void generateDefaultNurseRatings(List<Employer> employers, List<Nurse> nurses) {
		Random random = new Random();
		for (Nurse nurse : nurses) {
			for (Employer employer : employers) {
				NurseRatingDto rating = new NurseRatingDto();
				rating.setRating(random.nextInt(5) + 1);
				rating.setAdded(Instant.now());
				rating.setNurseId(nurse.getId());
				rating.setEmployerId(employer.getId());
				// NurseRatingDto nurseRatingDto = ratingService.nurseRatingToDto(rating);
				ratingController.createNurseRating(rating);
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

				jobController.createJob(jobDto);
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