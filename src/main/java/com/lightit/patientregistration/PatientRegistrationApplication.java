package com.lightit.patientregistration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class PatientRegistrationApplication {

	public static void main(String[] args) {
		SpringApplication.run(PatientRegistrationApplication.class, args);
	}

}
