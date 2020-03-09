package com.prozacto.vault.util;

import com.prozacto.vault.exception.EmptyFieldException;
import com.prozacto.vault.model.*;
import org.springframework.stereotype.Component;

@Component
public class ValidationUtil {

    public void validateAppointment(Appointment appointment){
        if(appointment.getDoctor()==null) throw new EmptyFieldException("doctor");
        if(appointment.getDoctor().getDoctorId()==null) throw new EmptyFieldException("doctor.doctorId");
        if(appointment.getPatient()==null) throw new EmptyFieldException("patient");
        if(appointment.getPatient().getPatientId()==null) throw new EmptyFieldException("patient.patientId");
        if(appointment.getDate()==null) throw new EmptyFieldException("date");
    }

    public void validateAssistant(Assistant assistant) {
        if(assistant.getUser()==null) throw new EmptyFieldException("user");
        if(assistant.getUser().getId()==null) throw new EmptyFieldException("user.id");
        if(assistant.getFirstName()==null) throw new EmptyFieldException("firstName");
        if(assistant.getLastName()==null) throw new EmptyFieldException("lastName");
    }

    public void validateClinic(Clinic clinic){
        if(clinic.getName()==null) throw new EmptyFieldException("name");
    }

    public void validateDoctor(Doctor doctor){
        if(doctor.getUser()==null) throw new EmptyFieldException("user");
        if(doctor.getUser().getId()==null) throw new EmptyFieldException("user.id");
        if(doctor.getFirstName()==null) throw new EmptyFieldException("firstName");
        if(doctor.getLastName()==null) throw new EmptyFieldException("lastName");
        if(doctor.getDoctorType()==null) throw new EmptyFieldException("doctorType");
        if(doctor.getQualification()==null) throw new EmptyFieldException("qualification");
        if(doctor.getClinic()==null) throw new EmptyFieldException("clinic");
        if(doctor.getClinic().getClinicId()==null) throw new EmptyFieldException("clinic.clinicId");
    }

    public void validatePatient(Patient patient) {
        if(patient.getUser()==null) throw new EmptyFieldException("user");
        if(patient.getUser().getId()==null) throw new EmptyFieldException("user.id");
        if(patient.getFirstName()==null) throw new EmptyFieldException("firstName");
        if(patient.getLastName()==null) throw new EmptyFieldException("lastName");
    }

    public void validateUser(ApplicationUser user) {
        if(user.getPassword()==null) throw new EmptyFieldException("password");
        if(user.getUsername()==null) throw new EmptyFieldException("username");
    }

}
