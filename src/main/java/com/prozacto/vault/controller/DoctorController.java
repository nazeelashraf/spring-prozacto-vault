package com.prozacto.vault.controller;

import com.prozacto.vault.exception.ClinicNotFoundException;
import com.prozacto.vault.exception.UserNotFoundException;
import com.prozacto.vault.model.ApplicationUser;
import com.prozacto.vault.model.Doctor;
import com.prozacto.vault.model.Role;
import com.prozacto.vault.repository.ClinicRepository;
import com.prozacto.vault.repository.DoctorRepository;
import com.prozacto.vault.repository.RoleRepository;
import com.prozacto.vault.repository.UserRepository;
import com.prozacto.vault.security.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
public class DoctorController {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private ClinicRepository clinicRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private TokenUtil tokenUtil;


    @GetMapping("/doctor")
    @Secured({"ROLE_ASSISTANT", "ROLE_DOCTOR"})
    List<Doctor> getDoctors(@RequestHeader("Authorization") String token) {
        return doctorRepository.doctorsInClinic(tokenUtil.getClinicId(token));
    }

    @PostMapping("/doctor")
    @Secured({"ROLE_ADMIN"})
    Doctor postDoctor(@RequestBody Doctor doctor){
        ApplicationUser user = userRepository.findById(doctor.getUser().getId())
                .orElseThrow(() -> {return new UserNotFoundException(doctor.getUser().getId());});
        clinicRepository.findById(doctor.getClinic().getClinicId())
                .orElseThrow(() -> {return new ClinicNotFoundException(doctor.getClinic().getClinicId());});

        Role role = roleRepository.findByName("ROLE_DOCTOR");
        user.setRoles(Arrays.asList(role));
        doctor.setUser(user);
        return doctorRepository.save(doctor);

    }
}
