package com.prozacto.vault;

import com.prozacto.vault.model.*;
import com.prozacto.vault.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class VaultApplication {

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder(){
		return new BCryptPasswordEncoder();
	}

	public static void main(String[] args) {
		SpringApplication.run(VaultApplication.class, args);
	}

	@Bean
	CommandLineRunner initDatabase(
			UserRepository userRepository,
			RoleRepository roleRepository,
			DoctorRepository doctorRepository,
			PatientRepository patientRepository,
			AssistantRepository assistantRepository,
			ClinicRepository clinicRepository
	) {

		return args -> {

			Role adminRole = new Role("ROLE_ADMIN");
			Role patientRole = new Role("ROLE_PATIENT");
			Role assistantRole = new Role("ROLE_ASSISTANT");
			Role doctorRole = new Role("ROLE_DOCTOR");
			Role clientRole = new Role("ROLE_CLIENT");

			Clinic clinic = new Clinic();
			clinic.setName("Cool Clinic");
			clinicRepository.save(clinic);

			ApplicationUser user = new ApplicationUser();
			user.setPassword(bCryptPasswordEncoder().encode("admin"));
			user.setUsername("admin");
			user.setRoles(Arrays.asList(adminRole));
			userRepository.save(user);
			roleRepository.save(adminRole);

			Doctor doctor = new Doctor();
			doctor.setDoctorType("Neurosurgeon");
			doctor.setFirstName("Stephen");
			doctor.setLastName("Strange");
			doctor.setQualification("MBBS");
			doctor.setClinic(clinic);
			clinic.getDoctors().add(doctor);
			user = new ApplicationUser();
			user.setUsername("doctor");
			user.setPassword(bCryptPasswordEncoder().encode("doctor"));
			user.setRoles(Arrays.asList(doctorRole));
			doctor.setUser(user);
			userRepository.save(user);
			doctorRepository.save(doctor);
			roleRepository.save(doctorRole);

			Assistant assistant = new Assistant();
			assistant.setFirstName("Alan");
			assistant.setLastName("Turing");
			assistant.setClinic(clinic);
			user = new ApplicationUser();
			user.setUsername("assistant");
			user.setPassword(bCryptPasswordEncoder().encode("assistant"));
			user.setRoles(Arrays.asList(assistantRole));
			assistant.setUser(user);
			userRepository.save(user);
			assistantRepository.save(assistant);
			roleRepository.save(assistantRole);

			Set<Clinic> clinicSet = new HashSet<>();
			clinicSet.add(clinic);

			Patient patient = new Patient();
			patient.setFirstName("Angus");
			patient.setLastName("Young");
			patient.setClinics(clinicSet);
			user = new ApplicationUser();
			user.setUsername("patient");
			user.setPassword(bCryptPasswordEncoder().encode("patient"));
			user.setRoles(Arrays.asList(patientRole));
			patient.setUser(user);
			userRepository.save(user);
			patientRepository.save(patient);
			roleRepository.save(patientRole);

			roleRepository.save(clientRole);


		};
	}
}
