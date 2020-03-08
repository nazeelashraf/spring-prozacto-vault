package com.prozacto.vault.controller;

import com.prozacto.vault.exception.ClinicNotFoundException;
import com.prozacto.vault.exception.DoctorNotFoundException;
import com.prozacto.vault.exception.PatientNotFoundException;
import com.prozacto.vault.model.*;
import com.prozacto.vault.repository.*;
import com.prozacto.vault.security.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class AppointmentController {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private AssistantRepository assistantRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private ClinicRepository clinicRepository;

    @Autowired
    private TokenUtil tokenUtil;

    @GetMapping("/appointment")
    @Secured({"ROLE_ASSISTANT", "ROLE_DOCTOR", "ROLE_PATIENT"})
    List<Appointment> getAppointments(@RequestHeader("Authorization") String token){
        List<String> roles = tokenUtil.getRoles(token);
        if(roles.contains("ROLE_ASSISTANT")){
            Assistant assistant = assistantRepository.findAssistantByUserId(tokenUtil.getUserId(token));
            return appointmentRepository.appointmentsWithAssistant(assistant.getAssistantId());
        } else if (roles.contains("ROLE_DOCTOR")) {
            Doctor doctor = doctorRepository.findDoctorByUserId(tokenUtil.getUserId(token));
            return appointmentRepository.appointmentsWithDoctor(doctor.getDoctorId());
        } else if (roles.contains("ROLE_PATIENT")){
            Patient patient = patientRepository.findPatientByUserId(tokenUtil.getUserId(token));
            return appointmentRepository.appointmentsWithPatient(patient.getPatientId());
        }

        return new ArrayList<Appointment>();
    }

    @PostMapping("/appointment")
    @Secured({"ROLE_ASSISTANT"})
    Appointment createAppointment(@RequestBody Appointment appointment, @RequestHeader("Authorization") String token){
        Doctor doctor = doctorRepository.findById(appointment.getDoctor().getDoctorId())
                .orElseThrow(() -> {return new DoctorNotFoundException(appointment.getDoctor().getDoctorId());});
        appointment.setDoctor(doctor);

        Patient patient = patientRepository.findById(appointment.getPatient().getPatientId())
                .orElseThrow(() -> {return new PatientNotFoundException(appointment.getPatient().getPatientId());});
        appointment.setPatient(patient);

        Long clinicId = tokenUtil.getClinicId(token);
        Clinic clinic = clinicRepository.findById(clinicId)
                .orElseThrow(() -> {return new ClinicNotFoundException(clinicId);});
        appointment.setClinic(clinic);

        Assistant assistant = assistantRepository.findAssistantByUserId(tokenUtil.getUserId(token));
        appointment.setAssistant(assistant);

        return appointmentRepository.save(appointment);
    }

}
